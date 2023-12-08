package com.smart.dao;                  //contacts ki sari details nikalne ke liye contactRepository banayi//pageable pass kar paye isiliye contactrepository banayi he

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.smart.entites.Contact;
import com.smart.entites.User;

//repository banana ke liye extends JpaRepository is repository ko extend karana jaruri hota he aur usame batana padata he kis chiz ka 
//object nikala na he aur us obect ka type kya he
//@EnableJpaRepositories
public interface ContactRepository  extends JpaRepository<Contact, Integer>{
	
	
	
	
	            
	                                   //find Contact by user
//ContactRepository  ki help se hum saare contact ko fecth kar sakate he user ke  coroller me muze aise hi conatcts mil rahe he to 
//mai extra kam kyu kar raha hu..kuki mai yaha Pagination ka method implement karene wala hu jisase yaha pagination ki funality mil jaye
	
	//is reposiroty ko controller me use karo autowired kardo inject kardo
	
	//user ki id denge o is user ki id ki query banayega where wali userId 18 doge to o 18 user ki saari contacts muze dega
//c.user.id conatact class ke andhar ka user us user ki id..jab aap findContactsByUser is method ko call karoge to aap user ki id doge
//o id where clouse me aayegi to from contacts o sare contacts aayenge apane jo id diyi he.jb bhi  contacts id ki match ho jayegi  jo apne
//diyi he suppose 8 id diyi to 8 wale user ke sare contact aajayenge as a list
	
	
	@Query("from Contact as c where c.user.id=:userId")
	
// hum yaha page return karenge naki list nhi aur page contain karega contacts ko page ek interface he data.domain pakage me
//apake pass list he to sublist represnt karata he page object
	
//Pageable bhi ek interface he isake paas pagination ki sari information store hogi per page kitane conatct hone chiye aur cuuurent page
//isi hisab se data fecth karke layega pagable ke obect me ye 2 chize daldo
	//public List<Contact> findContactsByUser(@Param("userId")int userId);
	
    public Page<Contact> findContactsByUser(@Param("userId")int userId,Pageable pageable);
	
	
	
	
	
	  //search bar ka result
	//apake fontend se koi query gayi background me to backgrund me serch method he o us query ko search karegi interface he iasaki body nahi de
	//sakate to hum khudake finder method create kar sakate he par yaha hum predefined method use karenge  findByname
	//name ke through search karana chahata hu  aap emil,phone,secound name,kisiske bhi throgh serch kar sakate ho   
	//findByName name jo he o hamari fild ka naam he..containing matlab ye o sare contancts ki nikalege jismae me jis naam se serch kar raha
		
	//o naam usame hoga ex=main amit serch kiya to amit naam ke jitane bhi contacts hoge o sare dikhai denge.ex=th likha to jis jis naam me th he
	//o sare contacts muze dikhai denge ..par jo banda login he usike hi contact search hone chaiye to me aur ek condition lagata hu And
	//user jo hoga o wahi user hoga jo me yaha se pass karunga User user to pura method banega   findByNameContainigAndUser() ye custom
		//method nahi he predefined method he

	//humane is method ki body nhi dei kyuki hum interface me kam kar rahe he to humare pass ye power nahi he ki hum isaki body de interface he
	//to spring jpi khudh isaki implementation de dega auto jab autowirid karoge to usaki jo child class hoga usaki jo implemention hogi
	// implemetation class ka object create hoga o implemetion class hume likhana nahi padata o spring jpa matlb spring boot ka module hi dega

		//isako use karana he controller ke andhar to ek controller banate he RestController ki tarah matlab jo hum web service bana rahe he usaki
		//tarah banane vale he purane controller pe bana sakate he par yaha hum naya controller banayenge
		
		public List<Contact> findByNameContainingAndUser(String name,User user);
	
	
	
	
		
	
		
		
		
		
		
	
	                         //delete contact by Id id ko use karake contact ko delete karo
	//id se pahile user ko nikalo fir delete karo
	//deleteContactById ye method hum tayar karenge agar isa tarah se data delete karoge to database se bhi data chala jayage
	
	@Modifying
	@Transactional
	@Query("delete from Contact c where c.cId=:id")
    public void deleteContactById(@Param("id") Integer id);
	
	
	
	
	         
	
	
	
	
	
	
}

//pagination ko karane ke liye hume milti he kuch class aur interfaces to
