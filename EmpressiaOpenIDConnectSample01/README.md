# Empressia OpenID Connect Sample01

[Empressia OpenID Connect](https://github.com/Empressia/EmpressiaOpenIDConnect)のMicrosoft ID プラットフォームを使用するサンプル（サンプル01）です。  

事前にMicrosoft Azureのポータルで、以下の状態である想定です。  
* Azure Active Directoryにアプリ尾登録をして、client_idを確認している。  
* リダイレクト URIに、以下のアドレスを登録している（サンプルの設定値の場合）。
	> http://localhost:8080/api/auth/Microsoft/authenticated
* クライアント シークレット（client_secret）を発行している。

必要に応じて、以下を設定してビルド＆起動します。  

```
jp.empressia.enterprise.security.oidc.Microsoft.client_id=
jp.empressia.enterprise.security.oidc.Microsoft.client_secret=
```

起動したら、以下のアドレスに、アクセスすることで、動作を確認できます。  

> http://localhost:8080/api/test/username
