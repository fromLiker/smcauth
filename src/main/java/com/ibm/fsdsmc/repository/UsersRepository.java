package com.ibm.fsdsmc.repository;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ibm.fsdsmc.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
		
	UsersEntity findByUsername(String username);
	// UsersEntity findByEmail(String email);
	
//	#########################################################################
  

 
 @Modifying
 @Transactional
 @Query("update UsersEntity u set u.confirmed = :confirmed where u.username=:username")
 int saveUsersEntityByUsernameAndConfirmed(@Param("username") String username, @Param("confirmed") String confirmed);


// @GeneratedValue(strategy = GenerationType.IDENTITY)
// UsersEntity saveUsersEntity(UsersEntity usersEntity);
 
  
}