## Ruoyi-Nbcio V1.0.0 NBCIO亿事达企业管理平台简介

[![码云Gitee](https://gitee.com/nbacheng/ruoyi-nbcio/badge/star.svg?theme=blue)](https://gitee.com/nbacheng/ruoyi-nbcio)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://gitee.com/nbacheng/ruoyi-nbcio/blob/master/LICENSE)
[![](https://img.shields.io/badge/Author-宁波阿成-orange.svg)](http://122.227.135.243:9666/)
[![](https://img.shields.io/badge/Blog-个人博客-blue.svg)](https://nbacheng.blog.csdn.net)
[![](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)](https://gitee/nbacheng/ruoyi-nbcio)
[![使用STS开发维护](https://img.shields.io/badge/STS-提供支持-blue.svg)](https://spring.io/tools)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-blue.svg)]()
[![JDK-8+](https://img.shields.io/badge/JDK-8-green.svg)]()
[![JDK-11](https://img.shields.io/badge/JDK-11-green.svg)]()

- 本项目基于 [RuoYi-Flowable-Plus](https://gitee.com/KonBAI-Q/ruoyi-flowable-plus) 进行二次开发，从nbcio-boot(https://gitee.com/nbacheng/nbcio-boot)项目
  移植过来相关功能，脚手架功能同步更新 [RuoYi-Vue-Plus](https://gitee.com/dromara/RuoYi-Vue-Plus) 项目。
- 项目处于开发移植阶段，因为还没完成相关工作，工作流流程还存在不足。因此，目前仅推荐用于学习、毕业设计等个人使用。

## 参考文档
- 项目文档：[RuoYi-Nbcio开发文档 目前暂时指向原项目文档](http://rfp-doc.konbai.work)
- 脚手架文档：[RuoYi-Vue-Plus文档](https://gitee.com/dromara/RuoYi-Vue-Plus/wikis/pages)

## 项目地址
- Gitee：<https://gitee.com/nbacheng/ruoyi-nbcio>

## 增加的主要功能

   1、表单设计修改为formdesigner。
   
   2、增加消息提醒功能。
   
   3、支持动态角色与用户,全面修改原有的以userid修改成username的方式。
   
   4、全新修改了原先启动流程的过程。
   
   

## 支持项目
-  如果项目对你有帮助，请给项目点个Star，同时也可以请作者喝杯咖啡吧！
![](https://oscimg.oschina.net/oscnet/up-58088c35672c874bd5a95c2327300d44dca.png)

## 在线演示
演示服务不限制操作，希望大家按需使用，不要恶意添加脏数据或对服务器进行攻击等操作。

[RuoYi-Nbcio 在线演示](http://122.227.135.243:9666/)

|                 | 账号  | 密码      |
|---------------- | ----- | -------- |
| 超管账户         | admin | admin123 |
| 监控中心（未运行） | ruoyi | 123456   |
| 任务调度中心      | admin | 123456   |
| 数据监控中心      | ruoyi | 123456   |

## 技术交流群

QQ交流群: 703572701 

## 友情链接
- [基于jeec-boo3.0的nbcio-boot项目](https://gitee.com/nbacheng/nbcio-boot)： NBCIO 亿事达企业管理平台。

## 后端技术架构
- 基础框架：Spring Boot 2.7.11

- 持久层框架：Mybatis-plus 3.5.3.1

- 安全框架：Sa-Token 1.34.0

- 缓存框架：redis

- 日志打印：logback

- 其他：fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。

## 开发环境

- 语言：Java 8 java 11

- IDE(JAVA)： STS安装lombok插件 或者 IDEA

- 依赖管理：Maven

- 数据库：MySQL5.7+  &  Oracle 11g & SqlServer & postgresql & 国产等更多数据库

- 缓存：Redis



## 参与开源
- 如遇到问题，欢迎提交到 [issues](https://gitee.com/nbacheng/ruoyi-nbcio/issues)（请按模版进行填写信息）。
- 欢迎fork项目，同时提交相关功能。

## 特别鸣谢
- [RuoYi-Flowable-Plus](https://gitee.com/KonBAI-Q/ruoyi-flowable-plus) 
- [bpmn-process-designer](https://gitee.com/MiyueSC/bpmn-process-designer)
- [formDesigner](https://gitee.com/wurong19870715/formDesigner)

## 演示图例
<table style="width:100%; text-align:center">
<tbody>
<tr>
  <td>
    <span>登录页面</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/164043_74b57010_5096840.png" alt="登录页面"/>
  </td>
  <td>
    <span>用户管理</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/164236_2de3b8da_5096840.png" alt="用户管理"/>
  </td>
</tr>
<tr>
  <td>
    <span>流程分类</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/164839_ca79b066_5096840.png" alt="流程分类"/>
  </td>
  <td>
    <span>流程表单</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/165118_688209fd_5096840.png" alt="流程表单"/>
  </td>
</tr>
<tr>
  <td>
    <span>流程定义</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/165916_825a85c8_5096840.png" alt="流程定义"/>
  </td>
  <td>
    <span>流程发起</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/171409_ffb0faf3_5096840.png" alt="流程发起"/>
  </td>
</tr>
<tr>
  <td>
    <span>表单设计</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/172933_7222c0f2_5096840.png" alt="表单设计"/>
  </td>
  <td>
    <span>流程设计</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/165827_44fa412b_5096840.png" alt="流程设计"/>
  </td>
</tr>
<tr>
  <td>
    <span>发起流程</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/171651_4639254b_5096840.png" alt="发起流程"/>
  </td>
  <td>
    <span>待办任务</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/171916_7ba22063_5096840.png" alt="代办任务"/>
  </td>
</tr>
<tr>
  <td>
    <span>任务办理</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/172204_04753399_5096840.png" alt="任务办理"/>
  </td>
  <td>
    <span>流转记录</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/172350_179e8341_5096840.png" alt="流转记录"/>
  </td>
</tr>
<tr>
  <td>
    <span>流程跟踪</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/172547_fe7414d4_5096840.png" alt="流程跟踪"/>
  </td>
  <td>
    <span>流程完结</span>
    <img src="https://images.gitee.com/uploads/images/2022/0424/173159_8cc57e74_5096840.png" alt="流程完结"/>
  </td>
</tr>
</tbody>
</table>
