package ug.systemzarzadzaniakregielnia.systemzarzadzaniakregielnia.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Created by Lukasz on 2017-12-11.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    UserDetailsService userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {

        http
                .authorizeRequests()
                .antMatchers("/login**").permitAll()
                .antMatchers("/UIDL/**").permitAll()
                .antMatchers("/HEARTBEAT/**").authenticated()
                .antMatchers("/VAADIN/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .csrf().disable();
    }


}