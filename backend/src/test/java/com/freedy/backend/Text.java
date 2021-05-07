package com.freedy.backend;


import com.freedy.backend.common.utils.DateUtils;
import com.freedy.backend.common.utils.IPUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
public class Text {
    public static void main(String[] args) throws IOException {
        for (Method method : ITest.class.getMethods()) {
            Type superclass = method.getReturnType().getGenericSuperclass();
            System.out.println(method.getReturnType().getGenericSuperclass().getTypeName());
        }
    }

}

