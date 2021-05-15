package com.freedy.backend.apiFront;

import com.freedy.backend.common.utils.Result;
import com.freedy.backend.entity.SubscriberEntity;
import com.freedy.backend.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Freedy
 * @date 2021/5/15 20:27
 */
@RestController
@RequestMapping("/front/sys")
public class FrontSysController {
    @Autowired
    private SubscriberService subscriberService;

    @GetMapping("/subscribe")
    public Result subscribe(@RequestParam(name = "email") String email){
        SubscriberEntity entity = new SubscriberEntity();
        entity.setSubscriberEmail(email);
        subscriberService.save(entity);
        return Result.ok();
    }


}
