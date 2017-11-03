package cn.ffcs.uom.common.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.ffcs.uom.common.vo.PageInfo;

/**
 * jdbc定义的接口
 * 
 * @author ZhaoF
 * 
 */
public interface BaseDao extends Dao {
	/**
	 * 获取JdbcTemplate
	 * 
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate();

	/**
	 * 获取当前的总条数。
	 * 
	 * @param sql
	 *            原始SQL语句
	 * @param params
	 *            参数列表
	 * @return 条数
	 */
	public abstract int jdbcGetSize(final String sql, final List<Object> params);

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
	public abstract <E> E jdbcFindObject(String sql, List<?> params,
			Class<E> elementType);

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
	public abstract List<Object[]> jdbcFindArrayList(String sql,
			List<?> params, final int arrayLength);

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
	public abstract <E> List<E> jdbcFindList(String sql, List<Object> params,
			Class<E> elementType);

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
	public abstract <E> PageInfo jdbcFindPageInfo(String sql,
			List<Object> params, int currentPageIndex, int itemsPerPage,
			Class<E> elementType);

	/**
	 * 获取sequence
	 * 
	 * @param sql
	 * @return
	 */
	public long jdbcGetSeqNextval(final String seqName);

	/**
	 * 获取流水号
	 * 
	 * @param prefix
	 *            前缀
	 * @param length
	 *            总长度
	 * @param seqName
	 *            序列号名称
	 * @return
	 */
	public String genTransId(String prefix, final int length, String seqName);
}
