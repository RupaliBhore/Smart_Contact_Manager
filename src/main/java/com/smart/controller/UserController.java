package com.smart.controller;
                                                    //controller pe koi bhi change karte ho to logot ho jaoge
                                                   //@requestparam(otp) <input name=otp to requestparam me input ka name hota he
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import com.smart.helper.Message;

//isa controller ke liye http://localhost:8080/user/index ye url fire karo
@Controller
@RequestMapping("/user")
public class UserController { 
	
	
	@Autowired
	private BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	//ye laga diya to hum userRepository ka getUserByUserName ye method bhi yaha is class me call kar sakate he eamil pass kar sakte
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	                                   //common data add hoga her handler ke liye
//is class me jithane bhi handler honge uname user add karane ke liye ek alag se msthod he banate he..ye method har bar chalega har bar
//chanale ke liye hum use karenge @modelAttribute ka.ab har handler ke liye ye method chalega
	@ModelAttribute
	public void addCommonData(Model m,Principal principal)
	{
		
		String  userName = principal.getName();//debugging karana jaruru hota he ki user name aaya he ki nahi console pe print karake dekhe
		
		//isae pata chalega ki hamara user name kya aaya he usi hisab se aage processing karenge aur isa username ke through aap database me query 
		//likha sakate ho query likhkar data nikal sakate ho
				 System.out.println("USERNAME : " +userName); 
				
				 
				 //get the user using username(Email)
				 User user = userRepository.getUserByUserName(userName); //jo banda login hoga o yaha aa jayega.to user ka data print kar sakte ho
				 
		//user ke andhar jo user aayega o print hoga consol pe..yahi user hum dasboard pe bhejenge aur data int karnge
				 System.out.println("USER "+user); 
				 
				 
				 //dashboard pe ye user ko bhejenge
				 m.addAttribute("user",user);
		
	}
	
	
	
	
	                              //dashboard Home
	//Principal ki madat se hum user ka email id nikal payenege jo bhi user ka unique identifire he use nikal sakate he yaha email he
	@RequestMapping("/index")
	public String dashborad(Model model,Principal principal)
	{
		model.addAttribute("title","user DashBord");
		
		return "normal/user_dashbord";  //normal folder ke andhar "user_dashbord ye meri file ha
	}
	
	
	
	//jab aap fire karoge user/add-contact to add-content-form ye form open hoga
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");        //page ke uper jo title ata he o humne yaha se dyanamic bheja he
		model.addAttribute("contact",new Contact());
		
		return "normal/add-content-form";  //normal folder ke andhar ye htmal page he
	}
	
	              //username yaha email he   add contact
//imge ke saat saat text bhi store karana he database me processing add contact foram
//postmapping hogi kyuki jo data hum send kar rahe apane form se o post method ke roop me aa raha he to apako batana nahi padega ki aap 
//post method use kar rahe ho @requestMapping use karoge to waha apako batane ki jarurat hoti he
	//url form ke action se match hona chaiye
	
	//reuestparam image ki file kis varible aayagi is liye humne ye annotation ka use kiya.@RequestParam form me jo data ata he use save
	//save karega image file aap primitive datatype to nahi store kar sakate..to isi file ko store karane ke liye ek object hota he
	//multipartFile naam ka isame save karenge file naame ke verible me save karenge ye MultipartFile ka varible he iamge file me aaygi
	
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal principal,HttpSession session)
	{
		
		
		
		try {
			
			
	//contact kisi na kisi user ka hoga jo banda add kar raha hoga usi user ke pass ye contact jayega pahile us user ki detali nikalo
			//user ki details apako principle se mil jayegi
		  String name= principal.getName();   //username nikala
		  User user = this.userRepository.getUserByUserName(name);//user nikala. mil user jayga jo user ye contact add karega user ke pass list hogi contact
		
		 
		  //processing and uploading imalge file in file varible
		  //pahile check karo file empty to nehi
		  if(file.isEmpty())
		  {
			  //file agar empty he to ye msg dekhega
			  System.out.println("File Is Empty");
			  
			  //agar contact ki koi photo nahi he to by defoult hum use ek photo dikhayenge contact.png
			  contact.setImage("contact.png");
			  
		  }
		  else {
			  
	//else me aya matlab file me kuch he to upload karo file me jo he folder me.then file ke naam ko conatct ke andhar upadte karana he
			  contact.setImage(file.getOriginalFilename());  //file ka naam nikalo
			  
			  //file kaha upload karni he usaka path dedo ClassPathResource ye ek class he uspe click karo pado
			  File saveFile = new ClassPathResource("static/image").getFile();
			  
//files naam ka ek class he nio pacakge me he copy()method source se sare byte ko uatayga aur apake destination folder me copy kar dega

			  
			  Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			  
			  
//inputStream, target, Option ye vali copy method lelo inputStream matlab jaha se data read karana he o sream ,target matlab o path dena
//he jaha data write karna he  opetin matlab opetion kya hoga agar pahile se file mil jayegi kya use replace karna he kya nhin karana he
//kya file nayi banani he ye file ke option pass karne he
			  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			  
			  System.out.println("Image is uploadede");
			  
			  
		  }
		  
		  
		  contact.setUser(user); // user ko conact do
		  
		  user.getContact ().add(contact);//user hi contact ko add karta he. user  se contact nikale aur list me add kare
		  
		  
		  this.userRepository.save(user);  //database me user save ho raha he..user upadte ho jaygea
		
//contact object jab print karoge to contact ka tostring method chalega dekho contact class me tostring method he kya
		System.out.println("DATA "+contact);
		
		System.out.println("Added to Database");
		
		
//success msg dena he user ko jab contact add ho jaye me data ko httpsession me rakhe bhejunga dyanamicallly msg session var me store
		session.setAttribute("message", new Message("Your Contact is added!! Add More.." ,"success"));
		
		}catch (Exception e) {
			System.out.println("Error "+e.getMessage());
			e.printStackTrace(); //error ki deatils mil jaygi consol pe
			
			//contact add hone me agar koi problem hota he to ye msg dena chiye
			session.setAttribute("message", new Message("Something Went Wrong!! Try Again" ,"danger"));
		}
		
		return "normal/add-content-form";   //sucessfully contact add ho jaye to firse add contact form open ho
		
		
//form ke andhar jo data he jo feild he o contact class/ entity ka he ye sare feild humane match karake rakhe he alredy contact class ke
//andhar jo feild thi vahi feild ke naam humane humane contact form me sue kiye he matlab mapping kiye he
//contact object me o sari feild aayegi <input name="name" contact class ke feild ka naam diya he mapping hogi to is feild ka data
		//isame aayega isai tarah saare feild ka data ayega saare feild ko map kiya he
	}
	

	
	
	
	                                    //hander for show contact
	
	  //pagemention yaha pe sare contacts hume bhejane he...me contact ki repository banaunga contacts ko 
	//change karne ke liye dao main jise user ki repository he vaise hum contact ki bana lenge
	
	//per page kitane contact hone chaiye 5 =5[n]
	//current page =0 [page]  kis page pe ho ye chiz chiye user se path verible se lenge...page maltab current page
	
	@GetMapping("/show-contacts/{page}")   
	public String showContacts(@PathVariable("page") Integer page,Model m,Principal principal)
	{
		m.addAttribute("title","Show User Contacts");
		
		String  userName = principal.getName();  //username mil jayga..is project me username emailid he username aur id diffent he
		
		//yaha jo username mila he usase hum pura ka pura fetch karenge userreposiroty se
		User user = this.userRepository.getUserByUserName(userName);   //user nikala is user se aap id nikal sakate ho userid
		
		
		//Pageable bhi ek interface he isake paas pagination ki sari information store hogi per page kitane conatct hone chiye aur cuuurent page
		//isi hisab se data fecth karke layega pagable ke obect me ye 2 chize daldo
		//pageable ka object banate he PageRequest ye ek class he isaki ek method he of..pageable parent inerface he to hum parent 
		//ke varible me child ko store kar sakate he
		
		Pageable pageable= PageRequest.of(page,3);  //ek page pe 3 contact dikahi dena chaiye aap jithane chahe utane contact dikho 300 
	
//custom method implement karane ke liye findContactsByUser ye custom humane method banai he..humane contactsRepository banai
//hume sirf usi bande ke contacts nikalane he jo logged in ho jisane login kiya he to muze custom method use karani ho.principle se user
//ko nikalana hoga
		
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(),pageable);//contacts me sare contacts store he
		
		
		//contacts ko wapas bhejenge
		m.addAttribute("contacts",contacts);    //sare contacts aa jayenge
		
		//current page
		m.addAttribute("Currentpage",page);           //current page current kis page ho
		
		m.addAttribute("totalPages",contacts.getTotalPages());  //toatl kitane pages he
		
		return "normal/show_contacts";
	}
	
	
	
	
                               //Email pe click kare to user ki details dekhai de
	//jab eamil pe click kare to us user contact ki sari deatils hume show homi chaiye isake liye ye handler banaya he
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId,Model model,Principal principal)
	{
		System.out.println("CID "+cId);
		
		Optional<Contact> contactOptinal = this.contactRepository.findById(cId);
		
		Contact contact = contactOptinal.get();   //actual contact ayaga aur is conact verible ko wapas bhej do apane view me model ke
		
		
		
//jis bande ne login kiya he vahi bandha jo usane contact save karake rakhe he sirf use hi dekhe sake aur kisi user ne save kare huye
//contact ko ye dekh na sake security purpose principle se jo bandha login he use nikalo
		String userName = principal.getName();  //usrname niklo isale current user kon login he ye nikalo
		
		
		User user= this.userRepository.getUserByUserName(userName);  //jis user ne login kiya he o mil jayega
		
		
//user.getId matalb usaki id jo bandha login he..apane check kiya kya ye id equl he contcat.getuser malalab apane jo contact nikala he
//usake jo user he usake id ke barabar he userid....contact ke user ki id aur jo bandha login he ye dono ek hi he to maltb jisane
//login kiya he usika conates he to  data bhej do
		if(user.getId()==contact.getUser().getId())
		{
			
			model.addAttribute("contact",contact);
			model.addAttribute("title ",contact.getName());    
			
		}

		return "normal/contact_detail";
	}
	
	
	
	
	
	
	                                    //delete contact
//jab delete buton pe click kare to contcat delete ho jaye /user lagane ki jarurat nahu he kyuki humane upar handle banaya user ka
//to o handler sab ke liye chalega  jo cId path me diyi he usako recevie karane keliye pathVaribale use karte he kuch bhejan he to model
	
	
	@GetMapping("/delete/{cid}")         //kis form ko muze yaha se bhejana he is id se pata chal jayega cid
	public String deleteContact(@PathVariable("cid") Integer cId,Model model,HttpSession session)
	{
		
		System.out.println("CID "+cId);
		
		Contact contact = this.contactRepository.findById(cId).get();  //id se user ko get kiya aur contact varible me user ki id store
		
		//check exepetion  assingment
		
		System.out.println("Contact "+contact.getcId());

//contact ko delete kar sakte he with the help of contactRepository.contactRepository us contact ko delete karega jisaki id is 
//contact ke andhar he agar contact delete nhi hoga to IllegaleArgumentExpecption aayegi and given entity is null
//aap deleteById bhi use kar sakate the agar apako contcat fetch nahi karana tha lekin humane yaha sirf delete() hi use kiya kyuki
//pichali bar jo problem ho rahi thi yaha bhi ho sakti he matlab koi bhi yrl me chedchad karake dusare user ke contacts ko dekha sakata
//he deleteById()method use kiyi to isliye apako yaha check  lagana hoga  ki contact kon delete kar raha he jo contact delete kar raha he
//usaki contact ki id aur jo delete kar raha he usaki id agar match ho rahi he to he o contact delete hona chaiye
		
		
		
		           // delete karne se pahile hum contact me null rakhenge matlab contact ko unlinked karnge us user se
		
		               contact.setUser(null);
		               
		              
//assingment jo contact delete huva he usaki photo bhi delete honi chaiye photo rakhi he image folder me usaka naam apako 
//contact.getimage karake mil jayaga
		               
		               
		
		
		//conact delete hota he to me ek msg dunga httpsession ke andhar....success matlab green color ka dekhai dega
		 
		
	     this.contactRepository.deleteContactById(cId);   //deleteContactById
		
		
		System.out.println("DELETED");
		
		session.setAttribute("message", new Message("Contact deleted Successfully.." ,"success"));
		
		
//show-contacts ke 0 page pe agar apako usi page pe le jana he jis page ka contact delete ho raha ho to {} baracket me current page bhi
//lo
		return "redirect:/user/show-contacts/0";   
		
		
		
		
		
		
		//note about delete contact
//apane jo yaha contact fecth kiya he o user se associated he user pe jab apane mapping ki thi to casecade all lagake kiyi thi
//@OneToMany(cascade=CascadeType.ALL that means sare casecade apane true kare the isaliye ye contact delete nhi o raha he contact linked 
//he user se to hum contact ko delte karne se pahile isa user se unlinked kar denge. delete karne se pahile hum contact me null rakhenge


		
	}
	
	
	
	
	
	
	
	
	
	                               //open upadte_form
//update button pe click kare to ye form open hoga	                            
//agar aap getMapping use karoge to ya method get use karte ho to o by defolut get ho jayaega aur koi bhi url se me id deke get kar sakata he
//agar aap post use kare to by defoult o secure rahega..button click hogi tabi o handler chalega dono me se kuch bhi use kar sakate ho

	
//post method matlab aap url copy karte ho acess karne ki koshis karoge to acess nhi hoga url error aayaegi request method GET not supported
//agar aap get method use karoge to aap ko if condition alaga se likhani padegi
	
	
	
	
	
	//is handler ko chalane ke liye post ki request marani padegi...update button pe post request marani hogi
	
	@PostMapping("/update-contact/{cid}")               //kis form ko muze yaha se bhejana he is id se pata chal jayega cid
	public String updateForm(@PathVariable("cid")Integer cid, Model m)
	{
		m.addAttribute("title","Update Contact");
		
		
		
		
		
		//jisko print karana he uska data bhi bhejana he
		Contact contact = this.contactRepository.findById(cid).get();
		
		m.addAttribute("contact",contact);
		return "normal/update_form";
	}
	
	
	
	/*
	 * @PostMapping("/update-contact/{id}") //kis form ko muze yaha se bhejana he is
	 * id se pata chal jayega cid public String
	 * updateUserForm(@PathVariable("id")Integer id, Model m) {
	 * m.addAttribute("title","Update Contact");
	 * 
	 * 
	 * 
	 * 
	 * 
	 * //jisko print karana he uska data bhi bhejana he Contact contact =
	 * this.contactRepository.findById(id).get();
	 * 
	 * m.addAttribute("contact",contact); return "user_update_form"; }
	 */
	
	
	
	
	
	
	
	
	
	
	
	                             //update contact handler   //upadte photo         
	//jab upadte karke form submit karoge to ye wala handler/ url chalega
//jis contact ko upadte karenge usiko show karenge request post konsi bhi mapping aap kar sakate ho yaha hum requestmapping karke dekhenge
//uper jo humane postMagging kiyi he usika alernativ ye he bas requestMapping me aap ko batana padata he ki konasi method use kana he
//kis entity ka data aa raha he ye aap @ModelAttribute se store kar sakate ho
	
	//apaki jo image hogi nhi wali o file varible me aayegi
	
	@RequestMapping(value="/process-update",method=RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model m,
			                    HttpSession session, Principal principal) //conatact vaerible me data aa jayega sab ayaga par id nahi ayegi
	{
		try {
		
//old photo ka naam bhi apako chiye aur o naam purane contact detail ke pass hi hoga na to purana contact nikalo fecth
//contact.getcId() contact ki id pass kari aur .get() lagoge to purani detalis mil jayagi
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			
			
			
			
//pahile check karnge update karte waqt uasane  kya nayi image select kiyi thi select kiyi he to usako rewrite karana hogo usake 
//destination me agar usane image selet nahi kiyi he to hum rewrite nahi karenge file empty he matlab usane select nahi kiya he image
//humusaka ulta karnage agar usane file select nahi kiyi he to !file.isEmpty to apako file ko rewrite karana he malab nayi wali file
//upuload karani he old wali file
			
			if(!file.isEmpty())
			{
//jo upadte hoke file ayagi usame nayi photo hogi matlab file empty nahi hogi to purane file ko delete karo aur nayi file le aao
				

				//detete old photo
				 File deleteFile = new ClassPathResource("static/image").getFile();//file mil gayi matlab image
	
				 
 //right click on project->show in-> system exploer->target src image static folder me dekho old imge delete huyi hogi..
				File file1=new File(deleteFile,oldContactDetail.getImage());
				file1.delete();
				
				
				//update new photo
				 //file kaha upload karni he usaka path dedo ClassPathResource ye ek class he uspe click karo pado
				  File saveFile = new ClassPathResource("static/image").getFile();
				  
	//files naam ka ek class he nio pacakge me he copy()method source se sare byte ko uatayga aur apake destination folder me copy kar dega

				  
				  Path path = Paths.get(saveFile.getAbsolutePath()+File.separator + file.getOriginalFilename());
				  
				  
	//inputStream, target, Option ye vali copy method lelo inputStream matlab jaha se data read karana he o sream ,target matlab o path dena
	//he jaha data write karna he  opetin matlab opetion kya hoga agar pahile se file mil jayegi kya use replace karna he kya nhin karana he
	//kya file nayi banani he ye file ke option pass karne he
				  Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
				  
				  
				  //jis naam se file upload hogi us file ko upadted contact me dalana he
				  contact.setImage(file.getOriginalFilename());
				  System.out.println("Image is uploadede");
				  
			}else
			{
				//agar file empty he to maine punare contact detail ko naye wale me dal diya he to image wahi rahegi old wali
				contact.setImage(oldContactDetail.getImage());
			}
			
			
			
			
			
			//user id firse set karte he varna database me userid null dekai degi
			User user=this.userRepository.getUserByUserName(principal.getName()); //user ka name milega us name se id set karne niche
			contact.setUser(user);
			
			
			//contact ko upadte karana he to save kardo o apane aap update kar dega
			this.contactRepository.save(contact);
			
			
			//update hone ke baad green colur me ye msg dekhai dega
			session.setAttribute("message", new Message("Your Contact Is Updated.....","success"));
			
			
		} catch (Exception e) {
		e.printStackTrace();
		}
		System.out.println("CONTACT NAME :"+contact.getName());
		System.out.println("CONTACT ID :"+contact.getcId());
		return "redirect:/user/"+contact.getcId() + "/contact";    //jis contact ko delete kiya tha vahi contact dekhai dega
	}
	
	
	
	                              //Your Profile handler
	//profile koi bhi dekha sakata he to getmapping use karnge koi bhi use kar sakate ho
	
	@GetMapping("/profile")
	public String yourProfile(Model model)
	{
		model.addAttribute("title","profile page");
		return "normal/profile";
	}
	
	
	
	
	
	/*
	          //search handler
		@GetMapping("/search/{query}")
		public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
		{
			System.out.println(query);
			
			//jo banda login he usaki details nikala he o user verible me  //getname jo current user login he usaki details dega
			User user=this.userRepository.getUserByUserName(principal.getName());
			
			List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user); //contacts ki list milegi
			
			return  ResponseEntity.ok(contacts);
			
		}
	
	*/
	
	
	              //open setting handler
	            @GetMapping("/settings")  //user /settings
	            public String openSetting()
	            {
	            	return "normal/settings";
	            }
	            
	            
	            
	            //change password handler
	            //form se data lana he to requestparam use karenge.url se data lana hota to @pathvarible use karte
	            @PostMapping("/change-password")
	            public String changepassword(@RequestParam("oldPassword") String oldPassword,
	            		@RequestParam("newPassword")String newPassword,Principal Principal,HttpSession session)
	            {
	            	System.out.println("OLD PASSWORD :"+oldPassword);
	            	System.out.println("NEW PASSWORD :"+newPassword);
	            	
//check karo purna password kya is oldPassword se match he match kiya tohi humara kam hoga varna nahi hoga purana password apako user
//database me milega jo database me store hoga to pahile user nikalo principle se jo user login he o mil jayega
	            	
	            	String userName = Principal.getName();
	            	
	            	//userRepository use karake user ki details nikalo
	            	User currentUser = this.userRepository.getUserByUserName(userName);
	            	
	            	//jo user nikala he usaka password console pe print karo encrpted hoga o pasword
	            	
	            	System.out.println(currentUser.getPassword());
	            	
//old password agar is currentpassword se match ho raha he to hume new password ko save karana he isaki jagah becryptpassword ko inject 
//karo becryptpassword ke pass ek method he matches(rawPassword,encodedPassword)rawpassword matlab jisako match karana he matlab oldpassword
//encodedPassword matlab kise match karana he current user se
	            	
	            	if(this.bCryptPasswordEncoder.matches(oldPassword,currentUser.getPassword()))
	            	{
	            		//chage the password
	            		currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
	            		this.userRepository.save(currentUser);//password update ho jayega
	            		session.setAttribute("message", new Message("Your password is sucessfully changed.....","success"));
	            	}
	            	else {
	            		
	            		session.setAttribute("message", new Message("Please Enter Correct Old Password.!","danger"));
	            		return "redirect:/user/settings";  //usi page pe le jayega firse sahi password enter karne ke liye
	            	}
	            	
	            	return "redirect:/user/index";
	            }
	
	
	
}


                
//jo user naya register kar raha he o sahi user he kya usane jo email id dali he so sahi he kya ye sab cakeck karana hogo ya koi bhi 
//abc@gmail.com ise email bhej na paye eamil verification karana padega sahi emil hoga chiye register karte waqt
//user ne sali details de di to jaise hi o email dalega hum log eamil ko verify karane ke liye ek otp bhej denge eamil pe tabtak form ko
//hold pe hide karake rakhenge agar otp sahi he to otp ke saat saat humara form set kara denge
                                              























     //ise bhi user mil jate he par humane contectreposiroy ko use karke conatact nikale princilple ko use karake yaha nikale he
/*//hander for show contact  pagemention yaha pe sare contacts hume bhejane he...me contact ki repository banaunga contacts ko 
	//change karne ke liye dao main jise user ki repository he vaise hum contact ki bana lenge
	@GetMapping("/show-contacts")   //http://localhost:8080/user/show-contacts
	public String showContacts(Model m, Principal principal)
	{
		m.addAttribute("title","Show User Contacts");
		
//contacts ki list ko bhijane he yaha se show contacts me then vaha pe show karani he contac ki list database se aaygi contacts humane
//database me rakhe he na jo user login he usake ssare contacts nikal sakate he..to pahile user nikalo fir usake sare contact ko fectch
		
		String userName = principal.getName();//user name mil jayga
		
		User user = this.userRepository.getUserByUserName(userName);  //user mil gaya
		
		user.getContact();                             //jo user nikala he us user ke conatact ki list melegi
		
		
		return "normal/show-contacts";    
	}
*/