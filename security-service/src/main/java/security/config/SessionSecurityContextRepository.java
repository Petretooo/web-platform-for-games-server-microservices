package security.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

@Component
public class SessionSecurityContextRepository extends HttpSessionSecurityContextRepository {

	@Override
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		if (context != null && context.getAuthentication() != null) {
            super.setAllowSessionCreation(true);
            super.saveContext(context, request, response);
        }
	}

	@Override
	public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
		super.setAllowSessionCreation(true);
        return super.loadContext(requestResponseHolder);
	}

	@Override
	public boolean containsContext(HttpServletRequest request) {
        return super.containsContext(request);
	}
}
