package com.freedy.backend.api;

import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/19 17:29
 */
@RestController
@RequestMapping("backend/operationLog")
public class OperationLogController {

    @Autowired
    private OperationLogService service;

    @ApiOperation("获取操作日志")
    @Cacheable(cacheNames = CacheConstant.OPERATION_CACHE_NAME,key = "#root.methodName+'-'+#root.args[0]['page']+'-'+#root.args[0]['limit']+'-'+#root.args[0]['sidx']+'-'+#root.args[0]['order']")
    @GetMapping("/getOperationLog")
    public Result getOperationLog(@RequestParam Map<String,Object> param){
        PageUtils pageUtils=service.queryPage(param);
        return Result.ok().setData(pageUtils);
    }

}
