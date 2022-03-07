

### 准备工作
因为需要通过 `Github` 授权登录, 所有需要在 `Github` 新建应用, 具体操作流程见 [这里](https://docs.github.com/en/developers/apps/building-oauth-apps/creating-an-oauth-app).

新建应用后, 可以得到这两个数据: `Client ID`, `Client secret`, 
然后配置到 `application.yml` 的配置: `security.oauth2.client.client-id`, `security.oauth2.client.client-secret`


### 启动验证

启动应用程序, 然后访问 `http://localhost:8080/`, 点击 `Login with GitHub` 按钮, 即可触发 `Github` 授权登录.