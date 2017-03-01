package com.asiainfo.o2p.wsdl.bmo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.wsdl.WSDLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;




import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.asiainfo.foundation.exception.BusinessException;
import com.asiainfo.foundation.log.Logger;
import com.asiainfo.o2p.wsdl.exception.WsdlExceptionCode;
import com.ibm.wsdl.util.StringUtils;

public final class WsdlFileHelper {

	private final static Logger LOG = Logger.getLog(WsdlFileHelper.class);
	
	private WsdlFileHelper() {}

	/**
	 * 通过url取得WSDL二进制文件
	 * 
	 * @param url
	 * @param resourceAliss
	 */
	public static byte[] getFileByUrl(String url, String resourceAliss)
			throws BusinessException {

		byte[] b = null;
		InputStream inputStream = null;
		try {
			URL u = StringUtils.getURL(null, url);
			inputStream = StringUtils.getContentAsInputStream(u);
			b = updateFile(inputStream, resourceAliss);
		} catch (MalformedURLException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl MalformedURLException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (SecurityException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl SecurityException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (IllegalArgumentException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl IllegalArgumentException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (IOException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl IOException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (WSDLException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl WSDLException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (ParserConfigurationException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl ParserConfigurationException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (SAXException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl SAXException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} catch (TransformerException e) {
			LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl TransformerException", e.getMessage());
			throw new BusinessException(WsdlExceptionCode.RUNTIME_ERROR,
					e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error(WsdlExceptionCode.o2pWsdl + " getFileByUrl finally IOException", e.getMessage());
			}
		}
		return b;
	}

	/**
	 * 修改文件内容
	 * 
	 * @param inputStream
	 * @param resourceAliss
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws WSDLException
	 * @throws TransformerException
	 */
	private static byte[] updateFile(InputStream inputStream,
			String resourceAliss) throws WSDLException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		InputSource inputSource = new InputSource(inputStream);

		Document doc = getDocument(inputSource);

		Element root = doc.getDocumentElement();

		updateImportNode(root, root, resourceAliss);

		return docToByte(root);
	}

	/**
	 * 递归查找import节点，并修改schemaLocation属性
	 * 
	 * @param node
	 * @param root
	 * @param resourceAliss
	 */
	private static void updateImportNode(Node node, Element root,
			String resourceAliss) {

		String nodeName = node.getNodeName();

		if (node.getPrefix() != null) {
			nodeName = nodeName.substring(node.getPrefix().length() + 1);
		}

		if ("import".equals(nodeName.toLowerCase())) {
			String schemaLocation = ((Element) node)
					.getAttribute("schemaLocation");
			String location = ((Element) node)
					.getAttribute("location");
			
			if(!org.apache.commons.lang.StringUtils.isEmpty(schemaLocation)) {
				int index = schemaLocation.lastIndexOf('/');
				// 修改schemaLocation属性值为“资源别名+文件名”
				schemaLocation = resourceAliss + "."
						+ schemaLocation.substring(index > -1 ? index + 1 : 0);
				((Element) node).removeAttribute("schemaLocation");
				((Element) node).setAttribute("schemaLocation", schemaLocation);
			} else if(!org.apache.commons.lang.StringUtils.isEmpty(location)) {
				int index = location.lastIndexOf('/');
				// 修改location属性值为“资源别名+文件名”
				location = resourceAliss + "."
						+ location.substring(index > -1 ? index + 1 : 0);
				((Element) node).removeAttribute("location");
				((Element) node).setAttribute("location", location);
			}
			
			getRoot(node, root);
		} else {
			if (node.hasChildNodes()) {

				NodeList list = node.getChildNodes();

				for (int i = 0; i < list.getLength(); i++) {

					Node chileNode = list.item(i);

					updateImportNode(chileNode, root, resourceAliss);
				}
			}
		}

	}

	/**
	 * 将原根节点替换为新根节点
	 * 
	 * @param node
	 * @param root
	 */
	private static void getRoot(Node node, Element root) {
		if (node.isSameNode(root)) {
			return;
		} else {

			getRoot(node.getParentNode(), root);
		}
	}

	/**
	 * 通过输入流获取文档对象
	 * 
	 * @param inputSource
	 * @return
	 * @throws WSDLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document getDocument(InputSource inputSource)
			throws WSDLException, ParserConfigurationException, SAXException,
			IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setNamespaceAware(true);
		factory.setValidating(false);

		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(inputSource);

		return doc;
	}

	/**
	 * 文档对象转换为二进制
	 * 
	 * @param doc
	 * @return
	 * @throws TransformerException
	 */
	public static byte[] docToByte(Element doc) throws TransformerException {

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty("encoding", "UTF-8");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		t.transform(new DOMSource(doc), new StreamResult(bos));

		return bos.toByteArray();
	}
}
