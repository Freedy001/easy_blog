package com.freedy.backend.utils;

/**
 * 邮件的模板
 * @author Freedy
 * @date 2021/5/17 18:42
 */
public class EmailTemplate {

    public static String replayTemplate(String fatherCommentUsername,String commentTime,
                                        String sonCommentUsername,String fatherComment,
                                        String sonComment,String articleUrl){
        return "  <div style=\"display: flex;flex-direction: column;color: #424242;line-height: 20px\">\n" +
                "    <h1>有人回复你的评论啦\uD83D\uDE00</h1>\n" +
                "    <p style=\"margin: 8px 0\">亲爱的 "+fatherCommentUsername+" 你好:</p>\n" +
                "    <p style=\"margin: 8px 0;text-indent: 20px\">"+sonCommentUsername+" 在"+commentTime+"回复了你的评论\""+fatherComment+"\"</p>\n" +
                "    <p style=\"margin: 8px 0;text-indent: 20px\">内容是:\""+sonComment+"\"</p>\n" +
                "    <p style=\"margin: 8px 0;text-indent: 20px\">赶紧去<a href=\""+articleUrl+"\" style=\"color:#fc0000\">article</a>查看吧!</p>\n" +
                "  </div>";
    }

}
