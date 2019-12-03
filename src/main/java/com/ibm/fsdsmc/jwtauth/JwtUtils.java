package com.ibm.fsdsmc.jwtauth;

import org.apache.tomcat.util.codec.binary.Base64;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import com.ibm.fsdsmc.jwtauth.CheckResult;

@Service
public class JwtUtils {
	
    /**
     * a由字符串生成加密key
     *
     * @return
     */
    public static SecretKey generalKey() {
    	
    	// a本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(JWTConstant.JWT_SECERT);
        
        // a根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        
        return key;
    }
	
    /**
     * A签发JWT
     * @param id
     * @param issuer
     * @param subject 可以是JSON数据 尽可能少,  JWT依赖于Jackson，需要在程序中加入Jackson的jar包且版本大于2.x
     * @param ttlMillis
     * @return  String
     *
     */
    public static String createJWT(String id, String issuer, String subject, long ttlMillis) {
    	
    	// a指定签名的时候使用的签名算法，也就是header那部分，jjwt已经将这部分内容封装好了。
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        // a生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        
        // a创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("uid", "123456");
//        claims.put("user_name", "admin");
//        claims.put("nick_name", "X-rapido");        
        
        // a生成签名的时候使用的秘钥secret，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。
        // a一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
        SecretKey secretKey = generalKey();
        
        // a下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
        		
//        		.setClaims(claims)     // a如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的 
                .setId(id)             // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
                .setSubject(subject)   // sub(Subject)：代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志
                .setIssuer(issuer)     // issuer：jwt签发人
                .setIssuedAt(now)      // iat: jwt的签发时间
                .signWith(signatureAlgorithm, secretKey); // 设置签名使用的签名算法和签名使用的秘钥
        
        // a设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate); // 过期时间
        }
        return builder.compact();
    }
    
    /**
     * A验证JWT ?
     * @param jwtStr
     * @return
     */
    public static CheckResult validateJWT(String jwtStr) {
        CheckResult checkResult = new CheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        } catch (ExpiredJwtException e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_EXPIRE);
            checkResult.setSuccess(false);
        } catch (SignatureException e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        } catch (Exception e) {
            checkResult.setErrCode(JWTConstant.JWT_ERRCODE_FAIL);
            checkResult.setSuccess(false);
        }
        return checkResult;
    }
    
    /**
     * 
     * a解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
    	
    	//a签名秘钥，和生成的签名的秘钥一模一样
        SecretKey secretKey = generalKey();
        
        Claims claims = Jwts.parser()  //a得到DefaultJwtParser
                .setSigningKey(secretKey)                 //a设置签名的秘钥
                .parseClaimsJws(jwt).getBody();     //a设置需要解析的jwt
        
        return claims;
    }

}
