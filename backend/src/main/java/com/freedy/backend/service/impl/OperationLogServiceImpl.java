package com.freedy.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.dao.OperationLogDao;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.entity.vo.dashboard.OperationVo;
import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/18 11:48
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogDao, OperationLogEntity> implements OperationLogService {

    @Override
    public PageUtils queryPage(Map<String, Object> param) {
        IPage<OperationLogEntity> page = this.page(new Query<OperationLogEntity>().getPage(param));
        List<OperationVo> operationVoList = page.getRecords().stream().map(item -> {
            OperationVo operationVo = new OperationVo();
            BeanUtils.copyProperties(item, operationVo);
            operationVo.setId(item.getId().toString());
            operationVo.setOperationStatus(item.getIsSuccess() == null ? "失败" : item.getIsSuccess() == 0 ? "成功" : "失败");
            operationVo.setCreatTime(DateUtils.formatRelevantTime(item.getCreatTime()));
            return operationVo;
        }).collect(Collectors.toList());
        PageUtils utils = new PageUtils(page);
        utils.setList(operationVoList);
        return utils;
    }

}
