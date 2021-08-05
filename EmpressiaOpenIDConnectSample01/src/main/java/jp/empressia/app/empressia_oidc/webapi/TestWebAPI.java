package jp.empressia.app.empressia_oidc.webapi;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author すふぃあ
 */
@DeclareRoles("User")
@Path("test")
public class TestWebAPI {

	@GET
	@Path("username")
	@RolesAllowed({"User"})
	@Produces("text/plain")
	public String username(@Context SecurityContext context) throws IOException {
		Principal p = context.getUserPrincipal();
		String name = p.getName();
		System.out.println(name);
		return name;
	}

}
