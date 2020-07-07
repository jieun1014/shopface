package kr.ac.sunmoon.shopface.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import kr.ac.sunmoon.shopface.member.MemberService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final MemberService memberService;
	private final CustomSuccessHandler successHanlder;
	
	@Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/templates/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/member", "/branch", "/branch/**").hasRole("ADMIN")
		.antMatchers("/schedule/**", "/salary").hasRole("MEMBER")
    	  .antMatchers("/employ/**", "/timetable/**", "/occupation/**", "/branch").hasRole("BUSINESSMAN")
    	  .antMatchers(HttpMethod.PUT, "/employ","/employ/invite/").permitAll() 
    	  .antMatchers(HttpMethod.POST, "/member", "/employ", "/login", "/working", "/quitting", "/rejoin").permitAll() 
    	  .antMatchers("/login", "/member/form", "/member/check", "/employ/check","/forgotpassword", "/employ/auth").permitAll() 
    	  .anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.successHandler(successHanlder)
			.usernameParameter("id")
			.failureUrl("/login?error")
			.permitAll()
		.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login")
			.invalidateHttpSession(true)
			.deleteCookies("JSESSIONID")
		.and()
			.exceptionHandling().accessDeniedPage("/login");
	}
}
