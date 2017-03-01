package com.asiainfo.o2p.wsdl.common.spring.web;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.linkage.rainbow.util.spring.ContextHolder;

/**
 * web.xml启动时候注册spring context
 * 而后使用者可以通过SpringBeanInvoker直接调用服务
 * web.xml配置例子:
 * 	<listener>
 *		<listener-class>
 *			com.ailk.eaap.o2p.common.spring.web.ContextHolderListener
 *		</listener-class>
 *	</listener>
 * 
 *
 */
public class ContextHolderListener extends ContextLoaderListener{
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		ContextHolder.setApplicationContext(ctx);
	}
}
