package com.freedy.backend.apiFront;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.entity.vo.manager.UserInfoVo;
import com.freedy.backend.exception.ArgumentErrorException;
import com.freedy.backend.service.ManagerService;
import com.freedy.backend.utils.DateUtils;
import com.freedy.backend.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Freedy
 * @date 2021/5/21 15:31
 */
@RestController
@RequestMapping("/frontend/manager")
public class FrontManagerController {

    @Autowired
    private ManagerService managerService;

    @ApiOperation("获取基础用户消息")
    @GetMapping("/infoById")
    public Result getAuthorInfo(@RequestParam Integer id) {
        ManagerEntity entity = managerService.getById(id);
        return getResult(entity);
    }

    @ApiOperation("获取基础用户消息")
    @GetMapping("/infoByNickname")
    public Result getAuthorInfo(@RequestParam String nickname) {
        ManagerEntity entity = managerService.getOne(new QueryWrapper<ManagerEntity>().lambda().eq(ManagerEntity::getNickname, nickname));
        return getResult(entity);
    }

    private Result getResult(ManagerEntity entity) {
        if (entity == null) throw new ArgumentErrorException();
        UserInfoVo userVo = new UserInfoVo();
        BeanUtils.copyProperties(entity, userVo);
        userVo.setUsername(null);
        userVo.setRootAdmin(null);
        userVo.setStatus(null);
        userVo.setCreateDuration(DateUtils.formatRelevantTime(entity.getCreateTime()).replace("前", ""));
        return Result.ok().setData(userVo);
    }


}
