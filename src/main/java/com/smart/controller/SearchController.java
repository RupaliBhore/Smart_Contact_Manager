
 package com.smart.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;               //contact/ user ko search karane ke liye ye repository banai he
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.smart.entites.*;


//search controoler ko me return karaunga JSON to json ke liye me restcontroller annotion use karunga..ye kisi page /view ko return nhi kar 
//raha hoga view return karata to ise hum controller banate....
//@RestController matlab usey response body vise hi return karani he..json hoga to json return karega as a string
@RestController
public class SearchController {
	
	
	//ye laga diya to hum userRepository ka getUserByUserName ye method bhi yaha is class me call kar sakate he eamil pass kar sakte
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	
	//search handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query, Principal principal)
	{
		System.out.println(query);
		
		//jo banda login he usaki details nikala he o user verible me  //getname jo current user login he usaki details dega
		User user=this.userRepository.getUserByUserName(principal.getName());
		
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user); //contacts ki list milegi
		
		return  ResponseEntity.ok(contacts);
		
		
		                              
		                                 //process
		
//jo bhi query aap doge o query query varible me aayegi principal.getName() isase jo banda login he usaki username aayega username aur
//jo query di he o pass hojayga findByNameContainigAndUser is method ko ye method result dega o result humane contacts verible me rakha 
//he  us result ko json ke roop me return karnge wapas  ResponseEntity.ok(contacts)
	}
  
  
  
}

