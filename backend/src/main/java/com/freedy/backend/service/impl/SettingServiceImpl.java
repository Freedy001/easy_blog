package com.freedy.backend.service.impl;

import com.freedy.backend.entity.vo.setting.OssSettingVo;
import com.freedy.backend.exception.ArgumentErrorException;
import com.freedy.backend.utils.ResourceUrlUtil;
import com.freedy.backend.entity.vo.setting.CommentSettingVo;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.entity.vo.setting.SMTPSettingVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;

import com.freedy.backend.dao.SettingDao;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.service.SettingService;
import org.springframework.util.StringUtils;


/**
 * @author Freedy
 */
@Service("settingService")
public class SettingServiceImpl extends ServiceImpl<SettingDao, SettingEntity> implements SettingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SettingEntity> page = this.page(
                new Query<SettingEntity>().getPage(params),
                new QueryWrapper<SettingEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveCommonSetting(CommonSettingVo settingVo) {
        //转化为高清图片
        settingVo.setLogo(ResourceUrlUtil.ConvertToHDUrl(settingVo.getLogo()));
        settingVo.setPoster(ResourceUrlUtil.ConvertToHDUrl(settingVo.getPoster()));
        List<SettingEntity> list = getSettingEntities(settingVo);
        //因为settingVo里面没有indexArticleIdAndTitle所以要自己构建
        CommonSettingVo.IndexArticle article = settingVo.getIndexArticle();
        SettingEntity entity = new SettingEntity();
        entity.setItem("indexArticleIdAndTitle");
        if (StringUtils.hasText(article.getId()) && StringUtils.hasText(article.getTitle())) {
            entity.setValue(article.getId() + "," + article.getTitle());
        } else {
            entity.setValue("");
        }
        list.add(entity);
        baseMapper.updateBatch(list);
    }

    @Override
    public void saveSMTP(SMTPSettingVo smtpSettingVo) {
        baseMapper.updateBatch(getSettingEntities(smtpSettingVo));
    }

    @Override
    public void saveComment(CommentSettingVo commentSettingVo) {
        baseMapper.updateBatch(getSettingEntities(commentSettingVo));
    }

    @Override
    public void saveAttachment(OssSettingVo settingVo) {
        baseMapper.updateBatch(getSettingEntities(settingVo));
    }


    /**
     * 利用反射进行字段填充
     */
    private <T> List<SettingEntity> getSettingEntities(T settingVo) {
        return Arrays.stream(settingVo.getClass().getDeclaredFields()).map(field -> {
            SettingEntity entity = new SettingEntity();
            entity.setItem(field.getName());
            field.setAccessible(true);
            try {
                Object value = field.get(settingVo);
                if (value == null) throw new ArgumentErrorException();
                if (value instanceof Boolean) {
                    entity.setValue(value.toString());
                } else {
                    entity.setValue((String) value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException ignore) {
            }
            return entity;
        }).collect(Collectors.toList());
    }

}