package com.smart.config;
                                                                              //secound step 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.entites.User;

//add unplmenet method
public class UserDetailServiceImpl implements UserDetailsService{

	     @Autowired   
	    private UserRepository userRepository;  //userRepository is verible me spring class ka obeject inject kar dega aur hum use use karenge
	
	
	
	//is maethod ke andhar database se user ko lana he aur yaha se user autentication kya kya he ye return karna he maltalb userDetails
	//database se nikalane ke liye DaoResopesrty call karni padegi humari Dao he UserRepository to usame se usake method ko call karana hoga
	//usaka menthod call yaha pe karana he to hume usake object ki jarurat padegi to apako UserRepository ko yaha me Autowired karana padega
	//tabi aap us class ka objech yaha bana sakate he aur autowired kiya to spring samaj jata he ki ye boject is calss ka he
	     
	     //loadUserByUsername humare dao ko use kar raha he customeuserdetils bhu use ho rahi heh
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		
		//fetching user from database
		User user = userRepository.getUserByUserName(username); //email hi username he
		
         
		
		//user null to nahi he ye check karo
		if(user==null)
		{
			//user nahi mela to throw user null hoga tab ye throw karega
			throw new UsernameNotFoundException("could not found user !!");	
		}
		
		
		
		
//agar user null nahi he to usaki details return karo CustomUserDetails class ke constuructor me user pass kiya he to yaha vahi pass karo
		CustomUserDetails customUserDetails =new CustomUserDetails(user);
		return customUserDetails;
	}

}
