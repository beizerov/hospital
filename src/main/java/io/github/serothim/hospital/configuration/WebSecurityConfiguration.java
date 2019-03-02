/*
 * MIT License
 * 
 * Copyright (c) 2018 Alexei Beizerov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.serothim.hospital.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.github.serothim.hospital.service.UserLoading;

/**
 *
 * @author Alexei Beizerov
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserLoading userLoading;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userLoading)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/receptionist/**")
                    	.hasAuthority("RECEPTIONIST").anyRequest()
                    	.authenticated()
                    .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                    .authenticated()
                .and()
                	.formLogin()
                    .loginPage("/").failureUrl("/?error=true")
                    .successHandler(customAuthenticationSuccessHandler)
                //    .defaultSuccessUrl("/admin/home")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                	.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                .and().exceptionHandling()
                    .accessDeniedPage("/access-denied")
                .and()
        			.headers()
        			.contentSecurityPolicy("script-src 'self' "
        					+ "'sha256-Obmw9p0O7AaxeYrMEHYb++nI2y46vwJdq"
        					+ "YzUIoKjyg8=' "
        					+ "'sha256-iJkSaGuCbu1BX4t2MvuuQLfXvWefMyTnB"
        					+ "/OaiI91Kng=' "
        					+ "'sha256-JpXDpqiMLXNvEPj/uPg4SNXA9eS/xp3lE"
        					+ "wedizpS1cQ=' "
        					+ "'sha256-U+KoAOSZ0zEjevqVVn679I0AXcbksHI3S"
        					+ "CDYiakQdWY=';"
        				+ "report-uri /csp-report-endpoint/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                		"/resources/**", 
                		"/static/**", 
                		"/css/**", 
                		"/js/**", 
                		"/images/**"
                		);
    }
}
