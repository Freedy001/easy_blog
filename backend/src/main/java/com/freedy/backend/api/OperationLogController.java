package com.freedy.backend.api;

import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("getOperationLog")
    public Result getOperationLog(@RequestParam Map<String,Object> param){
        PageUtils pageUtils=service.queryPage(param);
        return Result.ok().setData(pageUtils);
    }

}
