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
import java.util.Date;

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
    ValueOperations<String, String> ops;

    public FrontSysController(StringRedisTemplate redisTemplate) {
        ops = redisTemplate.opsForValue();
    }

    @ApiOperation("订阅文章")
    @GetMapping("/subscribe")
    public Result subscribe(@RequestParam(name = "email") String email) {
        SubscriberEntity entity = new SubscriberEntity();
        entity.setSubscriberEmail(email);
        subscriberService.save(entity);
        return Result.ok();
    }

    @ApiOperation("获取首页文章")
    @GetMapping("/getIndexSetting")
    public Result getIndexSetting() {
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
        ArticleEntity article = articleService.getOne(new QueryWrapper<ArticleEntity>()
                .lambda().eq(ArticleEntity::getId,
                        Long.parseLong(loadSetting.getIndexArticleIdAndTitle().split(",")[0]))
        );
        CommonSettingVo.IndexArticle indexArticle = new CommonSettingVo.IndexArticle();
        indexArticle.setTitle(article.getTitle());
        indexArticle.setArticleDesc(article.getArticleDesc());
        indexArticle.setPublishTime(DateUtils.formatChineseDate(article.getPublishTime()));
        settingVo.setIndexArticle(indexArticle);
        return Result.ok().setData(settingVo);
    }

    @ApiOperation("心跳接口,用于统计数据与通知客户端最新变化")
    @GetMapping("/heartbeat")
    public Result heartBeat(HttpServletRequest request) {
        String ipAddr = IPUtil.getRemoteIpAddr(request);
        //redis 存的类容是开始的时间加上新的时间
        String val = ops.get(RedisConstant.USER_IP_HEADER + ":" + ipAddr);
        if (StringUtils.hasText(val)) {
            String[] split = val.split("-");
            ops.set(RedisConstant.USER_IP_HEADER + ":" + ipAddr,split[0]+"-"+new Date().getTime());
        }else {
            long time = new Date().getTime();
            ops.set(RedisConstant.USER_IP_HEADER + ":" + ipAddr, time +"-"+time);
        }
        return Result.ok();
    }


}