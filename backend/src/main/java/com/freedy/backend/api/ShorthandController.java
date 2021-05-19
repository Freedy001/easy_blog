package com.freedy.backend.api;

import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.entity.ShorthandEntity;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.service.ShorthandService;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Freedy
 * @date 2021/5/20 1:58
 */
@RestController
@RequestMapping("backend/shorthand")
public class ShorthandController {

    @Autowired
    private ShorthandService service;

    @RecordLog(type = RecordEnum.SHORTHAND)
    @PostMapping("/publish")
    public Result publishShorthand(@RequestBody ShorthandEntity entity){
        entity.setManagerId(Local.MANAGER_LOCAL.get().getId());
        entity.setCreateTime(new Date().getTime());
        service.save(entity);
        return Result.ok();
    }

}
