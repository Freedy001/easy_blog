create table if not exists blog_category
(
    id               int auto_increment comment '主键',
    category_name    varchar(50)       null comment '分类名称',
    category_img_url varchar(300)      null comment '图片url',
    priority         tinyint default 0 null comment '优先级 越小越大',
    creator_id       int               null comment '创建者id',
    constraint blog_category_id_uindex
        unique (id)
)
comment '分类表';

alter table blog_category
    add primary key (id);

create table if not exists blog_article
(
    id                  bigint auto_increment comment '主键',
    title               varchar(50)       not null comment '文章标题',
    article_desc        varchar(200)      null comment '文章描述',
    author_id           int               not null comment '文章作者id',
    article_poster      varchar(300)      null comment '文章封面',
    content             longtext          null comment '文章类容',
    word_num            bigint            null comment '文章字数',
    visit_num           bigint  default 0 null comment '访问数量',
    comment_num         bigint  default 0 null comment '评论数量',
    like_num            bigint  default 0 null comment '点赞数量',
    article_status      tinyint default 0 null comment '文章状态 1:未发布 2:回收站 3:已发布 4:顶置 5:推荐 ',
    article_comment     tinyint default 0 null comment '是否顶置 0可以评论 1不允许评论',
    article_category_id int               null comment '分类id',
    create_time         bigint            null comment '文章创建时间',
    update_time         bigint            null comment '文章更改时间',
    publish_time        bigint            null comment '发布时间',
    constraint blog_article_id_uindex
        unique (id),
    constraint blog_article_blog_category_id_fk
        foreign key (article_category_id) references blog_category (id)
) AUTO_INCREMENT=1388865816372539446 comment '文章表';

create index blog_article_author_id_index
    on blog_article (author_id);

create index blog_article_title_index
    on blog_article (title);

alter table blog_article
    add primary key (id);

create table if not exists blog_comment
(
    id                bigint auto_increment comment '主键',
    article_id        bigint      not null comment '文章id
',
    father_comment_id bigint      null comment '父评论id',
    flore             int         null comment '当前评论的楼数',
    content           text        not null comment '评论类容',
    username          varchar(50) not null comment '评论人名称',
    email             varchar(50) null comment '评论人邮箱',
    ip                varchar(20) null comment '评论人ip',
    has_read          tinyint(1)  null comment '是否已读',
    comment_status    tinyint(1)  null comment '评论状态 0待审核 1发布',
    region            varchar(50) null comment '评论人地区',
    create_time       bigint      null comment '评论时间',
    constraint blog_comment_id_uindex
        unique (id),
    constraint blog_comment_blog_article_id_fk
        foreign key (article_id) references blog_article (id)
)
    comment '评论表';

create index blog_comment_father_comment_id_index
    on blog_comment (father_comment_id);

alter table blog_comment
    add primary key (id);

create table if not exists blog_manager
(
    id          int auto_increment comment '主键',
    nickname    varchar(50)       null comment '名称',
    username    varchar(50)       null comment '用户名',
    password    varchar(60)       null comment '密码
',
    email       varchar(50)       null comment '电子邮箱',
    introduce   text              null comment '个人说明',
    head_img    varchar(300)      null comment '头像',
    create_time bigint            null comment '创建时间',
    update_time bigint            null comment '更改时间',
    status      tinyint default 2 null comment '状态 1主管理员 2启用 3不启用',
    constraint blog_manager_id_uindex
        unique (id),
    constraint blog_manager_username_uindex
        unique (username)
)
    comment '管理员表';

alter table blog_manager
    add primary key (id);

create table if not exists blog_operation_log
(
    id             bigint auto_increment comment '主键',
    operation_name text        null comment '操作名称 由于批量操作的存在 可能会产生很长的名称
所以这里设置为 text',
    operator       varchar(50) null comment '操作者',
    operation_type varchar(20) null comment '操作类型',
    is_success     tinyint(1)  null comment '是否操作成功 0成功 1失败',
    creat_time     bigint      null comment '操作时间',
    constraint blog_operation_log_id_uindex
        unique (id)
);

alter table blog_operation_log
    add primary key (id);

create table if not exists blog_resource
(
    id           bigint auto_increment,
    resource_url varchar(100) null comment '图片url
',
    creator_id   int          null,
    create_time  bigint       null comment '创建时间',
    constraint blog_resource_id_uindex
        unique (id),
    constraint blog_resource_resource_url_uindex
        unique (resource_url)
);

alter table blog_resource
    add primary key (id);

create table if not exists blog_role_permission
(
    id               int auto_increment comment '主键',
    manager_id       int         null comment '管理员id',
    permission_value varchar(50) null comment '权限值',
    constraint blog_role_permission_id_uindex
        unique (id),
    constraint blog_role_permission_blog_manager_id_fk
        foreign key (manager_id) references blog_manager (id)
)
    comment '角色权限表';

alter table blog_role_permission
    add primary key (id);

create table if not exists blog_setting
(
    id    int auto_increment comment '主键',
    item  varchar(50)  null comment '设置项名称',
    value varchar(300) null comment '设置项值',
    constraint blog_setting_id_uindex
        unique (id)
)
    comment '系统设置表';

alter table blog_setting
    add primary key (id);

create table if not exists blog_shorthand
(
    id          bigint       null,
    content     varchar(350) null,
    create_time bigint       null,
    manager_id  int          null
);

create table if not exists blog_subscriber
(
    id               int auto_increment,
    subscriber_email varchar(50) null comment '订阅者的邮箱',
    create_time      bigint      null comment '创建时间',
    constraint blog_subscriber_id_uindex
        unique (id),
    constraint blog_subscriber_subscriber_email_uindex
        unique (subscriber_email)
);

alter table blog_subscriber
    add primary key (id);

create table if not exists blog_tag
(
    id          int auto_increment comment '主键',
    tag_name    varchar(50)       not null comment '标签名称',
    tag_img_url varchar(300)      null comment '标签图片',
    priority    tinyint default 0 null comment '越小优先级越高',
    creator_id  int               null comment '创建者id
',
    constraint blog_tag_id_uindex
        unique (id),
    constraint blog_tag_tag_name_uindex
        unique (tag_name)
)
    comment '文章标签';

alter table blog_tag
    add primary key (id);

create table if not exists blog_article_tag_relation
(
    id         int auto_increment comment '主键',
    tag_id     int    null comment '标签id',
    article_id bigint null comment '文章id',
    constraint blog_article_tag_relation_id_uindex
        unique (id),
    constraint blog_article_tag_relation_blog_article_id_fk
        foreign key (article_id) references blog_article (id),
    constraint blog_article_tag_relation_blog_tag_id_fk
        foreign key (tag_id) references blog_tag (id)
)
    comment '标签文章关联表';

alter table blog_article_tag_relation
    add primary key (id);

create table if not exists blog_visitor
(
    id             bigint auto_increment comment '主键',
    ip_address     varchar(20) null comment '访客ip',
    region         varchar(50) null comment '访客地域',
    visit_time     bigint      null comment '访问时间',
    visit_duration bigint      null comment '访问时长',
    constraint blog_visitor_id_uindex
        unique (id)
)
    comment '访客记录表';

alter table blog_visitor
    add primary key (id);

