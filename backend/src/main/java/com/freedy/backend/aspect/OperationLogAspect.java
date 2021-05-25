package com.freedy.backend.aspect;

import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.exception.ArgumentErrorException;
import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.Local;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 操作日志切面
 * 用于生成操作日志
 *
 * @author Freedy
 * @date 2021/5/18 10:19
 */
@Component
@Aspect
@Slf4j
public class OperationLogAspect {

    @Autowired
    private OperationLogService logService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Around("@annotation(com.freedy.backend.aspect.annotation.RecordLog)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method targetMethod = signature.getMethod();
        RecordLog recordLog = targetMethod.getDeclaredAnnotation(RecordLog.class);
        //构建操作日志实体类
        OperationLogEntity logEntity = new OperationLogEntity();
        logEntity.setCreatTime(System.currentTimeMillis());
        String nickname = Local.MANAGER_LOCAL.get().getNickname();
        logEntity.setOperator(nickname);
        logEntity.setIsSuccess(0);
        if (StringUtils.hasText(recordLog.logMsg())){
            logEntity.setOperationName(recordLog.logMsg());
        }else {
            logEntity.setOperationName(autoGuess(targetMethod.getName(), args[0], pjp.getTarget()));
        }
        logEntity.setOperationType(recordLog.type().name());
        try {
            Object proceed = pjp.proceed(args);
            //删除缓存
            Set<String> cacheKeys = redisTemplate.keys(CacheConstant.OPERATION_CACHE_NAME + "*");
            if (cacheKeys!=null&&cacheKeys.size()>0)
                redisTemplate.delete(Objects.requireNonNull(cacheKeys));
            return proceed;
        } catch (Throwable throwable) {
            logEntity.setIsSuccess(1);
            throw throwable;
        } finally {
            logService.save(logEntity);
        }
    }

    /**
     * 自动推测操作语句
     *
     * @param methodName 目标操作方法名称
     * @param arg        目标操作方法的参数
     * @param targetObj  目标操作方法所在的对象
     * @return 构成的操作语句
     */
    private String autoGuess(String methodName, Object arg, Object targetObj) throws Exception {
        boolean save = methodName.toLowerCase(Locale.ROOT).contains("save");
        boolean update = methodName.toLowerCase(Locale.ROOT).contains("update");
        boolean delete = methodName.toLowerCase(Locale.ROOT).contains("delete");
        boolean replay = methodName.toLowerCase(Locale.ROOT).contains("replay");
        boolean confirm = methodName.toLowerCase(Locale.ROOT).contains("confirm");
        boolean upload = methodName.toLowerCase(Locale.ROOT).contains("upload");
        boolean create = methodName.toLowerCase(Locale.ROOT).contains("create");
        boolean publish = methodName.toLowerCase(Locale.ROOT).contains("publish");
        boolean or = methodName.contains("Or");
        boolean draft = methodName.toLowerCase(Locale.ROOT).contains("draft");
        boolean setting = methodName.toLowerCase(Locale.ROOT).contains("setting");
        boolean article = methodName.toLowerCase(Locale.ROOT).contains("article");
        boolean user = methodName.toLowerCase(Locale.ROOT).contains("user");
        boolean shorthand = methodName.toLowerCase(Locale.ROOT).contains("shorthand");
        boolean category = methodName.toLowerCase(Locale.ROOT).contains("category");
        boolean tag = methodName.toLowerCase(Locale.ROOT).contains("tag");
        boolean comment = methodName.toLowerCase(Locale.ROOT).contains("comment");
        boolean file = methodName.toLowerCase(Locale.ROOT).contains("file");
        boolean password = methodName.toLowerCase(Locale.ROOT).contains("password");
        boolean info = methodName.toLowerCase(Locale.ROOT).contains("info");
        boolean common = methodName.toLowerCase(Locale.ROOT).contains("common");
        boolean smtp = methodName.toLowerCase(Locale.ROOT).contains("smtp");
        //构建操作语句
        StringBuilder builder = new StringBuilder();
        //参数class
        Class<?> argClazz = arg.getClass();
        //构建动词
        if (or) {
            //当名称中带or的是具有两个操作
            Field id = argClazz.getDeclaredField("id");
            id.setAccessible(true);
            //当没有传入id的时候代表修改
            if (id.get(arg) != null) {
                builder.append("修改");
            } else {
                //当传入id的时候代表添加或者创建...
                if (article) builder.append("保存");
                if (user) builder.append("创建");
            }
        } else {
            //单个操作
            if (save) builder.append("保存");
            if (update) builder.append("修改");
            if (delete) builder.append("删除");
            if (upload) builder.append("上传");
            if (replay) builder.append("回复了id为");
            if (create) builder.append("创建");
            if (publish) builder.append("发表");
            if (confirm) builder.append("审核通过");
        }
        //*************************动词分割线******************************
        StringBuilder operatorObj = new StringBuilder();
        //构建operatorObj操作对象
        if (arg instanceof Number[]) {
            //表示该方法是批量删除或者通过等等批量执行的方法
            List<Number> ids = Arrays.asList((Number[]) arg);
            if (ids.size() == 0) {
                throw new ArgumentErrorException();
            }
            //目标操作类 名称
            String targetName = targetObj.getClass().getSimpleName().replace("Controller", "ServiceImpl");
            //获取service的class
            Class<?> service = Class.forName("com.freedy.backend.service.impl." + targetName);
            //从ioc中获取操作对象
            Object proxyService = applicationContext.getBean(service);
            //获取给出id下的所有实体类
            List<Object> entities = (List<Object>) service.getMethod("listByIds", Collection.class).invoke(proxyService, ids);
            for (Object entity : entities) {
                //取出所有实体类中要删除的title或者name
                for (Field field : entity.getClass().getDeclaredFields()) {
                    String name = field.getName();
                    if (name.toLowerCase(Locale.ROOT).contains("name") || name.toLowerCase(Locale.ROOT).contains("title")) {
                        //获取所有参数中带name或者title的 即为操作对象
                        field.setAccessible(true);
                        operatorObj.append((String) field.get(entity)).append(",");
                        break;
                    }
                }
            }
            operatorObj.deleteCharAt(operatorObj.length() - 1);
        } else {
            for (Field field : argClazz.getDeclaredFields()) {
                String fieldName = field.getName();
                if (fieldName.toLowerCase(Locale.ROOT).contains("name") ||
                        fieldName.toLowerCase(Locale.ROOT).contains("title")||
                        fieldName.contains("fatherCommentId")) {
                    //获取所有参数中带name或者title的 即为操作对象
                    field.setAccessible(true);
                    Object targetName = field.get(arg);
                    String name="";
                    if (targetName instanceof String) {
                        name = (String) targetName;
                    }else if (targetName instanceof Long){
                        name=Long.toString((Long) targetName);
                    }
                    if (StringUtils.hasText(name)) {
                        operatorObj.append(name);
                        break;
                    }
                }
            }
        }
        builder.append(operatorObj.length() == 0 ? "" : comment ? " " + operatorObj + "的 " : " " + operatorObj + " ");
        //构建宾语
        if (article) builder.append("文章");
        if (category) builder.append("分类");
        if (tag) builder.append("标签");
        if (comment) builder.append("评论");
        if (shorthand) builder.append("速记");
        if (user) builder.append("用户");
        if (file) builder.append("文件");
        if (draft) builder.append("草稿");
        if (password) builder.append("密码");
        if (info) builder.append("信息");
        if (common) builder.append("常规");
        if (smtp) builder.append("SMTP");
        if (setting) builder.append("设置");
        log.debug("构建的日志字符串-->{}", builder);
        return builder.toString();
    }
}
