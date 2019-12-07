package com.ibm.fsdsmc.repository;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.fsdsmc.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {
		
	Users findByUsername(String username);
	// Users findByEmail(String email);
	
//	#########################################################################
  

 
 @Modifying
 @Transactional
 @Query("update Users u set u.confirmed = :confirmed where u.username=:username")
 int saveUsersByUsernameAndConfirmed(@Param("username") String username, @Param("confirmed") String confirmed);

 @Modifying
 @Transactional
 @Query("update Users u set u.lastupdate = :lastupdate where u.username=:username")
 int saveUsersByUsernameAndLastupdate(@Param("username") String username, @Param("lastupdate") Date lastupdate);

// @GeneratedValue(strategy = GenerationType.IDENTITY)
// Users saveUsers(Users users);
 
  
}