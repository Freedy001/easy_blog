package com.freedy.backend.service.impl;

import com.freedy.backend.common.utils.Local;
import com.freedy.backend.constant.EntityConstant;
import com.freedy.backend.entity.*;
import com.freedy.backend.entity.vo.NewUserVo;
import com.freedy.backend.entity.vo.UserInfoVo;
import com.freedy.backend.enumerate.SettingEnum;
import com.freedy.backend.properties.PermissionItemProperties;
import com.freedy.backend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Query;

import com.freedy.backend.dao.ManagerDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ManagerEntity> page = this.page(
                new Query<ManagerEntity>().getPage(params),
                new QueryWrapper<ManagerEntity>()
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
        if (userEntity.getStatus().equals(EntityConstant.ROOT_ADMIN)){
            infoVo.setRootAdmin(true);
        }
        //获取主页uri
        CompletableFuture<Void> f2 = CompletableFuture.runAsync(() -> {
            SettingEntity page = settingService.getOne(new QueryWrapper<SettingEntity>()
                    .eq("item_name", SettingEnum.pageUrl.name()));
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
            infoVo.setTotalComment(totalComment==null?0:totalComment);
        }, executor);
        //获取访问总数
        Long totalVisit = articleService.getTotalVisit();
        infoVo.setTotalVisit(totalVisit==null?0:totalVisit);
        CompletableFuture.allOf(f2, f3, f4, f5, f6).get();
        return infoVo;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void createManager(NewUserVo manager) throws ExecutionException, InterruptedException {
        long time = System.currentTimeMillis();
        ManagerEntity entity = new ManagerEntity();
        String encodedPassword = passwordEncoder.encode(manager.getPassword());
        manager.setPassword(encodedPassword);
        BeanUtils.copyProperties(manager, entity);
        entity.setCreateTime(time);
        entity.setUpdateTime(time);
        baseMapper.insert(entity);
        long middleTime = System.currentTimeMillis();
        log.debug("baseMapper.insert(entity)耗时{}", middleTime- time);
        ArrayList<RolePermissionEntity> userPermissionList = new ArrayList<>();
        for (Field field : manager.getClass().getDeclaredFields()) {
            if(field.getType().getSimpleName().equals("List")){
                try {
                    //中文权限列表
                    field.setAccessible(true);
                    List<String> cnPermission = (List<String>) field.get(manager);
                    Field translateFiled = permissionItem.getClass().getDeclaredField(field.getName());
                    translateFiled.setAccessible(true);
                    //获取翻译map
                    Map<String, String> translateMap = (Map<String, String>) translateFiled.get(permissionItem);
                    translateMap.forEach((k,v)->{
                        for (String cName : cnPermission) {
                            if (v.equals(cName)){
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
        permissionService.saveBatch(userPermissionList);
        log.debug("createManager总耗时{}", System.currentTimeMillis()-time);
    }


}