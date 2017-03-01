package com.asiainfo.o2p.wsdl.bmo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;

import net.minidev.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.foundation.util.ExceptionUtils;
import com.asiainfo.o2p.wsdl.dao.IWsdlParseDao;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;
import com.asiainfo.o2p.wsdl.util.CommonUtil;
import com.asiainfo.o2p.wsdl.util.FileUtils;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.WsdlInterfaceFactory;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlOperation;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.impl.wsdl.support.BindingImporter;
import com.eviware.soapui.impl.wsdl.support.UrlSchemaLoader;
import com.eviware.soapui.impl.wsdl.support.soap.Soap11HttpBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.Soap12HttpBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.SoapJMSBindingImporter;
import com.eviware.soapui.impl.wsdl.support.soap.SoapMessageBuilder;
import com.eviware.soapui.impl.wsdl.support.soap.TibcoSoapJMSBindingImporter;
import com.eviware.soapui.impl.wsdl.support.wsdl.WsdlContext;
import com.eviware.soapui.impl.wsdl.support.xsd.SchemaException;
import com.eviware.soapui.impl.wsdl.support.xsd.SchemaUtils;
import com.eviware.soapui.model.iface.Operation;
import com.eviware.soapui.model.propertyexpansion.DefaultPropertyExpansionContext;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;
import com.eviware.soapui.model.propertyexpansion.PropertyExpansionContext;
import com.eviware.soapui.support.SoapUIException;

public class WsdlParseImpl implements IWsdlParse {
	private static final Logger LOG = Logger.getLog(WsdlParseImpl.class);
	private IWsdlParseDao parseDao;

	public void setParseDao(IWsdlParseDao parseDao) {
		this.parseDao = parseDao;
	}

	public IWsdlParseDao getParseDao() {
		return parseDao;
	}

	private static Map<String, String> mapBody = new HashMap<String, String>();// service
	private static Map<String, Integer> mapSeq = new HashMap<String, Integer>();// 相关主键ID
	private static Map<String, String> namespaceMap = new HashMap<String, String>();// 子节点命名空间
	private String dateFormat = "";

	private static final String nevlConsType = "nevlConsType";
	private static final String state = "state";// 状态标识
	private static final String contractId = "contractId";// 协议ID
	private static final String javaField = "javaField";// 文件路径
	private static final String description = "description";// 说明
	private static final String nevlConsDesc = "nevlConsDesc";
	private static final String nodeDescId = "nodeDescId";// 节点描述ID
	private static final String nodeName = "nodeName";// 节点名称
	private static final String nodeLengthCons = "nodeLengthCons";
	private static final String seqNodeDesc = "SEQ_NODE_DESC";// seq节点描述ID
	private static final String childNodeDescId = "childNodeDescId";// 子节点描述ID
	private static final String nodeNumberCons = "nodeNumberCons";
	private static final String nodeCode = "nodeCode";// 节点Code
	private static final String nodeDescIdRoot = "nodeDescIdRoot";
	private static final String createTime = "createTime";// 建立时间
	private static final String tcpCtrFId = "tcpCtrFId";// 协议格式ID
	private static final String descriptor = "descriptor";
	private static final String contractVersionId = "contractVersionId";
	private static final String parentNodeDescId = "parentNodeId";
	private static final String nodePath = "nodePath";
	private static final String nodeType = "nodeType";
	private static final String nodeTypeCons = "nodeTypeCons";
	private static final String nevlConsValue = "nevlConsValue";
	private static final String isNeedSign = "isNeedSign";
	private static final String sFileContent = "SFILECONTENT";

	@Autowired
	private static WsdlProject wsdlProject;
	private static List<BindingImporter> bindingImporters = new ArrayList<BindingImporter>();

	static {
		try {
			bindingImporters.add(new Soap11HttpBindingImporter());
			bindingImporters.add(new Soap12HttpBindingImporter());
			bindingImporters.add(new SoapJMSBindingImporter());
			bindingImporters.add(new TibcoSoapJMSBindingImporter());
		} catch (BusinessException e) {
			SoapUI.logError(e);
		}
	}

	public WsdlParseImpl() throws XmlException, IOException, SoapUIException {
		if (null == wsdlProject) {
			wsdlProject = new WsdlProject();
		}

	}

	/**
	 * 重新创建wsdlProject对象 TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.asiainfo.o2p.wsdl.bmo.IWsdlParse#updateWsdlProject()
	 */
	public void updateWsdlProject(String tenantId) throws XmlException, IOException,
			SoapUIException {

	}

	/**
	 * 解析WSDL
	 * 
	 * @param wsdlFileUrl
	 *            WSDL路径
	 * @param resourceAliss
	 *            资源别名
	 * @throws Exception
	 */
	public String parseWsdl(String wsdlFileUrl, String resourceAliss,
			String version, String tenantId) throws BusinessException {
		long start = System.currentTimeMillis();

		if (StringUtils.isEmpty(wsdlFileUrl)
				|| StringUtils.isEmpty(resourceAliss)) {
			return null;
		}

		PropertyExpansionContext pContext = new DefaultPropertyExpansionContext(
				wsdlProject.getModelItem());
		String newWsdlFileUrl = wsdlFileUrl;
		newWsdlFileUrl = PropertyExpander.expandProperties(pContext,
				newWsdlFileUrl);

		if (wsdlFileUrl.startsWith("file:")) {
			newWsdlFileUrl = newWsdlFileUrl.replace('/', File.separatorChar);
			newWsdlFileUrl = newWsdlFileUrl.replace('\\', File.separatorChar);
		}
		WsdlContext wsdlContext = new WsdlContext(newWsdlFileUrl);

		try {
			wsdlContext.getDefinition();
		} catch (Exception e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " parseWsdl wsdlContext getDefinition Exception",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat = sdf.format(date);
		// 生成响应信息
		JSONObject json = new JSONObject();
		JSONObject jsonData = new JSONObject();

		JSONObject jsonFile = new JSONObject();

		JSONObject jsonDoc = new JSONObject();
		getContractDoc(resourceAliss, version, newWsdlFileUrl, jsonDoc);

		// 获取operation
		JSONArray jsonContracts = new JSONArray();

		WsdlInterface[] facesInfo = null;
		try {
			facesInfo = WsdlInterfaceFactory.importWsdl(wsdlProject,
					newWsdlFileUrl, false);
		} catch (SoapUIException e) {
			LOG.error(
					WsdlExceptionCode.o2pWsdl
							+ " parseWsdl WsdlInterfaceFactory importWsdl SoapUIException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		Map<String, String> map = new HashMap<String, String>();
		for (WsdlInterface wface : facesInfo) {
			parseFacesInfo(resourceAliss, version, jsonContracts, map, wface);
		}
		map.clear();
		jsonDoc.put("contracts", jsonContracts);

		JSONArray jsonXsds = null;
		try {
			jsonXsds = getWsdlXsd(newWsdlFileUrl, version);
		} catch (SchemaException e) {
			LOG.error("parseWsdl SchemaException {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		jsonDoc.put("xsdContractDocs", jsonXsds);

		jsonFile.put("contractDoc", jsonDoc);
		jsonData.put("wsdlFileShare", jsonFile);
		json.put("dataFlow", jsonData);

		long end = System.currentTimeMillis();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wsdl parse times : {0}", end - start);
			
			LOG.debug("Wsdl parse json : {0}", json.toString());
		}
		return json.toString();

	}

	protected void parseFacesInfo(String resourceAliss, String version,
			JSONArray jsonContracts, Map<String, String> map,
			WsdlInterface wface) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("WsdlInterface name : {0}", wface.getName());
		}

		List<Operation> list = wface.getOperationList();

		for (Operation operation : list) {
			WsdlOperation op = wface.getOperationByName(operation.getName());

			try {
				if (null == map.get(resourceAliss + "." + operation.getName())) {
					getWsdlOperation(resourceAliss, version, jsonContracts,
							operation, op);
					map.put(resourceAliss + "." + operation.getName(),
							resourceAliss + "." + operation.getName());
				}

			} catch (BusinessException e) {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " parseWsdl getWsdlOperation BusinessException",
						ExceptionUtils.populateExecption(e, 500));
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
			}
		}
	}

	private void getContractDoc(String resourceAliss, String version,
			String newWsdlFileUrl, JSONObject jsonDoc) {
		jsonDoc.put("contractDocId", parseDao.getSeq("SEQ_CONTRACT_DOC"));
		jsonDoc.put("baseConDocId", "");
		jsonDoc.put("docType", "1");
		jsonDoc.put("docName", new File(newWsdlFileUrl).getName());
		jsonDoc.put("resourceAliss", resourceAliss);
		jsonDoc.put("docVersion", version);
		jsonDoc.put("docCreateTime", dateFormat);
		jsonDoc.put(state, "A");
		jsonDoc.put("docPath", "");
		jsonDoc.put("address", newWsdlFileUrl);
	}

	private void getWsdlOperation(String resourceAliss, String version,
			JSONArray jsonContracts, Operation operation, WsdlOperation op)
			throws BusinessException {
		// 协议规格
		JSONObject jsonContract = new JSONObject();
		mapSeq.put(contractId, parseDao.getSeq("SEQ_CONTRACT"));
		getContract(resourceAliss, version, operation, jsonContract);
		// 协议版本
		JSONObject jsonVersion = new JSONObject();
		mapSeq.put(contractVersionId, parseDao.getSeq("SEQ_CONTRACT_VERSION"));
		getContractVersion(resourceAliss, operation, jsonVersion);

		JSONArray jsonFormats = new JSONArray();
		// 获取webservice 接口
		WsdlInterface face = (WsdlInterface) op.getInterface();
		// 获取到soap数据的builder
		SoapMessageBuilder builder = face.getMessageBuilder();
		Definition definitionIn;
		try {
			definitionIn = face.getWsdlContext().getDefinition();
		} catch (Exception e) {
			LOG.error(
					WsdlExceptionCode.o2pWsdl
							+ " getWsdlOperation face getWsdlContext getDefinition Exception",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		// 找到方法
		BindingOperation bo = op.findBindingOperation(definitionIn);
		// 构建soap格式数据。。。
		String soapData = "";
		try {
			soapData = builder.buildSoapMessageFromInput(bo, true, true);
		} catch (Exception e) {
			LOG.error(
					WsdlExceptionCode.o2pWsdl
							+ " getWsdlOperation builder buildSoapMessageFromInput Exception",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wsdl operation request : {0}", soapData);
		}
		// 请求协议格式
		mapSeq.put("contractFormatIdREQ",
				parseDao.getSeq("SEQ_CONTRACT_FORMAT"));
		JSONObject jsonFormat = new JSONObject();
		getReqContractFormat(jsonFormat,soapData);
		// 请求节点描述
		JSONArray jsonNodeDescs = new JSONArray();
		// 调用节点描述
		jsonFormats.add(jsonFormat);
		try {
			parseDocument(soapData, jsonNodeDescs, 1);
		} catch (DocumentException e) {
			LOG.error("getWsdlOpertion DocumentException {0}",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		jsonFormat.put("nodeDescs", jsonNodeDescs);
		// 获取了返回的输出参数的XML
		String soapDatareturn = "";
		try {
			soapDatareturn = builder.buildSoapMessageFromOutput(bo, true, true);
		} catch (Exception e) {
			LOG.error(
					WsdlExceptionCode.o2pWsdl
							+ " getWsdlOperation builder buildSoapMessageFromOutput Exception",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Wsdl operation rsponse : {0}", soapDatareturn);
		}
		// 响应协议格式
		mapSeq.put("contractFormatIdRSP",
				parseDao.getSeq("SEQ_CONTRACT_FORMAT"));
		jsonFormat = new JSONObject();
		getRspContractFormat(jsonFormat,soapDatareturn);
		// 请求节点描述
		jsonNodeDescs = new JSONArray();
		// 调用节点描述
		jsonFormats.add(jsonFormat);
		try {
			parseDocument(soapDatareturn, jsonNodeDescs, 2);
		} catch (DocumentException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " getWsdlOperation parseDocument DocumentException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}
		jsonFormat.put("nodeDescs", jsonNodeDescs);
		jsonVersion.put("contractFormats", jsonFormats);

		getWsdlService(resourceAliss, jsonVersion);
		jsonContract.put("contractVersion", jsonVersion);
		JSONObject jsonContractData = new JSONObject();
		jsonContractData.put("contract", jsonContract);
		jsonContracts.add(jsonContractData);
	}

	private void getRspContractFormat(JSONObject jsonFormat,String soapDatareturn) {
		jsonFormat.put("contractFormatId", mapSeq.get("contractFormatIdRSP"));
		jsonFormat.put(contractVersionId, mapSeq.get(contractVersionId));
		jsonFormat.put("reqRsp", "RSP");
		jsonFormat.put("conType", "1");
		jsonFormat.put("xsdHeaderFor", "");
		jsonFormat.put("xsdFormat", soapDatareturn);
		jsonFormat.put("xsdDemo", soapDatareturn);// soapDatareturn
		jsonFormat.put(state, "A");
		jsonFormat.put(descriptor, "");
	}

	private void getReqContractFormat(JSONObject jsonFormat,String data) {
		jsonFormat.put("contractFormatId", mapSeq.get("contractFormatIdREQ"));
		jsonFormat.put(contractVersionId, mapSeq.get(contractVersionId));
		jsonFormat.put("reqRsp", "REQ");
		jsonFormat.put("conType", "1");
		jsonFormat.put("xsdHeaderFor", "");
		jsonFormat.put("xsdFormat", data);
		jsonFormat.put("xsdDemo", data);// soapData
		jsonFormat.put(state, "A");
		jsonFormat.put(descriptor, "");
	}

	private void getContractVersion(String resourceAliss, Operation operation,
			JSONObject jsonVersion) {
		jsonVersion.put(contractVersionId, mapSeq.get(contractVersionId));// 协议版本ID
		jsonVersion.put(contractId, mapSeq.get(contractId));// 协议规格ID
		jsonVersion.put("version", resourceAliss + "." + operation.getName());
		jsonVersion.put(createTime, dateFormat);
		jsonVersion.put("isNeedCheck", "Y");
		jsonVersion.put(state, "A");
		jsonVersion.put("effDate", dateFormat);
		jsonVersion.put("expDate", "3000-01-01 00:00:00");
		jsonVersion.put(descriptor, "");
	}

	private void getContract(String resourceAliss, String version,
			Operation operation, JSONObject jsonContract) {
		jsonContract.put(contractId, mapSeq.get(contractId));// 主键
		jsonContract.put("baseContractId", "");// 基类协议ID
		jsonContract.put("name", resourceAliss + "." + operation.getName());
		jsonContract.put("code", resourceAliss + "." + operation.getName()
				+ "." + version);
		jsonContract.put(state, "A");
		jsonContract.put(createTime, dateFormat);
		jsonContract.put(descriptor, "");
		if (LOG.isDebugEnabled()) {
			LOG.debug("parse code: {0}" + resourceAliss + "."
					+ operation.getName());
		}
	}

	private JSONArray getWsdlXsd(String wsdlFileUrl, String version)
			throws SchemaException {
		// XSD
		File file = new File(wsdlFileUrl);
		String wsdlUrl = null;
		if (wsdlFileUrl.toLowerCase().startsWith("http")) {
			wsdlUrl = wsdlFileUrl;
		} else {
			wsdlUrl = file.toURI().toString();
		}

		Map<String, XmlObject> mapXmls = SchemaUtils.getSchemas(wsdlUrl,
				new UrlSchemaLoader(wsdlFileUrl));
		JSONArray jsonXsds = new JSONArray();
		for (String xsdFile : mapXmls.keySet()) {
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("xsdFile: {0}" + xsdFile);
				LOG.debug("wsdlUrl: {0}" + wsdlUrl);
			}
			
			if (xsdFile.split("@").length == 1 && !xsdFile.equals(wsdlUrl)) {

				JSONObject jsonXsd = new JSONObject();
				jsonXsd.put("contractDocId",
						parseDao.getSeq("SEQ_CONTRACT_DOC"));
				jsonXsd.put("baseConDocId", "");
				jsonXsd.put("docType", "1");
				jsonXsd.put("docName", new File(xsdFile).getName());
				jsonXsd.put("resourceAliss", new File(xsdFile).getName());
				jsonXsd.put("docVersion", version);
				jsonXsd.put("docCreateTime", dateFormat);
				jsonXsd.put(state, "A");
				jsonXsd.put("docPath", "");
				jsonXsd.put("address", xsdFile);
				JSONObject jsonXsdData = new JSONObject();
				jsonXsdData.put("xsdContractDoc", jsonXsd);
				jsonXsds.add(jsonXsdData);
			}
		}
		return jsonXsds;
	}

	private void getWsdlService(String resourceAliss, JSONObject jsonVersion) {
		// service
		if (mapBody.size() > 0) {
			for (String name : mapBody.keySet()) {
				JSONObject jsonService = new JSONObject();
				jsonService.put("serviceId", parseDao.getSeq("SEQ_SERVICE"));
				jsonService.put(contractVersionId,
						mapSeq.get(contractVersionId));
				jsonService.put("serviceCnName", resourceAliss + "." + name);
				jsonService.put("serviceEnName", resourceAliss + "." + name);
				jsonService.put("serviceCode", resourceAliss + "." + name);
				jsonService.put("serviceType", "0");
				jsonService.put("serviceVersion", resourceAliss + "." + name);
				jsonService.put("auditFlowId", "");
				jsonService.put(createTime, dateFormat);
				jsonService.put(state, "A");
				jsonService.put("serviceDesc", "");
				jsonService.put("isPublished", "Y");
				jsonService.put("servicePriority", "3");
				jsonService.put("serviceTimeout", 300);
				jsonVersion.put("service", jsonService);
			}
			mapBody.clear();
		}
	}

	/**
	 * 解析Document
	 * 
	 * @param data
	 * @param jsonArray
	 * @param type
	 *            RES/RSP
	 * @throws DocumentException
	 */
	@SuppressWarnings({ "unchecked" })
	private void parseDocument(String data, JSONArray jsonArray, int type)
			throws DocumentException {

		int tcpCtrFIdForContractFormatIdReq = 0;
		if (type == 1) {
			tcpCtrFIdForContractFormatIdReq = mapSeq.get("contractFormatIdREQ");
		}
		if (type == 2) {
			tcpCtrFIdForContractFormatIdReq = mapSeq.get("contractFormatIdRSP");
		}
		Document doc = DocumentHelper.parseText(data);
		// 根节点解析
		Element root = doc.getRootElement();
		JSONObject jsonNodeDesc = new JSONObject();
		mapSeq.put(nodeDescIdRoot, parseDao.getSeq(seqNodeDesc));
		getNodeDesc(tcpCtrFIdForContractFormatIdReq, root, jsonNodeDesc);
		jsonArray.add(jsonNodeDesc);// 添加节点描述数组

		// 获取当前命名空间
		Namespace nproot = root.getNamespace();
		JSONObject jsonNamespace = new JSONObject();
		getNamespace(tcpCtrFIdForContractFormatIdReq, root, nproot,
				jsonNamespace);
		jsonArray.add(jsonNamespace);// 添加节点描述数组

		// 获取其他命名
		List<Namespace> listN = root.additionalNamespaces();
		for (Namespace npIt : listN) {
			jsonNamespace = new JSONObject();
			getNamespace(tcpCtrFIdForContractFormatIdReq, root, npIt,
					jsonNamespace);
			jsonArray.add(jsonNamespace);// 添加节点描述数组
		}

		// 属性
		List<Attribute> arList = root.attributes();
		for (Attribute ab : arList) {
			JSONObject jsonAttr = new JSONObject();
			getAttr(tcpCtrFIdForContractFormatIdReq, root, ab, jsonAttr);
			jsonArray.add(jsonNamespace);// 添加节点描述数组
		}

		// 其他节点
		List<Element> eList = root.elements();
		parseChildElement(eList, mapSeq.get(nodeDescIdRoot), jsonArray,
				tcpCtrFIdForContractFormatIdReq, type);

		namespaceMap.clear();
	}

	private void getAttr(int tcpCtrFIdForContractFormatIdReq, Element root,
			Attribute ab, JSONObject jsonAttr) {
		jsonAttr.put(nodeDescId, parseDao.getSeq(seqNodeDesc));
		jsonAttr.put(tcpCtrFId, tcpCtrFIdForContractFormatIdReq);
		jsonAttr.put(parentNodeDescId, mapSeq.get(nodeDescIdRoot));
		jsonAttr.put(nodeName, root.getQualifiedName());
		jsonAttr.put(nodeCode, ab.getQualifiedName());
		jsonAttr.put(nodePath, ab.getUniquePath());
		jsonAttr.put(nodeType, "7");
		jsonAttr.put(nodeLengthCons, "");
		jsonAttr.put(nodeTypeCons, 1);
		jsonAttr.put(nodeNumberCons, 1);
		jsonAttr.put(nevlConsType, "");
		jsonAttr.put(nevlConsValue, "");
		jsonAttr.put(nevlConsDesc, "");
		jsonAttr.put(isNeedSign, "");
		jsonAttr.put(state, "A");
		jsonAttr.put(createTime, dateFormat);
		jsonAttr.put(javaField, "");
		jsonAttr.put(description, "");
	}

	private void getNamespace(int tcpCtrFIdForContractFormatIdReq,
			Element root, Namespace nproot, JSONObject jsonNamespace) {
		jsonNamespace.put(nodeDescId, parseDao.getSeq(seqNodeDesc));
		jsonNamespace.put(tcpCtrFId, tcpCtrFIdForContractFormatIdReq);
		jsonNamespace.put(parentNodeDescId, "");
		jsonNamespace.put(nodeName, root.getQualifiedName());
		jsonNamespace.put(nodeCode, nproot.getPrefix() + "=" + nproot.getURI());
		namespaceMap.put(nproot.getPrefix(), nproot.getURI());// 添加当前XML已获取属性
		jsonNamespace.put(nodePath, "");
		jsonNamespace.put(nodeType, "6");
		jsonNamespace.put(nodeLengthCons, "");
		jsonNamespace.put(nodeTypeCons, 1);
		jsonNamespace.put(nodeNumberCons, 1);
		jsonNamespace.put(nevlConsType, "");
		jsonNamespace.put(nevlConsValue, "");
		jsonNamespace.put(nevlConsDesc, "");
		jsonNamespace.put(isNeedSign, "");
		jsonNamespace.put(state, "A");
		jsonNamespace.put(createTime, dateFormat);
		jsonNamespace.put(javaField, "");
		jsonNamespace.put(description, "");
	}

	private void getNodeDesc(int tcpCtrFIdForContractFormatIdReq, Element root,
			JSONObject jsonNodeDesc) {
		jsonNodeDesc.put(nodeDescId, mapSeq.get(nodeDescIdRoot));
		jsonNodeDesc.put(tcpCtrFId, tcpCtrFIdForContractFormatIdReq);
		jsonNodeDesc.put(parentNodeDescId, "");
		jsonNodeDesc.put(nodeName, root.getQualifiedName());
		jsonNodeDesc.put(nodeCode, root.getQualifiedName());
		jsonNodeDesc.put(nodePath, "");
		jsonNodeDesc.put(nodeType, "2");
		jsonNodeDesc.put(nodeLengthCons, "");
		jsonNodeDesc.put(nodeTypeCons, 1);
		jsonNodeDesc.put(nodeNumberCons, 1);
		jsonNodeDesc.put(nevlConsType, "");
		jsonNodeDesc.put(nevlConsValue, "");
		jsonNodeDesc.put(nevlConsDesc, "");
		jsonNodeDesc.put("isNeedCheck", "");
		jsonNodeDesc.put(isNeedSign, "");
		jsonNodeDesc.put(state, "A");
		jsonNodeDesc.put(createTime, dateFormat);
		jsonNodeDesc.put(javaField, "");
		jsonNodeDesc.put(description, "");
	}
	
	public static String changeToLocal(String path){
		if(path.contains(":")){
			String locas[] = path.split("/");
			for (int i = 0; i < locas.length; i++) {
				
				if(locas[i].contains(":")){
					
					String att[] = locas[i].split(":");
					String newStr = "*[local-name()='"+att[1]+"']";
					
					path = path.replace(locas[i], newStr);
					
					
				}
			}
		}
		return path;
	}

	/**
	 * 子节点解析
	 * 
	 * @param childElementList
	 * @param parentNodeId
	 * @param jsonArray
	 * @param tcpCtr_FId
	 * @param type
	 */
	@SuppressWarnings("unchecked")
	private void parseChildElement(List<Element> childElementList,
			int parentNodeId, JSONArray jsonArray, int tcpCtr_FId, int type) {
		for (Element e : childElementList) {
			// 子节点信息
			JSONObject jsonElement = new JSONObject();
			mapSeq.put(childNodeDescId, parseDao.getSeq(seqNodeDesc));
			jsonElement.put(nodeDescId, mapSeq.get(childNodeDescId));
			jsonElement.put(tcpCtrFId, tcpCtr_FId);
			jsonElement.put(parentNodeDescId, parentNodeId);
			jsonElement.put(nodeName, e.getQualifiedName());
			jsonElement.put(nodeCode, e.getQualifiedName());
			jsonElement.put(nodePath, changeToLocal(e.getPath()));
			jsonElement.put(nodeType, "2");
			jsonElement.put(nodeLengthCons, "");
			jsonElement.put(nodeTypeCons, 1);
			jsonElement.put(nodeNumberCons, 1);
			jsonElement.put(nevlConsType, "");
			jsonElement.put(nevlConsValue, "");
			jsonElement.put(nevlConsDesc, "");
			jsonElement.put(isNeedSign, "");
			jsonElement.put(state, "A");
			jsonElement.put(createTime, dateFormat);
			jsonElement.put(javaField, "");
			jsonElement.put(description, "");
			jsonArray.add(jsonElement);// 添加节点数组

			// 子节点命名空间,通常只有一个
			Namespace chnp = e.getNamespace();
			if (chnp != null && namespaceMap.get(chnp.getPrefix()) == null
					&& !"".equals(chnp.getURI())) {
				JSONObject jsonNamespace = new JSONObject();
				jsonNamespace.put(nodeDescId, parseDao.getSeq(seqNodeDesc));
				jsonNamespace.put(tcpCtrFId, tcpCtr_FId);
				jsonNamespace
						.put(parentNodeDescId, mapSeq.get(childNodeDescId));
				jsonNamespace.put(nodeName, e.getQualifiedName());
				jsonNamespace.put(nodeCode,
						chnp.getPrefix() + "=" + chnp.getURI());
				namespaceMap.put(chnp.getPrefix(), chnp.getURI());// 添加当前XML已获取属性
				jsonNamespace.put(nodePath, "");
				jsonNamespace.put(nodeType, "8");
				jsonNamespace.put(nodeLengthCons, "");
				jsonNamespace.put(nodeTypeCons, 1);
				jsonNamespace.put(nodeNumberCons, 1);
				jsonNamespace.put(nevlConsType, "");
				jsonNamespace.put(nevlConsValue, "");
				jsonNamespace.put(nevlConsDesc, "");
				jsonNamespace.put(isNeedSign, "");
				jsonNamespace.put(state, "A");
				jsonNamespace.put(createTime, dateFormat);
				jsonNamespace.put(javaField, "");
				jsonNamespace.put(description, "");
				jsonArray.add(jsonNamespace);// 添加节点数组
			}

			// 子节点属性
			List<Attribute> arList = e.attributes();
			for (Attribute ab : arList) {
				JSONObject jsonAttr = new JSONObject();
				jsonAttr.put(nodeDescId, parseDao.getSeq(seqNodeDesc));
				jsonAttr.put(tcpCtrFId, tcpCtr_FId);
				jsonAttr.put(parentNodeDescId, mapSeq.get(childNodeDescId));
				jsonAttr.put(nodeName, e.getQualifiedName());
				jsonAttr.put(nodeCode, ab.getQualifiedName());
				jsonAttr.put(nodePath, ab.getUniquePath());
				jsonAttr.put(nodeType, "7");
				jsonAttr.put(nodeLengthCons, "");
				jsonAttr.put(nodeTypeCons, 1);
				jsonAttr.put(nodeNumberCons, 1);
				jsonAttr.put(nevlConsType, "");
				jsonAttr.put(nevlConsValue, "");
				jsonAttr.put(nevlConsDesc, "");
				jsonAttr.put(isNeedSign, "");
				jsonAttr.put(state, "A");
				jsonAttr.put(createTime, dateFormat);
				jsonAttr.put(javaField, "");
				jsonAttr.put(description, "");
				jsonArray.add(jsonAttr);
			}
			// 下级节点
			List<Element> chList = e.elements();
			if (chList.size() > 0) {
				parseChildElement(chList, mapSeq.get(childNodeDescId),
						jsonArray, tcpCtr_FId, type);
				if ("Body".equals(e.getName()) && 1 == type) {
					getBodyService(chList);
				}
			}

		}
	}

	/**
	 * 获取body method
	 * 
	 * @param list
	 */
	private void getBodyService(List<Element> list) {
		Element e = list.get(0);
		mapBody.put(e.getName(), e.getName());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String judgeWsdl(String id, String wsdlType, String resourceAliss,
			String version,String tenantId) throws BusinessException {
		List<HashMap> files = parseDao.getFileShare(id,tenantId);
		validationJudge(id, files);
		if (LOG.isDebugEnabled()) {
			LOG.debug("file share id : {0}", id);
			LOG.debug("file name : {0}", files.get(0).get("SFILENAME"));
		}

		// 获取前缀路径
		String path = CommonUtil.wsdlDownDir;

		if (path == null) {
			path = System.getProperty("user.dir") + File.separator + "down";
		}

		String name = (String) files.get(0).get("SFILENAME");
		
		String filePath = path + File.separator + tenantId;
		File newPath = new File(filePath);
		if(!newPath.exists()){
			newPath.mkdirs();
		}
		String relaPath = path + File.separator + tenantId +File.separator + name;

		judgeFileType(files, relaPath);

		relaPath = judgeSystem(relaPath);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("relaPath file {0}", relaPath);
		}
		String json = null;
		// 当个文件判断
		int i = relaPath.lastIndexOf('.');
		if ("wsdl".equals(relaPath.substring(i + 1))) {
			json = parseWsdl(relaPath, resourceAliss, version,tenantId);
		} else {// 压缩文件
				// 解压文件
			String wsdlFileUrl = FileUtils.uFile(relaPath);
			if ("=".equals(wsdlFileUrl.substring(wsdlFileUrl.length()-1))) {
				wsdlFileUrl = wsdlFileUrl.substring(0, wsdlFileUrl.length()-1);
			}
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("judgeWsdl file {0}", wsdlFileUrl);
			}
			String[] wsdlFileList = wsdlFileUrl.split("=");
			if (wsdlFileList.length >= 2) {
				WsdlInterface[] facesInfo = null;
				for (String wsdlFile : wsdlFileList) {
					try {
						facesInfo = WsdlInterfaceFactory.importWsdl(wsdlProject,
								wsdlFile, false);
					} catch (SoapUIException e) {
						LOG.error(
								WsdlExceptionCode.o2pWsdl
										+ " parseWsdl WsdlInterfaceFactory importWsdl SoapUIException",
								ExceptionUtils.populateExecption(e, 500));
						throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
					}
					if (LOG.isDebugEnabled()) {
						LOG.debug("facesInfo file {0}", wsdlFile);
					}
					if (facesInfo.length > 0) {
						// 解析文件
						json = parseWsdl(wsdlFile, resourceAliss, version,tenantId);
						break;
					}
				}
				
			}else {
				json = parseWsdl(wsdlFileUrl, resourceAliss, version,tenantId);
			}
			
		}

		JSONObject jsonObject = JSONObject.fromObject(json);
		jsonObject.put("relaPath", relaPath);

		String suffix = relaPath.split("\\.")[1];
		if ("zip".equals(suffix) || "rar".equals(suffix)) {
			// 删除压缩文件
			FileUtils.deleteFile(relaPath);
		}

		return jsonObject.toString();
	}

	@SuppressWarnings("rawtypes")
	private void judgeFileType(List<HashMap> files, String relaPath) {
		if (files.get(0).get(sFileContent) instanceof oracle.sql.BLOB) {
			oracleBlob(files, relaPath);

		} else if (files.get(0).get(sFileContent) instanceof oracle.sql.CLOB) {
			oracleClob(files, relaPath);

		} else {
			// 获取文件字节数组
			byte[] byteArr = (byte[]) files.get(0).get(sFileContent);

			if (StringUtils.isEmpty(relaPath)) {
				LOG.error("blob download path is null");
				return;
			}

			final String newPath = relaPath;
			// 写文件
			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(
						newPath));

			} catch (FileNotFoundException e) {
				LOG.error(
						WsdlExceptionCode.o2pWsdl
								+ " judgeFileType bufferedOutputStream FileNotFoundException",
						ExceptionUtils.populateExecption(e, 500));
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
			}
			try {
				out.write(byteArr);
				out.flush();
				out.close();
				if (LOG.isDebugEnabled()) {
					LOG.debug("stream byte {0}", byteArr);
					LOG.debug("stream down is success {0}", newPath);
				}
			} catch (IOException e) {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " judgeFileType close IOException",
						ExceptionUtils.populateExecption(e, 500));
				throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
			}

		}
	}

	@SuppressWarnings("rawtypes")
	protected void oracleClob(List<HashMap> files, String relaPath) {
		oracle.sql.CLOB clob = (oracle.sql.CLOB) files.get(0).get(sFileContent);
		BufferedReader in;
		BufferedWriter out = null;

		if (StringUtils.isEmpty(relaPath)) {
			LOG.error("blob download path is null");
			return;
		}

		final String newPath = relaPath;
		try {
			in = new BufferedReader(clob.getCharacterStream());
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(newPath), "UTF-8"));

		} catch (SQLException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " judgeFileType oracle.sql.CLOB SQLException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		} catch (IOException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " judgeFileType oracle.sql.CLOB IOException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}

		int i;
		try {
			while ((i = in.read()) != -1) {
				out.write(i);
			}
		} catch (IOException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " judgeFileType oracle.sql.CLOB write IOException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				LOG.error(WsdlExceptionCode.o2pWsdl
						+ " judgeFileType oracle.sql.CLOB close IOException",
						ExceptionUtils.populateExecption(e, 500));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected void oracleBlob(List<HashMap> files, String relaPath) {
		oracle.sql.BLOB b = (oracle.sql.BLOB) files.get(0).get(sFileContent);
		java.io.InputStream is;
		java.io.FileOutputStream fos = null;
		if (StringUtils.isEmpty(relaPath)) {
			LOG.error("blob download path is null");
			return;
		}
		
		final String newPath = relaPath;
		try {
			is = b.getBinaryStream();
			fos = new java.io.FileOutputStream(newPath);

		} catch (SQLException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " judgeFileType oracle.sql.BLOB SQLExption",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		} catch (FileNotFoundException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " judgeFileType oracle.sql.BLOB FileNotFoundException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		}

		int i = 0;
		try {
			while ((i = is.read()) != -1) {
				fos.write(i);
			}
		} catch (IOException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ "judgeFileType oracle.sql.BLOB IOException",
					ExceptionUtils.populateExecption(e, 500));
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.error(
							WsdlExceptionCode.o2pWsdl
									+ " judgeFileType oracle.sql.BLOB output closs IOException",
							ExceptionUtils.populateExecption(e, 500));
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error(
							WsdlExceptionCode.o2pWsdl
									+ " judgeFileType oracle.sql.BLOB input closs IOException",
							ExceptionUtils.populateExecption(e, 500));
				}
			}
		}
	}

	private String judgeSystem(final String relaPath) {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		LOG.debug("system : {0}", osName);
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}

		String transformerPath = relaPath;
		if (isWindowsOS) {
			transformerPath = transformerPath.replace('/', '\\');
		} else {
			// linux环境设置读写执行权限
			final String newTansformerPath = transformerPath;
			File filePermissions = new File(newTansformerPath);
			filePermissions.setExecutable(true);
			filePermissions.setReadable(true);
			filePermissions.setWritable(true);
		}
		return transformerPath;
	}

	@SuppressWarnings("rawtypes")
	private void validationJudge(String id, List<HashMap> files) {
		if (files.size() == 0) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " data is null id is {0}", id);
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.judgeWsdl", null, null);
		}
		if (files.get(0).get(sFileContent) == null) {
			LOG.error(WsdlExceptionCode.o2pWsdl
					+ " data sfileContent is null id {0}", id);
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					"o2p.wsdl.judgeWsdl", null, null);
		}
	}

}
