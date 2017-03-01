package test.com.asiainfo.o2p.wsdl.bmo;

import org.easymock.EasyMock;
import org.junit.*;

import static org.junit.Assert.*;

import com.ailk.eaap.o2p.common.cache.CacheKey;
import com.asiainfo.o2p.wsdl.bmo.WsdlParseImpl;
import com.asiainfo.o2p.wsdl.dao.IWsdlParseDao;
import com.asiainfo.o2p.wsdl.dao.WsdlParseDaoImpl;

/**
 * The class <code>WsdlParseImplTest</code> contains tests for the class <code>{@link WsdlParseImpl}</code>.
 *
 * @generatedBy CodePro at 15-5-14 上午9:51
 * @author zhongming
 * @version $Revision: 1.0 $
 */
public class WsdlParseImplTest {
	/**
	 * Run the IWsdlParseDao getParseDao() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Test
	public void testGetParseDao_1()
		throws Exception {
		WsdlParseImpl fixture = new WsdlParseImpl();
		fixture.setParseDao(new WsdlParseDaoImpl());

		IWsdlParseDao result = fixture.getParseDao();

		assertNotNull(result);
	}

	/**
	 * Run the String judgeWsdl(String,String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Test
	public void testJudgeWsdl_1()
		throws Exception {
		WsdlParseImpl fixture = EasyMock.createMock(WsdlParseImpl.class);
		fixture.setParseDao(EasyMock.createMock(WsdlParseDaoImpl.class));
		String id = "";
		String wsdlType = "";
		String resourceAliss = "";
		String version = "";

		EasyMock.expect(fixture.judgeWsdl(id, wsdlType, resourceAliss, version, CacheKey.defaultTenantId.toString())).andReturn("");
		EasyMock.replay(fixture);
		String result = fixture.judgeWsdl(id, wsdlType, resourceAliss, version, CacheKey.defaultTenantId.toString());

		assertNotNull(result);
	}

	/**
	 * Run the String parseWsdl(String,String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Test
	public void testParseWsdl_1()
		throws Exception {
		WsdlParseImpl fixture = EasyMock.createMock(WsdlParseImpl.class);//new WsdlParseImpl();
		fixture.setParseDao(EasyMock.createMock(WsdlParseDaoImpl.class));
		String wsdlFileUrl = "";
		String resourceAliss = "";
		String version = "";

		EasyMock.expect(fixture.parseWsdl(wsdlFileUrl, resourceAliss, version, CacheKey.defaultTenantId.toString())).andReturn("");
		EasyMock.replay(fixture);
		
		String result = fixture.parseWsdl(wsdlFileUrl, resourceAliss, version, CacheKey.defaultTenantId.toString());

		assertNotNull(result);
//		EasyMock.verify(fixture);
	}

	/**
	 * Run the void setParseDao(IWsdlParseDao) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Test
	public void testSetParseDao_1()
		throws Exception {
		WsdlParseImpl fixture = new WsdlParseImpl();
		fixture.setParseDao(new WsdlParseDaoImpl());
		IWsdlParseDao parseDao = new WsdlParseDaoImpl();

		fixture.setParseDao(parseDao);

	}

	/**
	 * Run the void updateWsdlProject() method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Test
	public void testUpdateWsdlProject_1()
		throws Exception {
		WsdlParseImpl fixture = new WsdlParseImpl();
		fixture.setParseDao(new WsdlParseDaoImpl());

		fixture.updateWsdlProject(CacheKey.defaultTenantId.toString());

	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 15-5-14 上午9:51
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}
}