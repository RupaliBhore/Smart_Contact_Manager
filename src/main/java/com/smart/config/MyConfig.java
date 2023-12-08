package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


//spring bean ko read kar sake isliye @Configuration on kardo //security ki configuration enable ho jayegi
@Configuration
//@EnableWebSecurity              //security ki configuration enable ho jayegi
public class MyConfig {                   //extends WebSecurityConfiguration {
	
	@Bean
	public UserDetailsService getUserDetailService()
	{
		return new UserDetailServiceImpl();    // UserDetailServiceImpl is class ke pass jo info he o return hogi malatb user ki detalis
	}
	

//password ko decrept kar dega database me to jisaki website he use bhi nahi pata chalega ki user ne password kya dala he code me 
//password jayaga database me  password sesure ho jayaga..pahile user jo password dalata tha o dikhai deta tha ab nahi dega
	@Bean
	public BCryptPasswordEncoder passwordEncoder()    //password ko decrept kar dega database me to jisaki website he use bhi nahi
	{
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider =new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	             //configure methods
	
	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	
	}
	

	
//yaha hum configure karaenge konasi url private rahenge aur konase public
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
	{
		
		
		httpSecurity.authorizeRequests()
		.antMatchers("/admin/**")              //jisaka role ROLE_ADMIN he..wahi admin se suru hone wale sare url ko dekha sakata he
		.hasRole("ADMIN")                      //humare project me admin nahi he par pata hona chiye
		
		
		.antMatchers("/user/**")      //jiska role ROLE_USER he vahi normal page ko access kar sakata he.user se suru hone wale url deka
		.hasRole("USER")
		.antMatchers("/**")
		
		
//admin aur user url ko chodake baki jithane bi url honge .permitAll()kiya matlab sare ko permission de di koi bhi access kar sakata he
//aur muze chiye form besed login to fromLogin()ye spring provide karata he login from
		.permitAll().and()
		
		//spring boot ko batao humane apana khud ka login page banaya he apaka login page hume nahi chaiye mera login page he signin.
		.formLogin().loginPage("/signin")
		
		.loginProcessingUrl("/dologin")     //username or password wala form muze isi url pe sumbit karana he kuch bhi naam de sakate ho
		
		.defaultSuccessUrl("/user/index")    //login sucessful ho jaye to ye page open hoga
		
		.failureUrl("/login-failed")                    //login unsuccessful ho jaye to ye page dikhega
		
		.and().csrf().disable();
				
		return httpSecurity.build();
		
		//spring boot ko batao humane apana khud ka login page banaya he apaka login page hume nahi chaiye
		
	}
}

	              //MUST READ ALL CONCEPT COVER HE
/*       
	             //configure mthod old he ab nahi chalte
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.authenticationProvider(authenticationProvider());
		
	}
	
//HttpSecurityise hum bata pate he ki sare rout mat protecd karo jo me kahata hu sirf vahi karo	
	protected void configure(HttpSecurity http) throws Exception
	{
//jo url admin se start hote he o sirf vahi wale bande use karnge jinka role admin ka he..antMatchers url ko match karane ke liye lagate
		http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
		
//jinke pass user role he o user se strat hone wale url ko use kar sakate he
		.antMatchers("/user/**").hasRole("USER")   //jaha normal he waha user use karo
		
		//isake baad jitane bhi url he unko permit all kardunga and muze chaiye from based login
		.antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
	}
	
	
	
//koi banda admin he to o normal page ko access nahi kar sakata..admin ke liye database me role ROLE_ADMIN karana hota he..acess karne ki
//koshish karega to FORBIDDEN ki exception aayegi
	
	
	
             //NEW  Configure METHODS he yahi abhi chalte he user mention kiye he
	
	
	@Bean
	public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	
	}
	

	
//yaha hum configure karaenge konasi url private rahenge aur konase public
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
	{
		
		
		httpSecurity.authorizeRequests()
		.antMatchers("/admin/**")              //jisaka role ROLE_ADMIN he..admin se suru hone wale sare url ko dekha sakata he
		.hasRole("ADMIN")
		
		
		.antMatchers("/user/**")      //jiska role ROLE_USER he vahi normal page ko access kar sakata he.user se suru hone wale url deka
		.hasRole("USER")
		.antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
				
		return httpSecurity.build();
		
		
		
	}
}
		
	*/	
		
		
	
	







/*
 * 
 * @Bean

public AuthenticationManager authenticationManager( AuthenticationConfiguration authenticationConfiguration) throws Exception {

return authenticationConfiguration.getAuthenticationManager();

}
 
@Bean

public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")

.antMatchers("/**").permitAll().and(). formLogin().and().csrf().disable();

http.formLogin().defaultSuccessUrl("/ user/index", true);

return http.build();

*/