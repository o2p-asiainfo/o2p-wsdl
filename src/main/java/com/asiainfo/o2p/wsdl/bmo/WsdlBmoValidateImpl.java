package com.asiainfo.o2p.wsdl.bmo;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.EOPDomain;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.o2p.wsdl.dao.IWsdlDao;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;

public class WsdlBmoValidateImpl implements IWsdlBmoValidate {
	private IWsdlDao wsdlDao;

	public void setWsdlDao(IWsdlDao wsdlDao) {
		this.wsdlDao = wsdlDao;
	}

	public IWsdlDao getWsdlDao() {
		return wsdlDao;
	}

	@Override
	public String checkFileShare(Map<String, Object> fsParam,String tenantId)
			throws BusinessException {

		Object sFileId = fsParam.get("sFileId");
		if (sFileId == null) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.sFileId", null, null);
		}

		Object sFileContent = fsParam.get("sFileContent");
		if (sFileContent == null) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.fileContent", null, null);
		}
		return null;
	}

	@Override
	public String checkContract(Contract contract, Integer contractDocId,String tenantId)
			throws BusinessException {

		if (!EOPDomain.NORMAL.equals(contract.getState())) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.contractState", null, null);
		}
		return null;
	}

	@Override
	public String checkService(Service service,String tenantId) throws BusinessException {

		if (StringUtils.isEmpty(service.getServiceEnName())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.serviceEnName", null, null);
		}

		if (StringUtils.isEmpty(service.getServiceVersion())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.serviceVersion", null, null);
		}

		if (!EOPDomain.NORMAL.equals(service.getState())) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.serviceState", null, null);
		}
		return null;

	}

	@Override
	public String checkContractDoc(ContractDoc contractDoc,String tenantId)
			throws BusinessException {

		if (StringUtils.isEmpty(contractDoc.getDocPath())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.docPath", null, null);
		}

		if (StringUtils.isEmpty(contractDoc.getResourceAliss())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.resourceAliss", null, null);
		}

		if (StringUtils.isEmpty(contractDoc.getDocType())
				|| !"1".equals(contractDoc.getDocType())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.docType", null, null);
		}

		if (!EOPDomain.NORMAL.equals(contractDoc.getState())) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.contractDocState", null, null);
		}
		return null;
	}

	@Override
	public void checkContractVersion(ContractVersion contractVersion,String tenantId)
			throws BusinessException {

		if (!EOPDomain.NORMAL.equals(contractVersion.getState())) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.contractVersion", null, null);
		}

	}

	@Override
	public void checkContractFormat(Map<String, Object> param,String tenantId)
			throws BusinessException {

		String state = param.get("state") == null ? "" : param.get("state")
				.toString();

		if (!EOPDomain.NORMAL.equals(state)) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.contractFormatState", null, null);
		}

	}

	@Override
	public String checkNodeDesc(NodeDesc nodeDesc,String tenantId) throws BusinessException {

		if (!EOPDomain.NORMAL.equals(nodeDesc.getState())) {
			throw new BusinessException(WsdlExceptionCode.STATE_UNUSEABLE,
					"o2p.wsdl.nodeDescState", null, null);
		}

		if (StringUtils.isEmpty(nodeDesc.getNodeCode())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.nodeCode", null, null);
		}

		if (StringUtils.isEmpty(nodeDesc.getNodeType())) {
			throw new BusinessException(WsdlExceptionCode.NULL_VALUE,
					"o2p.wsdl.nodeType", null, null);
		}
		return null;
	}

	@Override
	public boolean hasResourceAlissVersion(String resourceAliss,
			String docVersion,String tenantId) throws BusinessException {

		List<Map<String, Object>> list = wsdlDao.getContractDocList(
				resourceAliss, docVersion,tenantId);

		return (list.size() > 0);

	}

	@Override
	public List<Map<String, Object>> getContractDocVersionList(
			String resourceAliss,String tenantId) throws BusinessException {
		List<Map<String, Object>> list = wsdlDao.getContractDocList(
				resourceAliss, null,tenantId);
		return list;
	}

	public boolean hasContractByCode(String contractCode,String tenantId)
			throws BusinessException {
		List<Map<String, Object>> list = wsdlDao
				.getContractByCode(contractCode,tenantId);

		return (list.size() > 0);
	}
}
