package com.freedy.backend.api;

import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.setting.CommentSettingVo;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.entity.vo.setting.SMTPSettingVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.service.SettingService;


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

    @ApiOperation("获取常规设置")
    @GetMapping("/getCommonSetting")
    public Result getCommonSetting() {
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting,settingVo);
        CommonSettingVo.IndexArticle article = new CommonSettingVo.IndexArticle();
        String[] split = loadSetting.getIndexArticleIdAndTitle().split(",", 2);
        article.setId(split[0]);
        article.setTitle(split[1]);
        settingVo.setIndexArticle(article);
        return Result.ok().setData(settingVo);
    }

    @ApiOperation("保存常规设置")
    @PostMapping("/saveCommon")
    public Result saveCommon(@RequestBody CommonSettingVo settingVo){
        settingService.saveCommonSetting(settingVo);
        loadSetting.refreshSetting();
        return Result.ok();
    }
    @ApiOperation("获取smtp设置")
    @GetMapping("/getSMTPSetting")
    public Result getSMTPSetting(){
        SMTPSettingVo settingVo = new SMTPSettingVo();
        BeanUtils.copyProperties(loadSetting,settingVo);
        return Result.ok().setData(settingVo);
    }

    @PostMapping("/saveSMTP")
    public Result saveSMTP(@RequestBody SMTPSettingVo smtpSettingVo){
        settingService.saveSMTP(smtpSettingVo);
        loadSetting.refreshSetting();
        return Result.ok();
    }

    @GetMapping("/getCommentSetting")
    public Result getCommentSetting(){
        CommentSettingVo settingVo = new CommentSettingVo();
        BeanUtils.copyProperties(loadSetting,settingVo);
        return Result.ok().setData(settingVo);
    }

    @PostMapping("/saveComment")
    public Result saveComment(@RequestBody CommentSettingVo commentSettingVo){
        settingService.saveComment(commentSettingVo);
        loadSetting.refreshSetting();
        return Result.ok();
    }


    @GetMapping("/refresh")
    public Result refresh(){
        loadSetting.refreshSetting();
        return Result.ok();
    }


}
