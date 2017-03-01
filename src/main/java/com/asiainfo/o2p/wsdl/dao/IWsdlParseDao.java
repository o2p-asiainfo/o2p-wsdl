package com.asiainfo.o2p.wsdl.dao;

import java.util.HashMap;
import java.util.List;


public interface IWsdlParseDao {
	
	int getSeq(String fileName);
	
	@SuppressWarnings("rawtypes")
	List<HashMap> getFileShare(String sFileId,String tenantId);
	
	void deleteFileShare(String sFileId,String tenantId);

}
