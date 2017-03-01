package com.asiainfo.o2p.wsdl.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.linkage.rainbow.dao.impl.IBatisSqlMapDAOImpl;

public class WsdlParseDaoImpl implements IWsdlParseDao {
	private SqlSessionTemplate sqlSessionTemplate;
	


	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	/**
	 * 获取主键ID
	 */
	public int getSeq(String sequenceName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sequenceName", sequenceName);
		return (Integer) sqlSessionTemplate.selectOne("wsdlservice.sequence", map);
	}

	/**
	 * 获取压缩文件
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.asiainfo.o2p.wsdl.dao.IWsdlParseDao#getFileShare(java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<HashMap> getFileShare(String sFileId,String tenantId) {
		Map map = new HashMap();
		map.put("sFileId", sFileId);
		map.put("tenantId", tenantId);
		return sqlSessionTemplate.selectList("wsdl.GET_FILE_SHARE", map);
	}

	@Override
	public void deleteFileShare(String sFileId,String tenantId) {
		Map map = new HashMap();
		map.put("sFileId", sFileId);
		map.put("tenantId", tenantId);
		sqlSessionTemplate.delete("wsdl.DELETE_FILE_SHARE", map);
	}

}
