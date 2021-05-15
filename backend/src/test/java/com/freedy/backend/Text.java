package com.freedy.backend;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.common.utils.MarkDown;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Arrays;
/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
public class Text {
    public static void main(String[] args) throws Exception {
        myTest myTest = new myTest();
        String toJSONString = JSON.toJSONString(myTest);
        System.out.println(toJSONString);
    }
}

class myTest{
    public Integer getNum(){
        return 12;
    }

    public String getName(){
        return "小明";
    }
}

