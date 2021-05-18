package com.freedy.backend.api;

import java.util.ArrayList;
import java.util.Arrays;

import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.properties.PermissionItemProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.RolePermissionEntity;
import com.freedy.backend.service.RolePermissionService;


/**
 * 角色权限表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@RestController
@RequestMapping("backend/rolePermission")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private PermissionItemProperties permissionItem;

    @ApiOperation("返回权限列表")
    @GetMapping("/getPermissionItem")
    public Result getPermissionItem(){
        NewUserVo userVo = new NewUserVo();
        userVo.setArticlePermission(new ArrayList<>(permissionItem.getArticlePermission().values()));
        userVo.setCommentPermission(new ArrayList<>(permissionItem.getCommentPermission().values()));
        userVo.setUserPermission(new ArrayList<>(permissionItem.getUserPermission().values()));
        userVo.setSettingPermission(new ArrayList<>(permissionItem.getSettingPermission().values()));
        return Result.ok().setData(userVo);
    }

    @ApiOperation("列出")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		RolePermissionEntity rolePermission = rolePermissionService.getById(id);
        return Result.ok().put("rolePermission", rolePermission);
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public Result save(@RequestBody RolePermissionEntity rolePermission){
		rolePermissionService.save(rolePermission);
        return Result.ok();
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody RolePermissionEntity rolePermission){
		rolePermissionService.updateById(rolePermission);
        return Result.ok();
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		rolePermissionService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }

}
