package com.freedy.backend.controller;

import com.freedy.backend.common.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Freedy
 * @date 2021/5/6 23:21
 */
@Slf4j
@RestController("/backend/file")
public class FileController {

    @PostMapping("/upload")
    public Result UploadFile(MultipartFile file){
        if (file.isEmpty()){
            log.info("上传失败,文件为空");
            return Result.error(ResultCode.FILE_IS_EMPTY.getCode(),ResultCode.FILE_IS_EMPTY.getMessage());
        }
        String originalFilename = file.getOriginalFilename();
        String fileName=UUID.randomUUID().toString().replaceAll("-","")+"-"+originalFilename;
        try {
            file.transferTo(new File(System.getProperty("user.home") + ".easy", fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

}
