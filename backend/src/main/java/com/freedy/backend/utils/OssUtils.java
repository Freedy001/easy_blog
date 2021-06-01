package com.freedy.backend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.DeleteObjectsResult;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyuncs.regions.Endpoint;
import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.aspect.OperationLog;
import com.freedy.backend.exception.LackConfigValue;
import org.codehaus.jettison.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/31 16:39
 */
public class OssUtils {

    public static Map<String, String> generatePolicy(LoadSetting loadSetting) {
        try {
            String accessId = loadSetting.getAccessId(); // 请填写您的AccessKeyId。
            String accessKey = loadSetting.getAccessKey(); // 请填写您的AccessKeySecret。
            String endpoint = loadSetting.getEndpoint(); // 请填写您的 endpoint。
            String bucket = loadSetting.getBucket(); // 请填写您的 bucketname。
            String host = "http://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            String dir = DateUtils.formatDate(new Date()) + "/"; // 用户上传文件时指定的前缀。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            respMap.put("accessid", accessId);
            respMap.put("signature", postSignature);
            respMap.put("policy", encodedPolicy);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            return respMap;

        }catch (NullPointerException e){
            OperationLog.RecordLogManually("oss配置信息不全，无法上传",false,"system");
            throw new LackConfigValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBatch(List<String> urls,LoadSetting loadSetting){
        String accessId = loadSetting.getAccessId(); // 请填写您的AccessKeyId。
        String accessKey = loadSetting.getAccessKey(); // 请填写您的AccessKeySecret。
        String endpoint = loadSetting.getEndpoint(); // 请填写您的 endpoint。
        String bucket = loadSetting.getBucket(); // 请填写您的 bucketname 。
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        // 删除文件。key等同于ObjectName，表示删除OSS文件时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        List<String> keys = urls.stream().map(ResourceUrlUtil::getOssKeyByUrl).collect(Collectors.toList());
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucket).withKeys(keys);
        deleteObjectsRequest.setQuiet(false);
        ossClient.deleteObjects(deleteObjectsRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
