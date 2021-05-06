package com.freedy.backend;


import com.freedy.backend.common.utils.IPUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
public class Text {
    public static void main(String[] args) {
        HashSet<String> set1 = new HashSet<>(Arrays.asList("1", "2", "3", "455", "6"));
        System.out.println(set1.contains("1"));

    }
}

