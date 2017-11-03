package cn.ffcs.uom.common.dao;

import java.io.Serializable;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * @版权：福富软件 版权所有 (c) 2007
 * @文件：com.ffcs.crm.common.dao.hibernate.BaseDaoHibernate.java
 * @所含类：BaseDaoHibernate
 * @author: wuq
 * @version: V1.0
 * @see:
 * @创建日期：2007-9-13
 * @功能说明：
 * @修改记录： =============================================================<br>
 *        日期:2007-9-13 wuq 创建
 *        =============================================================<br>
 */
@SuppressWarnings({ "unchecked" })
public class BaseDaoHibernate extends HibernateDaoSupport implements Dao {
	/**
	 * log Log
	 */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * 强制分页开关,-1为不控制
	 */
	public static int forcePageNum = -1;

	/**
	 * 分页的最大条数，如果传的参数超过该值，则强制设为此值，-1为不控制
	 */
	public static int forceMaxPageNum = 500;

	/**
	 * @param o
	 *            Object
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public void saveObject(final Object o) {
		this.getHibernateTemplate().saveOrUpdate(o);
	}

	/**
	 * @param o
	 *            Object
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public void addObject(final Object o) {
		this.getHibernateTemplate().save(o);
	}

	/**
	 * @param o
	 *            类对象
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public void updateObject(final Object o) {
		this.getHibernateTemplate().update(o);
	}

	/**
	 * @param clazz
	 *            参数类
	 * @param id
	 *            参数类
	 * @return Object
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public Object getObject(final Class clazz, final Serializable id) {
		final Object o = this.getHibernateTemplate().get(clazz, id);

		// if (o == null) {
		// throw new ObjectRetrievalFailureException(clazz, id);
		// }

		return o;
	}

	/**
	 * @param clazz
	 *            参数类
	 * @param obj
	 *            类对象
	 * @return List
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public List getObjects(final Class clazz, final Object obj) {

		// return getHibernateTemplate().loadAll(clazz);
		/*
		 * Remove the line above and uncomment this code block if you want to
		 * use Hibernate's Query by Example API.
		 */

		if (obj == null) {
			return this.getHibernateTemplate().loadAll(clazz);
			// return getHibernateTemplate().find("from "+clazz.getName());
		} else {
			// filter on properties set in the saleTask
			final HibernateCallback callback = new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {
					Example ex = Example.create(obj).excludeNone()
							.excludeZeroes().ignoreCase()
							.enableLike(MatchMode.ANYWHERE);
					return session.createCriteria(obj.getClass()).add(ex)
							.list();
				}
			};
			return (List) this.getHibernateTemplate().execute(callback);
		}

	}

	/**
	 * @param clazz
	 *            参数类
	 * @param id
	 *            参数类
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-9-13 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public void removeObject(final Class clazz, final Serializable id) {
		this.getHibernateTemplate().delete(this.getObject(clazz, id));
	}

	/**
	 * 
	 * @param o
	 *            要删除的象
	 * @author fanggq
	 * 
	 */
	public void removeObject(final Object o) {
		this.getHibernateTemplate().delete(o);
	}

	/**
	 * @param hql
	 *            查询语句
	 * @return List
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-4-13 chenjun 创建方法，并实现其功能 根据HQL语句查询对象集合
	 *        ==============================================================<br>
	 */
	public List findByHql(final String hql) {
		if (BaseDaoHibernate.forcePageNum < 1) {
			// 如果小于1，则不强制进行分页
			return this.getHibernateTemplate().find(hql);
		} else {
			// 如果大于等于1,则默认返回第一页的信息
			return this.findFirstPageInfoByHQLAndParams(hql, null, 1,
					BaseDaoHibernate.forcePageNum).getDataList();
		}
	}

	public Integer executeUpdateByHqlAndparams(final String sqlStr,
			final List params) {
		return (Integer) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final Query query = session.createQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}
						return query.executeUpdate();
					}
				});
	}

	/**
	 * 
	 * 通过jdbc的SQL操作数据
	 * 
	 * @param sql
	 *            jdbcsql
	 * @param params
	 *            参数
	 * @return List
	 * @author: fanggq
	 * @修改记录： ==============================================================<br>
	 *        日期:2010-3-25 fanggq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public Integer executeUpdateByJdbcAndParams(final String sqlStr,
			final List params) {
		return (Integer) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final Query query = session.createSQLQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}
						return query.executeUpdate();
					}
				});
	}

	/**
	 * 根据hibernate的Hql语句和参数查询出分页信息
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param currentPage
	 *            当前页
	 * @param perPageNum
	 *            每页记录数
	 * @return PageInfo
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-3-29 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findPageInfoByHQLAndParams(final String sql,
			final List params, final int currentPage, final int perPageNum) {

		return (PageInfo) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final PageInfo pageInfo = new PageInfo();
						List list = new ArrayList();

						String sqlStr = sql;

						int currentPageNum = currentPage;
						int perPageSize = perPageNum;

						if (BaseDaoHibernate.forceMaxPageNum >= 1) {
							// 如果有强制控制返回的最大分页记录数
							if (perPageSize > BaseDaoHibernate.forceMaxPageNum) {
								perPageSize = BaseDaoHibernate.forceMaxPageNum;
							}
						}

						if (currentPageNum == 0) {
							currentPageNum = 1;
						}

						sqlStr = sqlStr.replaceAll("from ", "FROM ");
						sqlStr = sqlStr.replaceAll("From ", "FROM ");

						final String totalCountSql = "select count(*) "
								+ sqlStr.substring(sqlStr.indexOf("FROM "));

						final Query totalQuery = session
								.createQuery(totalCountSql);
						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {
								final Object param = params.get(k);

								if (param instanceof Integer) {
									totalQuery.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									totalQuery.setString(k, params.get(k)
											.toString());
								}
								if (param instanceof Long) {
									totalQuery.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									totalQuery.setDate(k,
											((Date) params.get(k)));
								}
							}
						}

						final List countList = totalQuery.list();

						// 获取总记录数
						int totalCounts = 0;

						// 以下是处理sql语句中有"group by"的时候取总记录数就是取list的个数
						if (sqlStr.indexOf("Group by") == -1
								&& sqlStr.indexOf("group by") == -1
								&& sqlStr.indexOf("Group By") == -1) {
							totalCounts = ((Long) countList.get(0)).intValue();
						} else {
							totalCounts = list.size();
						}

						if (perPageSize == 0) {
							perPageSize = 10;
						}
						// 计算总页数
						int totalPages = totalCounts / perPageSize;
						totalPages = (totalCounts % perPageSize) > 0 ? (totalPages + 1)
								: totalPages;

						final Query query = session.createQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}

						// 获取开始记录
						final int firstNum = currentPageNum * perPageSize
								- perPageSize;

						query.setFirstResult(firstNum);
						query.setMaxResults(perPageSize);

						// sql=hsql+ " limit " + (pageNo-1)*page_size + ","
						// +page_size;

						list = query.list();

						if (list == null) {
							list = new ArrayList(0);
						}
						// 当取当前页的条数
						// perPageSize=list.size();

						pageInfo.setTotalCount(totalCounts);
						pageInfo.setTotalPageCount(totalPages);
						pageInfo.setCurrentPage(currentPageNum);
						pageInfo.setPerPageCount(perPageSize);
						pageInfo.setDataList(list);

						return pageInfo;
					}
				});
	}

	/**
	 * @param sql
	 *            查询语句
	 * @param currentPage
	 *            当前页
	 * @param perPageNum
	 *            每页记录数
	 * @return PageInfo
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-3-29 chenjun 创建方法， 并实现其功能 根据HQL语句分页查询
	 *        ==============================================================<br>
	 */
	public PageInfo findPageInfoByHQL(final String sql, final int currentPage,
			final int perPageNum) {
		return this.findPageInfoByHQLAndParams(sql, null, currentPage,
				perPageNum);
	}

	/**
	 * 保存对象，可实现对于已有的对象更新，新的对象增加
	 * 
	 * @param o
	 *            对象类
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-4-14 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public void merge(final Object o) {
		this.getHibernateTemplate().merge(o);
	}

	/**
	 * @param entities
	 *            数据集
	 * @author: liujr
	 * @修改记录： ==============================================================<br>
	 *        日期: 2007-4-11 liujr 创建方法，并实现其功能 从数据库删除指定数据集的记录
	 *        ==============================================================<br>
	 */
	public void deleteAll(final Collection entities) {

		this.getHibernateTemplate().deleteAll(entities);

	}

	/**
	 * 根据JDBCSQL语句查询数据
	 * 
	 * @param sql
	 *            查询语句
	 * @return List
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期: 2007-6-8 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public List findListByJDBCSQL(final String sql) {
		if (BaseDaoHibernate.forcePageNum < 1) {
			// 如果小于1，则不强制进行分页
			return (List) this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(final Session session)
								throws HibernateException, SQLException {
							List list = new ArrayList();
							final Query query = session.createSQLQuery(sql);
							list = query.list();
							return list;
						}
					});
		} else {
			// 如果大于等于1,则默认返回第一页的信息
			return this.findFirstPageInfoByJDBCSQLAndParams(sql, null, 1,
					BaseDaoHibernate.forcePageNum, false).getDataList();
		}
	}

	/**
	 * 获取单一值
	 * 
	 * @param sql
	 * @return
	 * @author: zfz
	 * @修改记录： ==============================================================<br>
	 *        日期:2008-6-12 zfz 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSingleValueByJDBCSQL(final String sql) {
		String result = "";
		final List list = this.findListByJDBCSQL(sql);
		if (list != null && list.size() > 0) {
			result = StrUtil.strnull(list.get(0));
		}
		return result;
	}

	/**
	 * 获取单一值
	 * 
	 * @param sql
	 * @return
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:2009-1-5 wuq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSingleValueByJDBCAndParamsSQL(final String sql,
			final List params) {
		String result = "";
		final List list = this.findListByJDBCSQLAndParams(sql, params);
		if (list != null && list.size() > 0) {
			result = StrUtil.strnull(list.get(0));
		}
		return result;
	}

	/**
	 * 获取单一值(获取CLOb的单一值数据,以字符串的格式进行输出)
	 * 
	 * @param sql
	 * @return
	 * @author: zengshl
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-10 zengshl 创建方法，并实现其功能
	 *        ==============================================================<br>
	 * @throws SQLException
	 */
	public String getSingleClobValueByJDBCAndParamsSQL(final String sql,
			final List params) throws SQLException {
		String result = "";
		final List<Object[]> list = this
				.findListByJDBCSQLAndParams(sql, params);
		if (list != null && list.size() > 0) {
			Object objClob = list.get(0);
			Clob clobtmp = (Clob) objClob;
			if (clobtmp == null || clobtmp.length() == 0) {
				result = "";
			} else {
				result = clobtmp.getSubString((long) 1, (int) clobtmp.length());
			}
		}
		return result;
	}

	/**
	 * 根据hql查询唯一值
	 * 
	 * @param hql
	 * @return
	 * @author: zfz
	 * @修改记录： ==============================================================<br>
	 *        日期:Sep 4, 2008 zfz 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSingleValueByHql(final String hql) {
		String result = "";
		final List list = this.findByHql(hql);
		if (list != null && list.size() > 0) {
			result = StrUtil.strnull(list.get(0));
		}
		return result;
	}

	/**
	 * 根据JDBC SQL语句分页查询
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            参数
	 * @param currentPage
	 *            单前页
	 * @param perPageNum
	 *            每页记录数
	 * @param isGroupby
	 *            是否分组查询
	 * @return PageInfo
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-6-8 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findPageInfoByJDBCSQLAndParams(final String sql,
			final List params, final int currentPage, final int perPageNum,
			final boolean isGroupby) {

		return (PageInfo) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final PageInfo pageInfo = new PageInfo();
						List list = new ArrayList();

						String sqlStr = sql;

						int currentPageNum = currentPage;
						int perPageSize = perPageNum;

						if (BaseDaoHibernate.forceMaxPageNum >= 1) {
							// 如果有强制控制返回的最大分页记录数
							if (perPageSize > BaseDaoHibernate.forceMaxPageNum) {
								perPageSize = BaseDaoHibernate.forceMaxPageNum;
							}
						}

						if (currentPageNum == 0) {
							currentPageNum = 1;
						}

						sqlStr = sqlStr.replaceAll("from ", "FROM ");
						sqlStr = sqlStr.replaceAll("From ", "FROM ");

						final String totalCountSql = "select count(*) "
								+ sqlStr.substring(sqlStr.indexOf("FROM "));
						// + sqlStr.substring(sqlStr.lastIndexOf("FROM "));

						final Query totalQuery = session
								.createSQLQuery(totalCountSql);
						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {
								final Object param = params.get(k);

								if (param instanceof Integer) {
									totalQuery.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									totalQuery.setString(k, params.get(k)
											.toString());
								}
								if (param instanceof Long) {
									totalQuery.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									totalQuery.setDate(k,
											((Date) params.get(k)));
								}
							}
						}

						final List countList = totalQuery.list();

						// 获取总记录数
						int totalCounts = 0;

						// 以下是处理sql语句中有"group by"的时候取总记录数就是取list的个数
						if (!isGroupby
								|| (sqlStr.indexOf("Group by") == -1
										&& sqlStr.indexOf("group by") == -1 && sqlStr
										.indexOf("Group By") == -1)) {

							// 防止countList.size=0时越界访问get(0)
							if (countList.size() > 0) {
								final Long l = new Long(countList.get(0)
										.toString());
								totalCounts = l.intValue();
							} else {
								totalCounts = 0;
							}
						} else {
							totalCounts = countList.size();
						}

						if (perPageSize == 0) {
							perPageSize = 10;
						}
						// 计算总页数
						int totalPages = totalCounts / perPageSize;
						totalPages = (totalCounts % perPageSize) > 0 ? (totalPages + 1)
								: totalPages;

						// 获取开始记录
						final int firstNum = currentPageNum * perPageSize
								- perPageSize;

						final int lastNum = firstNum + perPageSize;
						// 组分页的Sql
						sqlStr = "select * from (select src.*,rownum rn from ("
								+ sqlStr;
						sqlStr = sqlStr + ") src ) where rn > " + firstNum
								+ " and rn <= " + lastNum;

						final Query query = session.createSQLQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}

						// query.setFirstResult(firstNum);
						// query.setMaxResults(perPageSize);

						// sql=hsql+ " limit " + (pageNo-1)*page_size + ","
						// +page_size;

						list = query.list();

						if (list == null) {
							list = new ArrayList(0);
						}
						// 当取当前页的条数
						// perPageSize=list.size();

						pageInfo.setTotalCount(totalCounts);
						pageInfo.setTotalPageCount(totalPages);
						pageInfo.setCurrentPage(currentPageNum);
						pageInfo.setPerPageCount(perPageSize);
						pageInfo.setDataList(list);

						return pageInfo;
					}
				});
	}

	/**
	 * 根据JDBC SQL语句分页查询
	 * 
	 * @param sql
	 *            查询语句
	 * @param currentPage
	 *            当前页
	 * @param perPageNum
	 *            每页记录数
	 * @param isGroupby
	 *            是否分组
	 * @return PageInfo
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-3-29 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findPageInfoByJDBCSQL(final String sql,
			final int currentPage, final int perPageNum, final boolean isGroupby) {
		return this.findPageInfoByJDBCSQLAndParams(sql, null, currentPage,
				perPageNum, isGroupby);
	}

	/**
	 * 取SEQ
	 * 
	 * @param seqName
	 *            序列名称
	 * @return String
	 * @author: panchh
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-6-21 panchh 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSeqNextval(final String seqName) {
		String retStr = "0";
		final String sql = "select " + seqName + ".nextval from dual";
		final List list = this.findListByJDBCSQL(sql);
		if (list != null && list.size() > 0) {
			final Object obj = list.get(0);
			retStr = obj.toString();
		}

		return retStr;
	}

	/**
	 * @param sql
	 *            HQL语句
	 * @param params
	 *            参数
	 * @return List
	 * @author: liujr
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-10-29 liujr 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public List findListByHQLAndParams(final String sql, final List params) {
		if (BaseDaoHibernate.forcePageNum < 1) {
			// 如果小于1，则不强制进行分页
			return (List) this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(final Session session)
								throws HibernateException, SQLException {
							List list = new ArrayList();

							String sqlStr = sql;

							sqlStr = sqlStr.replaceAll("from ", "FROM ");
							sqlStr = sqlStr.replaceAll("From ", "FROM ");
							final Query query = session.createQuery(sqlStr);

							// 判断是否有传入参数
							if (params != null) {
								for (int k = 0; k < params.size(); k++) {

									final Object param = params.get(k);

									if (param instanceof Integer) {
										query.setInteger(k, ((Integer) params
												.get(k)).intValue());
									}
									if (param instanceof String) {
										query.setString(k, params.get(k)
												.toString());
									}
									if (param instanceof Long) {
										query.setLong(k, ((Long) params.get(k))
												.longValue());
									}
									if (param instanceof Date) {
										query.setDate(k, ((Date) params.get(k)));
									}
								}
							}

							list = query.list();

							if (list == null) {
								list = new ArrayList(0);
							}

							return list;
						}
					});
		} else {
			// 如果大于等于1,则默认返回第一页的信息
			return this.findFirstPageInfoByHQLAndParams(sql, params, 1,
					BaseDaoHibernate.forcePageNum).getDataList();
		}
	}

	/**
	 * @param sql
	 *            jdbcsql
	 * @param params
	 *            参数
	 * @return List
	 * @author: liujr
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-10-29 liujr 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public List<Object[]> findListByJDBCSQLAndParams(final String sql,
			final List params) {
		if (BaseDaoHibernate.forcePageNum < 1) {
			// 如果小于1，则不强制进行分页
			return (List) this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(final Session session)
								throws HibernateException, SQLException {
							List list = new ArrayList();

							String sqlStr = sql;

							sqlStr = sqlStr.replaceAll("from ", "FROM ");
							sqlStr = sqlStr.replaceAll("From ", "FROM ");
							final Query query = session.createSQLQuery(sqlStr);

							// 判断是否有传入参数
							if (params != null) {
								for (int k = 0; k < params.size(); k++) {

									final Object param = params.get(k);

									if (param instanceof Integer) {
										query.setInteger(k, ((Integer) params
												.get(k)).intValue());
									}
									if (param instanceof String) {
										query.setString(k, params.get(k)
												.toString());
									}
									if (param instanceof Long) {
										query.setLong(k, ((Long) params.get(k))
												.longValue());
									}
									if (param instanceof Date) {
										query.setDate(k, ((Date) params.get(k)));
									}
								}
							}

							list = query.list();

							if (list == null) {
								list = new ArrayList(0);
							}

							return list;
						}
					});
		} else {
			// 如果大于等于1,则默认返回第一页的信息
			return this.findFirstPageInfoByJDBCSQLAndParams(sql, params, 1,
					BaseDaoHibernate.forcePageNum, false).getDataList();
		}
	}

	/**
	 * 获取字段comment
	 * 
	 * @param owner
	 *            表空间所有者
	 * @param tableName
	 *            表名
	 * @param colName
	 *            列名
	 * @return 字段中文名
	 * @throws ManagerException
	 *             ManagerException
	 * @author: panchh
	 * @修改记录： ==============================================================<br>
	 *        日期:Dec 3, 2007 panchh 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getColumnChineseName(final String owner,
			final String tableName, final String colName) {
		String comment = "";
		String sql = "select comments from all_col_comments";
		if (StrUtil.isEmpty(owner)) {
			sql += " where owner = 'CRM' ";
		} else {
			sql += " where owner = '" + owner + "'";
		}
		sql += " and table_name = '" + tableName + "'";
		sql += " and column_name ='" + colName + "'";
		final List list = this.findListByJDBCSQL(sql);
		for (int i = 0; list != null && i < list.size(); i++) {
			final Object obj = list.get(i);
			comment = StrUtil.strnull(obj);
		}
		return comment;
	}

	/**
	 * 获取单一值HQL,绑定变量
	 * 
	 * @param hql
	 * @param params
	 * @return
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2008-12-31 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSingleValueByHqlAndParams(final String hql,
			final List params) {
		String result = "";
		final List list = this.findListByHQLAndParams(hql, params);
		if (list != null && list.size() > 0) {
			result = StrUtil.strnull(list.get(0));
		}
		return result;
	}

	/**
	 * 获取单一值，绑定变量
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @author: yejb
	 * @修改记录： ==============================================================<br>
	 *        日期:2008-12-31 yejb 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public String getSingleValueByJDBCSQLAndParams(final String sql,
			final List params) {
		String result = "";
		final List list = this.findListByJDBCSQLAndParams(sql, params);
		if (list != null && list.size() > 0) {
			result = StrUtil.strnull(list.get(0));
		}
		return result;
	}

	/**
	 * 根据hibernate的Hql语句和参数查询出分页信息
	 * 
	 * @param sql
	 *            查询语句
	 * @param params
	 *            查询参数
	 * @param currentPage
	 *            当前页
	 * @param perPageNum
	 *            每页记录数
	 * @return PageInfo
	 * @author: chenjun
	 * @修改记录： ==============================================================<br>
	 *        日期:2007-3-29 chenjun 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findFirstPageInfoByHQLAndParams(final String sql,
			final List params, final int currentPage, final int perPageNum) {

		return (PageInfo) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final PageInfo pageInfo = new PageInfo();
						List list = new ArrayList();

						final String sqlStr = sql;

						int currentPageNum = currentPage;
						int perPageSize = perPageNum;

						if (BaseDaoHibernate.forceMaxPageNum >= 1) {
							// 如果有强制控制返回的最大分页记录数
							if (perPageSize > BaseDaoHibernate.forceMaxPageNum) {
								perPageSize = BaseDaoHibernate.forceMaxPageNum;
							}
						}

						if (currentPageNum == 0) {
							currentPageNum = 1;
						}

						if (perPageSize == 0) {
							perPageSize = 10;
						}

						final Query query = session.createQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}

						// 获取开始记录
						final int firstNum = currentPageNum * perPageSize
								- perPageSize;

						query.setFirstResult(firstNum);
						query.setMaxResults(perPageSize);

						// sql=hsql+ " limit " + (pageNo-1)*page_size + ","
						// +page_size;

						list = query.list();

						if (list == null) {
							list = new ArrayList(0);
						}
						// 当取当前页的条数
						// perPageSize=list.size();

						// pageInfo.setTotalCount(totalCounts);
						// pageInfo.setTotalPageCount(totalPages);
						// pageInfo.setCurrentPage(currentPageNum);
						// pageInfo.setPerPageCount(perPageSize);
						pageInfo.setDataList(list);

						return pageInfo;
					}
				});
	}

	/**
	 * @param sql
	 * @param params
	 * @param currentPage
	 * @param perPageNum
	 * @param isGroupby
	 * @return
	 * @author: wuq
	 * @修改记录： ==============================================================<br>
	 *        日期:Mar 20, 2009 wuq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findFirstPageInfoByJDBCSQLAndParams(final String sql,
			final List params, final int currentPage, final int perPageNum,
			final boolean isGroupby) {

		return (PageInfo) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final PageInfo pageInfo = new PageInfo();
						List list = new ArrayList();

						final String sqlStr = sql;

						int currentPageNum = currentPage;
						int perPageSize = perPageNum;

						if (BaseDaoHibernate.forceMaxPageNum >= 1) {
							// 如果有强制控制返回的最大分页记录数
							if (perPageSize > BaseDaoHibernate.forceMaxPageNum) {
								perPageSize = BaseDaoHibernate.forceMaxPageNum;
							}
						}

						if (currentPageNum == 0) {
							currentPageNum = 1;
						}

						if (perPageSize == 0) {
							perPageSize = 10;
						}

						final Query query = session.createSQLQuery(sqlStr);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}

						// 获取开始记录
						final int firstNum = currentPageNum * perPageSize
								- perPageSize;

						query.setFirstResult(firstNum);
						query.setMaxResults(perPageSize);

						// sql=hsql+ " limit " + (pageNo-1)*page_size + ","
						// +page_size;

						list = query.list();

						if (list == null) {
							list = new ArrayList(0);
						}
						// 当取当前页的条数
						// perPageSize=list.size();

						// pageInfo.setTotalCount(totalCounts);
						// pageInfo.setTotalPageCount(totalPages);
						// pageInfo.setCurrentPage(currentPageNum);
						// pageInfo.setPerPageCount(perPageSize);
						pageInfo.setDataList(list);

						return pageInfo;
					}
				});
	}

	/**
	 * @param sql
	 * @param zz
	 * @return
	 * @author: nip
	 * @修改记录： ==============================================================<br>
	 *        日期:May 22, 2009 nip 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public List findListByJDBCSQL(final String sql, final Class zz) {

		return (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						List list = new ArrayList();
						final Query query = session.createSQLQuery(sql)
								.addEntity(zz);
						list = query.list();
						return list;
					}
				});
	}

	/**
	 * @param sql
	 * @param params
	 * @param currentPage
	 * @param perPageNum
	 * @param isGroupby
	 * @param zz
	 * @return
	 * @author: nip
	 * @修改记录： ==============================================================<br>
	 *        日期:May 22, 2009 nip 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public PageInfo findPageInfoByJDBCSQLAndParams(final String sql,
			final List params, final int currentPage, final int perPageNum,
			final boolean isGroupby, final Class zz) {

		return (PageInfo) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(final Session session)
							throws HibernateException, SQLException {
						final PageInfo pageInfo = new PageInfo();
						List list = new ArrayList();

						String sqlStr = sql;

						int currentPageNum = currentPage;
						int perPageSize = perPageNum;

						if (currentPageNum == 0) {
							currentPageNum = 1;
						}

						sqlStr = sqlStr.replaceAll("from ", "FROM ");
						sqlStr = sqlStr.replaceAll("From ", "FROM ");

						final String totalCountSql = "select count(*) "
								+ sqlStr.substring(sqlStr.indexOf("FROM "));
						// + sqlStr.substring(sqlStr.lastIndexOf("FROM "));

						final Query totalQuery = session
								.createSQLQuery(totalCountSql);
						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {
								final Object param = params.get(k);

								if (param instanceof Integer) {
									totalQuery.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof Double) {
									totalQuery.setDouble(k, ((Double) params
											.get(k)).doubleValue());
								}
								if (param instanceof String) {
									totalQuery.setString(k, params.get(k)
											.toString());
								}
								if (param instanceof Long) {
									totalQuery.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									totalQuery.setDate(k,
											((Date) params.get(k)));
								}
							}
						}

						final List countList = totalQuery.list();

						// 获取总记录数
						int totalCounts = 0;

						// 以下是处理sql语句中有"group by"的时候取总记录数就是取list的个数
						if (!isGroupby
								|| (sqlStr.indexOf("Group by") == -1
										&& sqlStr.indexOf("group by") == -1 && sqlStr
										.indexOf("Group By") == -1)) {

							// 防止countList.size=0时越界访问get(0)
							if (countList.size() > 0) {
								final Long l = new Long(countList.get(0)
										.toString());
								totalCounts = l.intValue();
							} else {
								totalCounts = 0;
							}
						} else {
							totalCounts = countList.size();
						}

						if (perPageSize == 0) {
							perPageSize = 10;
						}
						// 计算总页数
						int totalPages = totalCounts / perPageSize;
						totalPages = (totalCounts % perPageSize) > 0 ? (totalPages + 1)
								: totalPages;

						final Query query = session.createSQLQuery(sqlStr)
								.addEntity(zz);

						// 判断是否有传入参数
						if (params != null) {
							for (int k = 0; k < params.size(); k++) {

								final Object param = params.get(k);

								if (param instanceof Integer) {
									query.setInteger(k, ((Integer) params
											.get(k)).intValue());
								}
								if (param instanceof Double) {
									query.setDouble(k, ((Double) params.get(k))
											.doubleValue());
								}
								if (param instanceof String) {
									query.setString(k, params.get(k).toString());
								}
								if (param instanceof Long) {
									query.setLong(k,
											((Long) params.get(k)).longValue());
								}
								if (param instanceof Date) {
									query.setDate(k, ((Date) params.get(k)));
								}
							}
						}

						// 获取开始记录
						final int firstNum = currentPageNum * perPageSize
								- perPageSize;

						query.setFirstResult(firstNum);
						query.setMaxResults(perPageSize);

						// sql=hsql+ " limit " + (pageNo-1)*page_size + ","
						// +page_size;

						list = query.list();

						if (list == null) {
							list = new ArrayList(0);
						}
						// 当取当前页的条数
						// perPageSize=list.size();

						pageInfo.setTotalCount(totalCounts);
						pageInfo.setTotalPageCount(totalPages);
						pageInfo.setCurrentPage(currentPageNum);
						pageInfo.setPerPageCount(perPageSize);
						pageInfo.setDataList(list);

						return pageInfo;
					}
				});
	}

	@Resource(name = "hibernateTemplate")
	public void setDaoTemplate(HibernateTemplate hibernateTemplate) {
		super.setHibernateTemplate(hibernateTemplate);
	}

	public HibernateTemplate getDaoTemplate() {
		HibernateTemplate hibernateTemplate = (HibernateTemplate) super
				.getHibernateTemplate();
		if (hibernateTemplate == null) {
			hibernateTemplate = (HibernateTemplate) ApplicationContextUtil
					.getBean("hibernateTemplate");
			super.setHibernateTemplate(hibernateTemplate);
		}
		return hibernateTemplate;
	}
}
