package com.freedy.backend.service.impl;

import com.freedy.backend.exception.ArgumentErrorException;
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
                infoVo.setStatus("?????????");
            } else if (item.getStatus().equals(EntityConstant.USER_DISABLED)) {
                infoVo.setStatus("?????????");
            }
            Long createTime = item.getCreateTime();
            infoVo.setCreateDuration((System.currentTimeMillis() - createTime) / (1000 * 60 * 60 * 24) + "???");
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
        //?????????????????????
        infoVo.setPermissionStr(Local.PERMISSION_LOCAL.get());
        //??????????????????
        long timeLong = System.currentTimeMillis() - userEntity.getCreateTime();
        infoVo.setCreateDuration((timeLong / (1000 * 60 * 60 * 24)) + "???");
        if (userEntity.getStatus().equals(EntityConstant.ROOT_ADMIN)) {
            infoVo.setRootAdmin(true);
        }
        infoVo.setPageUrl("/");
        //??????????????????
        CompletableFuture<Void> f3 = CompletableFuture.runAsync(() -> {
            int totalArticle = articleService.count(new QueryWrapper<ArticleEntity>().eq("author_id", managerId));
            infoVo.setTotalArticle(totalArticle);
        }, executor);
        //??????????????????
        CompletableFuture<Void> f4 = CompletableFuture.runAsync(() -> {
            int totalCategory = categoryService.count(new QueryWrapper<CategoryEntity>().eq("creator_id", managerId));
            infoVo.setTotalCategory(totalCategory);
        }, executor);
        //??????????????????
        CompletableFuture<Void> f5 = CompletableFuture.runAsync(() -> {
            int totalTags = tagService.count(new QueryWrapper<TagEntity>().eq("creator_id", managerId));
            infoVo.setTotalTags(totalTags);
        }, executor);
        //??????????????????
        CompletableFuture<Void> f6 = CompletableFuture.runAsync(() -> {
            //???????????????????????????MANAGER_LOCAL????????????
            Local.MANAGER_LOCAL.set(userEntity);
            Long totalComment = articleService.getTotalComment();
            infoVo.setTotalComment(totalComment == null ? 0 : totalComment);
        }, executor);
        //??????????????????
        Long totalVisit = articleService.getTotalVisit();
        infoVo.setTotalVisit(totalVisit == null ? 0 : totalVisit);
        CompletableFuture.allOf( f3, f4, f5, f6).get();
        return infoVo;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void createOrUpdateManager(NewUserVo manager) throws ExecutionException, InterruptedException {
        //????????????
        permissionCheck(manager);
        long time = System.currentTimeMillis();
        ManagerEntity entity = new ManagerEntity();
        if (manager.getId() == null) {
            String password = manager.getPassword();
            if (!StringUtils.hasText(password)) throw new ArgumentErrorException();
            String encodedPassword = passwordEncoder.encode(password);
            manager.setPassword(encodedPassword);
            entity.setCreateTime(time);
        }
        BeanUtils.copyProperties(manager, entity);
        entity.setUpdateTime(time);
        CompletableFuture<Void> f1 = null;
        CompletableFuture<Void> f2 = null;
        if (manager.getId() == null) {
            //????????????nickname?????????
            entity.setNickname(UUID.randomUUID().toString().replaceAll("-","").substring(0,10));
            entity.setHeadImg("/resource/Boy-0"+(System.currentTimeMillis()%5+1)+".svg");
            baseMapper.insert(entity);
        } else {
            if (entity.getPassword() != null) {
                entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            }
            f1 = CompletableFuture.runAsync(() -> baseMapper.updateById(entity), executor);
            if (AuthorityUtils.hasAnyAuthority("user-permission-manager","root-admin")) {
                //????????????????????????
                f2 = CompletableFuture.runAsync(() -> permissionService.deletePermissionByUserIds(Collections.singletonList(entity.getId())));
            }
        }
        if (AuthorityUtils.hasAnyAuthority("user-permission-manager","root-admin")) {
            long middleTime = System.currentTimeMillis();
            log.debug("baseMapper.insert(entity)??????{}", middleTime - time);
            ArrayList<RolePermissionEntity> userPermissionList = new ArrayList<>();
            for (Field field : manager.getClass().getDeclaredFields()) {
                if ("List".equals(field.getType().getSimpleName())) {
                    try {
                        //??????????????????
                        field.setAccessible(true);
                        List<String> cnPermission = (List<String>) field.get(manager);
                        Field translateFiled = permissionItem.getClass().getDeclaredField(field.getName());
                        translateFiled.setAccessible(true);
                        //????????????map
                        Map<String, String> translateMap = (Map<String, String>) translateFiled.get(permissionItem);
                        translateMap.forEach((k, v) -> {
                            for (String cName : cnPermission) {
                                if (v.equals(cName)) {
                                    //???????????? ?????????????????????list???
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
            log.debug("userPermissionList????????????{}", System.currentTimeMillis() - middleTime);
            if (f2 != null) {
                f2.get();
            }
            permissionService.saveBatch(userPermissionList);
            if (manager.getId() != null) {
                //????????????????????? ?????????????????????
                redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER + entity.getUsername());
            }
            log.debug("createManager?????????{}", System.currentTimeMillis() - time);
            if (f1 != null) {
                f1.get();
            }
        }
    }

    @Override
    public void deleteUserByIds(List<Integer> list) {
        list.forEach(id->{
            //1??????????????? ????????????
            if (id.equals(1)) throw new NoPermissionsException();
        });
        List<String> usernames = baseMapper.getUsernamesByIds(list);
        for (String username : usernames) {
            //????????????
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
            //?????????????????????
            ManagerEntity entity = Local.MANAGER_LOCAL.get();
            permissionStr = Local.PERMISSION_LOCAL.get();
            BeanUtils.copyProperties(entity, userVo);
        } else {
            //?????????????????????
            f1 = CompletableFuture.runAsync(() -> {
                ManagerEntity entity = baseMapper.selectOne(new QueryWrapper<ManagerEntity>().eq("id", id));
                BeanUtils.copyProperties(entity, userVo);
            });
            permissionStr = permissionService.getPermissionsByManagerId(id);
        }
        //??????????????????
        for (PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(userVo.getClass())) {
            if ("List".equals(descriptor.getPropertyType().getSimpleName())){
                ArrayList<String> permissionNameList = new ArrayList<>();
                //???????????????????????????
                String typeName = descriptor.getName().replace("Permission", "");
                if (StringUtils.hasText(permissionStr)){
                    for (String permission : permissionStr.split(",")) {
                        if (permission.startsWith(typeName)) {
                            //???????????????
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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void createRootUser() {
        ManagerEntity entity = new ManagerEntity();
        entity.setId(1);
        entity.setUsername("root");
        entity.setNickname(UUID.randomUUID().toString().replaceAll("-","").substring(0,10));
        entity.setHeadImg("/resource/Boy-0"+(System.currentTimeMillis()%5+1)+".svg");
        entity.setPassword(passwordEncoder.encode("12345678"));
        entity.setStatus(1);
        entity.setEmail("xxxxxx@xxx.com");
        entity.setCreateTime(System.currentTimeMillis());
        entity.setUpdateTime(System.currentTimeMillis());
        baseMapper.createRoot(entity);
        RolePermissionEntity permissionEntity = new RolePermissionEntity();
        permissionEntity.setManagerId(1);
        permissionEntity.setPermissionValue("root-admin");
        permissionService.save(permissionEntity);
    }

    private void permissionCheck(NewUserVo manager){
        //????????????????????????
        if (AuthorityUtils.isRoot()) return;
        //1??????????????? ????????????
        if (manager.getId().equals(AuthorityUtils.ROOT_ADMIN)) {
            throw new NoPermissionsException();
        }
        //????????????????????????????????????????????????????????????
        if (!AuthorityUtils.hasAuthority("user-permission-manager")) {
            //?????????????????????????????? ??????
            try {
                for (Field field : manager.getClass().getDeclaredFields()) {
                    if ("List".equals(field.getType().getSimpleName())) {
                        field.setAccessible(true);
                        @SuppressWarnings("unchecked")
                        List<String> list = (List<String>) field.get(manager);
                        try {
                            //?????????????????? ????????????????????????????????????
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
        //??????user-permission-manager????????????????????? ??? ????????????????????????
        if (AuthorityUtils.hasAuthority("user-permission-manager")
                //????????????????????????
                &&AuthorityUtils.isUser(manager.getId())) return;
        String permissions = permissionService.getPermissionsByManagerId(manager.getId());
        if (AuthorityUtils.hasAuthority(permissions,"user-permission-manager")) {
            throw new NoPermissionsException();
        }
    }


}
