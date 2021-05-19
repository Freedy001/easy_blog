package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.dao.SettingDao;
import com.freedy.backend.dao.ShorthandDao;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.entity.ShorthandEntity;
import com.freedy.backend.service.ShorthandService;
import org.springframework.stereotype.Service;

/**
 * @author Freedy
 * @date 2021/5/20 1:58
 */
@Service
public class ShorthandServiceImpl extends ServiceImpl<ShorthandDao, ShorthandEntity>  implements ShorthandService {
}
