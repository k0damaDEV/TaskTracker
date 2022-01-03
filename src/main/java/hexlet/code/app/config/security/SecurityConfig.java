package hexlet.code.app.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static hexlet.code.app.controller.UsersController.ID;
import static hexlet.code.app.controller.UsersController.USERS_CONTROLLER_PATH;
import static hexlet.code.app.controller.AuthController.LOGIN_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskStatusController.TASK_STATUS_CONTROLLER_PATH;
import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RequestMatcher publicUrls;

    private final RequestMatcher protectedUrls;

    private final TokenAuthenticationProvider authenticationProvider;

    public SecurityConfig(@Value("${base-url}") final String baseUrl,
                          @Lazy final TokenAuthenticationProvider tokenAuthenticationProvider) {
        this.authenticationProvider = tokenAuthenticationProvider;
        this.publicUrls = new OrRequestMatcher(
                new AntPathRequestMatcher(baseUrl + LOGIN_CONTROLLER_PATH, HttpMethod.POST.toString()),
                new AntPathRequestMatcher(baseUrl + USERS_CONTROLLER_PATH, HttpMethod.POST.toString()),
                new AntPathRequestMatcher(baseUrl + USERS_CONTROLLER_PATH, HttpMethod.GET.toString()),
                new AntPathRequestMatcher(baseUrl + TASK_STATUS_CONTROLLER_PATH, HttpMethod.GET.toString()),
                new AntPathRequestMatcher(baseUrl + TASK_STATUS_CONTROLLER_PATH + ID, HttpMethod.GET.toString()),
                new AntPathRequestMatcher(baseUrl + TASK_CONTROLLER_PATH, HttpMethod.GET.toString()),
                new AntPathRequestMatcher(baseUrl + TASK_STATUS_CONTROLLER_PATH + ID, HttpMethod.GET.toString()),
                new NegatedRequestMatcher(new AntPathRequestMatcher(baseUrl + "/**"))
        );
        this.protectedUrls = new NegatedRequestMatcher(publicUrls);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        http.csrf().ignoringAntMatchers("/h2console/**");
        http.authorizeRequests().antMatchers("/h2console/**").permitAll();

        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers(publicUrls).permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                .sessionManagement().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    public TokenAuthenticationFilter restAuthenticationFilter() throws Exception {
        final var authenticationFilter = new TokenAuthenticationFilter(protectedUrls);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(successHandler());
        return authenticationFilter;
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        final var successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(((request, response, url) -> {

        }));
        return successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
