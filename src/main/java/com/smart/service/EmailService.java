
package com.smart.service;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


import org.springframework.stereotype.Service;



@Service
public class EmailService {
	
	public boolean sendEmail(String subject,String message,String to) {
		
		boolean f=false;
		
		String from="bhorerupali7488@gmail.com";
		
		
		//varible to gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		
		System.out.println("PROPERTIES :"+properties);
		
		
		//setting importanat information to properties object
		
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		
		//step 1 :to get the session object..
		Session session= Session.getInstance(properties,new Authenticator()
		{
			@Override
			protected PasswordAuthentication  getPasswordAuthentication()
			{
//jis email aur password se login kiya tha o dalo otp send karane ke liye..kykuki o pahile databse me dekhega ki ye banda database me
				//he ye nahi
				
	//you have to enter app generated password instead of putting google account password			
				return new PasswordAuthentication ("bhorerupali7488@gmail.com","vfqgbcgkzhmwbbbf");
			}
			
		});
		
		session.setDebug(true);
		
		//step 2: compose the message [text,multi media]
		
		MimeMessage  m= new MimeMessage(session);
		
		try {
			
			//form email
			m.setFrom(from);
			
			
			
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
			
			//adding subject to message
			m.setSubject(subject);
			
			//adding text to message
			m.setText(message);
			

			//html me jo bhi likhoge o as it is jayega
			//m.setContent(message,"text/html");
			
			
			//send
			
			
			//step 3:send message using tranport calss
			Transport.send(m);
			
			System.out.println("sent success...................");
			f=true;
			
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return f;
		
	}

}
