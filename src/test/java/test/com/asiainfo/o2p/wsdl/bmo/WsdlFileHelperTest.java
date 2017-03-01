package test.com.asiainfo.o2p.wsdl.bmo;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.asiainfo.o2p.wsdl.bmo.WsdlFileHelper;

/**
 * The class <code>WsdlFileHelperTest</code> contains tests for the class <code>{@link WsdlFileHelper}</code>.
 *
 * @generatedBy CodePro at 15-5-13 下午3:00
 * @author zhongming
 * @version $Revision: 1.0 $
 */
public class WsdlFileHelperTest {
	/**
	 * Run the byte[] docToByte(Element) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午3:00
	 */
	@Test
	public void testDocToByte_1()
		throws Exception {
//		Element doc = EasyMock.createMock(IIOMetadataNode.class);
//		byte[] re = new byte[0];
//		EasyMock.expect(WsdlFileHelper.docToByte(doc)).andReturn(re);
//		EasyMock.replay(doc);
//		byte[] result = new byte[0];//WsdlFileHelper.docToByte(doc);
//
//		assertNotNull(result);
	}

	/**
	 * Run the byte[] getFileByUrl(String,String) method test.
	 *
	 * @throws Exception
	 *
	 * @generatedBy CodePro at 15-5-13 下午3:00
	 */
	@Test(expected = com.asiainfo.foundation.exception.BusinessException.class)
	public void testGetFileByUrl_1()
		throws Exception {
		String url = "";
		String resourceAliss = "";

		byte[] result = WsdlFileHelper.getFileByUrl(url, resourceAliss);

		// add additional test code here
		assertNotNull(result);
	}

	/**
	 * Perform pre-test initialization.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 * @generatedBy CodePro at 15-5-13 下午3:00
	 */
	@Before
	public void setUp()
		throws Exception {
		// add additional set up code here
	}
}