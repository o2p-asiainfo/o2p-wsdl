package com.asiainfo.o2p.wsdl.bmo;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.foundation.exception.BusinessException;



public interface IWsdlBmo {
	
	/**
	 * 保存机构
	 * @param org
	 * @return 
	 * @throws BusinessException
	 */
	public void saveOrg(JSONObject org,String tenantId) throws BusinessException;
	
	/**
	 * 保存组件
	 * @param component
	 * @return 
	 * @throws BusinessException
	 */
	public void saveComponent(JSONObject component, int orgId,String tenantId) throws BusinessException;
	
	/**
	 * 保存文件
	 * @param fs
	 * @return 
	 * @throws BusinessException
	 */
	public void saveFileShare(JSONObject contractDocJson, String resourceAliss, String xsdResourceAliss, int sFileId,String tenantId) throws BusinessException;
	
	/**
	 * 保存协议文档
	 * @param cd
	 * @return 
	 * @throws BusinessException
	 */
	public void saveContractDoc(JSONObject cd,String tenantId) throws BusinessException;
	
	/**
	 * 保存协议版本
	 * @param cv
	 * @return 
	 * @throws BusinessException
	 */
	public void saveContractVersion(JSONObject cv, Integer contractDocId,String tenantId) throws BusinessException;
	
	/**
	 * 保存协议格式
	 * @param cf
	 * @return 
	 * @throws BusinessException
	 */
	public void saveContractFormat(JSONArray cf,String tenantId) throws BusinessException;
	
	/**
	 * 保存节点描述
	 * @param nd
	 * @return 
	 * @throws BusinessException
	 */
	public void saveNodeDesc(JSONArray nodeDescs, int contractFormatId,String tenantId) throws BusinessException;
	
	/**
	 * 保存协议
	 * 
	 * @param proto
	 * @return 
	 * @throws BusinessException
	 */
	public void saveProto(JSONObject proto, Integer contractDocId,String tenantId) throws BusinessException;

	/**
	 * 保存服务
	 * 
	 * @param service
	 * @return 
	 * @throws BusinessException
	 */
	public void saveService(JSONObject service, int contractVersionId,String tenantId) throws BusinessException;


	/**
	 * 保存API
	 * 
	 * @param service
	 * @return 
	 * @throws BusinessException
	 */
	public void saveApi(Service service,String tenantId) throws BusinessException;
	
	/**
	 * 保存消息流数据
	 * 
	 * @param dataFlow
	 * @return 
	 * @throws BusinessException
	 */
	public void save(String dataFolw,String tenantId) throws BusinessException;
	
	/**
	 * 保存协议文档关联
	 * @param contractDocRelaJson
	 * @return 
	 * @throws BusinessException
	 */
	public void saveContractDocRela(JSONObject contractDocRelaJson, ContractDoc contractDoc,String tenantId) throws BusinessException;
	
	/**
	 * 取id
	 * @param string
	 * @return
	 * @throws BusinessException
	 */
	public int getSeq(String string)  throws BusinessException;
	
	/**
	 * 保存协议版本文档关联
	 * @param contractVersionRelaJson
	 * @param contractVersionId
	 * @return 
	 * @throws BusinessException
	 */
	public void saveContractVersionRelaJson(
			JSONObject contractVersionRelaJson, Integer contractVersionId,String tenantId)   throws BusinessException;
	
}
