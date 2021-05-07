package com.freedy.backend.controller;

import com.freedy.backend.common.utils.DateUtils;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.constant.FileConstant;
import com.freedy.backend.dao.ResourceDao;
import com.freedy.backend.entity.ResourceEntity;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

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

    @PostMapping("/upload")
    public Result UploadFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            log.info("上传失败,文件为空");
            return Result.error(ResultCode.FILE_IS_EMPTY.getCode(), ResultCode.FILE_IS_EMPTY.getMessage());
        }
        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = uuid + "-" + originalFilename;
        //缩略图名称
        String thumbnailsName = uuid + "-"+ FileConstant.ZIP_IMAGE_InFIX+"-" + originalFilename;
        String date = DateUtils.formatDate(new Date());
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            ResourceEntity entity = new ResourceEntity("/image/" + date + "/" + fileName);
            //保存url
            resourceService.save(entity);
        },executor);
        File f = new File(System.getProperty("user.home") + "/.easy/image/" + date + "/");
        if (!f.exists()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("文件夹创建失败");
            }
        }
        Thumbnails.of(file.getInputStream())
                .scale(0.5f)
                .outputQuality(0.5f)
                .toFile(new File(f, thumbnailsName));
        file.transferTo(new File(f, fileName));
        f1.get();
        return Result.ok().put("url", "/image/" + date + "/" + fileName);
    }

    @GetMapping("/getImages")
    public Result getImages(@RequestParam Map<String, Object> params){
        PageUtils page = resourceService.queryPage(params);
        return Result.ok().setData(page);
    }

}
