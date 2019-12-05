package com.ibm.fsdsmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ibm.fsdsmc.constant.Const;
import com.ibm.fsdsmc.entity.UsersEntity;
import com.ibm.fsdsmc.model.UsersInfo;
import com.ibm.fsdsmc.service.UsersService;
import com.ibm.fsdsmc.utils.BeanUtilsCopy;
import com.ibm.fsdsmc.utils.CommonResult;
import com.ibm.fsdsmc.utils.ResponseBean;

import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
@RequestMapping("smc/users")
public class UsersController {

  @Autowired
  private UsersService usersService;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity<CommonResult> signup(@RequestBody UsersInfo usersInfo) throws Exception {
	UsersEntity usersEntity = new UsersEntity();
    BeanUtilsCopy.copyPropertiesNoNull(usersInfo, usersEntity);
    String password = usersEntity.getPassword();
    usersEntity.setPassword(passwordEncoder.encode(password));
//    usersService.saveUsersInfo(usersEntity);
    return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "User signup successfully!"));

  }

  @GetMapping("/confirmed/{username}")
  public ResponseEntity<CommonResult> activeUserByUsername(@PathVariable("username") String username) throws Exception {
	  // user click this link from email received to confirmed user
	  if(usersService.setConfirmedByUsername(username, "1")>0)
		  return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "User have confirmed!"));
	  
	  return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_ERROR_CODE, "User confirme action failed!"));
//	  return ResponseEntity.ok().body(new ResponseBean(OK.value(), OK.getReasonPhrase()).data("User have confirmed!"));
  }
//  
//  @PostMapping("/settings")
//  public ResponseEntity<CommonResult> updateUsersInfo(@RequestBody UsersInfo usersInfo) throws Exception {
//	  return null;
//  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(UNAUTHORIZED)
  public ResponseEntity<ResponseBean> handleAuthentication401Exception(AuthenticationException exception) throws Exception {
    return ResponseEntity.status(UNAUTHORIZED)
                         .body(new ResponseBean(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase()).error(exception.getMessage()));
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(FORBIDDEN)
  public ResponseEntity<ResponseBean> handleAuthentication403Exception(AuthenticationException exception) throws Exception {
    return ResponseEntity.status(FORBIDDEN).body(new ResponseBean(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase()).error(exception.getMessage()));
  }
	  
}
