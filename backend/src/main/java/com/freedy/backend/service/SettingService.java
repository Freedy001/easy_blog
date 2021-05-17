package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.entity.vo.setting.CommentSettingVo;
import com.freedy.backend.entity.vo.setting.CommonSettingVo;
import com.freedy.backend.entity.vo.setting.SMTPSettingVo;

import java.util.Map;

/**
 * 系统设置表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface SettingService extends IService<SettingEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存常规设置
     */
    void saveCommonSetting(CommonSettingVo settingVo);

    /**
     * 保存smtp设置
     */
    void saveSMTP(SMTPSettingVo smtpSettingVo);

    /**
     * 保存评论设置
     */
    void saveComment(CommentSettingVo commentSettingVo);

}

