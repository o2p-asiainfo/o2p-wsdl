package test.com.asiainfo.o2p.wsdl.bmo;

import org.easymock.EasyMock;
import org.junit.*;

import static org.junit.Assert.*;

import com.ailk.eaap.op2.bo.ContractDoc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.o2p.wsdl.bmo.IWsdlBmoValidate;
import com.asiainfo.o2p.wsdl.bmo.WsdlBmoImpl;
import com.asiainfo.o2p.wsdl.bmo.WsdlBmoValidateImpl;
import com.asiainfo.o2p.wsdl.dao.IWsdlDao;
import com.asiainfo.o2p.wsdl.dao.WsdlDaoImpl;

/**
 * The class <code>WsdlBmoImplTest</code> contains tests for the class <code>{@link WsdlBmoImpl}</code>.
 *
 * @generatedBy CodePro at 15-5-13 下午2:24
 * @author zhongming
 * @version $Revision: 1.0 $
 */
public class WsdlBmoImplTest {
	/**
	 * Run the WsdlBmoImpl() constructor test.
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testWsdlBmoImpl_1()
		throws Exception {
		WsdlBmoImpl result = new WsdlBmoImpl();
		assertNotNull(result);
		// add additional test code here
	}

	/**
	 * Run the int getSeq(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testGetSeq_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		String string = "";
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		EasyMock.expect(fixture.getSeq(string)).andReturn(0);
		EasyMock.replay(fixture);

		int result = fixture.getSeq(string);

		assertEquals(0, result);
		EasyMock.verify(fixture);
	}

	/**
	 * Run the IWsdlDao getWsdlDao() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testGetWsdlDao_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());

		IWsdlDao result = fixture.getWsdlDao();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the IWsdlBmoValidate getWsdlValidate() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testGetWsdlValidate_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());

		IWsdlBmoValidate result = fixture.getWsdlValidate();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the void save(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSave_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		String jsonObject = "";

		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.save(jsonObject ,"22");

		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.save(jsonObject ,"22");
		
		EasyMock.verify(fixture);
	}

	/**
	 * Run the void saveApi(Service) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveApi_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		Service service = new Service();
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveApi(service ,"22");
		
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveApi(service ,"22");
		
		EasyMock.verify(fixture);

	}

	/**
	 * Run the void saveComponent(JSONObject,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveComponent_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject componentJson = new JSONObject();
		int orgId = 1;

		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveComponent(componentJson, orgId ,"22");
		
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveComponent(componentJson, orgId ,"22");
		
		EasyMock.verify(fixture);
	}

	/**
	 * Run the void saveContractDoc(JSONObject) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveContractDoc_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject cd = new JSONObject();
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveContractDoc(cd, "22");
		
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveContractDoc(cd, "22");
		
		EasyMock.verify(fixture);

	}

	/**
	 * Run the void saveContractDocRela(JSONObject,contractDoc, "22") method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveContractDocRela_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject contractDocRelaJson = new JSONObject();
		ContractDoc contractDoc = new ContractDoc();
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveContractDocRela(contractDocRelaJson, contractDoc, "22");

		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveContractDocRela(contractDocRelaJson, contractDoc, "22");
		
		EasyMock.verify(fixture);
		
		// add additional test code here
		// An unexpected exception was thrown in user code while executing this test:
		//    java.lang.NullPointerException
		//       at com.asiainfo.o2p.wsdl.dao.WsdlDaoImpl.getContractDocList(WsdlDaoImpl.java:113)
		//       at com.asiainfo.o2p.wsdl.bmo.WsdlBmoImpl.saveContractDocRela(WsdlBmoImpl.java:347)
	}

	/**
	 * Run the void saveContractFormat(JSONArray) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveContractFormat_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONArray contractFormatsJson = new JSONArray();

		fixture.saveContractFormat(contractFormatsJson ,"22");

		// add additional test code here
	}

	/**
	 * Run the void saveContractVersion(JSONObject,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveContractVersion_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject cv = new JSONObject();
		Integer contractDocId = new Integer(1);
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveContractVersion(cv, contractDocId ,"22");
		
		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveContractVersion(cv, contractDocId ,"22");
		
		EasyMock.verify(fixture);

	}

	/**
	 * Run the void saveContractVersionRelaJson(JSONObject,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveContractVersionRelaJson_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject contractVersionRelaJson = new JSONObject();
		Integer contractVersionId = new Integer(1);
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveContractVersionRelaJson(contractVersionRelaJson, contractVersionId ,"22");

		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveContractVersionRelaJson(contractVersionRelaJson, contractVersionId ,"22");
		
		EasyMock.verify(fixture);
		
	}

	/**
	 * Run the void saveFileShare(JSONObject,String,String,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveFileShare_1()
		throws Exception {
//		WsdlBmoImpl fixture = new WsdlBmoImpl();
//		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
//		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject contractDocJson = new JSONObject();
		String resourceAliss = "";
		String xsdResourceAliss = "";
		int sFileId = 1;
		
		WsdlBmoImpl fixture = EasyMock.createMock(WsdlBmoImpl.class);
		fixture.saveFileShare(contractDocJson, resourceAliss, xsdResourceAliss, sFileId ,"22");

		EasyMock.expectLastCall();
		EasyMock.replay(fixture);
		fixture.saveFileShare(contractDocJson, resourceAliss, xsdResourceAliss, sFileId ,"22");
		
		EasyMock.verify(fixture);
	}

	/**
	 * Run the void saveNodeDesc(JSONArray,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSaveNodeDesc_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONArray nodeDescs = new JSONArray();
		int contractFormatId = 1;

		fixture.saveNodeDesc(nodeDescs, contractFormatId ,"22");

		// add additional test code here
	}

	/**
	 * Run the void saveOrg(JSONObject) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testSaveOrg_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject orgJson = new JSONObject();

		fixture.saveOrg(orgJson ,"22");

		// add additional test code here
	}

	/**
	 * Run the void saveProto(JSONObject,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testSaveProto_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject contractJson = new JSONObject();
		Integer contractDocId = new Integer(1);

		fixture.saveProto(contractJson, contractDocId ,"22");

		// add additional test code here
	}

	/**
	 * Run the void saveService(JSONObject,int) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testSaveService_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		JSONObject serviceJson = new JSONObject();
		int contractVersionId = 1;

		fixture.saveService(serviceJson, contractVersionId ,"22");

		// add additional test code here
	}

	/**
	 * Run the void setWsdlDao(IWsdlDao) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSetWsdlDao_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		IWsdlDao wsdlDao = new WsdlDaoImpl();

		fixture.setWsdlDao(wsdlDao);

		// add additional test code here
	}

	/**
	 * Run the void setWsdlValidate(IWsdlBmoValidate) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Test
	public void testSetWsdlValidate_1()
		throws Exception {
		WsdlBmoImpl fixture = new WsdlBmoImpl();
		fixture.setWsdlValidate(new WsdlBmoValidateImpl());
		fixture.setWsdlDao(new WsdlDaoImpl());
		IWsdlBmoValidate wsdlValidate = new WsdlBmoValidateImpl();

		fixture.setWsdlValidate(wsdlValidate);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:24
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}
}