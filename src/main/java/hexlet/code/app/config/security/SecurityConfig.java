package hexlet.code.app.config.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationProvider authenticationProvider;

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
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/users").permitAll()
                .antMatchers("/h2-console").permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .sessionManagement().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        final var succesHandler = new SimpleUrlAuthenticationSuccessHandler();
        succesHandler.setRedirectStrategy(((request, response, url) -> {}));
        return succesHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
