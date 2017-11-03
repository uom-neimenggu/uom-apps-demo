/**
 * 
 */
package cn.ffcs.uom.common.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

/**
 * 数组的批量执行设置器。
 * 
 * @author wuzhb
 *
 */
public class ArrayBatchPreparedStatementSetter implements
		BatchPreparedStatementSetter {

	private Object[][] data;
	
	public ArrayBatchPreparedStatementSetter(Object[][] data) {
		this.data = data;
	}
	
	@Override
	public int getBatchSize() {
		return this.data.length;
	}
	
	@Override
	public void setValues(PreparedStatement ps, int rowIndex)
			throws SQLException {
		Object[] rowData = data[rowIndex];
		int length = rowData.length;
		for (int i = 0; i < length; i++) {
			if (rowData[i] instanceof Date) {
				ps.setDate(i + 1, new java.sql.Date(((Date)rowData[i]).getTime()));
			} else {
				ps.setObject(i + 1, rowData[i]);
			}
		}
	}

}
