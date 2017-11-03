package cn.ffcs.uom.ftpsyncfile.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.ftpsyncfile.dao.BuildFileSqlDao;

@Repository("buildFileSqlDao")
public class BuildFileSqlDaoImpl extends BaseDaoImpl implements BuildFileSqlDao {

	@Override
	public void createSyncTempTable(final Long treeId, final String lastDate,
			final String thisDate) {
		final String procedureCall = "{call P_UOM_BUILD_OS(?,?,?)}";
		this.getJdbcTemplate().execute(new ConnectionCallback() {
			@Override
			public Object doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				CallableStatement cstmt = conn.prepareCall(procedureCall);
				cstmt.setLong(1, treeId);
				cstmt.setString(2, lastDate);
				cstmt.setString(3, thisDate);
				return cstmt.execute();
			}
		});
	}

}
