package com.freedy.backend.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Result;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.properties.PermissionItemProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
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
    private PermissionItemProperties permissionItem;

    @ApiOperation("返回权限列表")
    @GetMapping("/getPermissionItem")
    public Result getPermissionItem(){
        NewUserVo userVo = new NewUserVo();
        userVo.setArticlePermission(new ArrayList<>(permissionItem.getArticlePermission().values()));
        userVo.setCommentPermission(new ArrayList<>(permissionItem.getCommentPermission().values()));
        userVo.setUserPermission(new ArrayList<>(permissionItem.getUserPermission().values()));
        userVo.setSettingPermission(new ArrayList<>(permissionItem.getSettingPermission().values()));
        HashMap<Object, Object> model = Arrays.stream(BeanUtils.getPropertyDescriptors(userVo.getClass()))
                .filter(itm -> !"class".equals(itm.getName()))
                .collect(HashMap::new, (map, pd) -> {
                        Object value = ReflectionUtils.invokeMethod(pd.getReadMethod(), userVo);
                        if (value!=null) map.put(pd.getName(), value);
                }, HashMap::putAll);
        model.put("isManager",AuthorityUtils.hasAnyAuthority("user-permission-manager","root-admin"));
        return Result.ok().setData(model);
    }
}
