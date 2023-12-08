package com.smart.entites;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name="USER")   //USER naam ki table banegi database me
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	
	@NotBlank(message="Name field is Required!!")
	@Size(min=2,max=20,message="min 2 and max characters are allowed")    //validation ko trigger kargene controoler me
	private String name;
	
	@Column(unique = true)   //email unique hona chaiye same nahi honi chiye
	private String email;
	private String password;
	private String role;
	private boolean enabled;
	private String inageUrl;    //imageUrl he par mapping sab ho gya he isiliye change nahi kiya
	
	@Column(length=500)        //about colunm me 500 ki lentgh honi cahiye
	private String about;
	
	
	
	
	
	

	//mapping kasie karoge one user has maney contacts to one to many mapping lagegi unidirectional ---->
	//bidrectional mapping matalb contacts se user tak aana he to vaha se maney to one mapping hogi
	//ek user ke pass bahot sare contact he to...o contact rakhne ke liye hum use kraenge list ko
	
	@OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")
	private List<Contact> contact=new ArrayList<>();    //isake liye alag se table banegi
	
	
	
	//orphanRemoval=true matlab koi bhi child entity  link ho jayagi apane parent entity se to us case me id ko remove kiya jayaga
	//@OneToMany(mappedBy ="user",cascade=CascadeType.ALL,orphanRemoval=true)
	//*private List<Contact> contact=new ArrayList<>();  */
	
	
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getInageUrl() {
		return inageUrl;
	}

	public void setInageUrl(String inageUrl) {
		this.inageUrl = inageUrl;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public List<Contact> getContact() {          //list return hoke meliegi
		return contact;
	}

	public void setContact(List<Contact> contact) {    //new list banyegi LIst me
		this.contact = contact;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", enabled=" + enabled + ", inageUrl=" + inageUrl + ", about=" + about + ", contact=" + contact + "]";
	}
	
	
	
	

}
