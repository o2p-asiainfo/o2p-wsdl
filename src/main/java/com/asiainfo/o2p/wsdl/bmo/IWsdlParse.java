package com.asiainfo.o2p.wsdl.bmo;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;

import com.asiainfo.foundation.exception.BusinessException;
import com.eviware.soapui.support.SoapUIException;


public interface IWsdlParse {
	
	public String parseWsdl(String wsdlFile, String resourceAliss, String version,String tenantId) throws BusinessException;
	
	public String judgeWsdl(String wsdlFile, String wsdlType, String resourceAliss, String version,String tenantId) throws BusinessException;

	public void updateWsdlProject(String tenantId) throws XmlException, IOException, SoapUIException;
}
