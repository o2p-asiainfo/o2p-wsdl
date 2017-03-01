package com.asiainfo.o2p.wsdl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class WsdlDaoImpl implements IWsdlDao {
	private SqlSessionTemplate sqlSessionTemplate;



	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	@Override
	public void saveProto(Contract contract,String tenantId) {
		contract.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT", contract);
	}

	@Override
	public void saveService(Service service,String tenantId) {
		service.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_SERVICE", service);
	}


	@Override
	public void saveOrg(Org org,String tenantId) {
		org.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_ORG", org);
		
	}

	@Override
	public void saveComponent(Component component,String tenantId) {
		component.setTenantId(new Integer(tenantId));
		
		sqlSessionTemplate.insert("wsdl.ADD_COMPONENT", component);
		
	}

	@Override
	public void saveContractVersion(ContractVersion contractVersion,String tenantId) {
		contractVersion.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION", contractVersion);
		
	}

	@Override
	public void saveContractDoc(ContractDoc contractDoc,String tenantId) {
		contractDoc.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC", contractDoc);
	}

	@Override
	public void saveDocContract(DocContract dc,String tenantId) {
		dc.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_DOC_CONTRACT", dc);
		
	}

	@Override
	public void saveContractFormat(Map<String, Object> param,String tenantId) {
		param.put("tenantId", tenantId);
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_FORMAT", param);
		
	}

	@Override
	public void saveNodeDesc(NodeDesc nodeDesc,String tenantId) {
		nodeDesc.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_NODE_DESC", nodeDesc);
		
	}

	@Override
	public void saveFileShare(Map<String, Object> param,String tenantId) {
		param.put("tenantId", tenantId);
		sqlSessionTemplate.insert("wsdl.ADD_FILE_SHARE", param);
		
	}

	@Override
	public int getSeq(String string) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sequenceName", string);
		return (Integer)sqlSessionTemplate.selectOne("wsdlservice.sequence", param);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getContractDocList(String resourceAliss, String docVersion,String tenantId) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("resourceAliss", resourceAliss);
		param.put("tenantId", tenantId);
		return sqlSessionTemplate.selectList("wsdl.GET_CONTRACT_DOC_LIST", param);
	}

	@Override
	public void saveContractDocRela(Map<String, Object> param,String tenantId) {
		param.put("tenantId", tenantId);
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_DOC_RELA", param);
		
	}

	@Override
	public void saveContractVersionRelaJson(Map<String, Object> param,String tenantId) {
		param.put("tenantId", tenantId);
		sqlSessionTemplate.insert("wsdl.ADD_CONTRACT_VERSION_RELA", param);
		
	}

	@Override
	public void saveApi(Api api,String tenantId) {
		api.setTenantId(new Integer(tenantId));
		sqlSessionTemplate.insert("wsdl.ADD_API", api);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getContractByCode(String contractCode,String tenantId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("contractCode", contractCode);
		param.put("tenantId", tenantId);
		return sqlSessionTemplate.selectList("wsdl.GET_CONTRACT_CODE_LIST", param);
	}
}
