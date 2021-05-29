# easy_blog

## 介绍

easy blog是一个基于后端基于springboot 前端基于vue3的前后端分离博客系统。它可以实现多人在线编辑发表博客。具有写文章，写笔记，写速记，角色的权限管理等待功能。

提供前台页面，后台页面与后台服务的全套代码。

总体比较简约。前端的页面设计借鉴了[这位大佬的博客](https://github.com/wsydxiangwang/Mood)。

## 使用技术

#### 后端技术

- springboot 2.4.5
  - 核心框架
- springSecurity+JWT
  - 基于token机制的权限认证。
- mybatis-plus 3.4.2
  - 用作数据库操作
- redis
  - 用于缓存
  - 用于存储用户token
  - 用于记录文章的一些参数 如访客数、点赞数等等
- elasticSearch 7.6.2
  - 用于文章的搜索
- rabbitmq
  - 消息队列，实现异步处理数据
- swagger
  - 自动生成接口文章
- thumbnailator
  - 上传图片的压缩
- flexmark
  - markdown 文本的解析

### 前端技术

- vue3 
  - 前端核心框架
- vite
  - 构建工具
- vue-roouter 
  - 前端路由导航
- vuex
  - 组件间的通讯
- elementUI-plus
  - ui框架
- echarts
  - 数据可视化
- @toast-ui/editor
  - markdown 文本编辑器
- highlight.js
  - 代码高亮

## 安装



## 本地开发

### 环境需要

1. mysql
2. redis
3. elasticSearch 需要安装ik分词
4. rabbitmq 需要安装延时队列的插件

上面的环境安装推荐用docker。

mysql

```shell
docker run -p 3306:3306 --name mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:latest
#执行上面的命令后会自动去官网拉去镜像，如果速度慢可以换阿里云的镜像。
```

redis

```shell
docker run -p 6379:6379 --name redis -d redis:latest
```

elasticSearch 

```shell
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e  "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \
-d elasticsearch:7.6.2 
```

rabbitmq 

```shell
docker run -dit --name Myrabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq:management
```

elasticSearch 插件的安装需要去https://github.com/medcl/elasticsearch-analysis-ik/releases 下载安装。找到与自己es版本一致插件，下载文件然后解压到es的plugin目录下（我的目录是/usr/share/elasticsearch/plugins）

```shell
docker cp (你下载的插件) elasticSearch:/usr/share/elasticsearch/plugins
```

然后重启就行了是不是很简单😂。

```shell
docker restart elasticsearch
```

rabbitmq 延时队列的插件也和上面相似。先去官网下载 https://github.com/rabbitmq/rabbitmq-rtopic-exchange/releases/tag/v3.8.0与自己版本一致的插件。

复制插件到plugins目录里面

```shell
docker cp rabbitmq_delayed_message_exchange-xxxx.ez(这里换成你下载的插件名称) rabbitmq:/plugins
```

进入docker内部：

```shell
docker exec -it rabbitmq bash
```

开启插件

```shell
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

最后和上面一样重启ok了。

```shell
docker restart rabbitmq
```

### 修改配置

配置好环境后，修改backend/src/resources/application.yaml 里面的数据库、redis、rabbitmq、es的连接信息

### 项目启动

```shell
启动后台 BackendApplication

然后前台
cd app-frontend 
npm install
npm run dev

cd manage-frontend
npm install
npm run dev
```



## 模块分层

### 后端模块

```shell
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

### 前台页面

前台页面的结构很简单这里就简单列一下吧。

- assets 静态资源

- components 前端组件

- router 路由包

- store vuex包

- util工具包

- view 页面包

  

##  说明

从大一到大三，从当年什么都不知道的小白到现在只知道调库的初级开发者。

平时遇到问题的时候总是第一时间去百度，总能从那些大佬的博客中找到答案。然而当时解决了问题就扔一边不管了，到后面再次遇到类似的问题就又去百度，实在是太浪费时间了。

后来我开始用markdown简单的记录一些问题，当遇到相似的问题时可以直接看自己笔记，这确实省了不少时间。但是我们上课经常去机房，或者是用同学的电脑。然鹅这时我写的笔记就不能看了，的确很难受。这时我就有了个想法，决定自己写一个博客系统。

这时你可能会问，市面上不是有很多博客像csdn、博客园、wordpress等等，你为什么不用呢？这里是因为我喜欢自定义，这是那些博客给不了我的，也顺便练下手。

以后我的学习笔记，问题解决方案啊，也都会放到博客上。(●ˇ∀ˇ●)

