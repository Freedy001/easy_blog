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

    public static String subscribeTemplate(String email,String code){
        return "<div class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=\"mailContentContainer\" style=\"\">\n" +
                "  <style type=\"text/css\">\n" +
                "    .qmbox body {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      background: #fff;\n" +
                "      font-family: \"Verdana, Arial, Helvetica, sans-serif\";\n" +
                "      font-size: 14px;\n" +
                "      line-height: 24px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox img {\n" +
                "      border: none;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .contaner {\n" +
                "      margin: 0 auto;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .title {\n" +
                "      margin: 0 auto;\n" +
                "      background: url() #CCC repeat-x;\n" +
                "      height: 30px;\n" +
                "      text-align: center;\n" +
                "      font-weight: bold;\n" +
                "      padding-top: 12px;\n" +
                "      font-size: 16px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .content {\n" +
                "      margin: 4px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .biaoti {\n" +
                "      padding: 6px;\n" +
                "      color: #000;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xtop, .qmbox .xbottom {\n" +
                "      display: block;\n" +
                "      font-size: 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "      display: block;\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                "      height: 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "      border-left: 1px solid #BCBCBC;\n" +
                "      border-right: 1px solid #BCBCBC;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1 {\n" +
                "      margin: 0 5px;\n" +
                "      background: #BCBCBC;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb2 {\n" +
                "      margin: 0 3px;\n" +
                "      border-width: 0 2px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb3 {\n" +
                "      margin: 0 2px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb4 {\n" +
                "      height: 2px;\n" +
                "      margin: 0 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xboxcontent {\n" +
                "      display: block;\n" +
                "      border: 0 solid #BCBCBC;\n" +
                "      border-width: 0 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .line {\n" +
                "      margin-top: 6px;\n" +
                "      border-top: 1px dashed #B9B9B9;\n" +
                "      padding: 4px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .neirong {\n" +
                "      padding: 6px;\n" +
                "      color: #666666;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .foot {\n" +
                "      padding: 6px;\n" +
                "      color: #777;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_darkblue {\n" +
                "      color: #006699;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_lightblue {\n" +
                "      color: #008BD1;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_gray {\n" +
                "      color: #888;\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "  </style>\n" +
                "  <div class=\"contaner\">\n" +
                "    <div class=\"title\">订阅</div>\n" +
                "    <div class=\"content\">\n" +
                "      <p class=\"biaoti\"><b>亲爱的用户，你好！</b></p>\n" +
                "      <b class=\"xtop\"><b class=\"xb1\"></b><b class=\"xb2\"></b><b class=\"xb3\"></b><b class=\"xb4\"></b></b>\n" +
                "      <div class=\"xboxcontent\">\n" +
                "        <div class=\"neirong\">\n" +
                "          <p><b>请核对你的邮箱：</b><span id=\"userName\" class=\"font_darkblue\">"+email+"</span></p>\n" +
                "          <p style=\"display:inline-block;margin: 20px 0 30px 10px\"><b>你的验证码是：</b>\n" +
                "            <span class=\"font_lightblue\">\n" +
                "            <span id=\"yzm\" onclick=\"return false;\"\n" +
                "                  style=\"border-bottom: 1px dashed rgb(204, 204, 204);\n" +
                "                   z-index: 1; position: static; font-size: 30px\">"+code+"</span>\n" +
                "            </span>\n" +
                "            <br><span class=\"font_gray\">(请输入该验证码完成验证，验证码30分钟内有效！)</span></p>\n" +
                "          <div class=\"line\">如果你未申请订阅服务，请忽略该邮件。</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <b class=\"xbottom\"><b class=\"xb4\"></b><b class=\"xb3\"></b><b class=\"xb2\"></b><b class=\"xb1\"></b></b>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <style type=\"text/css\">\n" +
                "    .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                "      display: none !important;\n" +
                "    }\n" +
                "  </style>\n" +
                "</div>";
    }

    public static String articleNotifyTemplate(String url,String title) {
        return "<div class=\"qmbox qm_con_body_content qqmail_webmail_only\" id=\"mailContentContainer\" style=\"\">\n" +
                "  <style type=\"text/css\">\n" +
                "    .qmbox body {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "      background: #fff;\n" +
                "      font-family: \"Verdana, Arial, Helvetica, sans-serif\";\n" +
                "      font-size: 14px;\n" +
                "      line-height: 24px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox div, .qmbox p, .qmbox span, .qmbox img {\n" +
                "      margin: 0;\n" +
                "      padding: 0;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox img {\n" +
                "      border: none;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .contaner {\n" +
                "      margin: 0 auto;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .title {\n" +
                "      margin: 0 auto;\n" +
                "      background: url() #CCC repeat-x;\n" +
                "      height: 30px;\n" +
                "      text-align: center;\n" +
                "      font-weight: bold;\n" +
                "      padding-top: 12px;\n" +
                "      font-size: 16px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .content {\n" +
                "      margin: 4px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .biaoti {\n" +
                "      padding: 6px;\n" +
                "      color: #000;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xtop, .qmbox .xbottom {\n" +
                "      display: block;\n" +
                "      font-size: 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1, .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "      display: block;\n" +
                "      overflow: hidden;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1, .qmbox .xb2, .qmbox .xb3 {\n" +
                "      height: 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb2, .qmbox .xb3, .qmbox .xb4 {\n" +
                "      border-left: 1px solid #BCBCBC;\n" +
                "      border-right: 1px solid #BCBCBC;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb1 {\n" +
                "      margin: 0 5px;\n" +
                "      background: #BCBCBC;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb2 {\n" +
                "      margin: 0 3px;\n" +
                "      border-width: 0 2px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb3 {\n" +
                "      margin: 0 2px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xb4 {\n" +
                "      height: 2px;\n" +
                "      margin: 0 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .xboxcontent {\n" +
                "      display: block;\n" +
                "      border: 0 solid #BCBCBC;\n" +
                "      border-width: 0 1px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .line {\n" +
                "      margin-top: 6px;\n" +
                "      border-top: 1px dashed #B9B9B9;\n" +
                "      padding: 4px;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .neirong {\n" +
                "      padding: 6px;\n" +
                "      color: #666666;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .foot {\n" +
                "      padding: 6px;\n" +
                "      color: #777;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_darkblue {\n" +
                "      color: #006699;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_lightblue {\n" +
                "      color: #008BD1;\n" +
                "      font-weight: bold;\n" +
                "    }\n" +
                "\n" +
                "    .qmbox .font_gray {\n" +
                "      color: #888;\n" +
                "      font-size: 12px;\n" +
                "    }\n" +
                "  </style>\n" +
                "  <div class=\"contaner\">\n" +
                "    <div class=\"title\">订阅</div>\n" +
                "    <div class=\"content\">\n" +
                "      <p class=\"biaoti\"><b>亲爱的用户，你好！</b></p>\n" +
                "      <b class=\"xtop\"><b class=\"xb1\"></b><b class=\"xb2\"></b><b class=\"xb3\"></b><b class=\"xb4\"></b></b>\n" +
                "      <div class=\"xboxcontent\">\n" +
                "        <div class=\"neirong\">\n" +
                "          <p><b>您关注的博客更新一篇名为：</b><span id=\"userName\" class=\"font_darkblue\">"+title+"</span><b>的文章</b></p>\n" +
                "          <p><a href=\""+url+"\" style=\"text-decoration: none;color: #3a9ff5\">赶紧去看看吧</a></p>\n" +
                "          <div class=\"line\">如果你未申请订阅服务，请忽略该邮件。</div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <b class=\"xbottom\"><b class=\"xb4\"></b><b class=\"xb3\"></b><b class=\"xb2\"></b><b class=\"xb1\"></b></b>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  <style type=\"text/css\">\n" +
                "    .qmbox style, .qmbox script, .qmbox head, .qmbox link, .qmbox meta {\n" +
                "      display: none !important;\n" +
                "    }\n" +
                "  </style>\n" +
                "</div>\n";
    }
}
