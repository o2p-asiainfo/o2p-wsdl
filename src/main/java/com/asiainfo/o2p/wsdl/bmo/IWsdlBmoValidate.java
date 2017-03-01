package com.asiainfo.o2p.wsdl.bmo;

import java.util.List;
import java.util.Map;

import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.foundation.exception.BusinessException;

public interface IWsdlBmoValidate {

	/**
	 * 校验共享文件
	 * 
	 * @param fsParam
	 * @return
	 * @throws BusinessException
	 */
	public String checkFileShare(Map<String, Object> fsParam,String tenantId)
			throws BusinessException;

	/**
	 * 校验协议
	 * 
	 * @param contract
	 * @param contractDocId
	 * @return
	 * @throws BusinessException
	 */
	public String checkContract(Contract contract, Integer contractDocId,String tenantId)
			throws BusinessException;

	/**
	 * 校验服务
	 * 
	 * @param service
	 * @return
	 * @throws BusinessException
	 */
	public String checkService(Service service,String tenantId) throws BusinessException;

	/**
	 * 校验协议文档
	 * 
	 * @param contractDoc
	 * @return
	 * @throws BusinessException
	 */
	public String checkContractDoc(ContractDoc contractDoc,String tenantId)
			throws BusinessException;

	/**
	 * 校验协议版本
	 * 
	 * @param contractVersion
	 * @throws BusinessException
	 */
	public void checkContractVersion(ContractVersion contractVersion,String tenantId)
			throws BusinessException;

	/**
	 * 校验协议格式
	 * 
	 * @param param
	 * @throws BusinessException
	 */
	public void checkContractFormat(Map<String, Object> param,String tenantId)
			throws BusinessException;

	/**
	 * 校验节点描述
	 * 
	 * @param nodeDesc
	 * @return
	 * @throws BusinessException
	 */
	public String checkNodeDesc(NodeDesc nodeDesc,String tenantId) throws BusinessException;

	/**
	 * '资源别名+文档版本'是否已存在
	 * 
	 * @param resourceAliss
	 * @throws BusinessException
	 */
	public boolean hasResourceAlissVersion(String resourceAliss,
			String docVersion,String tenantId) throws BusinessException;

	/**
	 * 根据'资源别名'获取版本列表
	 * 
	 * @param resourceAliss
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> getContractDocVersionList(
			String resourceAliss,String tenantId) throws BusinessException;

	/**
	 * 根据contract code获取列表
	 * 
	 * @param contractCode
	 * @throws BusinessException
	 */
	public boolean hasContractByCode(String contractCode,String tenantId)
			throws BusinessException;

}
