package cn.ffcs.uom.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * 公共父类dao
 * 
 * @author ZhaoF
 * 
 */
@Repository("baseDao")
public class BaseDaoImpl extends BaseDaoHibernate implements BaseDao {
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * jdbcTemplate
	 */
	@Resource(name = "jdbcTemplate")
	@Getter
	@Setter
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取当前的总条数。
	 * 
	 * @param sql
	 *            原始SQL语句
	 * @param params
	 *            参数列表
	 * @return 条数
	 */
	public int jdbcGetSize(final String sql, final List<Object> params) {
		final String totalCountSql = jdbcGetTotalCountSql(sql);

		Object[] array = null;
		if (params != null) {
			array = new Object[params.size()];
			params.toArray(array);
		}

		return this.getJdbcTemplate().queryForInt(totalCountSql, array);
	}
    /**
     * 获取当前的总条数。
     * 
     * @param sql
     *            原始SQL语句
     * @param params
     *            参数列表
     * @return 条数
     */
    public int jdbcGetSize2(final String sql, final List<Object> params) {
        final String totalCountSql = jdbcGetTotalCountSql2(sql);

        Object[] array = null;
        if (params != null) {
            array = new Object[params.size()];
            params.toArray(array);
        }

        return this.getJdbcTemplate().queryForInt(totalCountSql, array);
    }
	/**
	 * 构造总条数SQL语句。
	 * 
	 * @param sql
	 *            原始SQL语句
	 * @return 总条数SQL语句
	 */
	private String jdbcGetTotalCountSql(final String sql) {
		final StringBuffer buffer = new StringBuffer();
		final String sqlString = sql.trim();
		final int iIndex = (" " + sqlString.toLowerCase()).indexOf(" from ");
		String sqlHeaderString = sqlString.substring(0, iIndex);
		if (sqlHeaderString.toLowerCase().indexOf("distinct") == -1) {
			buffer.append("select count(*) ").append(
					sqlString.substring(iIndex));
		} else {
			buffer.append("select count(*) from (").append(sqlString)
					.append(") ");
		}
		return buffer.toString();
	}
    private String jdbcGetTotalCountSql2(final String sql) {
        final StringBuffer buffer = new StringBuffer();
        final String sqlString = sql.trim();
        final int iIndex = (" " + sqlString.toLowerCase()).indexOf(" from ");
        String sqlHeaderString = sqlString.substring(0, iIndex);
/*      if (sqlHeaderString.toLowerCase().indexOf("distinct") == -1) {
            buffer.append("select count(*) ").append(
                    sqlString.substring(iIndex));
        } else {
            buffer.append("select count(*) from (").append(sqlString)
                    .append(") ");
        }*/
        buffer.append("select count(*) from (").append(sqlString).append(") ");
        return buffer.toString();
    }
	/**
	 * 查询单个对象。
	 * 
	 * @param <E>
	 *            数据对象
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param elementType
	 *            对象类型
	 * @return 对象数据
	 */
	public <E> E jdbcFindObject(String sql, List<?> params, Class<E> elementType) {
		RowMapper<E> rowMapper = ParameterizedBeanPropertyRowMapper
				.newInstance(elementType);
		try {
			return this.getJdbcTemplate().queryForObject(sql,
					params.toArray(new Object[0]), rowMapper);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/**
	 * 获取数据列表。
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param arrayLength
	 *            数据长度
	 * @return 数组列表数据
	 */
	public List<Object[]> jdbcFindArrayList(String sql, List<?> params,
			final int arrayLength) {
		final List<Object[]> data = new ArrayList<Object[]>();
		this.getJdbcTemplate().query(sql, params.toArray(new Object[0]),
				new RowCallbackHandler() {
					public void processRow(ResultSet rs) throws SQLException {
						final List<Object[]> list = data;
						Object[] array = new Object[arrayLength];
						for (int i = 0; i < arrayLength; i++) {
							array[i] = JdbcUtils.getResultSetValue(rs, i + 1);
						}
						list.add(array);
					}
				});
		return data;
	}

	/**
	 * 查询列表数据。
	 * 
	 * @param <E>
	 *            数据对象
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param elementType
	 *            对象类型
	 * @return 列表数据
	 */
	public <E> List<E> jdbcFindList(String sql, List<Object> params,
			Class<E> elementType) {
		String sql4Log = sql;
		for (int i = 0; i < params.size(); i++) {
			sql4Log = sql4Log.replaceFirst("\\?", params.get(i).toString());
		}
		log.debug(sql4Log);
		RowMapper<E> rowMapper = ParameterizedBeanPropertyRowMapper
				.newInstance(elementType);
		return this.getJdbcTemplate().query(sql, params.toArray(new Object[0]),
				rowMapper);
	}

	/**
	 * 查询分页信息。
	 * 
	 * @param <E>
	 *            数据对象
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param currentPageIndex
	 *            当前页码
	 * @param itemsPerPage
	 *            每页条数
	 * @param elementType
	 *            对象类型
	 * @return 分页数据
	 */
	public <E> PageInfo jdbcFindPageInfo(String sql, List<Object> params,
			int currentPageIndex, int itemsPerPage, Class<E> elementType) {
		// 初始化
		if (currentPageIndex == 0) {
			currentPageIndex = 1;
		}
		if (itemsPerPage == 0) {
			itemsPerPage = 10;
		}

		// 列表
		int startRow = (currentPageIndex - 1) * itemsPerPage;
		List<E> list = this.executeQuery(sql, elementType,
				params.toArray(new Object[0]), startRow, itemsPerPage);

		// 总页数
		int totalCounts = this.jdbcGetSize(sql, params);
		int totalPages = totalCounts / itemsPerPage;
		if ((totalCounts % itemsPerPage) > 0) {
			totalPages++;
		}

		// 结果
		PageInfo pageInfo = new PageInfo();
		pageInfo.setTotalCount(totalCounts);
		pageInfo.setTotalPageCount(totalPages);
		pageInfo.setCurrentPage(currentPageIndex);
		pageInfo.setPerPageCount(itemsPerPage);
		pageInfo.setDataList(list);
		return pageInfo;
	}
    /**
     * 查询分页信息。
     * 
     * @param <E>
     *            数据对象
     * @param sql
     *            查询语句
     * @param params
     *            查询参数
     * @param currentPageIndex
     *            当前页码
     * @param itemsPerPage
     *            每页条数
     * @param elementType
     *            对象类型
     * @return 分页数据
     */
    public <E> PageInfo jdbcFindPageInfo2(String sql, List<Object> params,
            int currentPageIndex, int itemsPerPage, Class<E> elementType) {
        // 初始化
        if (currentPageIndex == 0) {
            currentPageIndex = 1;
        }
        if (itemsPerPage == 0) {
            itemsPerPage = 10;
        }

        // 列表
        int startRow = (currentPageIndex - 1) * itemsPerPage;
        List<E> list = this.executeQuery(sql, elementType,
                params.toArray(new Object[0]), startRow, itemsPerPage);

        // 总页数
        int totalCounts = this.jdbcGetSize2(sql, params);
        int totalPages = totalCounts / itemsPerPage;
        if ((totalCounts % itemsPerPage) > 0) {
            totalPages++;
        }

        // 结果
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTotalCount(totalCounts);
        pageInfo.setTotalPageCount(totalPages);
        pageInfo.setCurrentPage(currentPageIndex);
        pageInfo.setPerPageCount(itemsPerPage);
        pageInfo.setDataList(list);
        return pageInfo;
    }
	@SuppressWarnings("unchecked")
	private <E> List<E> executeQuery(final String sql,
			final Class<E> elementType, final Object[] params,
			final int startRow, final int itemsPerPage) {
		final List<E> data = new ArrayList<E>();
		final RowMapper<E> rowMapper = ParameterizedBeanPropertyRowMapper
				.newInstance(elementType);
		this.getJdbcTemplate().query(sql, params, new ResultSetExtractor() {
			public Object extractData(final ResultSet rs) throws SQLException {
				final List pageItems = data;
				int currentRow = 0;
				int lastRow = startRow + itemsPerPage;
				while (rs.next() && (lastRow == 0 || currentRow < lastRow)) {
					if (currentRow >= startRow) {
						pageItems.add(rowMapper.mapRow(rs, currentRow));
					}
					currentRow++;
				}
				return data;
			}
		});
		return data;
	}

	@Override
	public long jdbcGetSeqNextval(String seqName) {
		final String sql = "select " + seqName + ".nextval from dual";
		final long seq = this.getJdbcTemplate().queryForLong(sql);
		return seq;
	}

	@Override
	public String genTransId(String prefix, final int length, String seqName) {
		if (prefix == null) {
			prefix = "";
		}
		if (StrUtil.isEmpty(seqName)) {
			seqName = "SEQ_INTF_TRANS";
		}
		final long seq = this.jdbcGetSeqNextval(seqName);
		final String strSeq = StrUtil.padLeading(String.valueOf(seq), length
				- prefix.length(), "0");

		return prefix + strSeq;
	}
}
