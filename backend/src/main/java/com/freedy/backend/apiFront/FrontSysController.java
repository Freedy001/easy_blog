package com.freedy.backend.apiFront;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.vo.setting.CommentSettingVo;
import com.freedy.backend.exception.EmailAlreadyExistsException;
import com.freedy.backend.exception.VerifyErrorException;
import com.freedy.backend.utils.*;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.SubscriberService;
import com.google.common.base.VerifyException;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.mail.Email;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/15 20:27
 */
@RestController
@RequestMapping("/frontend/sys")
public class FrontSysController {
    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LoadSetting loadSetting;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @ApiOperation("订阅文章")
    @GetMapping("/subscribe")
    public Result subscribe(@RequestParam(name = "email") String email) {
        SubscriberEntity one = subscriberService.getOne(new QueryWrapper<SubscriberEntity>().lambda().eq(SubscriberEntity::getSubscriberEmail, email));
        if (one!=null){
            throw new EmailAlreadyExistsException();
        }
        SubscriberEntity entity = new SubscriberEntity();
        entity.setSubscriberEmail(email);
        //用于区别用户
        String uuid = UUID.randomUUID().toString();
        entity.setUUID(uuid);
        rabbitTemplate.convertAndSend(RabbitConstant.THIRD_PART_EXCHANGE_NAME, RabbitConstant.EMAIL_REPLAY_ROUTING_KEY, entity);
        return Result.ok().setData(uuid);
    }

    @ApiOperation("验证邮箱")
    @GetMapping("/verify")
    public Result verify(@RequestParam(name = "code") String code
            , @RequestParam(name = "UUID") String UUID) {
        String verifyCode = redisTemplate.opsForValue().get(RedisConstant.SUBSCRIBE_HEADER + UUID);
        if (StringUtils.hasText(verifyCode)){
            String[] split = verifyCode.split("-");
            if (split[0].equals(code)){
                //验证成功
                SubscriberEntity entity = new SubscriberEntity();
                entity.setSubscriberEmail(split[1]);
                entity.setCreateTime(System.currentTimeMillis());
                subscriberService.save(entity);
                redisTemplate.delete(RedisConstant.SUBSCRIBE_HEADER + UUID);
                return Result.ok();
            }
        }
        throw new VerifyErrorException();
    }


    @ApiOperation("获取首页设置")
    @GetMapping("/getIndexSetting")
    public Result getIndexSetting() {
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
        if (StringUtils.hasText(loadSetting.getIndexArticleIdAndTitle())){
            //构建首页文章
            ArticleEntity article = articleService.getOne(new QueryWrapper<ArticleEntity>()
                    .lambda().eq(ArticleEntity::getId,
                            Long.parseLong(loadSetting.getIndexArticleIdAndTitle().split(",")[0]))
            );
            CommonSettingVo.IndexArticle indexArticle = new CommonSettingVo.IndexArticle();
            indexArticle.setId(article.getId().toString());
            indexArticle.setTitle(article.getTitle());
            indexArticle.setArticleDesc(article.getArticleDesc());
            indexArticle.setPublishTime(DateUtils.formatChineseDate(article.getPublishTime()));
            settingVo.setIndexArticle(indexArticle);
        }
        CommentSettingVo commentSettingVo = new CommentSettingVo();
        BeanUtils.copyProperties(loadSetting, commentSettingVo);
        //将对象转换为map
        HashMap<Object, Object> settingMap = Arrays.stream(BeanUtils.getPropertyDescriptors(settingVo.getClass()))
                .filter(itm -> !"class".equals(itm.getName()))
                .collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), settingVo)),
                        HashMap::putAll);
        HashMap<Object, Object> commentSettingMap = Arrays.stream(BeanUtils.getPropertyDescriptors(commentSettingVo.getClass()))
                .filter(itm -> !"class".equals(itm.getName()))
                .collect(HashMap::new,
                        (map, pd) -> map.put(pd.getName(), ReflectionUtils.invokeMethod(pd.getReadMethod(), commentSettingVo)),
                        HashMap::putAll);
        //连接在一起返回
        settingMap.putAll(commentSettingMap);
        return Result.ok().setData(settingMap);
    }

    @ApiOperation("心跳接口,用于统计数据与通知客户端最新变化")
    @GetMapping("/heartbeat")
    public Result heartBeat(HttpServletRequest request) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String ipAddr = IPUtil.getRemoteIpAddr(request);
        //redis 存的类容是开始的时间加上新的时间
        String val = ops.get(RedisConstant.USER_IP_HEADER + ":" + ipAddr);
        if (StringUtils.hasText(val)) {
            String[] split = val.split("-");
            ops.set(RedisConstant.USER_IP_HEADER + ":" + ipAddr, split[0] + "-" + System.currentTimeMillis());
        } else {
            long time = System.currentTimeMillis();
            ops.set(RedisConstant.USER_IP_HEADER + ":" + ipAddr, time + "-" + time);
        }
        Set<String> keys = redisTemplate.keys(RedisConstant.NOTIFY_HEADER + "*");
        if (keys != null && keys.size() > 0) {
            //有通知 需要通知前端
            List<String> notifyList = keys.stream().map(ops::get).collect(Collectors.toList());
            return Result.goNotify(notifyList);
        }
        return Result.ok();
    }

    @GetMapping("/")
    public Result test() {
        return Result.ok();
    }


}
