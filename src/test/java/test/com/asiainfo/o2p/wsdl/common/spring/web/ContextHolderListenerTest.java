/** 
 * Project Name:o2p-wsdl 
 * File Name:ContextHolderListenerTest.java 
 * Package Name:test.com.asiainfo.o2p.wsdl.common.spring.web 
 * Date:2015年6月5日下午4:02:30 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package test.com.asiainfo.o2p.wsdl.common.spring.web;  

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.asiainfo.o2p.wsdl.common.spring.web.ContextHolderListener;
import com.linkage.rainbow.util.spring.ContextHolder;

/** 
 * ClassName:ContextHolderListenerTest <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2015年6月5日 下午4:02:30 <br/> 
 * @author   zhongming 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class ContextHolderListenerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testcontextInitialized(){
		ContextHolderListener fixture = EasyMock.createMock(ContextHolderListener.class);
		
		ServletContextEvent event = EasyMock.createMock(ServletContextEvent.class);
		fixture.contextDestroyed(event);
		
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		
//		EasyMock.verify(fixture);
		
		
	}

}
