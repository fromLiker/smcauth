package com.ibm.fsdsmc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.ibm.fsdsmc.constant.Const;
import com.ibm.fsdsmc.filters.SmcUserDetailsService;
import com.ibm.fsdsmc.model.AuthRequest;
//import com.ibm.fsdsmc.model.AuthResponse;
import com.ibm.fsdsmc.model.UserInfo;
import com.ibm.fsdsmc.utils.CommonResult;
import com.ibm.fsdsmc.utils.JwtTokenUtil;
import com.ibm.fsdsmc.utils.ResponseBean;
import java.util.Set;
import static org.springframework.http.HttpStatus.*;

@CrossOrigin
@RestController
//@RequestMapping(value = "api/security", produces = MediaType.APPLICATION_JSON_VALUE)
// a可访问api/security/login
public class AuthController {

  @Autowired
  private SmcUserDetailsService smcuserDetailsService;
  @Autowired
  private AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public ResponseEntity<CommonResult> login(@RequestBody AuthRequest request) throws Exception {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Reload password post-security so we can generate token
    UserDetails userDetails = smcuserDetailsService.loadUserByUsername(request.getEmail());
    String jwtToken = JwtTokenUtil.generateToken(userDetails, false);

    UserInfo userInfo = new UserInfo();
    userInfo.setUsername(userDetails.getUsername());
    Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
    userInfo.setUsertype(authorities.toArray()[0].toString());
    
//    AuthResponse authResponse = new AuthResponse();
//    authResponse.setJwtToken(jwtToken);
//    authResponse.setUserInfo(userInfo);
//    return ResponseEntity.ok().body(new ResponseBean(OK.value(), OK.getReasonPhrase()).data(authResponse));
    
//    return ResponseEntity.ok().header("JWT-Token", jwtToken).body(new ResponseBean(OK.value(), OK.getReasonPhrase()).data(userInfo));
    return ResponseEntity.ok().header("JWT-Token", jwtToken).body(CommonResult.build(Const.COMMONRESULT_OK_CODE, "Login successfully!", userInfo));
  }

//  @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
//  @ResponseBody
//  public ResponseEntity<ResponseBean> authenticated() throws Exception {
//      return ResponseEntity.ok().body(new ResponseBean(OK.value(), OK.getReasonPhrase()).data("AUTHENTICATED - USER VERIFIED"));
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
    return ResponseEntity.status(FORBIDDEN)
    	.body(new ResponseBean(FORBIDDEN.value(), FORBIDDEN.getReasonPhrase()).error(exception.getMessage()));
  }

}
