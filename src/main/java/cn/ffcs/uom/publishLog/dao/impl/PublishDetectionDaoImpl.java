/**
 * 
 */
package cn.ffcs.uom.publishLog.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.publishLog.dao.PublishDetectionDao;

/**
 * @author wenyaopeng
 *
 */
@Repository("publishDetectionDao")
public class PublishDetectionDaoImpl extends BaseDaoImpl implements PublishDetectionDao{

	@Override
	public List queryPublishRange(Map arg){
		List object = null;
		final String staffId = arg.get("staffId").toString();
		final String orgCode = arg.get("orgCode").toString();
		if(!StrUtil.isEmpty(staffId)||!StrUtil.isEmpty(orgCode)){
			object = (List)super.getJdbcTemplate().execute(new ConnectionCallback<Object>() {
				@Override
				public Object doInConnection(Connection conn) throws SQLException, DataAccessException {
					CallableStatement cstmt = null;
					if(!StrUtil.isEmpty(staffId)){
						cstmt = conn.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.queryStaffPublishRange(?,?)}");
						cstmt.setString(1, staffId);
					}else if(!StrUtil.isEmpty(orgCode)){
						cstmt = conn.prepareCall("{CALL PKG_OPERATELOG_PUBLISH.queryOrgPublishRange(?,?)}");
						cstmt.setString(1, orgCode);
					}else{
						return null;
					}
				    cstmt.registerOutParameter(2, OracleTypes.CURSOR);
				    cstmt.execute();
				    return getResultSet((ResultSet)cstmt.getObject(2));
				}
			});	
		}
		return object;
	}

	
	/**
	 * 方法功能说明：将分页取出的结果集ResultSet对象组装成 List<--Map<--(columnName:columnValue),
	 * 每一个map对应一条记录，map长度 == column数量
	 * 创建：2012-10-16 by hsy 
	 * 修改：日期 by 修改者
	 * 修改内容：
	 * @参数： @param rs
	 * @参数： @return    
	 * @return Map   
	 * @throws
	 */
	private List getResultSet(ResultSet rs)throws SQLException{
		List list = new ArrayList();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			//每循环一次遍历出来1条记录，记录对应的所有列值存放在map中(columnName:columnValue)
			while(rs.next()){
				Map map = new HashMap(); 
				int columnCount = rsmd.getColumnCount();
				for(int i=0;i<columnCount;i++){
					String columnName = rsmd.getColumnName(i+1);
					map.put(columnName, rs.getObject(i+1));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
