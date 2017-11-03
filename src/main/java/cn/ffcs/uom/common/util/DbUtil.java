package cn.ffcs.uom.common.util;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author 曾臻
 * @date 2012-10-22
 * 
 */
public class DbUtil {
	@Resource(name = "dataSource")
	private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
	
	public static long fetchNextSeq(JdbcTemplate jdbcTemplate, String seqName) {
		final long[] id = new long[1];
		String sql = "select " + seqName + ".nextval from dual";
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				id[0] = rs.getLong(1);
			}
		});
		return id[0];
	}
	public static long fetchNextSeq4Postgres(JdbcTemplate jdbcTemplate, String seqName) {
		final long[] id = new long[1];
		String sql = "select nextval('" + seqName + "');";
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				id[0] = rs.getLong(1);
			}
		});
		return id[0];
	}
	public static boolean isHaveColumn(ResultSet rs,String colname) throws SQLException{
		ResultSetMetaData rsmd=rs.getMetaData();
		for(int i=0;i<rsmd.getColumnCount();i++)
			if(colname.equalsIgnoreCase(rsmd.getColumnName(i+1)))
					return true;
		return false;
	}

	public static boolean isFieldAssigned(String field) {
		return field != null&&!"".equals(field);
	}

	public static boolean isFieldAssigned(long field) {
		return field != 0;
	}
	
	public static boolean isFieldAssigned(Long field) {
		return field !=null;
	}

	/**
	 * 分页查询工具
	 * 
	 * @author 曾臻
	 * @param <T>
	 * @date 2012-11-20
	 * @param jdbcTemplate
	 * @param sql
	 * @param page
	 * @param size
	 * @param extraColumns
	 *            额外要查询的列
	 * @return [0]=结果列表,[1]=总记录数
	 */
	public static <T> Object[] queryPaged(JdbcTemplate jdbcTemplate, String sql,
			String extraColumns, List<Object> params, RowMapper<T> rm, int page, int size) {
		// fetch total count
		final long[] totalCount = new long[1];
		String tcSql = "select count(*) " + sql.substring(sql.indexOf("from"));
		jdbcTemplate.query(tcSql, params.toArray(), new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				totalCount[0] = rs.getLong(1);
			}
		});

		// query page records
		int firstNum = page * size - size + 1;
		int lastNum = firstNum + size ;

		sql = "select * from (select src.*,rownum rn" + extraColumns + " from (" + sql
				+ ") src ) where rn >= " + firstNum + " and rn < " + lastNum;

		List<T> list = jdbcTemplate.query(sql, params.toArray(), rm);

		return new Object[] { list, totalCount[0] };
	}

	public static <T> Object[] queryPaged(JdbcTemplate jdbcTemplate, String sql,
			List<Object> params, RowMapper<T> rm, int page, int size) {
		return queryPaged(jdbcTemplate, sql, "", params, rm, page, size);
	}

	/**
	 * 作用类似oracle中的decode
	 * 
	 * @author 曾臻
	 * @date 2012-11-20
	 * @param params 键值成对出现，最后一个为默认值（可选）
	 * @return
	 */
	public static String decode(String value, String... params) {
		String defaultValue="";
		int len=params.length;
		if (params.length % 2 != 0){
			defaultValue=params[params.length-1];
			len=params.length-1;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < len; i += 2)
			map.put(params[i], params[i+1]);
		if(map.containsKey(value))
			return map.get(value);
		else return 
				defaultValue;
	}
	
	/**
	 * 将毫秒数转换成timestamp
	 * 0返回null，在设置sql日期型参数时方便用到
	 * @author 曾臻
	 * @date 2012-11-27
	 * @param ms
	 * @return
	 */
	public static Timestamp toTimestamp(long ms){
		if(ms==0)
			return null;
		else
			return  new Timestamp(ms);
	}
	/**
	 * 将timestamp转换成毫秒数
	 * null返回0
	 * @author 曾臻
	 * @date 2012-11-27
	 * @param ts
	 * @return
	 */
	public static long toTimeInMillis(Timestamp ts){
		if(ts==null)
			return 0;
		else
			return ts.getTime();
	}
	
	/**
	 * 调用存储过程
	 * @param jdbcTemplate
	 * @param procedureCallSql
	 * @param paramsList
	 */
	public static void procedureCall(JdbcTemplate jdbcTemplate,final String procedureCallSql,final List<Object> paramsList){

		jdbcTemplate.execute(new ConnectionCallback() {
			@Override
			public Object doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				CallableStatement cstmt = conn.prepareCall(procedureCallSql);
				for(int i=0;i< paramsList.size();i++){
					Object objParam = paramsList.get(i);
					cstmt.setObject((i+1), objParam);
				}
				return cstmt.execute();
			}
		});	
	}
}
