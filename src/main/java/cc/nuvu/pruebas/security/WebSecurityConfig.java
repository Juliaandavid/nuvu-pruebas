package cc.nuvu.pruebas.security;

import cc.nuvu.pruebas.filters.UserOwnershipFilter;
import cc.nuvu.pruebas.services.auth.JWTAuthEntryPoint;
import cc.nuvu.pruebas.services.jwt.JWTProvider;
import cc.nuvu.pruebas.filters.JwtAuthenticationFilter;
import cc.nuvu.pruebas.services.auth.AppUserDetailsService;
import cc.nuvu.pruebas.utils.security.AESEncryptionDecryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private AppUserDetailsService appUserDetailsService;

  @Autowired
  private JWTProvider jwt;

  @Autowired
  private JWTAuthEntryPoint authEntryPoint;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling().authenticationEntryPoint(authEntryPoint)
        .and()
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers("/auth/**").permitAll()
        .antMatchers("/api/**").authenticated()
        .and()
        .antMatcher("/api/user/**")
        .addFilterBefore(userOwnershipFilter(), FilterSecurityInterceptor.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().anyRequest();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(appUserDetailsService).passwordEncoder(encoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected UserDetailsService userDetailsService() {
    return appUserDetailsService;
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter () {
    return new JwtAuthenticationFilter(jwt);
  }

  @Bean
  public UserOwnershipFilter userOwnershipFilter () {
    return new UserOwnershipFilter();
  }

  @Bean
  public AESEncryptionDecryption aesEncryptionDecryption () {
    return new AESEncryptionDecryption(jwt.secretKey);
  }

}
