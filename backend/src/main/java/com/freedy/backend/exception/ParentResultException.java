package com.freedy.backend.exception;

/**
 *
 * 此类用于返回错误消息
 *
 * 即继承此类后通过构造方法将ResultCode枚举类的名称传入,
 * 就可以当你抛出你的自定义异常类的时候自动根据ResultCode的code和msg返回给客户端.
 *
 * 例如:
 *   当抛出NoPermissionsException这个类时就会返回
 *   ResultCode里面名称是NO_PERMISSION的code与msg
 *
 * @author Freedy
 * @date 2021/5/12 0:06
 */
public class ParentResultException extends RuntimeException{
    public ParentResultException(String message) {
        super(message);
    }
}
