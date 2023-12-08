package com.smart.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import com.smart.helper.Message;

@Controller
public class HomeController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	
	      @Autowired
	       private UserRepository userRepository;
	
	//Model ki help se hum data template me bhejate he
	@RequestMapping("/")
	public String home(Model model)
	{
		model.addAttribute("title","HOME PAGE SAMRT CONTACT MANANAGER");
		return "home";
	}
	
	
	//Model ki help se hum data template me bhejate he
		@RequestMapping("/about")
		public String about(Model model)
		{
			model.addAttribute("title","About-SAMRT CONTACT MANANAGER");
			return "about";
		}
		
		//singup handler
		
		@RequestMapping("/singup")
		public String singup(Model model)
		{
			model.addAttribute("title","Register-SAMRT CONTACT MANANAGER");
			
			//jab singup form open hoga to user naam ki key me ye data aajayga
			model.addAttribute("user",new User());
			return "singup";
		}
		
//this handler handling user do_register  by defolut get method hota he to post he ye batana hoga @postmethod ye annotation use karte to
//method ko mention nahikarini padati  @ModelAttribute se hum data ko accept karenge pure object me.jo jo feild match karegi apane form se
//aur is user object se match karega o values apane aap user object me aayegi..checkbox ke liye alag se veribale banayenge usame checkbox
		//ka sara data aayega..agar user ne check nahi kiya to ek defoult value denge check kiya to aargeement true ayaega check nahi
		//kiya to agreement flase ayagga if lagake agreement true he ya flase he ye check kardo
		
		//user varible liya user ka data rakhane ke liye booelan liya agreement ke liye aur data bhejane ke liye model liya
//jaise he aap @valid lagaoge apane jo rule applya kiye ye user class ke liye annotation ko use karke ex name blank nahi hona exrta.
//vaigara o sare rules applya ho jayenge
		
		@RequestMapping(value="/do_register",method=RequestMethod.POST)
		public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
				@RequestParam(value="agreement",defaultValue ="false")boolean agreement, Model model,HttpSession session)
		{
			
	      try {
			
	    	  
	    	  
	    	  if (!agreement)
				{
					System.out.println("you are not agreed the term and conditions");
					throw new Exception("you are not agreed the term and conditions");
				}
	    	  
	    	  
	    	  if(result1.hasErrors())     //agar error he to if condition chalegi aur singup page return hoga user ko firse
	    	  {
	    		  System.out.println("ERROR "+result1.toString());   //jo bhi ayaga result result ko tostring me convert karke print karenge
	    		  model.addAttribute("user",user);          //jo bhi data he o form me wapas chala jaye
	    		  return "singup";
	    	  }
		      
		      user.setRole("ROLE_USER");
		      user.setEnabled(true);
		      user.setInageUrl("default.png");
		      user.setPassword(passwordEncoder.encode(user.getPassword()));
		      
				System.out.println("Agreement "+agreement);
				System.out.println("User"+user);
				
				
	//user ko save karane ke liye apako dao ka object chiye hoga matlab userRespository ka object chiye to userRespositroy ko apako autowired
	//karana padega tabi apako usaka object milega
				User result= this.userRepository.save(user);
				
				model.addAttribute("user",new User()); //submit karane ke baad new user ke liye form open hoga
				
				
				session.setAttribute("message",new Message("Sucessfully Registered!!", "alert-success"));
				return "singup";  //dono case me singup page chaiye
	    	  
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);//jo data aya he isa user me vahi data vapas chala jayega aur apake form pe o dikha jayga
			
			session.setAttribute("message",new Message("Something Went Wrong!!"+e.getMessage(), "alert-danger"));
			return "singup";
			
			
            //process
/*agar apane aggrement check nahi kiya he to naya exception throw karnge o user ko add karenga aur simply ek message dalega
* aur singup page apako dikhai dega agar apane check kiya he terms to o user ko set karega enble karga image set karega print
* karega save bi karega then user naya dalega aur singup  page firse return karega */
		}
			
		}
		
		
		
		//handler for custom login
		@GetMapping("/signin")
		public String customLogin(Model model)
		{
			model.addAttribute("title","Login Page");
			return "login";
		}
		
		
		
		
		
}
   



                       //simple conroller
/*
//yaha pe userrepositoy ko agar hum autowired karte he to hum apane handler me userresposiry ke verible use karke usake
	//method access kar sakate he
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/test")
	@ResponseBody
	public String test()
	{
		User user = new User();
		user.setName("gaugi bai");
		user.setEmail("gangubai@gmail.com");
		
		//contact ko add karenge o database me add hoga database me jake check karo data select *from user;
		Contact contact=new Contact();
		user.getContact().add(contact);
		
		userRepository.save(user);
		return "Working";
	}
*/
