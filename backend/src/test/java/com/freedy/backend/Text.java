package com.freedy.backend;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
public class Text {
    //模拟100万条评论大小
    public static void main(String[] args) throws Exception {
        File file = new File("C:\\Users\\Freedy\\Desktop\\资料\\test.assets\\test.txt");
        if (!file.exists()&&!file.createNewFile()){
            throw new RuntimeException("文件创建失败");
        }
        FileOutputStream stream = new FileOutputStream(file);
        String str="i am a test string , and i must be long as you see, ha ha !!";
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i <10; i++) {
            threads.add(new Thread(()->{
                try {
                    for (int j = 0; j < 100000; j++) {
                        stream.write(str.getBytes(StandardCharsets.UTF_8));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+Thread.currentThread().getName()+"完成写入");
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(item->{
            try {
                item.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stream.close();
    }
}

