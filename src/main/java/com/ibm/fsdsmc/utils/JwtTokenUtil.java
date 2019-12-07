package com.ibm.fsdsmc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class JwtTokenUtil implements Serializable {

  /**
	 * 
	 */
  private static final long serialVersionUID = 5479877508375537272L;
  public static final String TOKEN_HEADER = "JWT-Token";
  public static final String TOKEN_PREFIX = "Shazi ";
  public static final long EXPIRATION = 86400000L; // 1 day (millisecond)
  public static final long EXPIRATION_REMEMBER = 604800000L; // 7 days
  private static final String SECRET = "zheshiyigeSecret";
  private static final String ISSUSER = "DSWLiker";
  private static final String ROLE_CLAIMS = "SMCRole";

  /**
   * a生成jwt token
   */
  public static String generateToken(UserDetails details, boolean isRememberMe) {
    // if click remember me，the token expiration time will be EXPIRATION_REMEMBER
    long expiration = isRememberMe ? EXPIRATION_REMEMBER:EXPIRATION;
	// a now date 
	Date nowDate = new Date();
    // a过期时间
//    Date expireDate = new Date(nowDate.getTime() + expiration * 1000);
    Date expireDate = new Date(System.currentTimeMillis() + expiration);
    // a打印这些时间
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	System.out.println("nowDate  >>>>"+df.format(nowDate)); 
    System.out.println("登录过期时间  >>>>"+df.format(expireDate));

    HashMap<String, Object> map = new HashMap<>();
    map.put(ROLE_CLAIMS, details.getAuthorities()); // roles
    System.out.println("details.getAuthorities()) >>>>"+details.getAuthorities());
    System.out.println("1) >>>>"+map);
    System.out.println("2 >>>>"+ISSUSER);
    System.out.println("3 >>>>"+nowDate);
    System.out.println("4 >>>>"+expireDate);

    return Jwts.builder()
			   .signWith(SignatureAlgorithm.HS512, SECRET) // Algorithm
		       .setClaims(map) // customer info
		       .setIssuer(ISSUSER) // jwt issuser
		       .setSubject(details.getUsername()) // jwt user
		       .setIssuedAt(nowDate) // jwt issuser date
		//     .setIssuedAt(new Date()) // jwt issuser date
		       .setExpiration(expireDate) // expiration time for key
		       .compact();
  }

  /**
   * token是否过期
   * @return  true：过期
   * lastLoginDate 最后一次登录时间
   * issueDate token 签发时间
   */
  public static boolean isTokenExpired(Date expireDate, Date lastupDate, Date issueDate) {
	  System.out.println("expireDate,lastupDate,issueDate >>>>"+expireDate+">>>>"+lastupDate+">>>>"+issueDate);
      //token签发时间小于上次登录时间 过期
      if(lastupDate == null){
    	  
    	  // a原来token没过期
          return expireDate.before(new Date());
      }else{
    	  
    	  // a原来token过期
          return issueDate.before(lastupDate);
      }
  }

  public static String generateToken(Authentication authentication, boolean isRememberMe) {
    long expiration = isRememberMe ? EXPIRATION_REMEMBER:EXPIRATION;

    HashMap<String, Object> map = new HashMap<>();
    map.put(ROLE_CLAIMS, authentication.getAuthorities());

    return Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET) // Algorithm
               .setClaims(map) // customer info
               .setIssuer(ISSUSER) // jwt issuser
               .setSubject(authentication.getName()) // jwt user
               .setIssuedAt(new Date()) // jwt issuser date
               .setExpiration(new Date(System.currentTimeMillis() + expiration)) // expiration time for key
               .compact();
  }

  public static String getUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  public static Set<String> getUserRole(String token) {
    List<GrantedAuthority> userAuthorities = (List<GrantedAuthority>) getTokenBody(token).get(ROLE_CLAIMS);
    return AuthorityUtils.authorityListToSet(userAuthorities);
  }

  public static boolean isExpiration(String token) {
    return getTokenBody(token).getExpiration().before(new Date());
  }

  // getClaimByToken
  public static Claims getTokenBody(String token) { // parseClaimsJws is also verifying the token and will throw exception if token invalid
    return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
  }

  public static boolean validateToken(String token, UserDetails userDetails) {
    User user = (User) userDetails;
    final String tokenUsername = getUsername(token);
    return (tokenUsername.equals(user.getUsername()) && isExpiration(token) == false);
  }

}
