package com.freedy.backend.apiFront;

import com.freedy.backend.service.ShorthandService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/21 1:22
 */
@RestController
@RequestMapping("/frontend/shorthand")
public class FrontShortController {

    @Autowired
    private ShorthandService service;

    @GetMapping("/list")
    public Result getList(@RequestParam Map<String, Object> param){
        PageUtils pageUtils= service.queryPage(param);
        return Result.ok().setData(pageUtils);
    }

}
