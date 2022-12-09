package study.corespringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authorize -> authorize.anyRequest()
                                .authenticated()
                )
                .formLogin(
                        fromLogin -> fromLogin
//                                .loginPage("/loginPage")
                                .defaultSuccessUrl("/")
//                                .failureUrl("/loginPage")
                                .usernameParameter("userId")
                                .passwordParameter("passwd")
                                .loginProcessingUrl("/login_proc")
                                .successHandler((request, response, authentication) -> {
                                    System.out.println("authentication: " + authentication.getName());
                                    response.sendRedirect("/");
                                })
                                .failureHandler((request, response, exception) -> {
                                    System.out.println("exception: " + exception.getMessage());
                                    response.sendRedirect("/loginPage");
                                })
                                .permitAll()
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login")
                                .addLogoutHandler((request, response, authentication) -> {
                                    HttpSession session = request.getSession();
                                    session.invalidate();
                                })
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.sendRedirect("/login");
                                })
                                .deleteCookies("remember-me")
                )
                .build();
    }
}
