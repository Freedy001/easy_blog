package com.freedy.backend.utils;

import com.freedy.backend.constant.FileConstant;

import java.util.Scanner;

/**
 * @author Freedy
 * @date 2021/5/17 11:03
 */
public class ResourceUrlUtil {
    /**
     * 转化低清图片到高清图片
     * 如果本来就是高清或者url不符合规定就直接返回原字符串
     * <p>
     * 例如下面第一行转化为第二行
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-thumbnails-alex-azabache-3214944.jpg
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-alex-azabache-3214944.jpg
     * 或者
     * https://freedy.oss-cn-shanghai.aliyuncs.com/2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg?x-oss-process=image/resize,w_300
     * https://freedy.oss-cn-shanghai.aliyuncs.com/2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg
     */
    public static String ConvertToHDUrl(String url) {
        if (url==null) return null;
        if (url.startsWith("http")) {
            String[] split = url.split("/", 4);
            url = split[0] + "/" + split[1] + "/" + split[2] + HDConverter("/" + split[3]);
        } else {
            url = HDConverter(url);
        }
        return url;
    }

    private static String HDConverter(String url) {
        if (url.contains("?x-oss-process")) {
            return url.split("\\?x-oss-process")[0];
        }
        if (url.startsWith("/image/") || url.startsWith(System.getProperty("user.home"))) {
            String[] split = url.split("-", 5);
            if (split[3].equals(FileConstant.ZIP_IMAGE_INFIX)) {
                //转化为高清图
                return split[0] + "-" + split[1] + "-" + split[2] + "-" + split[4];
            }
        }
        return url;
    }


    /**
     * 转化高清图片到低清图片
     * 如果本来就是低清或者url不符合规定就直接返回原字符串
     * <p>
     * 例如下面第一行转化为第二行
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-alex-azabache-3214944.jpg
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-thumbnails-alex-azabache-3214944.jpg
     * 或者
     * https://freedy.oss-cn-shanghai.aliyuncs.com/2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg
     * https://freedy.oss-cn-shanghai.aliyuncs.com/2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg?x-oss-process=image/resize,w_300
     */
    public static String ConvertToSDUrl(String url) {
        return ConvertToSDUrl(url,300);
    }

    /**
     * 如果时Oss类型则可以指定缩放的宽度
     * 如果不是则第二个参数无效
     */
    public static String ConvertToSDUrl(String url,int picWidth) {
        if (url==null) return null;
        if (url.startsWith("http")) {
            String[] split = url.split("/", 4);
            url = split[0] + "/" + split[1] + "/" + split[2] + SDConverter("/" + split[3],picWidth);
        } else {
            url = SDConverter(url,picWidth);
        }
        return url;
    }

    private static String SDConverter(String url,int picWidth) {
        if (url.split("/")[1].matches("\\d{4}-\\d{2}-\\d{2}")) {
            if (!url.contains("?x-oss-process")) {
                return url + "?x-oss-process=image/resize,w_"+picWidth;
            }
        }
        if (url.startsWith("/image/")) {
            String[] spl = url.split("-", 4);
            if (!spl[3].startsWith(FileConstant.ZIP_IMAGE_INFIX)) {
                //转化为低清图
                return spl[0] + "-" + spl[1] + "-" + spl[2] + "-" + FileConstant.ZIP_IMAGE_INFIX + "-" + spl[3];
            }
        }
        return url;
    }


    /**
     * 将下面一行转化为下下行
     * https://freedy.oss-cn-shanghai.aliyuncs.com/2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg
     * -> 2021-05-31/413d8b75-287a-42a5-855e-62af07ae0c24nature-3824498.jpg
     */
    public static String getOssKeyByUrl(String url) {
        String hdUrl = ConvertToHDUrl(url);
        String[] split = hdUrl.split("/", 5);
        return split[3] + "/" + split[4];
    }


}
