package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.dao.OperationLogDao;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * @author Freedy
 * @date 2021/5/18 11:48
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDao, OperationLogEntity> implements OperationLogService {

}
