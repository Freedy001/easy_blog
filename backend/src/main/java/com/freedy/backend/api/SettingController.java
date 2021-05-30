package com.freedy.backend.api;

import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.setting.CommentSettingVo;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.entity.vo.setting.SMTPSettingVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.service.SettingService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * 系统设置表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @Autowired
    private LoadSetting loadSetting;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PreAuthorize("hasAuthority('setting-common')")
    @ApiOperation("获取常规设置")
    @GetMapping("/getCommonSetting")
    public Result getCommonSetting() {
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
        CommonSettingVo.IndexArticle article = new CommonSettingVo.IndexArticle();
        if (StringUtils.hasText(loadSetting.getIndexArticleIdAndTitle())) {
            String[] split = loadSetting.getIndexArticleIdAndTitle().split(",", 2);
            article.setId(split[0]);
            article.setTitle(split[1]);
        }else {
            article.setId("");
            article.setTitle("");
        }
        settingVo.setIndexArticle(article);
        return Result.ok().setData(settingVo);
    }

    @PreAuthorize("hasAuthority('setting-common')")
    @RecordLog(type = RecordEnum.SETTING)
    @ApiOperation("保存常规设置")
    @PostMapping("/saveCommon")
    public Result saveCommonSetting(@Validated @RequestBody CommonSettingVo settingVo) {
        settingService.saveCommonSetting(settingVo);
        loadSetting.refreshSetting();
        //通知前台页面
        redisTemplate.opsForValue().set(RedisConstant.NOTIFY_HEADER + UUID.randomUUID(), ResultCode.NOTIFY_INDEX_SETTING.name(), 5, TimeUnit.SECONDS);
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('setting-smtp')")
    @ApiOperation("获取smtp设置")
    @GetMapping("/getSMTPSetting")
    public Result getSMTPSetting() {
        SMTPSettingVo settingVo = new SMTPSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
        return Result.ok().setData(settingVo);
    }

    @PreAuthorize("hasAuthority('setting-smtp')")
    @RecordLog(type = RecordEnum.SETTING, logMsg = "修改smtp设置")
    @ApiOperation("保存smtp设置")
    @PostMapping("/saveSMTP")
    public Result saveSMTPSetting(@Validated @RequestBody SMTPSettingVo smtpSettingVo) {
        settingService.saveSMTP(smtpSettingVo);
        loadSetting.refreshSetting();
        return Result.ok();
    }

    @PreAuthorize("hasAuthority('setting-comment')")
    @ApiOperation("获取评论设置")
    @GetMapping("/getCommentSetting")
    public Result getCommentSetting() {
        CommentSettingVo settingVo = new CommentSettingVo();
        BeanUtils.copyProperties(loadSetting, settingVo);
        return Result.ok().setData(settingVo);
    }

    @PreAuthorize("hasAuthority('setting-comment')")
    @RecordLog(type = RecordEnum.SETTING)
    @ApiOperation("保存评论设置")
    @PostMapping("/saveComment")
    public Result saveCommentSetting(@Validated @RequestBody CommentSettingVo commentSettingVo) {
        settingService.saveComment(commentSettingVo);
        loadSetting.refreshSetting();
        //通知前台页面
        redisTemplate.opsForValue().set(RedisConstant.NOTIFY_HEADER + UUID.randomUUID(), ResultCode.NOTIFY_INDEX_SETTING.name(), 5, TimeUnit.SECONDS);
        return Result.ok();
    }



    @PreAuthorize("hasAnyAuthority('setting-common','setting-smtp','setting-comment')")
    @ApiOperation("刷新设置")
    @GetMapping("/refresh")
    public Result refresh() {
        loadSetting.refreshSetting();
        return Result.ok();
    }

}
