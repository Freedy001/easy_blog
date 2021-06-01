# easy_blog

## 介绍

easy blog是一个后端基于springboot 前端基于vue3的前后端分离博客系统。它可以实现多人在线编辑发表博客。具有写文章，写笔记，写速记，角色的权限管理等待功能。

提供前台页面，后台页面与后台服务的全套代码。

总体比较简约。前端的页面设计借鉴了[这位大佬的博客](https://github.com/wsydxiangwang/Mood)。

## 使用技术

#### 后端技术

* springboot 2.4.5
    * 核心框架
* springSecurity+JWT
    * 基于token机制的权限认证。
* mybatis-plus 3.4.2
    * 用作数据库操作
* redis
    * 用于缓存
    * 用于存储用户token
    * 用于记录文章的一些参数 如访客数、点赞数等等
* elasticSearch 7.6.2
    * 用于文章的搜索
* rabbitmq
    * 消息队列，实现异步处理数据
* swagger
    * 自动生成接口文章
* thumbnailator
    * 上传图片的压缩
* flexmark
    * markdown 文本的解析

### 前端技术

* vue3
    * 前端核心框架
* vite
    * 构建工具
* vue-roouter
    * 前端路由导航
* vuex
    * 组件间的通讯
* elementUI-plus
    * ui框架
* echarts
    * 数据可视化
* @toast-ui/editor
    * markdown 文本编辑器
* highlight.js
    * 代码高亮

## 安装

### 使用docker一键部署

1. 安装docker 可以去官方看教程https://docs.docker.com/engine/install/centos/
2. 安装docker-compose
    1. 下载docker-compose

    ``` shell
    sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    ```

    2. 设置权限

    ``` shell
    sudo chmod +x /usr/local/bin/docker-compose
    ```

    3. 检测是否安装成功

    ``` shell
     docker-compose --version
    ```

    如果出现版本信息代表安装完毕.
    如果没有出现可以去官网看看详细安装教程https://docs.docker.com/compose/install/
3. 下载我发行的docker-compose.zip

``` shell
wget https://github.com/Freedy001/easy_blog/releases/download/v1.0/docker-compose.zip.zip
```

3. 解压

``` shell
unzip docker-compose.zip.zip
```

4. 在当前目录下输入docker-compose一键启动

``` shell
docker-compose up -d
```

5. docker ps检测是否安装成功

``` shell
[root@freedy freedy]# docker ps
CONTAINER ID   IMAGE                    CREATED       STATUS       PORTS                              NAMES
8bdcf84332f7   ebbuild_app              5 hours ago   Up 5 hours   0.0.0.0:80->80/tcp, :::80->80/tcp  blog-app
fe28637baff4   ebbuild_rabbitmq         5 hours ago   Up 5 hours   5671-5672/tcp            		 rabbitmq_delayedQueue
8e4ccd86f7f2   ebbuild_elastic-search   5 hours ago   Up 5 hours   9200/tcp, 9300/tcp                 elasticSearch_ik
f82e786067e2   ebbuild_mysql            5 hours ago   Up 5 hours   3306/tcp, 33060/tcp                mysql
aa9afa2ae9be   redis                    5 hours ago   Up 5 hours   6379/tcp                           redis
```

出现上面详细代表安装成功.(这里的信息被我简化了)

### 手动部署

1. 配置环境 见下面的本地开发
2. 和上面一样下载docker-compose.zip。里面有一个easyBlog-1.jar的jar包，和一个application.yaml的文件。
3. 修改application.yaml配置，将相关服务的地址配成你所安装的服务的地址

``` yaml
spring:
  datasource:
    url: jdbc:mysql://mysql:3306/blog_db?serverTimezone=UTC  #需要在mysql中创建blog_db数据库
    username: root #改为自己设置的用户名
......
......
#下面的配置也要修改
#修改为自己配置的地址与端口
```

4. 启动

``` shell
java -jar easyBlog-1.jar
```

## 本地开发

### 环境需要

1. mysql
2. redis
3. elasticSearch 需要安装ik分词
4. rabbitmq 需要安装延时队列的插件

上面的环境安装推荐用docker。

mysql

``` shell
docker run -p 3306:3306 --name mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:latest
#执行上面的命令后会自动去官网拉去镜像，如果速度慢可以换阿里云的镜像。
```

redis

``` shell
docker run -p 6379:6379 --name redis -d redis:latest
```

elasticSearch

``` shell
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e  "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \
-d elasticsearch:7.6.2
```

rabbitmq

``` shell
docker run -dit --name Myrabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq:management
```

elasticSearch 插件的安装需要去https://github.com/medcl/elasticsearch-analysis-ik/releases 下载安装。找到与自己es版本一致插件，下载文件然后解压到es的plugin目录下（我的目录是/usr/share/elasticsearch/plugins）

``` shell
docker cp (你下载的插件) elasticSearch:/usr/share/elasticsearch/plugins
```

然后重启就行了是不是很简单😂。

``` shell
docker restart elasticsearch
```

rabbitmq 延时队列的插件也和上面相似。先去官网下载 https://github.com/rabbitmq/rabbitmq-rtopic-exchange/releases/tag/v3.8.0与自己版本一致的插件。

复制插件到plugins目录里面

``` shell
docker cp rabbitmq_delayed_message_exchange-xxxx.ez(这里换成你下载的插件名称) rabbitmq:/plugins
```

进入docker内部：

``` shell
docker exec -it rabbitmq bash
```

开启插件

``` shell
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

最后和上面一样重启ok了。

``` shell
docker restart rabbitmq
```

### 修改配置

配置好环境后，修改backend/src/resources/application.yaml 里面的数据库、redis、rabbitmq、es的连接信息

### 项目启动

``` shell
启动后台 BackendApplication

然后前台
cd app-frontend 
npm install
npm run dev

cd manage-frontend
npm install
npm run dev
```

### 说明

> 程序会在启动的时候自动检相关表是否存在,当检测到没有相关表时会自动创建表.
>
> 原理就是先查询表是否存在不存在就执行sql脚本创建表.

## 模块分层

### 后端模块

``` shell
dbblog
├── api  # 后台页面接口
│
├── apiFront   # 前台页面接口
│
├──| aspect # 切面
|  |
|  └── annotation # aop注解
|
├──| config # springboot的配置包
│  |
|  └──security # springsecurity的配置包
├── dao # 持久层
│
├── entity # 实体类包
│
├── enumerate # 枚举包
│
├──| middleWare # 相关中间件的包
│  |
|  └──es # es的数据模型与数据操作dao
|  |
|  └──mqCreator # 用于创建交换机与队列
|
├── scheduled # 定时任务
│
├── service # service层
│
├── utils # 主要工具类
│
├── BackendApplication # 主启动类
```

### 前端模块

两个前端模块的结构差不多,都很简单.

* assets 静态资源
* components 前端组件
* router 路由包
* store vuex包
* util 工具包
* view 页面包

## 前台页面

可以去->http://www.freedy.xyz/自己看

页面很简单,功能也很简单.所有的功能都是围绕着`博客`这个主题

总共有有7个页面:

* 主页 (博客列表)
* 文章页面
* 菜单页面
* 速记页面
* 速记页面
* 订阅页面
* 关于页面

还有一个我最喜欢的功能,那就是深色模式了.

对着电脑屏幕看白色背景的文章时间长了着实会让眼睛不舒服,所以我加上了贴心的深色模式.

## 后台页面

### 仪表盘
<br>
都是一些统计信息，也可以发表速记（相当于qq的个性签名）。

![image](E:\MyProjects\easy_blog\README.assets\3dd7bb4e-710e-4860-8f98-7438e7899bf5dsafa.png)

### 写文章
<br>
文章编辑器为markdown。它支持两种模式，一种是markdown一种所见即所得（右下角）

如果一次没有写完可以保存到草稿。也可直接发布的哈，只要你不怕半成品被别人看到。
![image](E:\MyProjects\easy_blog\README.assets\6d69da24-774f-48a5-9c70-babdd43013f1verSDVD.png)

### 文章列表
在这可以管理文章，可以点击左边的箭头查看更加详细的信息。

> 这里的文章列表默认只能看到自己的文章，如有拥有能管理他人文章的权限，则可以在这看到他人的文章 。
> 权限功能可以在用户->用户管理那里设置

![image](E:\MyProjects\easy_blog\README.assets\c6e89795-bf85-4a74-8bad-0a983ed7d49cF23TE3.png)

### 评论列表
在这可以看到和管理自己发布文章下面的评论。也可以点左边的箭头看到详细信息。
> 这里和上面的文章列表类似，默认只能看到自己的。

![image](E:\MyProjects\easy_blog\README.assets\2c82526c-3926-4ec7-af04-87a60201593dK56J46.png)

### 附件

这里可以管理上传的图片，双击图片可以打开预览。左上角的开关可以在操作图片与复制图片地址之间切换（这里这样设计主要原因是，考虑到写文章的时候可能会用到里面的图片）

也可以删除图片。但默认只能删除自己上传的，唯一能删除他人的就只有默认管理员（也就是系统创建的账号）。

如果你删除了被文章引用的图片，会导致文章中的图片失效。请谨慎删除
![image](E:\MyProjects\easy_blog\README.assets\ae3ca9c3-2be1-424f-a2c4-716192bc07deG23.png)


### 用户
这里可以修改自己的相关消息，也可以创建其他用户（前提是有权限）。

如果你写的文章比较多，建议不要更换昵称。因为更换后会导致在elasticSearch里面文章全部失效，然后重新上架，这样可能会导致服务器卡顿。

![image](E:\MyProjects\easy_blog\README.assets\992b0491-2341-42db-b7c5-e06c7cf03dd81WASA.png)


### 设置

这里可以对整个博客进行相关设置。前提也是要有权限。
- 可以修改首页封面、首页文章、logo等等。
- 可以配置smtp与oss
- 关于页面的修改也集成到这了。
![image](E:\MyProjects\easy_blog\README.assets\eb5aab0d-2015-4a6f-b8b9-2bd58b562fe7G43.png)

## 说明

1. 初始化的登录账号密码
   ``` shell
    账号:root
    密码:12345678
   ```

2. 如果发布文章的状态一直显示未发布,可能是第一次初始化的时候es的索引没有建立成功，重启一下项目就行了

    ``` shell
    docker restart blog-app
    ```

3. 刚开始会看不到设置页面，因为默认创建的用户是没有任何其他权限的。可以去用户->用户管理然后找到自己修改相关权限。
4. 仪表盘文章统计的信息（如：点赞数、评论数等等）每隔一个小时统计一遍。
5. 如果要使用邮件通知功能，需要到设置里面配置smtp服务，不然开启通知没用。
6. 博客的附件上传有两种类型，一种是上传到本地，一种是上传到阿里云oss（其他的云服务厂商暂时还不支持）。如果要上传到阿里云oss需要去设置里面配置。如何配置以及相关参数意思可以去阿里云帮助文档查看。
7. 发布文章时选择发布时间如果晚于当前时间，将于你所选的发布时间去上架文章。在文章上架前，前端页面是完全无法感知的，包括首页与搜索页面。
8. 访客的位置信息是通过淘宝的ip地址库查询的，可能会不准确。
9. 由于本人的技术能力有限可能会有一些bug,如果发现可以给我提意见。（由于时间的关系可能不会立即修改）

## 最后
> 说说我为什么会做这个博客系统

从大一到大三，从当年什么都不知道的小白到现在只知道调库的初级开发者。

平时遇到问题的时候总是第一时间去百度，总能从那些大佬的博客中找到答案。然而当时解决了问题就扔一边不管了，到后面再次遇到类似的问题就又去百度，实在是太浪费时间了。

后来我开始用markdown简单的记录一些问题，当遇到相似的问题时可以直接看自己笔记，这确实省了不少时间。但是我们上课经常去机房，或者是用同学的电脑。然鹅这时我写的笔记就不能看了，的确很难受。这时我就有了个想法，决定自己写一个博客系统。

这时你可能会问，市面上不是有很多博客像csdn、博客园、wordpress等等，你为什么不用呢？这里是因为我喜欢自定义，这是那些博客给不了我的，做这个项目也可以顺便练下手。

以后我的学习笔记，问题解决方案啊，也都会放到博客上。(●ˇ∀ˇ●)