package com.ibm.fsdsmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.fsdsmc.entity.UsersEntity;
import com.ibm.fsdsmc.repository.UsersRepository;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	public UsersEntity getUserByUsername(String username) {
		return usersRepository.findByUsername(username);
	}
	
	// public UsersEntity getUserByEmail(String email) {
	//   return usersRepository.findByEmail(email);
	// }
	
	//    #########################################################################################
	
	public int setConfirmedByUsername(String username, String confirmed) {
		return usersRepository.saveUsersEntityByUsernameAndConfirmed(username, "1");
	};
	
	public UsersEntity saveUsersInfo(UsersEntity usersEntity) {
		return usersRepository.save(usersEntity);
	};


    
}