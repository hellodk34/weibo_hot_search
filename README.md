# weibo_hot_search

本项目是 `微博热搜实时推送` 的后端程序，功能

- 获取微博热搜内容定时推送到 tg 频道
- 定时将热搜数据插入数据库
- 给对应前端项目，提供 restful 接口

对应前端项目：[front-end](https://github.com/hellodk34/wb-hotsearch-front-end)

关联阅读：[完善我的微博热搜实时推送频道](https://hellodk.cn/post/835)

# Changelog

## 2021-10-29 增加南京本地热搜

前后端项目均已更新。

example: request this [url](https://weibo.hellodk.com/get?city=nanjing&time=2021-10-29-15) to see nanjing local city's hot search for that moment.

---

频道链接: https://t.me/weibo_hot_search ，每日 6 点到 23 点，每个整点推送一次，欢迎订阅关注。

- 目前推送时间是每个整点的 10 分
- 推送内容只有 50 个热搜内容，没有置顶、推广的那些信息
- 由于 tg 推送字符数限制 ➕️ 个人看新闻的需求，故推送了所有热搜文本，而忽视了每条热搜新闻直达的链接

做了对应前端项目后，可以点击 `查看更多` 拷贝对应热搜标题和直链。
# Preview

比上次多了一个 **查看更多** 按钮

![b371c4eea00e7efef512d95ac7c797a](https://cdn.jsdelivr.net/gh/hellodk34/image@main/img/b371c4eea00e7efef512d95ac7c797a.jpg)

点击频道每次推送的 **查看更多**

![754f38ed02d74c9b30a7c983b856504](https://cdn.jsdelivr.net/gh/hellodk34/image@main/img/754f38ed02d74c9b30a7c983b856504.jpg)

**动图演示**

![weibo-hotsearch2.gif](https://cdn.jsdelivr.net/gh/hellodk34/image@main/img/weibo-hotsearch2.gif)

如果把热搜当作一种新闻源的话，当在 tg 这个频道中阅读到感兴趣的话题时，进入上面的页面即可复制对应热搜的 title 和 link 然后快速的分享给微信好友。

比如复制 **韩国一公斤牛肉1090元** 的 title 和 link，得到如下 text

```
【韩国一公斤牛肉1090元】

https://m.weibo.cn/search?containerid=100103type%3D1%26t%3D10%26q%3D%23%E9%9F%A9%E5%9B%BD%E4%B8%80%E5%85%AC%E6%96%A4%E7%89%9B%E8%82%891090%E5%85%83%23&isnewpage=1&extparam=seat%3D1%26filter_type%3Drealtimehot%26dgr%3D0%26cate%3D0%26pos%3D1%26realpos%3D2%26flag%3D1%26c_type%3D31%26display_time%3D1634702972%26pre_seqid%3D1634702853849023126271&luicode=10000011&lfid=106003type%3D25%26t%3D3%26disable_hot%3D1%26filter_type%3Drealtimehot
```

# 感谢

https://github.com/xiadd/tg-wb-trending
