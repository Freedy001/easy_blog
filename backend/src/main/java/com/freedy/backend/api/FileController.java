package com.freedy.backend.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.exception.FileDeleteErrorException;
import com.freedy.backend.utils.*;
import com.freedy.backend.constant.FileConstant;
import com.freedy.backend.entity.ResourceEntity;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/6 23:21
 */
@Slf4j
@RestController()
@RequestMapping("/backend/file")
public class FileController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Transactional(rollbackFor = Throwable.class)
    @PostMapping("/upload")
    @RecordLog(type = RecordEnum.UPLOAD)
    public Result UploadFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            log.info("上传失败,文件为空");
            return Result.error(ResultCode.FILE_IS_EMPTY.getCode(), ResultCode.FILE_IS_EMPTY.getMessage());
        }
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.hasText(originalFilename)) {
            String[] split = originalFilename.toLowerCase(Locale.ROOT).split("\\.");
            String suffix = split[split.length - 1];
            if ("jpg".equals(suffix) || "png".equals(suffix) || "jpeg".equals(suffix)) {
                //满足格式才能上传
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                String fileName = uuid + "-img." + suffix;
                //缩略图名称
                String thumbnailsName = uuid + "-" + FileConstant.ZIP_IMAGE_INFIX + "-img." + suffix;
                String date = DateUtils.formatDate(new Date());
                Integer managerId = Local.MANAGER_LOCAL.get().getId();
                CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
                    ResourceEntity entity = new ResourceEntity(
                            "/image/" + date + "/" + fileName, managerId);
                    //保存url
                    resourceService.save(entity);
                }, executor);
                File f = new File(System.getProperty("user.home") + "/.easy/image/" + date + "/");
                if (!f.exists()) {
                    if (!f.mkdirs()) {
                        throw new RuntimeException("文件夹创建失败");
                    }
                }
                //图片压缩
                Thumbnails.of(file.getInputStream())
                        .scale(0.5f)
                        .outputQuality(0.5f)
                        .toFile(new File(f, thumbnailsName));
                file.transferTo(new File(f, fileName));
                f1.get();
                return Result.ok().put("url", "/image/" + date + "/" + fileName);
            }
        }
        return Result.error(ResultCode.FILE_IS_NOT_SUIT.getCode(), ResultCode.FILE_IS_NOT_SUIT.getMessage());
    }

    @GetMapping("/getImages")
    public Result getImages(@RequestParam Map<String, Object> params) {
        PageUtils page = resourceService.queryPage(params);
        return Result.ok().setData(page);
    }

    @Transactional(rollbackFor = Throwable.class)
    @PostMapping("/delPic")
    public Result delPic(@RequestBody List<String> urls) {
        if (!AuthorityUtils.hasAuthority("root-admin")) {
            List<String> collect = urls.stream().map(url -> ResourceUrlUtil.ConvertToHDUrl(url.replaceFirst(".*?/image/", "/image/"))).collect(Collectors.toList());
            int count = resourceService.count(new QueryWrapper<ResourceEntity>().lambda()
                    .in(ResourceEntity::getResourceUrl, collect)
                    .and(wrapper -> wrapper.eq(ResourceEntity::getCreatorId, Local.MANAGER_LOCAL.get().getId())));
            if (urls.size() != count) {
                return Result.error(ResultCode.CAN_NOT_DEL_OTHER_ATTACHMENT.getCode(), ResultCode.CAN_NOT_DEL_OTHER_ATTACHMENT.getMessage());
            }
        }
        for (String url : urls) {
            String file = url.split("/image/", 2)[1];
            //文件绝对路径
            String fileName = System.getProperty("user.home") + "/.easy/image/" + file;
            File sd = new File(fileName);
            File hd = new File(ResourceUrlUtil.ConvertToHDUrl(fileName));
            resourceService.remove(new QueryWrapper<ResourceEntity>().lambda()
                    .eq(ResourceEntity::getResourceUrl, ResourceUrlUtil.ConvertToHDUrl("/image/" + file)));
            if (!sd.delete()) {
                throw new FileDeleteErrorException();
            }
            if (!hd.delete()) {
                throw new FileDeleteErrorException();
            }
        }
        return Result.ok();
    }

}
