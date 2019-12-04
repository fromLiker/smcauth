package com.ibm.fsdsmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.fsdsmc.entity.UsersEntity;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
		
	UsersEntity findByUsername(String username);
	// UsersEntity findByEmail(String email);

}