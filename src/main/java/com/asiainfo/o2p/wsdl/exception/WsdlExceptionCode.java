/** 
 * Project Name:o2p-wsdl 
 * File Name:WsdlExceptionCode.java 
 * Package Name:com.asiainfo.o2p.wsdl.exception 
 * Date:2014年10月11日上午10:43:20 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package com.asiainfo.o2p.wsdl.exception;  
/** 
 * ClassName:WsdlExceptionCode <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2014年10月11日 上午10:43:20 <br/> 
 * @author   wuwz 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public final class WsdlExceptionCode {

	/**
	 * 成功
	 */
	public static final int SUCCESS=0;
	/**
	 * 节点不存在
	 */
	public static final int NULL_OBJECT=9301;
	/**
	 * 节点值已经存在
	 */
	public static final int VALUE_EXIST=9302;
	/**
	 * 节点值为空
	 */
	public static final int NULL_VALUE=9303;
	/**
	 * 状态不可用
	 */
	public static final int STATE_UNUSEABLE=9304;
	/**
	 * 运行时异常
	 */
	public static final int RUNTIME_ERROR=9300;
	
	public static final String o2pWsdl = "o2p-wsdl";
	
	private WsdlExceptionCode() {}
}
