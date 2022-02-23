# NoHTML

我为什么要写`NoHTML`？  
我不喜欢通过HTML的方式书写资料或是文档，富文本也不行，只有Markdown才是真爱。

`NoHTML`可以干什么？  
我不知道，反正我目前是把这个用于做个人资料整理。然后放到GitHub仓库中，用readme做首页，就很方便。

## 使用

可以直接下载release的jar包，也可以将此项目pull下来，进行自定义修改。

1. 创建管理根目录（例如MyPage)
2. 将jar包放在管理根目录中（MyPage/NoHTML.jar)
3. 在管理根目录下创建docs文件夹用于放置需要管理的MD文件（MyPage/docs)
4. 运行jar包（例如`java -jar NoHTML.jar`
5. 完成

## 配置文件

### config.properties

总配置文件，用于设定参数

```properties
# 档案标题
title=Verlif & Idea
# 首页的最新更新文件显示数量
size=5
# 文件概览的最长显示字数
length=25
# 首页文件名
indexName=readme.md
```

------

### config/profile.md

首页概述md文件，里面的内容会被复制到生成的首页中。
位置在标题与最新更新文件列表中间。

------

### config/footer.md

首页结语md文件，里面的内容会被复制到生成的首页中。
位置在标题列表后面。

## 目录结构

档案管理器需要一个管理器的目录，用户的所有需要管理的档案需要放置在其下的`docs`文件夹中

| 名称                | 文件类型 | 文件说明    | 是否手动创建 |
|-------------------|------|---------|--------|
| index.md          | 文件   | 首页档案    | 否      |
| docs              | 文件夹  | 档案根目录   | 是      |
| config.properties | 文件   | 配置文件    | 否      |
| config            | 文件夹  | 配置文件附录  | 否      |
| tags              | 文件夹  | 标签档案文件夹 | 否      |