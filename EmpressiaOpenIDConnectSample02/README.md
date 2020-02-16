# Empressia OpenID Connect Sample02

[Empressia OpenID Connect](https://github.com/Empressia/EmpressiaOpenIDConnect)の複数のIssuerを同時に使用するサンプル（サンプル02）です。  

必要に応じて、以下を設定してビルド＆起動します。  

```
jp.empressia.enterprise.security.oidc.Google.client_id=
jp.empressia.enterprise.security.oidc.Google.client_secret=
jp.empressia.enterprise.security.oidc.LINE.client_id=
jp.empressia.enterprise.security.oidc.LINE.client_secret=
jp.empressia.enterprise.security.oidc.Microsoft.client_id=
jp.empressia.enterprise.security.oidc.Microsoft.client_secret=
```

サンプル01を確認済みであれば、  
具体的な値は、Microsoft分だけで、他は空でもビルド＆起動はできます。  

起動したら、以下のアドレスに、アクセスすることで、動作を確認できます。  

> http://localhost:8080/api/test/username
