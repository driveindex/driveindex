# DriveIndex

本项目为 OneIndex 的后继者，在此向 OneIndex 原作者致敬！

## 功能

将 OneDrive 文件直接以目录形式展示，支持直连下载。

## TODO

- [x] 文件目录展示
- [x] 文件直链下载
- [ ] 目录 `README.md` 和 `HEADER.md` 展示
- [ ] 目录密码设置
- [ ] 文件预览

## 部署

### 前端

前端官方实现为 `Flutter` 开发，支持静态 html 文件部署，欢迎各路大佬开发更美观、更好用的第三方前端实现。

前往 [DriveIndex 前端 release 页](https://github.com/driveindex/driveindex-web/releases/latest) 中寻找文件 `driveindex-web.tar.gz`，上传至服务器任意位置（推荐 `/var/www/driveindex` 方便管理部署）解压，新建 `nginx` 或 `apache` 配置将 `root` 指向解压路径即可。

### 后端

后端需要 Java 17，请 [下载 JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)，安装可参考 [CentOS 7 安装 Java 环境教程 – 忆丶距的博客 (sgpublic.xyz)](https://sgpublic.xyz/?p=30)。

部署支持 `SpringCloud` 分布式、``SpringBoot` 单体两种部署方式，普通用户推荐单体部署。

#### 单体部署

前往 [DriveIndex 后端 release 页](https://github.com/driveindex/driveindex-cloud/releases/latest) 中寻找文件 `driveindex-boot.tar.gz`，上传至服务器任意位置解压，得到文件 `driveindex-boot.jar`。

直接启动即可：

```shell
java -jar driveindex-boot.jar
```

单体部署默认占用端口 `11411`。

#### 分布式部署

**PS：分布式部署需要使用多个服务器独立运行服务，若您不打算使用多个服务器请使用单体部署。**

1. 部署 `Consul` 注册中心

   分布式部署基于 `Consul` 注册中心，请前往 [Downloads | Consul by HashiCorp](https://www.consul.io/downloads) 查看部署安装。

   **注意：注册中心一定先于 `DriveIndex` 启动，否则 `DriveIndex` 无法启动。**

   `Consul` 启动指令参考：

   ```shell
   consul agent -bind=127.0.0.1 -advertise=127.0.0.1 -server -bootstrap -data-dir=data -ui
   ```

2. 部署 `DriveIndex` 本体

   前往 [DriveIndex 后端 release 页](https://github.com/driveindex/driveindex-cloud/releases/latest) 中寻找文件 `driveindex-cloud.tar.gz`，上传至服务器任意位置解压，得到文件：

   + `admin-service-bin.jar`
   + `azure-service-bin.jar`
   + `gateway-bin.jar`

   依次运行即可，如果您对 SpringCloud 有所了解，您可以按照您自己的习惯完成部署。

   ```shell
   java -jar admin-service-bin.jar # 占用端口 11421
   java -jar azure-service-bin.jar # 占用端口 11422
   java -jar gateway-bin.jar # 占用端口 11411
   ```

### 服务器配置

尽管分布式部署比单体部署多占用了其他端口，但两者都只需要对外界暴露或反向代理端口 `11411`。

单体部署 `nginx` 配置参考：

```nginx
upstream driveindex {
	server 127.0.0.1:11411;
	keepalive 64;
}

server {
    listen 80;
    listen [::]:80;
    server_name drive.sgpublic.xyz;
    
    # 强制 https
    return 301 https://$server_name$request_uri;
}

server {
    listen 443;
    listen [::]:443;
    server_name drive.sgpublic.xyz;

    # driveindex-web 解压路径
    root /var/www/driveindex/;
	
    ssl_certificate /etc/nginx/cert/sgpublic.xyz.pem;
    ssl_certificate_key /etc/nginx/cert/sgpublic.xyz.key;
    
    index index.html;
    
    location ~ (/api/*|/download) {
		proxy_set_header Host $host;
		proxy_set_header X-Real-IP $remote_addr;
		proxy_set_header X-Forwarded-F $proxy_add_x_forwarded_for;
		proxy_set_header Upgrade $http_upgrade;
		proxy_set_header Connection "upgrade";
		proxy_pass http://driveindex;
    }
}
```

PS：很惭愧，本人服务器配置不够，无法针对分布式部署方式进行测试，若有问题请自行解决。

## 构建

如需自行构建请分别前往前、后端项目查看方法。

+ 前端：[driveindex-web](https://github.com/driveindex/driveindex-web)
+ 后端：[driveindex-cloud](https://github.com/driveindex/driveindex-cloud)

## 技术栈

+ SpringCloud + Consul + MyBatis Plus + SpringSecurity
+ SpringBoot + MyBatis Plus + SpringSecurity
+ Flutter

## 许可证

GPL v3
