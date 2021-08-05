package jp.empressia.app.empressia_oidc.webapi;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author すふぃあ
 */
@ApplicationScoped
@ApplicationPath("api")
public class Application extends javax.ws.rs.core.Application {

	@Override
	public Map<String, Object> getProperties() {
		return Map.of("jersey.config.jsonFeature", "JacksonFeature");
	}

	/**
	 * @author すふぃあ
	 */
	@Provider
	public static class ObjectMapperResolver implements ContextResolver<ObjectMapper> {

		private ObjectMapper mapper;

		public ObjectMapperResolver() {
			ObjectMapper m = new ObjectMapper();
			m.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
			m.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper = m;
		}

		@Override
		public ObjectMapper getContext(Class<?> type) {
			return mapper;
		}

	}

}
