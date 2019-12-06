package com.ibm.fsdsmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ibm.fsdsmc.constant.Const;
import com.ibm.fsdsmc.entity.UsersEntity;
import com.ibm.fsdsmc.model.UsersInfo;
import com.ibm.fsdsmc.service.MailService;
import com.ibm.fsdsmc.service.UsersService;
import com.ibm.fsdsmc.utils.BeanUtilsCopy;
import com.ibm.fsdsmc.utils.CommonResult;
import com.ibm.fsdsmc.utils.ResponseBean;

import static org.springframework.http.HttpStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
@RequestMapping("smc/users")
public class UsersController {

  private static Logger logger = LoggerFactory.getLogger(UsersController.class);

  @Autowired
  private UsersService usersService;
  
  // @Autowired
  // private PasswordEncoder passwordEncoder;
  
  @Autowired
  private MailService mailService;

  @PostMapping("/signup")
  public ResponseEntity<CommonResult> signup(@RequestBody UsersInfo usersInfo) throws Exception {
	UsersEntity usersEntity = new UsersEntity();
	usersInfo.setConfirmed("0");
	usersInfo.setUsertype("user");
    BeanUtilsCopy.copyPropertiesNoNull(usersInfo, usersEntity);
    
// a密码加密存储
//    String password = usersEntity.getPassword();
////  usersEntity.setPassword(passwordEncoder.encode(password));
//    usersEntity.setPassword(password);

    try {
    	usersService.saveUsersInfo(usersEntity);
    }catch (Exception e){
    	e.printStackTrace();
        System.out.println("signup user error!!"+e);
        return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_ERROR_CODE, "User sign up failed, please check your enters!"));
    }
    
    // send email
//    mailService.sendHTMLMail(usersInfo.getEmail(), usersInfo.getUsername());
    
    return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "A confirmation email have send to you, please confirm!"));

  }

  @GetMapping("/confirmed/{username}")
  public String activeUserByUsername(@PathVariable("username") String username) throws Exception {
	  // user click this link from email received to confirmed user
	  if(usersService.setConfirmedByUsername(username, "1")>0) {
//		  return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "User have confirmed!"));
		  System.out.println("发送html文本文件-发生成功");
		  logger.error("User have confirmed!");
		  return "<a href='http://localhost:4200/login'>please click here to login SMC system</a>";
	  }
	  return "User confirm action failed!";
//	  return ResponseEntity.ok().body(CommonResult.build(Const.COMMONRESULT_ERROR_CODE, "User confirm action failed!"));
//	  return ResponseEntity.ok().body(new ResponseBean(OK.value(), OK.getReasonPhrase()).data("User have confirmed!"));
  }
  
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
