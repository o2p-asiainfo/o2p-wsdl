package com.asiainfo.o2p.wsdl.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.LogModel;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;
import com.asiainfo.o2p.wsdl.bmo.IWsdlBmo;
import com.asiainfo.o2p.wsdl.bmo.IWsdlBmoValidate;
import com.asiainfo.o2p.wsdl.bmo.IWsdlParse;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;

public class WsdlServiceImpl implements IWsdlService {
	private IWsdlBmo wsdlBmo;
	private IWsdlBmoValidate wsdlValidate;
	private IWsdlParse wsdlParseImpl;
	private static final Logger log = Logger.getLog(WsdlServiceImpl.class);
	private static final String respCode = "RespCode";
	private static final String respDesc = "RespDesc";

	public void setWsdlBmo(IWsdlBmo wsdlBmo) {
		this.wsdlBmo = wsdlBmo;
	}

	public IWsdlBmo getWsdlBmo() {
		return wsdlBmo;
	}

	public IWsdlBmoValidate getWsdlValidate() {
		return wsdlValidate;
	}

	public void setWsdlValidate(IWsdlBmoValidate wsdlValidate) {
		this.wsdlValidate = wsdlValidate;
	}

	public void setWsdlParseImpl(IWsdlParse wsdlParseImpl) {
		this.wsdlParseImpl = wsdlParseImpl;
	}

	public IWsdlParse getWsdlParseImpl() {
		return wsdlParseImpl;
	}

	public String save(String dataFolw,String tenantId) {

		JSONObject resultJson = new JSONObject();

		try {

			wsdlBmo.save(dataFolw,tenantId);
			resultJson.put(respCode, "success");
			resultJson.put(respDesc, "Save success!");

		} catch (BusinessException e) {
			String strError = ExceptionUtils.populateExecption(e, 500);
			log.error("save Exception {0}", strError);
			resultJson.put(respCode, e.getResult().getCode());
			resultJson.put(respDesc, strError);
		}

		return resultJson.toString();
	}

	public boolean hasResourceAliss(String resourceAliss,String tenantId) {

		try {
			return wsdlValidate.hasResourceAlissVersion(resourceAliss, null,tenantId);
		} catch (BusinessException e) {
			log.error("hasResourceAliss Exception {0}",
					ExceptionUtils.populateExecption(e, 500));
		}
		return false;
	}

	public boolean hasResourceAlissVersion(String resourceAliss,
			String docVersion,String tenantId) {

		try {
			return wsdlValidate.hasResourceAlissVersion(resourceAliss,
					docVersion,tenantId);
		} catch (BusinessException e) {
			log.error("hasResourceAlissVersion Exception {0}",
					ExceptionUtils.populateExecption(e, 500));
		}
		return false;
	}

	public boolean hasContractByCode(String contractCode,String tenantId) {

		try {
			return wsdlValidate.hasContractByCode(contractCode,tenantId);
		} catch (BusinessException e) {
			log.error("hasContractBycode Exception {0}",
					ExceptionUtils.populateExecption(e, 500));
		}

		return false;
	}

	@Override
	public String parseWsdl(String wsdlFile, String resourceAliss,
			String version,String tenantId) {
		JSONObject resultJson = new JSONObject();
		try {
			String json = wsdlParseImpl.parseWsdl(wsdlFile, resourceAliss,
					version,tenantId);
			JSONObject jsonObject = JSONObject.fromObject(json);
			jsonObject.put("relaPath", wsdlFile);
			return jsonObject.toString();
		} catch (BusinessException e) {
			selectException(resultJson, e);
		}
		return resultJson.toString();
	}

	@Override
	public String judgeWsdl(String id, String wsdlType, String resourceAliss,
			String version,String tenantId) {
		JSONObject resultJson = new JSONObject();
		try {

			String json = wsdlParseImpl.judgeWsdl(id, wsdlType, resourceAliss,
					version,tenantId);
			return json;
		} catch (BusinessException e) {
			selectException(resultJson, e);
		}
		return resultJson.toString();
	}

	private void selectException(JSONObject resultJson, Exception e) {
		String strError = ExceptionUtils.populateExecption(e, 500);

		log.error(LogModel.EVENT_BIZ_EXCPT, strError);
		resultJson.put(respCode, WsdlExceptionCode.RUNTIME_ERROR);
		resultJson.put(respDesc, strError);
	}

	@Override
	public List<Map<String, Object>> getContractDocVersionList(
			String resourceAliss,String tenantId) {
		List<Map<String, Object>> list = null;
		try {
			list = wsdlValidate.getContractDocVersionList(resourceAliss,tenantId);
		} catch (BusinessException e) {

			log.error("getContractDocVersionList Exception {0}",
					ExceptionUtils.populateExecption(e, 500));
		}
		return list;
	}

}
