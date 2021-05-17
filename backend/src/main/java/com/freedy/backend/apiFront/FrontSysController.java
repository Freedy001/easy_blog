package com.freedy.backend.apiFront;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.ArticleEntity;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.service.ArticleService;
import com.freedy.backend.service.SubscriberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/subscribe")
    public Result subscribe(@RequestParam(name = "email") String email){
        SubscriberEntity entity = new SubscriberEntity();
        entity.setSubscriberEmail(email);
        subscriberService.save(entity);
        return Result.ok();
    }

    @GetMapping("/getIndexSetting")
    public Result getIndexSetting(){
        CommonSettingVo settingVo = new CommonSettingVo();
        BeanUtils.copyProperties(loadSetting,settingVo);
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


}
