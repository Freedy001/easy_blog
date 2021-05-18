package com.freedy.backend.api;

import com.freedy.backend.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Freedy
 * @date 2021/5/18 10:17
 */
@RequestMapping("backend/sys")
public class SysController {

    @GetMapping("getDashboardData")
    public Result getDashboardData(){
        return Result.ok();
    }

}
