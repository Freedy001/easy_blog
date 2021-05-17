package com.freedy.backend.utils;

import com.freedy.backend.constant.FileConstant;

/**
 * @author Freedy
 * @date 2021/5/17 11:03
 */
public class ResourceUrlUtil {

    /**
     * 将压缩图和非压缩图的url相互转换
     *
     * 例如下面两行的url会相互之间转换
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-alex-azabache-3214944.jpg
     * /image/2021-05-07/facf3e527aaa475daecea71dc061ae62-thumbnails-alex-azabache-3214944.jpg
     */
    public static String ConvertUrl(String url){
        if (url.startsWith("/image/")){
            String[] split = url.split("-", 5);
            if (split[3].equals(FileConstant.ZIP_IMAGE_INFIX)){
                //转化为高清图
                return split[0]+"-"+split[1]+"-"+split[2]+"-"+split[4];
            }else {
                String[] spl = url.split("-", 4);
                return spl[0]+"-"+spl[1]+"-"+spl[2]+"-"+FileConstant.ZIP_IMAGE_INFIX+"-"+spl[4];
            }
        }else {
            return url;
        }
    }

}
