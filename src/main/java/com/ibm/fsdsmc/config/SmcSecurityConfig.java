package com.ibm.fsdsmc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ibm.fsdsmc.filters.JwtAuthenticationTokenFilter;
import com.ibm.fsdsmc.handler.SmcAccessDeniedHandler;
import com.ibm.fsdsmc.handler.SmcAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SmcSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private SmcAuthenticationEntryPoint smcAuthenticationEntryPoint;
  @Autowired
  private SmcAccessDeniedHandler smcAccessDeniedHandler;
  @Autowired
  private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
      httpSecurity.csrf().disable() // diable csrf
	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // use JWT，don't create session
	    .and().exceptionHandling().accessDeniedHandler(smcAccessDeniedHandler).authenticationEntryPoint(smcAuthenticationEntryPoint) //
	    .and().authorizeRequests() // enable authorize HttpServletRequest
	    .antMatchers("/smc/secure/login").permitAll() // permit for login
	    .antMatchers("/smc/secure/logout").permitAll() // permit for logout
	    .antMatchers("/smc/secure/admin").hasRole("ADMIN") // only allowed for role ADMIN?
	    .antMatchers("/smc/secure/authenticated/**").permitAll() // test?
	    .antMatchers("/smc/users/signup").permitAll() // permit for sign up
	    .antMatchers("/smc/users/confirmed/**").permitAll() // permit for confirm user
	    .antMatchers("/smc/users/settings").permitAll() // permit for settings
	    .anyRequest().authenticated() // need authorize for all the others
	    .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class) // JWT based security filter
	    .headers().cacheControl(); // disable page caching
  }

}
