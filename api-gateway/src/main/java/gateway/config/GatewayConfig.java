package gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
	
	@Autowired
    private final AuthenticationFilter authenticationFilter;

    public GatewayConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("tictactoe-service", r -> r.path("/api/v1/games/tictactoe/**", "/api/v1/multiplayer/tictactoe/**")
                        .filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://tictactoe-service"))
				.route("tictactoe-service-multiplayer", r -> r.path("/websocket/**", "/tictactoe-ws/**", "/sockjs/**", "/info/**",  "/ws/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("http://localhost:8086"))
				.route("hangman-service", r -> r.path("/api/v1/games/hangman/**", "/api/v1/game/stats/**", "/api/v1/alphabet/**", "/api/v1/rank/**", "/api/v1/words/**", "/api/v1/multiplayer/hangman/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://hangman-service"))
				.route("hangman-service-multiplayer", r -> r.path("/websocket/**", "/hangman-ws/**", "/sockjs/**", "/info/**", "/ws/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("http://localhost:8087"))
				.route("user-service", r -> r.path("/api/v1/users/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://user-service"))
				.route("security-service", r -> r.path("/api/auth/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://security-service"))
				.route("chat-service", r -> r.path("/websocket/**", "/chat-ws/**", "/sockjs/**", "/info/**", "/ws/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("http://localhost:8084"))
				.route("data-aggregation-service", r -> r.path("/api/v1/stats/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("http://localhost:8088"))
				.route("dots-and-boxes-service", r -> r.path("/api/v1/games/dotsandboxes/**", "/api/v1/multiplayer/dotsandboxes/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("lb://dots-and-boxes-service"))
				.route("dots-and-boxes-service-multiplayer", r -> r.path("/websocket/**", "/dotsandboxes-ws/**", "/sockjs/**", "/info/**",  "/ws/**")
						.filters(f -> f.filter(authenticationFilter.apply(new AuthenticationFilter.Config())))
						.uri("http://localhost:8090"))
				.build();
	}
}
