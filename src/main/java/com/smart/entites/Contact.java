package com.smart.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="CONTACT")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String name;
	private String secoundName;
	private String work;
	private String email;   //yaha pe koi mathamatical operation perform nahi karana he isaliye phone string liye
	private String phone;
	private String image;
	
	@Column(length=5000)
	private String description;
	
	
	
	
//user ke liye json ko ignore karenge jise ki user ka data serilization nahi hoga circulre dependecy hogiagr ignore nhi karte to to user yaha pe  
//ayega to user class ke andhar jayega user ke andhar ka sab serilization ho jayega vaha se fir contcas ki taraf jayega isiliye usako mana kiya ki
	//user ko seriliza mat karo
	//bidirectional
	@ManyToOne
	//@JsonIgnore               
	private User user;   //join column isake liye ban jayega
	
	

	public int getcId() {
		return cId;
	}

	public void setcId(int cId) {
		this.cId = cId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecoundName() {
		return secoundName;
	}

	public void setSecoundName(String secoundName) {
		this.secoundName = secoundName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	//idhar tostring method call mat karo varana Stack overflow error aayegi
	
	/*For people who wants to know why we were getting stackoverflow error and why he removed toString() method -
	 
	
	In Contact toString() method we are printing User and in User class toString() method we are again printing Contact, this creates 
	
	an infinite loop (AKA infinite recursion) and fills our entire program stack memory.
	
	One simple solution of this problem is to not print the user (Or you can just print the userid and userName and not the whole user) 
	
	(i.e do not call User class toString() method from Contact class)
	/*@Override
	public String toString() {
		return "Contact [cId=" + cId + ", name=" + name + ", secoundName=" + secoundName + ", work=" + work + ", email="
				+ email + ", phone=" + phone + ", image=" + image + ", description=" + description + ", user=" + user
				+ "]";
	}*/
	
	
	//ye tostring method chalega to ye sare field ka data print hog
	
	
	
	

}
