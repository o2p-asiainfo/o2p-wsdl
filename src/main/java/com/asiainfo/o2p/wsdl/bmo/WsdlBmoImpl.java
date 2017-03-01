package com.asiainfo.o2p.wsdl.bmo;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ailk.eaap.op2.bo.Api;
import com.ailk.eaap.op2.bo.Component;
import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.DocContract;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Org;
import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.o2p.wsdl.dao.IWsdlDao;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;
import com.asiainfo.o2p.wsdl.util.FileUtils;

public class WsdlBmoImpl implements IWsdlBmo {
	private IWsdlDao wsdlDao;
	private static final Logger LOG = Logger.getLog(WsdlBmoImpl.class);
	private IWsdlBmoValidate wsdlValidate;

	private static final String relaTypeCd = "relaTypeCd";
	private static final String createTime = "createTime";
	private static final String stateString = "state";
	private static final String utf = "utf-8";

	public void setWsdlDao(IWsdlDao wsdlDao) {
		this.wsdlDao = wsdlDao;
	}

	public IWsdlDao getWsdlDao() {
		return wsdlDao;
	}

	public void setWsdlValidate(IWsdlBmoValidate wsdlValidate) {
		this.wsdlValidate = wsdlValidate;
	}

	public IWsdlBmoValidate getWsdlValidate() {
		return wsdlValidate;
	}

	@Override
	public void save(String jsonObject,String tenantId) throws BusinessException {

		
		LOG.debug("jsonObject:"+jsonObject);
		
		JSONObject json = JSONObject.fromObject(jsonObject);
		JSONObject dataFlowJson = json.getJSONObject("dataFlow");
		if (dataFlowJson == null) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " dataFlow is null",
					"JSONObject['dataFlow'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.dataFlow", null, null);
		}

		// 新建协议基类
		// 保存协议头
		JSONObject baseContractJson = dataFlowJson
				.getJSONObject("baseContract");
		if (!baseContractJson.isNullObject()) {
			saveProto(baseContractJson, null,tenantId);
		}

		// 保存机构
		JSONObject orgJson = dataFlowJson.getJSONObject("org");
		if (!orgJson.isNullObject()) {
			saveOrg(orgJson,tenantId);
		}

		// 保存文件内容
		JSONObject wsdlFileShareJson = dataFlowJson
				.getJSONObject("wsdlFileShare");
		if (wsdlFileShareJson.isNullObject()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " wsdlFileShare is null",
					"JSONObject['wsdlFileShare'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.wsdlFileShare", null, null);
		}
		JSONObject contractDocJson = wsdlFileShareJson
				.getJSONObject("contractDoc");
		if (contractDocJson.isNullObject()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " contractDoc is null",
					"JSONObject['contractDoc'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.contractDoc", null, null);
		}
		saveContractDoc(contractDocJson,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save dataFlow success!");
		}

		// 删除目录
		String relaPath = json.getString("relaPath");
		if (relaPath != null) {
			deleteDir(relaPath);
		}

	}

	private void deleteDir(String relaPath) {
		FileUtils.deleteDir(relaPath.substring(0, relaPath.lastIndexOf('.')));
	}

	@Override
	public void saveOrg(JSONObject orgJson,String tenantId) throws BusinessException {

		JSONObject component = orgJson.getJSONObject("component");
		if (component.isNullObject()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " component is null",
					"JSONObject['component'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.component", null, null);
		}
		orgJson.remove("component");

		Org org = (Org) JSONObject.toBean(orgJson, Org.class);

		wsdlDao.saveOrg(org,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save org success! orgId={0}", org.getOrgId());
		}

		saveComponent(component, org.getOrgId(),tenantId);

	}

	@Override
	public void saveComponent(JSONObject componentJson, int orgId,String tenantId)
			throws BusinessException {

		Component component = (Component) JSONObject.toBean(componentJson,
				Component.class);
		component.setOrgId(orgId);

		wsdlDao.saveComponent(component,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save component success! componentId={0}",
					component.getComponentId());
		}

	}

	@Override
	public void saveProto(JSONObject contractJson, Integer contractDocId,String tenantId)
			throws BusinessException {

		JSONObject contractVersionJson = contractJson
				.getJSONObject("contractVersion");
		if (contractVersionJson.isNullObject()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " contractVersion is null",
					"JSONObject['contractVersion'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.contractVersion", null, null);
		}
		contractJson.remove("contractVersion");

		Contract contract = (Contract) JSONObject.toBean(contractJson,
				Contract.class);
		contract.setLastestTime(new Date());

		if (LOG.isDebugEnabled()) {
			LOG.debug("save contractId: {0}", contract.getContractId());
			LOG.debug("save code: {0}", contract.getCode());
		}

		wsdlValidate.checkContract(contract, contractDocId,tenantId);

		wsdlDao.saveProto(contract,tenantId);
		if (contractVersionJson.get("contractId") == null) {
			contractVersionJson.put("contractId", contract.getContractId());
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("save contract success! contractId={0}",
					contract.getContractId());
		}

		saveContractVersion(contractVersionJson, contractDocId,tenantId);

	}

	@Override
	public void saveService(JSONObject serviceJson, int contractVersionId,String tenantId)
			throws BusinessException {

		Service service = (Service) JSONObject.toBean(serviceJson,
				Service.class);
		service.setCreateDate(new Date());
		service.setLastestDate(new Date());
		service.setServiceEnName(service.getServiceEnName());
		wsdlValidate.checkService(service,tenantId);

		wsdlDao.saveService(service,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save service success! serviceId={0}",
					service.getServiceId());
		}

		saveApi(service,tenantId);

	}

	@Override
	public void saveFileShare(JSONObject contractDocJson, String resourceAliss,
			String xsdResourceAliss, int sFileId,String tenantId) throws BusinessException {

		try {

			Map<String, Object> fsParam = new HashMap<String, Object>();

			String address = contractDocJson.getString("address");
			if (org.apache.commons.lang.StringUtils.isEmpty(address)) {
				LOG.error(WsdlExceptionCode.o2pWsdl + " address is null",
						"JSONObject['address'] not found.");
				throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
						"o2p.wsdl.address", null, null);
			}

			byte[] wsdlByte = WsdlFileHelper.getFileByUrl(address,
					resourceAliss);
			fsParam.put("sFileId", sFileId);
			fsParam.put("sFileName", resourceAliss + "." + xsdResourceAliss);
			fsParam.put("sFileContent", wsdlByte);
			fsParam.put(stateString, "A");
			fsParam.put(createTime, new Date());

			wsdlValidate.checkFileShare(fsParam,tenantId);

			wsdlDao.saveFileShare(fsParam,tenantId);

			if (LOG.isDebugEnabled()) {
				LOG.debug("save fileShare success! id={0}", sFileId);
			}

		} catch (BusinessException e) {
			if (e instanceof BusinessException) {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " save file error BusinessException",
						((BusinessException) e).getResult().getMsg());
				throw (BusinessException) e;
			} else {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " save file error Exception", e.getMessage());
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
			}

		}

	}

	@Override
	public void saveContractDoc(JSONObject cd,String tenantId) throws BusinessException {

		JSONArray contractsJson = cd.getJSONArray("contracts");
		if (contractsJson.isEmpty()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " contracts is null",
					"JSONObject['contracts'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.contracts", null, null);
		}
		cd.remove("contracts");

		JSONObject contractDocRelaJson = cd.getJSONObject("contractDocRela");
		cd.remove("contractDocRela");

		JSONArray xsdContractsDocJson = cd.getJSONArray("xsdContractDocs");
		cd.remove("xsdContractDocs");

		// 保存wsdl文件
		int sFileId = wsdlDao.getSeq("SEQ_FILE_SHARE");
		String resourceAliss = cd.getString("resourceAliss");
		String docVersion = cd.getString("docVersion");
		saveFileShare(cd, resourceAliss + "." + docVersion, "", sFileId,tenantId);

		// 保存wsdl协议文档
		ContractDoc contractDoc = (ContractDoc) JSONObject.toBean(cd,
				ContractDoc.class);
		contractDoc.setDocPath(sFileId + "");

		wsdlValidate.checkContractDoc(contractDoc,tenantId);

		wsdlDao.saveContractDoc(contractDoc,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save contractDoc success! contractDocId={0}",
					contractDoc.getContractDocId());
		}

		// 保存协议
		for (int i = 0; i < contractsJson.size(); i++) {
			saveProto(contractsJson.getJSONObject(i).getJSONObject("contract"),
					contractDoc.getContractDocId(),tenantId);
		}

		// 保存xsd文件
		if (!xsdContractsDocJson.isEmpty()) {
			for (int i = 0; i < xsdContractsDocJson.size(); i++) {
				int sXsdFileId = getSeq("SEQ_FILE_SHARE");
				JSONObject xsdContractDocJson = xsdContractsDocJson
						.getJSONObject(i).getJSONObject("xsdContractDoc");
				String xsdResourceAliss = xsdContractDocJson
						.getString("resourceAliss");
				saveFileShare(xsdContractDocJson, resourceAliss + "."
						+ docVersion, xsdResourceAliss, sXsdFileId,tenantId);

				// 保存xsd协议文档
				ContractDoc xsdContractDoc = (ContractDoc) JSONObject.toBean(
						xsdContractDocJson, ContractDoc.class);
				xsdContractDoc.setBaseConDocId(contractDoc.getContractDocId());
				xsdContractDoc.setDocPath(sXsdFileId + "");
				xsdContractDoc.setResourceAliss(resourceAliss + "."
						+ docVersion + "." + xsdResourceAliss);
				wsdlValidate.checkContractDoc(xsdContractDoc,tenantId);

				wsdlDao.saveContractDoc(xsdContractDoc,tenantId);
			}
		}

		// 保存协议文档关联
		saveContractDocRela(contractDocRelaJson, contractDoc,tenantId);

	}

	@Override
	public void saveContractDocRela(JSONObject contractDocRelaJson,
			ContractDoc contractDoc,String tenantId) throws BusinessException {

		List<Map<String, Object>> list = wsdlDao.getContractDocList(
				contractDoc.getResourceAliss(), null, tenantId);
		// 查询结果包含新添加的记录
		if (list.size() > 1) {
			List<Map<String, Object>> list1 = wsdlDao
					.getContractDocList(contractDoc.getResourceAliss(),
							contractDoc.getDocVersion(), tenantId);
			// 资源别名和版本号已存在
			if (list1.size() > 1) {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " ResourceAliss and version already exists",
						"ResourceAliss and version  already exists.");
				throw new BusinessException(WsdlExceptionCode.VALUE_EXIST,
						"o2p.wsdl.ResourceAlissAndVersionVal", null, null);
			} else {
				Map<String, Object> param = new HashMap<String, Object>();

				// 取最大的一条记录进行关联
				Map<String, Object> contractDocMap = list.get(0);
				int contractDocRelaId = getSeq("SEQ_CONTRACT_DOC_RELA");
				String relaContractDocId = "";
				String relaType_Cd = "";
				String state = "";

				// 如果前台传递数据为空，按默认数据
				if (!contractDocRelaJson.isNullObject()) {
					relaContractDocId = contractDocRelaJson
							.getString("contractDocRelaId");
					relaType_Cd = contractDocRelaJson.getString(relaTypeCd);
					state = contractDocRelaJson.getString(stateString);
				}

				if (org.apache.commons.lang.StringUtils
						.isEmpty(relaContractDocId)) {
					relaContractDocId = contractDocMap.get("CONTRACT_DOC_ID")
							.toString();
				}
				if (org.apache.commons.lang.StringUtils.isEmpty(relaType_Cd)) {
					relaType_Cd = "1";
				}
				if (org.apache.commons.lang.StringUtils.isEmpty(state)) {
					state = "A";
				}

				param.put("contractDocRelaId", contractDocRelaId);
				param.put("contractDocId", contractDoc.getContractDocId());
				param.put("relaContractDocId", relaContractDocId);
				param.put(relaTypeCd, relaType_Cd);
				param.put(stateString, state);
				param.put(createTime, new Date());

				wsdlDao.saveContractDocRela(param,tenantId);
			}
		}

	}

	@Override
	public void saveContractVersion(JSONObject cv, Integer contractDocId,String tenantId)
			throws BusinessException {

		JSONArray contractFormatsJson = cv.getJSONArray("contractFormats");
		if (contractFormatsJson.isEmpty()) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " contractFormats is null",
					"JSONObject['contractFormats'] not found.");
			throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
					"o2p.wsdl.contractFormats", null, null);
		}
		cv.remove("contractFormats");

		JSONObject serviceJson = cv.getJSONObject("service");
		cv.remove("service");

		JSONObject contractVersionRelaJson = cv
				.getJSONObject("contractVersionRela");
		cv.remove("contractVersionRela");

		ContractVersion contractVersion = (ContractVersion) JSONObject.toBean(
				cv, ContractVersion.class);
		contractVersion.setLastestTime(new Date());
		wsdlValidate.checkContractVersion(contractVersion,tenantId);

		wsdlDao.saveContractVersion(contractVersion,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save contractVersion success! contractVersion={0}",
					contractVersion.getContractVersionId());
		}

		saveContractFormat(contractFormatsJson,tenantId);

		if (!serviceJson.isNullObject()) {

			saveService(serviceJson, contractVersion.getContractVersionId(),tenantId);
		}

		if (!contractVersionRelaJson.isNullObject()
				&& contractVersionRelaJson.optBoolean("contractVersionRelaId")) {

			saveContractVersionRelaJson(contractVersionRelaJson,
					contractVersion.getContractVersionId(),tenantId);
		}

		if (contractDocId != null) {

			saveDocContract(contractVersion.getContractVersionId(),
					contractDocId,tenantId);
		}

	}

	public void saveContractVersionRelaJson(JSONObject contractVersionRelaJson,
			Integer contractVersionId,String tenantId) throws BusinessException {

		Map<String, Object> param = new HashMap<String, Object>();
		int contractVersionRelaId = getSeq("SEQ_CONTRACT_VERSION_ID");
		param.put("contractVersionRelaId", contractVersionRelaId);
		param.put("contractVersionId", contractVersionId);
		param.put("relaContractVersionId",
				contractVersionRelaJson.getString("relaContractVersionId"));
		param.put(relaTypeCd, contractVersionRelaJson.getString(relaTypeCd));
		param.put(stateString, contractVersionRelaJson.getString(stateString));
		param.put(createTime, new Date());

		wsdlDao.saveContractVersionRelaJson(param,tenantId);
	}

	/**
	 * 保存文档对应协议
	 * 
	 * @param contractVersionId
	 * @param contractDocId
	 */
	private void saveDocContract(Integer contractVersionId, int contractDocId,String tenantId)
			throws BusinessException {

		DocContract dc = new DocContract();
		int dcId = getSeq("SEQ_DOC_CONTRACT");
		dc.setDocContrId(dcId);
		dc.setContractVersionId(contractVersionId);
		dc.setContractDocId(contractDocId);

		wsdlDao.saveDocContract(dc,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save docContract success! DocContractId={0}", dcId);
		}

	}

	@Override
	public void saveContractFormat(JSONArray contractFormatsJson,String tenantId)
			throws BusinessException {
		
		
		
		for (int i = 0; i < contractFormatsJson.size(); i++) {

			JSONObject contractFormatJson = contractFormatsJson
					.getJSONObject(i);

			JSONArray nodeDescsJson = contractFormatJson
					.getJSONArray("nodeDescs");
			if (nodeDescsJson.isEmpty()) {
				/*LOG.error(WsdlExceptionCode.o2pWsdl + " nodeDescs is null",
						"JSONObject['nodeDescs'] not found.");
				throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
						"o2p.wsdl.contractFormat.utf", null, null);*/
				continue;
			}
			contractFormatJson.remove("nodeDescs");

			Map<String, Object> param = new HashMap<String, Object>();
			byte[] xsdHeadFor = null;
			byte[] xsdFormat = null;
			byte[] xsdDemo = null;
			try {
				xsdHeadFor = contractFormatJson.getString("xsdHeaderFor")
						.getBytes(utf);
				xsdFormat = contractFormatJson.getString("xsdFormat").getBytes(
						utf);
				xsdDemo = contractFormatJson.getString("xsdDemo").getBytes(utf);
			} catch (UnsupportedEncodingException e1) {
				LOG.error(WsdlExceptionCode.o2pWsdl + "utf error", "utf error.");
				throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
						"o2p.wsdl.nodeDesc", null, null);
			}

			param.put("tcpCtrFId",
					contractFormatJson.getString("contractFormatId"));
			param.put("contractVersionId",
					contractFormatJson.getString("contractVersionId"));
			param.put("reqRsp", contractFormatJson.getString("reqRsp"));
			param.put("conType", contractFormatJson.getString("conType"));
			param.put("xsdHeaderFor", xsdHeadFor);
			param.put("xsdFormat", xsdFormat);
			param.put("xsdDemo", xsdDemo);
			param.put(createTime, new Date());
			param.put(stateString, contractFormatJson.getString(stateString));
			param.put("lastestTime", new Date());
			param.put("descriptor", contractFormatJson.getString("descriptor"));

			wsdlValidate.checkContractFormat(param,tenantId);

			wsdlDao.saveContractFormat(param,tenantId);

			if (LOG.isDebugEnabled()) {
				LOG.debug("save contractFormat success! contractFormatId={0}",
						contractFormatJson.getString("contractFormatId"));
			}

			
			saveNodeDesc(nodeDescsJson, Integer.parseInt(contractFormatJson
					.getString("contractFormatId")),tenantId);
		}

	}

	@Override
	public void saveNodeDesc(JSONArray nodeDescs, int contractFormatId,String tenantId)
			throws BusinessException {
		
		
		for (int i = 0; i < nodeDescs.size(); i++) {

			JSONObject nodeDescJson = nodeDescs.getJSONObject(i);
			if (nodeDescJson.isNullObject()) {
				/*LOG.error(WsdlExceptionCode.o2pWsdl + " nodeDesc is null",
						"JSONObject['nodeDesc'] not found.");
				throw new BusinessException(WsdlExceptionCode.NULL_OBJECT,
						"o2p.wsdl.nodeDesc", null, null);*/
				continue;
			}
			NodeDesc nodeDesc = (NodeDesc) JSONObject.toBean(nodeDescJson,
					NodeDesc.class);
			String parentNodeId = nodeDescJson.getString("parentNodeId");
			if (!org.apache.commons.lang.StringUtils.isEmpty(parentNodeId)) {
				nodeDesc.setParentNodeId(Integer.parseInt(parentNodeId));
			}

			nodeDesc.setTcpCtrFId(contractFormatId);
			try{
				wsdlValidate.checkNodeDesc(nodeDesc,tenantId);
			}catch(BusinessException e){
				continue;
			}
			

			wsdlDao.saveNodeDesc(nodeDesc,tenantId);

			if (LOG.isDebugEnabled()) {
				LOG.debug("save nodeDesc success! nodeDescId={0}",
						nodeDesc.getNodeDescId());
			}
		}

	}

	public int getSeq(String string) throws BusinessException {

		try {
			return wsdlDao.getSeq(string);
		} catch (BusinessException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " Obtain seq anomalies",
					"seq: " + e.getMessage());
			LOG.error("seq:" + string + e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}

	}

	@Override
	public void saveApi(Service service,String tenantId) throws BusinessException {

		Api api = new Api();
		api.setApiId(getSeq("SEQ_API"));
		api.setApiMethod(service.getServiceCode());
		api.setApiVersion(service.getServiceVersion());
		api.setApiName(service.getServiceEnName());
		api.setApiRegTime(new Date());
		api.setApiState("D");
		api.setServiceId(service.getServiceId());
		wsdlDao.saveApi(api,tenantId);

		if (LOG.isDebugEnabled()) {
			LOG.debug("save api success! apiId=" + api.getApiId());
		}

	}
}
