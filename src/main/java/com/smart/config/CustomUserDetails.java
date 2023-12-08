package com.smart.config;
                                       //yaha userdetails ki implemention hume milegi humara 1st step pura hoga 2nd step he kargenr
                                      //userDetails ko use karenge userdetailsservices me
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smart.entites.User;

//jab implements karoge to ek error aayaei red cross pe click karo aur add unimplemets method pe click karo sare method auto aad honge
public class CustomUserDetails  implements UserDetails{
	
//me yaha pe ek varible leti hu user type ka password ke baad app email username kaha se laoge User class se hi  se laoge na aur ek 
	//constructor banate he consrutor using feilds..jab hum  CustomUserDetails ka object banayenge user pass karunga.isuser se hum roal
//nikal sakate he password username nikal sakata hu
	
	private User user;
	
	public CustomUserDetails(User user) {
	super();
	this.user = user;
}
	
	
	
//user ko kya kya authetication ho sakati he ya batane ke liye collection banan apadega mere pass ek class he SimpleGrantedAuthority
//jithane bhi authetication he o dedo roal matalab hi authetication hogi to getRoal ko call karo usako ek varible me store karunga list
//dalunga aur return karunga
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		
		return List.of(simpleGrantedAuthority);  //jo bhi us user ka roal hoga o aa jayega 
	}
	
	
	

	@Override
	public String getPassword() {
		
		return user.getPassword(); //humne jo user banaya he usake method trurn karnge
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();  //username maine as email consider karata hu is project me
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;                         //jaha jaha flase he waha true karo return
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return true;
	}

	
	

}
