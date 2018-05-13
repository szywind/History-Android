# History-Android

### 功能模块
- 百科模块
-- 静态内容，需要按一定频率从LeanCloud(AWS)读取数据在本地备份，用户可以审阅。

- 社区模块
-- 动态内容，需要实时更新。用户可以订阅某些板块主题，显示在“关注”一栏，也可以对文章进行收藏，点赞，评论，转发。

- 搜索模块
-- 从百科人物，百科事件，社区文章，用户中搜索关键词。

- 社交模块
-- 简单的用户关注。

### 目前实现所用工具
- 应用内通讯：EventBus 还是 LocalBroadcastManager
- 图片URL载入和缓存： Glide
- 文件URL载入: Ion

### 未完内容...
2018.05.13
- listview 是否需要加入sideIndexBar 和 section title
- 簡單的文章寫入頁面
- 簡單的文章評論頁面
- 分頁query，利用LC AVQuery的skip和limit方法
