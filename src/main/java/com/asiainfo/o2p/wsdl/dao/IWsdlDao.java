package com.asiainfo.o2p.wsdl.dao;

import java.util.List;
import java.util.Map;

import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.Component;
import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.DocContract;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Org;
import com.ailk.eaap.op2.bo.Service;


public interface IWsdlDao {
	/**
	 * 保存协议
	 * 
	 * @param proto
	 * 
	 */
	public void saveProto(Contract contract,String tenantId);

	/**
	 * 保存服务
	 * 
	 * @param service
	 * 
	 */
	public void saveService(Service service,String tenantId);

	/**
	 * 保存组件
	 * @param org
	 */
	public void saveOrg(Org org,String tenantId);

	/**
	 * 保存组件
	 * @param component
	 */
	public void saveComponent(Component component,String tenantId);

	/**
	 * 保存协议版本
	 * @param contractVersion
	 */
	public void saveContractVersion(ContractVersion contractVersion,String tenantId);

	/**
	 * 保存协议文档
	 * @param contractDoc
	 */
	public void saveContractDoc(ContractDoc contractDoc,String tenantId);

	/**
	 * 保存文档对应协议
	 * @param dc
	 */
	public void saveDocContract(DocContract dc,String tenantId);

	/**
	 * 保存协议格式
	 * @param param
	 */
	public void saveContractFormat(Map<String, Object> param,String tenantId);

	/**
	 * 保存节点描述
	 * @param nodeDesc
	 */
	public void saveNodeDesc(NodeDesc nodeDesc,String tenantId);

	/**
	 * 保存文件
	 * @param param
	 */
	public void saveFileShare(Map<String, Object> param,String tenantId);

	/**
	 * 获取id
	 * @param string
	 * @return
	 */
	public int getSeq(String string);

	/**
	 * 获取协议文档列表
	 * @param resourceAliss
	 * @param docVersion
	 * @return
	 */
	public List<Map<String, Object>> getContractDocList(String resourceAliss, String docVersion,String tenantId);

	/**
	 * 保存协议文档关联
	 * @param param
	 */
	public void saveContractDocRela(Map<String, Object> param,String tenantId);

	/**
	 * 保存协议版本文档关联
	 * @param param
	 */
	public void saveContractVersionRelaJson(Map<String, Object> param,String tenantId);

	/**
	 * 
	 * saveApi:(保存api). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author wuwz 
	 * @param api 
	 * @since JDK 1.6
	 */
	public void saveApi(Api api,String tenantId);
	/**
	 * 获取contract列表
	 * @param resourceAliss
	 * @param docVersion
	 * @return
	 */
	public List<Map<String, Object>> getContractByCode(String contractCode,String tenantId);


}
