package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/12 0:04
 */
public class LackConfigValue extends ParentResultException{
    public LackConfigValue() {
        super("LACK_VALUE");
    }
}
