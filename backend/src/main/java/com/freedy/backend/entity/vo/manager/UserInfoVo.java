package com.freedy.backend.entity.vo.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/4 14:44
 */
@Data
@ApiModel("用户信息回显实体类")
public class UserInfoVo {
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("个人说明")
    private String introduce;
    @ApiModelProperty("头像")
    private String headImg;
    @ApiModelProperty("是否是根管理员")
    private Boolean rootAdmin;
    @ApiModelProperty("是否是根管理员")
    private String status;
    @ApiModelProperty("主页url")
    private String pageUrl;
    @ApiModelProperty("建立时长")
    private String createDuration;
    @ApiModelProperty("总文章数")
    private Integer totalArticle;
    @ApiModelProperty("总分类数")
    private Integer totalCategory;
    @ApiModelProperty("总标签数")
    private Integer totalTags;
    @ApiModelProperty("总标评论数")
    private Long totalComment;
    @ApiModelProperty("总拜访量")
    private Long totalVisit;
}
