## 1.修改 jdbc.properties
数据库名称：pet_system
数据库版本 mysql5.6+

## 后台账号密码 
admin 123

## 答疑
1. 首页这三个帖子是如何通过后台控制展示的
- 后台代码：IndexController 95-97行
- 前台代码：index.jsp  33-59行
流程如下：
    - 用户通过浏览器访问项目首页 http://localhost:8080/
    - 后台 IndexController 类的 index 方法接受请求
    - 该方法第  95-97 行分别查询这三者的数据列表
    - 第 104 行返回前端页面 index.jsp，并将map数据传输给前端
    - index.jsp 33-59 行遍历数据，进行展示


