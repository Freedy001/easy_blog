package com.freedy.backend.service.impl;

import com.freedy.backend.utils.AuthorityUtils;
import com.freedy.backend.utils.Local;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.*;
import com.freedy.backend.entity.vo.manager.NewUserVo;
import com.freedy.backend.entity.vo.manager.UserInfoVo;
import com.freedy.backend.enumerate.SettingEnum;
import com.freedy.backend.exception.NoPermissionsException;
import com.freedy.backend.properties.PermissionItemProperties;
import com.freedy.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.utils.Query;

import com.freedy.backend.dao.ManagerDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("managerService")
public class ManagerServiceImpl extends ServiceImpl<ManagerDao, ManagerEntity> implements ManagerService {

    @Autowired
    private SettingService settingService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ThreadPoolExecutor executor;
    @Autowired
    private PermissionItemProperties permissionItem;
    @Autowired
    private RolePermissionService permissionService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ManagerEntity> page = this.page(
                new Query<ManagerEntity>().getPage(params),
                new QueryWrapper<>()
        );
        List<UserInfoVo> userInfoVos = page.getRecords().stream().map(item -> {
            UserInfoVo infoVo = new UserInfoVo();
            BeanUtils.copyProperties(item, infoVo);
            if (item.getStatus().equals(EntityConstant.ROOT_ADMIN)) {
                infoVo.setStatus("root admin");
            } else if (item.getStatus().equals(EntityConstant.USER_ENABLED)) {
                infoVo.setStatus("已启用");
            } else if (item.getStatus().equals(EntityConstant.USER_DISABLED)) {
                infoVo.setStatus("被禁止");
            }
            Long createTime = item.getCreateTime();
            infoVo.setCreateDuration((System.currentTimeMillis() - createTime) / (1000 * 60 * 60 * 24) + "天");
            return infoVo;
        }).collect(Collectors.toList());
        PageUtils utils = new PageUtils(page);
        utils.setList(userInfoVos);
        return utils;
    }

    @Override
    public UserInfoVo getUserInfo() throws ExecutionException, InterruptedException {
        ManagerEntity userEntity = Local.MANAGER_LOCAL.get();
        Integer managerId = userEntity.getId();
        UserInfoVo infoVo = new UserInfoVo();
        BeanUtils.copyProperties(userEntity, infoVo);
        //获取创建时长
        long timeLong = System.currentTimeMillis() - userEntity.getCreateTime();
        infoVo.setCreateDuration((timeLong / (1000 * 60 * 60 * 24)) + "天");
        if (userEntity.getStatus().equals(EntityConstant.ROOT_ADMIN)) {
            infoVo.setRootAdmin(true);
        }
        //获取主页uri
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            SettingEntity page = settingService.getOne(new QueryWrapper<SettingEntity>().lambda()
                    .eq(SettingEntity::getItem, SettingEnum.pageUrl.name()));
            infoVo.setPageUrl(page.getItem());
        }, executor);
        //获取文章总数
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> {
            int totalArticle = articleService.count(new QueryWrapper<ArticleEntity>().eq("author_id", managerId));
            infoVo.setTotalArticle(totalArticle);
        }, executor);
        //获取分类总数
        CompletableFuture<Void> f4 = CompletableFuture.runAsync(() -> {
            int totalCategory = categoryService.count(new QueryWrapper<CategoryEntity>().eq("creator_id", managerId));
            infoVo.setTotalCategory(totalCategory);
        }, executor);
        //获取标签总数
        CompletableFuture<Void> f5 = CompletableFuture.runAsync(() -> {
            int totalTags = tagService.count(new QueryWrapper<TagEntity>().eq("creator_id", managerId));
            infoVo.setTotalTags(totalTags);
        }, executor);
        //获取评论总数
        CompletableFuture<Void> f6 = CompletableFuture.runAsync(() -> {
            //让子线程也能获取到MANAGER_LOCAL里面的值
            Local.MANAGER_LOCAL.set(userEntity);
            Long totalComment = articleService.getTotalComment();
            infoVo.setTotalComment(totalComment == null ? 0 : totalComment);
        }, executor);
        //获取访问总数
        Long totalVisit = articleService.getTotalVisit();
        infoVo.setTotalVisit(totalVisit == null ? 0 : totalVisit);
        CompletableFuture.allOf(f2, f3, f4, f5, f6).get();
        return infoVo;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createOrUpdateManager(NewUserVo manager) throws ExecutionException, InterruptedException {
        //检查权限
        permissionCheck(manager);
        long time = System.currentTimeMillis();
        ManagerEntity entity = new ManagerEntity();
        if (manager.getId() == null) {
            String encodedPassword = passwordEncoder.encode(manager.getPassword());
            manager.setPassword(encodedPassword);
            entity.setCreateTime(time);
        }
        BeanUtils.copyProperties(manager, entity);
        entity.setUpdateTime(time);
        CompletableFuture<Void> f1 = null;
        CompletableFuture<Void> f2 = null;
        if (manager.getId() == null) {
            baseMapper.insert(entity);
        } else {
            if (entity.getPassword() != null) {
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            }
            f1 = CompletableFuture.runAsync(() -> baseMapper.updateById(entity), executor);
            if (AuthorityUtils.hasAnyAuthority("user-permission-manager","root-admin")) {
                //删除权限重新赋值
                f2 = CompletableFuture.runAsync(() -> permissionService.deletePermissionByUserIds(Collections.singletonList(entity.getId())));
            }
        }
        if (AuthorityUtils.hasAnyAuthority("user-permission-manager","root-admin")) {
            long middleTime = System.currentTimeMillis();
            log.debug("baseMapper.insert(entity)耗时{}", middleTime - time);
            ArrayList<RolePermissionEntity> userPermissionList = new ArrayList<>();
            for (Field field : manager.getClass().getDeclaredFields()) {
                if ("List".equals(field.getType().getSimpleName())) {
                    try {
                        //中文权限列表
                        field.setAccessible(true);
                        List<String> cnPermission = (List<String>) field.get(manager);
                        Field translateFiled = permissionItem.getClass().getDeclaredField(field.getName());
                        translateFiled.setAccessible(true);
                        //获取翻译map
                        Map<String, String> translateMap = (Map<String, String>) translateFiled.get(permissionItem);
                        translateMap.forEach((k, v) -> {
                            for (String cName : cnPermission) {
                                if (v.equals(cName)) {
                                    //匹配成功 将权限信息放入list中
                                    RolePermissionEntity permissionEntity = new RolePermissionEntity();
                                    permissionEntity.setManagerId(entity.getId());
                                    permissionEntity.setPermissionValue(k);
                                    userPermissionList.add(permissionEntity);
                                    break;
                                }
                            }
                        });
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("userPermissionList构建时长{}", System.currentTimeMillis() - middleTime);
            if (f2 != null) {
                f2.get();
            }
            permissionService.saveBatch(userPermissionList);
            if (manager.getId() != null) {
                //如果时修改的话 要让该用户下线
                redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + entity.getUsername());
            }
            log.debug("createManager总耗时{}", System.currentTimeMillis() - time);
            if (f1 != null) {
                f1.get();
            }
        }
    }

    @Override
    public void deleteUserByIds(List<Integer> list) {
        list.forEach(id->{
            //1是根管理员 权限最大
            if (id.equals(1)) throw new NoPermissionsException();
        });
        List<String> usernames = baseMapper.getUsernamesByIds(list);
        for (String username : usernames) {
            //退出登录
            AuthorityUtils.logout(username);
        }
        permissionService.deletePermissionByUserIds(list);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public NewUserVo getUserImportantInfo(Integer id) throws Exception {
        NewUserVo userVo = new NewUserVo();
        String permissionStr;
        CompletableFuture<Void> f1 = null;
        if (id.equals(Local.MANAGER_LOCAL.get().getId())) {
            //获取自己的权限
            ManagerEntity entity = Local.MANAGER_LOCAL.get();
            permissionStr = Local.PERMISSION_LOCAL.get();
            BeanUtils.copyProperties(entity, userVo);
        } else {
            //获取他人的权限
            f1 = CompletableFuture.runAsync(() -> {
                ManagerEntity entity = baseMapper.selectOne(new QueryWrapper<ManagerEntity>().eq("id", id));
                BeanUtils.copyProperties(entity, userVo);
            });
            permissionStr = permissionService.getPermissionsByManagerId(id);
        }
        //回显权限消息
        for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(userVo.getClass())) {
            if ("List".equals(descriptor.getPropertyType().getSimpleName())){
                ArrayList<String> permissionNameList = new ArrayList<>();
                //对应属性列表的名称
                String typeName = descriptor.getName().replace("Permission", "");
                if (StringUtils.hasText(permissionStr)){
                    for (String permission : permissionStr.split(",")) {
                        if (permission.startsWith(typeName)) {
                            //首字母大写
                            String upperType = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
                            //noinspection unchecked
                            Map<String, String> invoke = (Map<String, String>) permissionItem.getClass().getDeclaredMethod("get" + upperType + "Permission").invoke(permissionItem);
                            String permissionName = invoke.get(permission);
                            permissionNameList.add(permissionName);
                        }
                    }
                    descriptor.getWriteMethod().invoke(userVo,permissionNameList);
                }
            }
        }
        if (f1 != null) {
            f1.get();
        }
        userVo.setPassword(null);
        return userVo;
    }

    private void permissionCheck(NewUserVo manager){
        //根管理员直接返回
        if (AuthorityUtils.isRoot()) return;
        //1是根管理员 权限最大
        if (manager.getId().equals(AuthorityUtils.ROOT_ADMIN)) {
            throw new NoPermissionsException();
        }
        //最低级管理员只能创建用户不能带有任何权限
        if (!AuthorityUtils.hasAuthority("user-permission-manager")) {
            //没有管理用户权限的的 权限
            try {
                for (Field field : manager.getClass().getDeclaredFields()) {
                    if ("List".equals(field.getType().getSimpleName())) {
                        field.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        List<String> list = (List<String>) field.get(manager);
                        try {
                            //只能创建用户 如果给了权限就直接抛异常
                            if (list.size() > 0) {
                                throw new NoPermissionsException();
                            }
                        } catch (NullPointerException ignore) {
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //有用user-permission-manager权限的只有自己 和 根管理员可以操作
        if (AuthorityUtils.hasAuthority("user-permission-manager")
                //判断是否操作自己
                &&AuthorityUtils.isUser(manager.getId())) return;
        String permissions = permissionService.getPermissionsByManagerId(manager.getId());
        if (AuthorityUtils.hasAuthority(permissions,"user-permission-manager")) {
            throw new NoPermissionsException();
        }
    }


}
