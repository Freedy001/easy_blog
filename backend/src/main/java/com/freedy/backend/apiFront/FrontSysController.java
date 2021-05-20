package com.freedy.backend.apiFront;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.IPUtil;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.SubscriberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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


    @ApiOperation("订阅文章")
    @GetMapping("/subscribe")
    public Result subscribe(@RequestParam(name = "email") String email) {
        SubscriberEntity entity = new SubscriberEntity();
        entity.setSubscriberEmail(email);
        subscriberService.save(entity);
        return Result.ok();
    }

    @ApiOperation("获取首页设置")
    @GetMapping("/getIndexSetting")
    public Result getIndexSetting() {
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
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
        return Result.ok().setData(settingVo);
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
        if (keys!=null&&keys.size() > 0) {
            //有通知 需要通知前端
            List<String> notifyList = keys.stream().map(ops::get).collect(Collectors.toList());
            return Result.goNotify(notifyList);
        }
        return Result.ok();
    }

    @GetMapping("/")
    public Result test(){
        return Result.ok();
    }


}
