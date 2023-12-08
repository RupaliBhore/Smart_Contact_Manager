package com.smart.controller;
                                                   //@requestparam(otp) <input name=otp to requestparam me input ka name hota he
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entites.User;
import com.smart.service.EmailService;

@Controller
public class ForgotController {
	
	
	
	//generating otp of 4 digit random number hoga to random class ka object banate he java.util me ye class he
	
			Random random = new Random(1000);
			
			
			@Autowired
			private EmailService emailService;
			
			@Autowired
			private UserRepository userRepository;
			
			
			@Autowired
			private BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	//email id form open handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form"; //templet ke andhar ye form banayenge kyuku login sinup form templet me hi he
	}
	
	
	
	
	//send otp  to user eamil handler
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email,HttpSession session)
	{
		System.out.println("EMIL "+email);
		
		
		
		int otp = random.nextInt(100000);//999999 itane ke beech ke value aa jayegi randomley konsi bhi otp ke thor pe
		
		System.out.println("OTP"+otp);
		
		
		
		// code for send otp to user email
		
		String subject="OTP From SCM";
		String message= "OTP is "+otp;    //user ko usake email pe ye mail jayega  OTP is 112012
				
				
				
		
		String to=email;
		boolean flag = this.emailService.sendEmail(subject, message,to);
		
		// agar email sahi dala he to  otp us mail pe jayegi      flag ki value true ho gayi matlab otp bhej diya he succefully
		
		
		if(flag)
		{
			
			
//thodi der ke liye hum otp ko store karte he seesion me//session ke otp se form ka otp ko match karana he match ho gaya to jo eamil he 
//usako bhi bhej sakate ho yaha to form ke saat hum email bhi wapas bhej denge
//session ke chuze jabtak session valid he tabtak he use kar sakate he matlab jabtak sessin valid he tabtak aap email aur otp featch karke
			//use kar sakenge
			
			session.setAttribute("myotp", otp);
			
			
			//jo email user ne diya tha usako rakhenge
			session.setAttribute("email", email);
			
			
			//user ne otp galat dala to  verifry otp pe bhej denge
			return "verify_otp";
		
		
			
		}else{
		
			
			session.setAttribute("message","Check Your Email id");
			
			//error aayagi to isako bhej denge wapas forgot email form pe
			return "forgot_email_form";
		}
		
			
	}
	
	
	
	
	//verify otp
	// user jo otp dalega o  otp sahi ye ki nahi verify-otp handler
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp,HttpSession session)
	{
		//myOtp me purana otp he jo hume pahile mila tha aur ye us user ki email he dono ptp ko match karana he
	
		int myOtp= (int)session.getAttribute("myotp");
		
		System.out.println("User OTP "+otp);
		System.out.println("Our OTP "+myOtp);
		
		String email=(String) session.getAttribute("email");
		
		
		
		if(myOtp==otp)
		{
			//jab user sahi otp dalega to  password_change_from is form pe user ko bhej denge password change karane ke liye
			User user = this.userRepository.getUserByUserName(email);
			
			//user null he matlab us naam ka user he hi nahi he to check karo aur error msg bhej do
			if(user==null)
			{
				//send error massge erroe bhejke firse eamil wale page pe user ko bhej denge
				
				session.setAttribute("message","User does not exits with this email");
				
				//error aayagi to isako bhej denge wapas forgot email form pe
				return "forgot_email_form";
				
				
			}else {
				
				//send password_change_from
				
			}
			
			
			// user ne sahi otp dala he to myOtp agar same he otp se to user ko change password ka foram dikhana chaiye
			return "password_change_from";
			
			
		}else
		{
			//otp match nahi hoga to verify otp is page pe user ko le jaynge message ke ssat
			
			session.setAttribute("message", "You have entered wrong otp!!");
			return "verify_otp";
			
		}
		
	}
	
	
	//change-password  handler
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword,HttpSession session)
	{
		
		String email=(String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		
		// user ke password ko change karnge jo user ne dala he
		user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
		this.userRepository.save(user);
		
		return "redirect:/signin?change=password changed successfully..";
		
	}

}

//HttpSession session session ko use karte he data ko store karane ke liye
