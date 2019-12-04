package com.ibm.fsdsmc.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibm.fsdsmc.entity.UsersEntity;
import com.ibm.fsdsmc.service.UsersService;

@Service
public class SmcUserDetailsService implements UserDetailsService {

  @Autowired
  UsersService usersService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UsersEntity usersEntity = usersService.getUserByUsername(username);
    if (usersEntity == null) {
      throw new UsernameNotFoundException("USERNAME NOT FOUND");
    }
    String password = usersEntity.getPassword();
    String role = usersEntity.getUsertype();
    Boolean userDisabled = false;
    if (!usersEntity.getConfirmed().equalsIgnoreCase("1")) {
      userDisabled = true;
    }
    return User.withUsername(username).password(new BCryptPasswordEncoder().encode(password)).disabled(userDisabled).roles(role).build();
  }

}
