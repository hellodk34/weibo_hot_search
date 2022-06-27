# weibo_hot_search

![[object Object]](https://socialify.git.ci/hellodk34/weibo_hot_search/image?font=Inter&forks=1&issues=1&language=1&name=1&owner=1&pattern=Floating%20Cogs&pulls=1&stargazers=1&theme=Light)

一个微博热搜实时推送频道，只推送 50 条热搜内容。频道链接: https://t.me/weibo_hot_search ，欢迎订阅关注。

## 特点

- 每天 6 点到 23 点，每个整点 10 分进行一次实时热搜数据推送
- 点击推送内容中的标题（可点击）直达热搜页面
- 点击 `查看更多` 可以快捷复制热搜的标题和链接，方便分享给好友
- 工具类型项目，完全用爱发电（作者使用自己的服务器部署），频道本身无任何广告和推广，除非出现 tg 官方频道广告（一般小频道是不会有这个广告的）
- 对应前端项目：[front-end](https://github.com/hellodk34/wb-hotsearch-front-end)

如有任何问题，欢迎 [创建 issue](https://github.com/hellodk34/weibo_hot_search/issues/new) 讨论。如果觉得项目对你有用，欢迎点个免费的 star 鼓励一下，谢谢！

## 技术栈

- Spring Boot v2.5.5
- MySQL 8.0
- Yourls v1.9.1

# Preview

推送的消息体

![20220627091828](https://image.940304.xyz/i/2022/06/27/62b90570672f0.png)

点击标题确认后可跳转到热搜页（Windows tg 客户端可以按住 control 点击实现直接打开链接，macOS、Android、iOS tg 官方客户端目前没有发现该功能）

![20220627091945](https://image.940304.xyz/i/2022/06/27/62b905b280d78.png)

点击 `查看更多` 展示的新页面可以快捷复制热搜的标题和链接（初衷是方便在微信进行纯文本分享）

![20220627092638](https://image.940304.xyz/i/2022/06/27/62b9074f81338.png)

# 感谢

https://github.com/xiadd/tg-wb-trending
