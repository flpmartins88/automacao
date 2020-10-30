package automacao.auth

import feign.RequestInterceptor
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@SpringBootApplication
@EnableAuthorizationServer
@EnableResourceServer
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}

@Configuration
class SecurityAdapter : WebSecurityConfigurerAdapter() {

	@Bean
	override fun authenticationManager(): AuthenticationManager {
		return super.authenticationManager()
	}

	@Bean
	override fun userDetailsService(): UserDetailsService {
		return super.userDetailsService()
	}

	@Bean
	fun passwordEncoder() = BCryptPasswordEncoder()

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.inMemoryAuthentication()
			.passwordEncoder(passwordEncoder())
			.withUser("felipe")
			.password(passwordEncoder().encode("felipe"))
			.roles("USER")
	}
}

@Configuration
class AuthorizationAdapter(
	private val authenticationManager: AuthenticationManager,
	private val userDetailsService: UserDetailsService,
	private val passwordEncoder: PasswordEncoder
) : AuthorizationServerConfigurerAdapter() {

	override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
		endpoints.authenticationManager(authenticationManager)
			.userDetailsService(userDetailsService)
	}

	override fun configure(clients: ClientDetailsServiceConfigurer) {
		clients.inMemory()
			.withClient("aplicacao")
			.secret(passwordEncoder.encode("senha"))
			.authorizedGrantTypes("password")
			.scopes("web", "mobile")
	}

}

@RestController
class Controller {

	@GetMapping("/me")
	fun me(principal: Principal): Principal {
		return principal
	}

}

/*
 * Para configurar o security para os clients
 */
@Configuration
@EnableResourceServer
class ResourceAdapter : ResourceServerConfigurerAdapter() {

//	config para o feign saber
//	security:
//		oauth2:
//			resource:
//				user-info-uri: http://localhost:8888/me

//	é possível instruir a passagem de autenticação pelas coisas do spring tbm
//	no caso do zuul (api gateway)
//	zuul:
//		sensitive-headers:
//		- Cookie, Authorization

	// configurar o feign para fazer requests usando o token
//	@Bean
//	fun requestInterceptor(): RequestInterceptor {
//		return RequestInterceptor {
//			SecurityContextHolder.getContext().authentication
//				?.let { authentication ->
//					val details = authentication.details as OAuth2AuthenticationDetails
//					it.header("Authorization", "Bearer ${details.tokenValue}")
//				}
//
//		}
//	}

	override fun configure(http: HttpSecurity) {
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/order").hasRole("USER")
			.anyRequest().authenticated()
	}

}