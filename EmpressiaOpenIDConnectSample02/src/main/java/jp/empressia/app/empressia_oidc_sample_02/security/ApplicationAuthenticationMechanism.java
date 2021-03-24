package jp.empressia.app.empressia_oidc_sample_02.security;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.IdentityStoreHandler;

import jp.empressia.enterprise.security.oidc.GoogleAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.IOpenIDConnectAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.IOpenIDConnectIdentityStore;
import jp.empressia.enterprise.security.oidc.LINEAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.MicrosoftAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.MultipleIssuersOpenIDConnectAuthenticationMechanism;
import jp.empressia.enterprise.security.oidc.PublicKeyHelper;

/**
 * アプリケーションでのOpenID Connectのサービスを扱います。
 * @author すふぃあ
 */
@ApplicationScoped
public class ApplicationAuthenticationMechanism extends MultipleIssuersOpenIDConnectAuthenticationMechanism {

	/** Google、LINE、Microsoftをサポートする。 */
	@Dependent
	public static class ApplicationMechanismSupplier implements OpenIDConnectAuthenticationMechanismSupplier {

		private GoogleAuthenticationMechanism.Settings GoogleSettings;
		private LINEAuthenticationMechanism.Settings LINESettings;
		private MicrosoftAuthenticationMechanism.Settings MicrosoftSettings;

		private Collection<IOpenIDConnectAuthenticationMechanism> Mechanisms;

		@Inject
		public ApplicationMechanismSupplier(
			GoogleAuthenticationMechanism.Settings GoogleSettings,
			LINEAuthenticationMechanism.Settings LINESettings,
			MicrosoftAuthenticationMechanism.Settings MicrosoftSettings,
			IdentityStoreHandler IdentityStoreHandler,
			PublicKeyHelper PublicKeyHelper
		) {
			this.GoogleSettings = GoogleSettings;
			this.LINESettings = LINESettings;
			this.MicrosoftSettings = MicrosoftSettings;
			this.Mechanisms = Set.<IOpenIDConnectAuthenticationMechanism>of(
				new GoogleAuthenticationMechanism(this.GoogleSettings, IdentityStoreHandler, null, PublicKeyHelper) {
					@Override
					protected LinkedHashMap<String, String> handleAuthorizationRequestParameters(LinkedHashMap<String, String> parameters) {
						parameters.put("access_type", "offline");
						parameters.put("prompt", "consent");
						return parameters;
					}
				},
				new LINEAuthenticationMechanism(this.LINESettings, IdentityStoreHandler, null),
				new MicrosoftAuthenticationMechanism(this.MicrosoftSettings, IdentityStoreHandler, null, PublicKeyHelper)
			);
		}

		@Override
		public Collection<IOpenIDConnectAuthenticationMechanism> mechanisms(boolean useSecureCookie) {
			return this.Mechanisms;
		}

	}

	/**
	 * コンストラクタ。
	 */
	@Inject
	public ApplicationAuthenticationMechanism(Settings settings, OpenIDConnectAuthenticationMechanismSupplier MechanismSupplier, OpenIDConnectAuthenticationMechanismSelectable MechanismSelector, IssuerNotSelectedHandler IssuerNotSelectedHandler, IOpenIDConnectIdentityStore IdentityStore) {
		super(settings, MechanismSupplier, MechanismSelector, IssuerNotSelectedHandler, IdentityStore);
	}

}
