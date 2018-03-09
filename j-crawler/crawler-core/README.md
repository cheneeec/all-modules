# 流程
1. `Scheduler`负责启动爬虫。
2. 

## 简单流程

1. 初始化待请求的`TaskQueue`，对其放入需爬取的`url`。
2. `Fetcher`从网络上下载页面并且存储在一个队列`TaskQueue`中。
3. `Handler`对`pendingQueue`中的内容进行解析提取新的`url`，放入到`TaskQueue`。同时`pipe`也对其内容进行解析，提取需要的内容并且转化为响应的类型。

