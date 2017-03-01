/** 
 * Project Name:o2p-wsdl 
 * File Name:WsdlDaoImplTest.java 
 * Package Name:test.com.asiainfo.o2p.wsdl.dao 
 * Date:2015年6月5日下午4:44:56 
 * Copyright (c) 2015, www.asiainfo.com All Rights Reserved. 
 * 
*/  
  
package test.com.asiainfo.o2p.wsdl.dao;  

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionTemplate;

import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.Component;
import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.DocContract;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Org;
import com.ailk.eaap.op2.bo.Service;
import com.linkage.rainbow.dao.impl.IBatisSqlMapDAOImpl;

/** 
 * ClassName:WsdlDaoImplTest <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2015年6月5日 下午4:44:56 <br/> 
 * @author   zhongming 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class WsdlDaoImplTest {
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testSaveProto(){
		Contract contract = new Contract();
		contract.setContractId(1);
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT", contract);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT", contract);
		
		
		
	}

	@Test
	public void testSaveService() {
		Service service = new Service();
		service.setServiceId(2);
		sqlSessionTemplate.insert("wsdl.ADD_SERVICE", service);
		EasyMock.expectLastCall().andReturn(2);
		
		sqlSessionTemplate.insert("wsdl.ADD_SERVICE", service);
		
		
	}
	
	@Test
	public void testSaveOrg() {
		Org org = new Org();
		org.setOrgId(3);
		sqlSessionTemplate.insert("wsdl.ADD_ORG", org);
		EasyMock.expectLastCall().andReturn(3);
		
		sqlSessionTemplate.insert("wsdl.ADD_ORG", org);
		
		
	}
	
	@Test
	public void testSaveComponent() {
		Component component = new Component();
		component.setComponentId("4");
		sqlSessionTemplate.insert("wsdl.ADD_COMPONENT", component);
		EasyMock.expectLastCall().andReturn(4);
		
		sqlSessionTemplate.insert("wsdl.ADD_COMPONENT", component);
		
		
		
	}
	
	@Test
	public void testSaveContractVersion() {
		ContractVersion contractVersion = new ContractVersion();
		contractVersion.setContractId("5");;
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION", contractVersion);
		EasyMock.expectLastCall().andReturn(5);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION", contractVersion);
		
		
	}
	
	@Test
	public void testSaveContractDoc() {
		ContractDoc contractDoc = new ContractDoc();
		contractDoc.setContractDocId(6);
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC", contractDoc);
		EasyMock.expectLastCall().andReturn(6);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC", contractDoc);
		
		
	}
	
	@Test
	public void testSaveDocContract() {
		DocContract dc = new DocContract();
		dc.setContractDocId(7);
		sqlSessionTemplate.insert("wsdl.ADD_DOC_CONTRACT", dc);
		EasyMock.expectLastCall().andReturn(7);
		
		sqlSessionTemplate.insert("wsdl.ADD_DOC_CONTRACT", dc);
		
		
	}
	
	@Test
	public void testSaveContractFormat() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("key", "value");
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_FORMAT", param);
		EasyMock.expectLastCall().andReturn(8);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_FORMAT", param);
		
		
		
	}
	
	@Test
	public void testSaveNodeDesc() {
		NodeDesc nodeDesc = new NodeDesc();
		nodeDesc.setNodeDescId(9);
		sqlSessionTemplate.insert("wsdl.ADD_NODE_DESC", nodeDesc);
		EasyMock.expectLastCall().andReturn(9);
		
		sqlSessionTemplate.insert("wsdl.ADD_NODE_DESC", nodeDesc);
		
		
	}
	
	@Test
	public void testSaveFileShare() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("10", "10");
		sqlSessionTemplate.insert("wsdl.ADD_FILE_SHARE", param);
		EasyMock.expectLastCall().andReturn(10);
		
		sqlSessionTemplate.insert("wsdl.ADD_FILE_SHARE", param);
		
		
	}
	
	@Test
	public void testGetSeq() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sequenceName", "sequence");
		sqlSessionTemplate.selectOne("wsdlservice.sequence", param);
		
	}
	
	@Test
	public void testGetContractDocList() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("resourceAliss", "resourceAliss");
		sqlSessionTemplate.selectList("wsdl.GET_CONTRACT_DOC_LIST", param);

	}
	
	@Test
	public void testSaveContractDocRela() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("13", "13");
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC_RELA", param);
		EasyMock.expectLastCall().andReturn(13);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC_RELA", param);
		
		
	}
	
	@Test
	public void testSaveContractVersionRelaJson() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("14", "14");
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION_RELA", param);
		EasyMock.expectLastCall().andReturn(14);
		
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION_RELA", param);
		
		
	}
	
	@Test
	public void testSaveApi() {
		Api api = new Api();
		api.setApiId(15);
		sqlSessionTemplate.insert("wsdl.ADD_API", api);
		EasyMock.expectLastCall().andReturn(15);
		
		sqlSessionTemplate.insert("wsdl.ADD_API", api);
		
		
	}
	
	@Test
	public void testGetContractByCode() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("contractCode", "contractCode");
		sqlSessionTemplate.insert("wsdl.GET_CONTRACT_CODE_LIST", param);
	}
	
}
