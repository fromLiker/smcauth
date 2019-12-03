package com.ibm.fsdsmc.jwtauth;

import java.util.UUID;

public class JWTConstant {
	
    /**
     * <B>构造方法</B><BR>
     */
    private JWTConstant() {
    }
    
    /** JWTAuthResult返回的过期代码code */
    public static final int JWT_ERRCODE_EXPIRE = 5002;

    /** JWTAuthResult返回的错误代码code */
    public static final int JWT_ERRCODE_FAIL = 5001;
    
    /** a是javaJDK提供的一个自动生成主键的方法。
     * UUID(Universally Unique Identifier)全局唯一标识符,是指在一台机器上生成的数字，
     *a 它保证对在同一时空中的所有机器都是唯一的，是由一个十六位的数字组成,表现出来的形式。
     *a 由以下几部分的组合：当前日期和时间(UUID的第一个部分与时间有关，
     *a 如果你在生成一个UUID之后，过几秒又生成一个UUID，则第一个部分不同，其余相同)，时钟序列，
     *a 全局唯一的IEEE机器识别号（如果有网卡，从网卡获得，没有网卡以其他方式获得），UUID的唯一缺陷在于生成的结果串会比较长。
     */
    public static final String JWT_ID = UUID.randomUUID().toString();

    /** secret code */
    public static final String JWT_SECERT = "thisislikerssecretcode";
    
    public static final int JWT_TTL = 60*60*1000;  //millisecond
    
    
}
