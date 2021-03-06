package com.freedy.backend.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.enumerate.EsType;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.dto.UserTokenInfo;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.entity.vo.manager.UserInfoVo;
import com.freedy.backend.entity.vo.manager.UserPasswordVo;
import com.freedy.backend.enumerate.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.freedy.backend.entity.ManagerEntity;
import com.freedy.backend.service.ManagerService;
import com.freedy.backend.utils.PageUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * 管理员表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Validated
@RestController
@RequestMapping("backend/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private LoadSetting loadSetting;

    @ApiOperation("查询当前用户消息")
    @GetMapping("/getUserInfo")
    public Result info() throws ExecutionException, InterruptedException {
        UserInfoVo infoVo = managerService.getUserInfo();
        Map<Object, Object> resultMap = Arrays.stream(BeanUtils.getPropertyDescriptors(infoVo.getClass())).filter(item -> !"class".equals(item.getName()))
                .collect(HashMap::new, (map, item) -> {
                    Object value = ReflectionUtils.invokeMethod(item.getReadMethod(), infoVo);
                    if (value != null) {
                        map.put(item.getName(), value);
                    }
                }, HashMap::putAll);
        resultMap.put("uploadMode",loadSetting.getUploadMode());
        return Result.ok().setData(resultMap);
    }

    @RecordLog(type = RecordEnum.USER)
    @ApiOperation("修改个人用户密码")
    @PostMapping("/updateUserPassword")
    public Result updateUserPassword(@Validated @RequestBody UserPasswordVo passwordVo) {
        ManagerEntity oldUserEntity = Local.MANAGER_LOCAL.get();
        if (passwordEncoder.matches(passwordVo.getOldPassword(), oldUserEntity.getPassword())) {
            //旧密码匹配成功 用户名需要下线
            redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + oldUserEntity.getUsername());
            ManagerEntity entity = new ManagerEntity();
            entity.setId(oldUserEntity.getId());
            entity.setPassword(passwordEncoder.encode(passwordVo.getNewPassword()));
            managerService.updateById(entity);
            return Result.ok(ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getCode(),
                    ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getMessage());
        }
        return Result.error(ResultCode.OLD_PASSWORD_NOT_CORRECT.getCode(),
                ResultCode.OLD_PASSWORD_NOT_CORRECT.getMessage());
    }

    @RecordLog(type = RecordEnum.USER)
    @ApiOperation("修改用户个人信息")
    @PostMapping("/updateUserInfo")
    public Result updateUserInfo(@Validated @RequestBody ManagerEntity manager) {
        ManagerEntity oldUserEntity = Local.MANAGER_LOCAL.get();
        String originUsername = oldUserEntity.getUsername();
        manager.setId(oldUserEntity.getId());
        Result result;
        //表示修改了nickname 文章需要重新上架
        boolean flag = manager.getNickname() != null && !originUsername.equals(manager.getNickname());
        if (manager.getUsername() != null && !originUsername.equals(manager.getUsername())) {
            //用户修改了用户名需要下线
            redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + originUsername);
            managerService.updateById(manager);
            result = Result.ok(ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getCode(),
                    ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getMessage());
        } else {
            //用户名一致 不需要下线
            managerService.updateById(manager);
            try {
                //修改缓存中的数据
                String userInfoJson = redisTemplate.opsForValue().get(RedisConstant.USER_TOKEN_HEADER + originUsername);
                UserTokenInfo tokenInfo = JSON.parseObject(userInfoJson, UserTokenInfo.class);
                ManagerEntity managerEntity = managerService.getById(oldUserEntity.getId());
                assert tokenInfo != null;
                tokenInfo.setManager(managerEntity);
                redisTemplate.opsForValue().set(RedisConstant.USER_TOKEN_HEADER + originUsername,
                        JSON.toJSONString(tokenInfo));
                result = Result.ok();
            } catch (Exception e) {
                //发生异常直接下线 说明序列化的UserTokenInfo已被破坏
                redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + originUsername);
                result = Result.ok(ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getCode(),
                        ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getMessage());
            }
        }
        EsTypeDto dto = new EsTypeDto();
        dto.setType(EsType.RELOAD);
        if (flag) rabbitTemplate.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME
                , RabbitConstant.ES_QUEUE_NAME, dto);
        return result;
    }

    @ApiOperation("列出所有用户")
    @PreAuthorize("hasAnyAuthority('user-manager','root-admin')")
    @GetMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        PageUtils page = managerService.queryPage(params);
        return Result.ok().put("page", page);
    }

    @PreAuthorize("hasAnyAuthority('user-manager','root-admin')")
    @ApiOperation("回显用户的权限等账号信息")
    @GetMapping("/getUserImportantInfo")
    public Result getUserImportantInfo(@NotNull @RequestParam("id") Integer id) throws Exception {
        NewUserVo userVo = managerService.getUserImportantInfo(id);
        return Result.ok().setData(userVo);
    }

    @RecordLog(type = RecordEnum.USER)
    @ApiOperation("创建或者更新新管理员")
    @PreAuthorize("hasAnyAuthority('user-manager','root-admin')")
    @PostMapping("/createOrUpdateManager")
    public Result createOrUpdateUser(@Validated @RequestBody NewUserVo manager) throws ExecutionException, InterruptedException {
        managerService.createOrUpdateManager(manager);
        if (AuthorityUtils.isUser(manager.getId())) {
            return Result.ok(ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getCode(),
                    ResultCode.USER_CERTIFICATE_HAS_BEEN_CHANGED.getMessage());
        }
        return Result.ok();
    }

    @RecordLog(type = RecordEnum.USER)
    @ApiOperation("删除用户")
    @PreAuthorize("hasAnyAuthority('user-permission-manager','root-admin')")
    @GetMapping("/delete")
    public Result deleteUser(@NotNull @RequestParam Integer[] ids) {
        for (Integer id : ids) {
            if (AuthorityUtils.isUser(id)) {
                throw new NoPermissionsException();
            }
        }
        managerService.deleteUserByIds(Arrays.asList(ids));
        return Result.ok();
    }

}
