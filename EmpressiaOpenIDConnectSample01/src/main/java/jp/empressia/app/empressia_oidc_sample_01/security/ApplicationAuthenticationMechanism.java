package jp.empressia.app.empressia_oidc_sample_01.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

import jp.empressia.enterprise.security.oidc.MicrosoftAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.PublicKeyHelper;

/**
 * アプリケーションでのOpenID Connectのサービスを扱います。
 * 
 * @author すふぃあ
 */
@ApplicationScoped
public class ApplicationAuthenticationMechanism extends MicrosoftAuthenticationMechanism {

	/**
	 * コンストラクタ。
	 */
	@Inject
	public ApplicationAuthenticationMechanism(Settings settings, IdentityStoreHandler IdentityStoreHandler, PublicKeyHelper PublicKeyHelper) {
		super(settings, IdentityStoreHandler, null, PublicKeyHelper);
	}

}
