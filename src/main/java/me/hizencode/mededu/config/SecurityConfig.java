package me.hizencode.mededu.config;

import me.hizencode.mededu.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user-dashboard/manage-courses/**").hasAnyRole("ADMIN")
                .antMatchers("/user-dashboard/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course{\\\\d+}/lesson{\\\\d+}").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course{\\\\d+}/test{\\\\d+}").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course{\\\\d+}/continue").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course/lesson/complete").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course/test/submit").hasAnyRole("USER", "ADMIN")
                .antMatchers("/courses/course/signup").hasAnyRole("USER", "ADMIN")
                .antMatchers("/course/lesson/media/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/course/test/media/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .loginProcessingUrl("/login-process")
                .permitAll()
                .defaultSuccessUrl("/courses")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
