package pl.com.fenice.etyka.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/notification/showall/").authenticated()
                .antMatchers("/notification/show/**").authenticated()
//                .antMatchers("/notification/add/").permitAll()
//                .antMatchers("/notification/openone/").permitAll()
//                .antMatchers("/notification/onenotification/").permitAll()
//                .antMatchers("/notification/").permitAll()
//                //.anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/webjars/**");

    }
}