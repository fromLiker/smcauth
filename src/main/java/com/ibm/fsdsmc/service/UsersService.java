package com.ibm.fsdsmc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.fsdsmc.entity.Users;
import com.ibm.fsdsmc.repository.UsersRepository;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	public Users getUserByUsername(String username) {
		return usersRepository.findByUsername(username);
	}
	
	// public Users getUserByEmail(String email) {
	//   return usersRepository.findByEmail(email);
	// }
	
	//    #########################################################################################
	
	public int setConfirmedByUsername(String username, String confirmed) {
		return usersRepository.saveUsersByUsernameAndConfirmed(username, "1");
	};
	
	public Users saveUsersInfo(Users users) {
		return usersRepository.save(users);
	};


    
}