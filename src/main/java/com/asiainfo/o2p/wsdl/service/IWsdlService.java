package com.asiainfo.o2p.wsdl.service;

import java.util.List;
import java.util.Map;









import com.ailk.eaap.op2.common.EAAPException;

public interface IWsdlService {

	/**
	 * 保存wsdl数据
	 * 
	 * @param dataFlow
	 * @throws EAAPException
	 */
	public String save(String dataFlow,String tenantId );

	/**
	 * 资源别名是否已存在
	 * 
	 * @param resourceAliss
	 */
	public boolean hasResourceAliss(String resourceAliss,String tenantId);

	/**
	 * ‘资源别名+版本号’是否已存在
	 * 
	 * @param resourceAliss
	 */
	public boolean hasResourceAlissVersion(String resourceAliss,
			String docVersion,String tenantId);

	/**
	 * hasContractByCode:(contract对象的code是否存在). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @author zhongming
	 * @param contractCode
	 * @return
	 * @since JDK 1.6
	 */
	public boolean hasContractByCode(String contractCode,String tenantId);

	/**
	 * 解析WSDL
	 * 
	 * @param wsdlFileUrl
	 * @param resourceAliss
	 * @param version
	 * @return
	 */
	public String parseWsdl(String wsdlFileUrl, String resourceAliss,
			String version,String tenantId);

	/**
	 * 解压WSDL文件
	 * 
	 * @param id
	 * @param wsdlType
	 * @param resourceAliss
	 * @param version
	 * @return
	 */
	public String judgeWsdl(String id, String wsdlType, String resourceAliss,
			String version,String tenantId);

	/**
	 * 根据'资源别名'获取版本列表
	 * 
	 * @param resourceAliss
	 * @return
	 */
	public List<Map<String, Object>> getContractDocVersionList(
			String resourceAliss,String tenantId);

}
