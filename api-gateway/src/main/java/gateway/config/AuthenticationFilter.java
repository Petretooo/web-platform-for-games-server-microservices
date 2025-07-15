package gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import gateway.util.AuthenticationException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilterFactory<AuthenticationFilter.Config> {
	
	private final static String NOT_AUTHENTICATED_USER = "User is not authenticated!";
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public GatewayFilter apply(Config config) {
		
//		String user = restTemplate.getForObject("http://localhost:8085/api/auth/context/username", String.class);
//		
//		ResponseEntity<String> responseEntity = restTemplate.exchange(
//				"http://localhost:8085/api/auth/context/username",
//                HttpMethod.GET,
//                null,
//                String.class
//        );

		return (exchange, chain) -> {

//			String username = user.getBody();
//			System.out.println("Test auth");
//			System.out.println(user);
//			System.out.println(responseEntity);

			if (true) {
				return chain.filter(exchange).then(Mono.fromRunnable(() -> {
					// Code to execute after the response
				}));
			} else {
				ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
			}

		};
	}

	@Override
	public Class<Config> getConfigClass() {
		return Config.class;
	}

	public static class Config {
		// Add any configuration properties for the filter
	}
}