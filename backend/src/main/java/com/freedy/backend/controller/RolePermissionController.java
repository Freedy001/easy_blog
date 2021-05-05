package com.freedy.backend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.freedy.backend.common.utils.Result;
import com.freedy.backend.entity.vo.NewUserVo;
import com.freedy.backend.properties.PermissionItemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.RolePermissionEntity;
import com.freedy.backend.service.RolePermissionService;
import com.freedy.backend.common.utils.PageUtils;


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

    /**
     * 返回权限 列表
     */
    @GetMapping("/getPermissionItem")
    public Result getPermissionItem(){
        NewUserVo userVo = new NewUserVo();
        userVo.setArticlePermission(new ArrayList<>(permissionItem.getArticlePermission().values()));
        userVo.setTagPermission(new ArrayList<>(permissionItem.getTagPermission().values()));
        userVo.setCommentPermission(new ArrayList<>(permissionItem.getCommentPermission().values()));
        userVo.setSettingPermission(new ArrayList<>(permissionItem.getSettingPermission().values()));
        return Result.ok().setData(userVo);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Integer id){
		RolePermissionEntity rolePermission = rolePermissionService.getById(id);

        return Result.ok().put("rolePermission", rolePermission);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Result save(@RequestBody RolePermissionEntity rolePermission){
		rolePermissionService.save(rolePermission);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody RolePermissionEntity rolePermission){
		rolePermissionService.updateById(rolePermission);

        return Result.ok();
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public Result delete(@RequestBody Integer[] ids){
		rolePermissionService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
