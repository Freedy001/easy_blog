package com.freedy.backend.utils;

import com.freedy.backend.constant.FileConstant;

/**
 * @author Freedy
 * @date 2021/5/17 11:03
 */
public class ResourceUrlUtil {

    /**
     * 转化低清图片到高清图片
     * 如果本来就是高清或者url不符合规定就直接返回原字符串
     *
     * 例如下面第一行转化为第二行
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-thumbnails-alex-azabache-3214944.jpg
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-alex-azabache-3214944.jpg
     */
    public static String ConvertToHDUrl(String url){
        if (url.startsWith("/image/")){
            String[] split = url.split("-", 5);
            if (split[3].equals(FileConstant.ZIP_IMAGE_INFIX)){
                //转化为高清图
                return split[0]+"-"+split[1]+"-"+split[2]+"-"+split[4];
            }
        }
        return url;
    }

    /**
     * 转化高清图片到低清图片
     * 如果本来就是低清或者url不符合规定就直接返回原字符串
     *
     * 例如下面第一行转化为第二行
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-alex-azabache-3214944.jpg
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-thumbnails-alex-azabache-3214944.jpg
     */
    public static String ConvertToSDUrl(String url){
        if (url.startsWith("/image/")){
            String[] spl = url.split("-", 4);
            if (!spl[3].startsWith(FileConstant.ZIP_IMAGE_INFIX)){
                //转化为低清图
                return spl[0]+"-"+spl[1]+"-"+spl[2]+"-"+FileConstant.ZIP_IMAGE_INFIX+"-"+spl[3];
            }
        }
        return url;
    }

}
