/** 
 * Project Name:o2p-wsdl 
 * File Name:CommonUtil.java 
 * Package Name:com.asiainfo.o2p.wsdl.util 
 * Date:2014年9月28日上午10:37:36 
 * Copyright (c) 2014, www.asiainfo.com All Rights Reserved. 
 * 
 */

package com.asiainfo.o2p.wsdl.util;

import com.ailk.eaap.o2p.common.spring.config.ZKCfgCacheHolder;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;

/**
 * ClassName:CommonUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2014年9月28日 上午10:37:36 <br/>
 * 
 * @author zhongming
 * @version
 * @since JDK 1.6
 * @see
 */
public final class CommonUtil {
	
	private static final Logger LOG = Logger.getLog(CommonUtil.class);
	
	//wuzhy add
	public static final Properties properties= new Properties();
	
	public static String wsdlDownDir = null;
	static{
		try {
			wsdlDownDir = ZKCfgCacheHolder.PROP_ITEMS.getProperty("wsdlDownDir");
			
		} catch (Exception e) {
			
		}
	}
	
	private CommonUtil(){}
				
	/**
	 * 静态初始器
	 * 初始化Properties类和关闭文件流
	 * @param time
	 */
	static{
			
		InputStream in 	=null;
		
		try {
			
			in = CommonUtil.class.getResourceAsStream("/eaap_common.properties");
			properties.load(in);
			if(null != in){
				in.close();
			}
		
		} catch (IOException e) {
			LOG.error("CommonUtil IOException {0}", ExceptionUtils.populateExecption(e, 500));
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("CommonUtil finally IOException {0}", ExceptionUtils.populateExecption(e, 500));
				}
			}
		}			
	}	
		
	
	/**
	 * 获取配置文件中的值
	 * 
	 * @param proCode
	 * @return
	 */
	private static String getValueByProCode(String proCode) {
				
		String value =(String) properties.get(proCode);
		
		return value;
		
	}

}
