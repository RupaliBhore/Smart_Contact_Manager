package com.smart.dao;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.smart.entites.User;

@EnableJpaRepositories
public interface  UserRepository extends JpaRepository<User,Integer>{
	
//is method ko use karane wale he isake andhar ek method banalenge jo return karega apka user name method ka naam kuch bi do
//method me query likhenge jpa vali query he u.email ye user class ke andhar ki email he :email ye parameter he yaha dyanamic email lana he
//dayanamic value lane ke liye @param ka use karuga
	//@Query("SELECT u FROM  User u WHERE u.email=:email")
	//public User getUserByUserName(@Param("email") String email);

	
	@Query("SELECT u FROM  User u WHERE u.email=?1")
	//Optional<User> getUserByUserName(String email);
	public User getUserByUserName(@Param("email") String email);

	
	
	//jab bhi app getUserByUserName mehod call karoge to apako ek email pass karni he o email (:emil) is email ke jagah aa jayega aur o 
	//user aayega jisaki apane email diyi he
}
/*
@Query("FROM Student WHERE email = ?1")
Optional<Student> findStudentByEmail(String email);

@Query("SELECT s FROM Student s WHERE s.email = ?1")
Optional<Student> findStudentByEmail(String email);
*/