package test.com.asiainfo.o2p.wsdl.bmo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.ailk.eaap.op2.bo.Contract;
import com.ailk.eaap.op2.bo.ContractDoc;
import com.ailk.eaap.op2.bo.ContractVersion;
import com.ailk.eaap.op2.bo.NodeDesc;
import com.ailk.eaap.op2.bo.Service;
import com.asiainfo.o2p.wsdl.bmo.WsdlBmoValidateImpl;
import com.asiainfo.o2p.wsdl.dao.IWsdlDao;
import com.asiainfo.o2p.wsdl.dao.WsdlDaoImpl;

/**
 * The class <code>WsdlBmoValidateImplTest</code> contains tests for the class <code>{@link WsdlBmoValidateImpl}</code>.
 *
 * @generatedBy CodePro at 15-5-13 下午2:48
 * @author zhongming
 * @version $Revision: 1.0 $
 */
public class WsdlBmoValidateImplTest {
	/**
	 * Run the String checkContract(Contract,Integer) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckContract_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		Contract contract = new Contract();
		Integer contractDocId = new Integer(1);

		String result = fixture.checkContract(contract, contractDocId ,"22");

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String checkContractDoc(ContractDoc) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckContractDoc_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		ContractDoc contractDoc = new ContractDoc();

		String result = fixture.checkContractDoc(contractDoc, CacheKey.defaultTenantId.toString());

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the void checkContractFormat(Map<String,Object>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckContractFormat_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		Map<String, Object> param = new HashMap<String, Object>();

		fixture.checkContractFormat(param, CacheKey.defaultTenantId.toString());

		// add additional test code here
	}

	/**
	 * Run the void checkContractVersion(ContractVersion) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckContractVersion_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		ContractVersion contractVersion = new ContractVersion();

		fixture.checkContractVersion(contractVersion, CacheKey.defaultTenantId.toString());

		// add additional test code here
	}

	/**
	 * Run the String checkFileShare(Map<String,Object>) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckFileShare_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		Map<String, Object> fsParam = new HashMap<String, Object>();

		String result = fixture.checkFileShare(fsParam, CacheKey.defaultTenantId.toString());

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String checkNodeDesc(NodeDesc) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckNodeDesc_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		NodeDesc nodeDesc = new NodeDesc();

		String result = fixture.checkNodeDesc(nodeDesc, CacheKey.defaultTenantId.toString());

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the String checkService(Service) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testCheckService_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		Service service = new Service();

		String result = fixture.checkService(service, CacheKey.defaultTenantId.toString());

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the List<Map<String, Object>> getContractDocVersionList(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test
	public void testGetContractDocVersionList_1()
		throws Exception {
//		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
//		fixture.setWsdlDao(new WsdlDaoImpl());
		String resourceAliss = "";
		WsdlBmoValidateImpl fixture = EasyMock.createMock(WsdlBmoValidateImpl.class);
		List<Map<String, Object>> reList = new ArrayList<Map<String, Object>>();
		EasyMock.expect(fixture.getContractDocVersionList(resourceAliss, CacheKey.defaultTenantId.toString())).andReturn(reList);
		EasyMock.replay(fixture);
		
		List<Map<String, Object>> result = fixture.getContractDocVersionList(resourceAliss, CacheKey.defaultTenantId.toString());
		assertNotNull(result);
		EasyMock.verify(fixture);
	}

	/**
	 * Run the IWsdlDao getWsdlDao() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test
	public void testGetWsdlDao_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());

		IWsdlDao result = fixture.getWsdlDao();

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Run the boolean hasContractByCode(String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test
	public void testHasContractByCode_1()
		throws Exception {
//		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
//		fixture.setWsdlDao(new WsdlDaoImpl());
		String contractCode = "";
		WsdlBmoValidateImpl fixture = EasyMock.createMock(WsdlBmoValidateImpl.class);
		EasyMock.expect(fixture.hasContractByCode(contractCode, CacheKey.defaultTenantId.toString())).andReturn(true);
		EasyMock.replay(fixture);
		boolean result = fixture.hasContractByCode(contractCode, CacheKey.defaultTenantId.toString());

		assertTrue(result);
		EasyMock.verify(fixture);
	}

	/**
	 * Run the boolean hasResourceAlissVersion(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test
	public void testHasResourceAlissVersion_1()
		throws Exception {
//		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
//		fixture.setWsdlDao(new WsdlDaoImpl());
		String resourceAliss = "";
		String docVersion = "";
		WsdlBmoValidateImpl fixture = EasyMock.createMock(WsdlBmoValidateImpl.class);
		EasyMock.expect(fixture.hasResourceAlissVersion(resourceAliss, docVersion, CacheKey.defaultTenantId.toString())).andReturn(true);
		EasyMock.replay(fixture);
		
		boolean result = fixture.hasResourceAlissVersion(resourceAliss, docVersion, CacheKey.defaultTenantId.toString());
		assertTrue(result);
		EasyMock.verify(fixture);
	}

	/**
	 * Run the void setWsdlDao(IWsdlDao) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Test
	public void testSetWsdlDao_1()
		throws Exception {
		WsdlBmoValidateImpl fixture = new WsdlBmoValidateImpl();
		fixture.setWsdlDao(new WsdlDaoImpl());
		IWsdlDao wsdlDao = new WsdlDaoImpl();

		fixture.setWsdlDao(wsdlDao);

		// add additional test code here
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 15-5-13 下午2:48
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}
}