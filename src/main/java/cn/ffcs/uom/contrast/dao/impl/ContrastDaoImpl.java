/**
 * 
 */
package cn.ffcs.uom.contrast.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.contrast.dao.ContrastDao;
import cn.ffcs.uom.contrast.model.Contrast;
import cn.ffcs.uom.contrast.model.StaffContrast;
import cn.ffcs.uom.contrast.model.StaffContrastCrm;

/**
 * 员工对照表接口 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-10-28
 * @功能说明：
 * 
 */
@Repository("contrastDao")
@Transactional
public class ContrastDaoImpl extends BaseDaoImpl implements ContrastDao {

	/**
	 * 分页查询员工对照表信息
	 * 
	 * @param contrast
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByContrast(Contrast contrast, int currentPage,
			int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sb.append(
				"SELECT UOM_STAFF_ID AS uomStaffId,OSS_STAFF_ID AS ossStaffId,IS_TYPE AS isType,UUID AS uuId,")
				.append("STAFF_FIX_ID AS staffFixId,UOM_NBR AS uomNbr,OSS_NBR AS ossNbr,UOM_ACCOUNT AS uomAccount,")
				.append("OSS_ACCOUNT AS ossAccount,FLAG AS flag,OSS_NAME AS ossName,OSS_CERT_NUM AS ossCertNumber,")
				.append("REMARK AS remark,CREATE_DATE AS createDate,CREATE_STAFF AS createStaff FROM TAB_STAFF_ID_REFERENCE WHERE 1=1");

		sql = sql
				.append("SELECT UOM_STAFF_ID AS uomStaffId,OSS_STAFF_ID AS ossStaffId,IS_TYPE AS isType,UUID AS uuId,")
				.append("STAFF_FIX_ID AS staffFixId,UOM_NBR AS uomNbr,OSS_NBR AS ossNbr,UOM_ACCOUNT AS uomAccount,")
				.append("OSS_ACCOUNT AS ossAccount,FLAG AS flag,OSS_NAME AS ossName,OSS_CERT_NUM AS ossCertNumber,")
				.append("REMARK AS remark,CREATE_DATE AS createDate,CREATE_STAFF AS createStaff FROM TAB_STAFF_ID_REFERENCE WHERE 1=1");

		Long uomStaffId = contrast.getUomStaffId();
		if (null != uomStaffId) {
			sb.append(" AND UOM_STAFF_ID= ?");
			sql.append(" AND UOM_STAFF_ID= " + uomStaffId);
			params.add(uomStaffId);

		}

		Long ossStaffId = contrast.getOssStaffId();
		if (null != ossStaffId) {
			sb.append(" AND OSS_STAFF_ID= ?");
			sql.append(" AND OSS_STAFF_ID= " + ossStaffId);
			params.add(ossStaffId);
		}

		String uomNbr = contrast.getUomNbr();
		if (!StrUtil.isNullOrEmpty(uomNbr)) {
			sb.append(" AND UOM_NBR LIKE ?");
			sql.append(" AND UOM_NBR LIKE " + "'%" + StringEscapeUtils.escapeSql(uomNbr) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(uomNbr) + "%");
		}

		String ossNbr = contrast.getOssNbr();
		if (!StrUtil.isNullOrEmpty(ossNbr)) {
			sb.append(" AND OSS_NBR LIKE ?");
			sql.append(" AND OSS_NBR LIKE " + "'%" + StringEscapeUtils.escapeSql(ossNbr) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(ossNbr) + "%");
		}

		String uomAccount = contrast.getUomAccount();
		if (!StrUtil.isNullOrEmpty(uomAccount)) {
			sb.append(" AND UOM_ACCOUNT LIKE ?");
			sql.append(" AND UOM_ACCOUNT LIKE " + "'%" + StringEscapeUtils.escapeSql(uomAccount) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(uomAccount) + "%");
		}

		String ossAccount = contrast.getOssAccount();
		if (!StrUtil.isNullOrEmpty(ossAccount)) {
			sb.append(" AND OSS_ACCOUNT LIKE ?");
			sql.append(" AND OSS_ACCOUNT LIKE " + "'%" + StringEscapeUtils.escapeSql(ossAccount) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(ossAccount) + "%");
		}

		String ossName = contrast.getOssName();
		if (!StrUtil.isNullOrEmpty(ossName)) {
			sb.append(" AND OSS_NAME LIKE ?");
			sql.append(" AND OSS_NAME LIKE " + "'%" + StringEscapeUtils.escapeSql(ossName) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(ossName) + "%");
		}

		String ossCertNumber = contrast.getOssCertNumber();
		if (!StrUtil.isNullOrEmpty(ossCertNumber)) {
			sb.append(" AND OSS_CERT_NUM LIKE ?");
			sql.append(" AND OSS_CERT_NUM LIKE " + "'%" + StringEscapeUtils.escapeSql(ossCertNumber) + "%'");
			params.add("%" + StringEscapeUtils.escapeSql(ossCertNumber) + "%");
		}

		return this.jdbcFindPageInfo(sb.toString(), sql.toString(), params,
				currentPage, pageSize);
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
	public <E> PageInfo jdbcFindPageInfo(String sb, String sql,
			List<Object> params, int currentPageIndex, int itemsPerPage) {
		// 初始化
		if (currentPageIndex == 0) {
			currentPageIndex = 1;
		}
		if (itemsPerPage == 0) {
			itemsPerPage = 10;
		}

		// 列表
		int startRow = (currentPageIndex - 1) * itemsPerPage;
		// List<E> list = this.executeQuery(sql, elementType,
		// params.toArray(new Object[0]), startRow, itemsPerPage);
		List<Contrast> list = this.queryForPageBySql(sql, currentPageIndex,
				itemsPerPage);

		// 总页数
		int totalCounts = this.jdbcGetSize(sb, params);
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

	public List<Contrast> queryForPageBySql(final String sql,
			final int currentPage, final int pageSize) {
		try {
			@SuppressWarnings("unchecked")
			List<Contrast> list = getHibernateTemplate().executeFind(
					new HibernateCallback<Object>() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Query query = session
									.createSQLQuery(sql)
									.addScalar("uomStaffId", Hibernate.LONG)
									.addScalar("ossStaffId", Hibernate.LONG)
									.addScalar("isType", Hibernate.LONG)
									.addScalar("uuId", Hibernate.STRING)
									.addScalar("staffFixId", Hibernate.LONG)
									.addScalar("uomNbr", Hibernate.STRING)
									.addScalar("ossNbr", Hibernate.STRING)
									.addScalar("uomAccount", Hibernate.STRING)
									.addScalar("ossAccount", Hibernate.STRING)
									.addScalar("flag", Hibernate.LONG)
									.addScalar("ossName", Hibernate.STRING)
									.addScalar("ossCertNumber",
											Hibernate.STRING)
									.addScalar("remark", Hibernate.STRING)
									.addScalar("createDate", Hibernate.DATE)
									.addScalar("createStaff", Hibernate.LONG)
									.setResultTransformer(
											Transformers
													.aliasToBean(Contrast.class));
							query.setFirstResult(((currentPage < 1 ? 1
									: currentPage) - 1) * pageSize);
							query.setMaxResults(pageSize < 1 ? 20 : pageSize);
							List<Contrast> lst = query.list();
							session.close();
							return lst;
						}
					});
			return list;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PageInfo queryPageInfoByStaffContrast(
			StaffContrast staffContrastint, int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM TB_STAFF_CONTRAST WHERE 1=1");
		List params = new ArrayList();
		if (staffContrastint != null) {
			if (!StrUtil.isEmpty(staffContrastint.getStaffName())) {
				sb.append(" AND STAFF_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffContrastint.getStaffName()) + "%");
			}
			if (!StrUtil.isEmpty(staffContrastint.getStaffOssAccount())) {
				sb.append(" AND STAFF_OSS_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffContrastint.getStaffOssAccount()) + "%");
			}
			if (!StrUtil.isEmpty(staffContrastint.getStaffOaAccount())) {
				sb.append(" AND STAFF_OA_ACCOUNT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffContrastint.getStaffOaAccount()) + "%");
			}
		}
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, StaffContrast.class);
	}

	/**
	 * 分页查询CRM员工对照表信息
	 * 
	 * @param staffContrastCrm
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryPageInfoByStaffContrastCrm(
			StaffContrastCrm staffContrastCrm, int currentPage, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM STAFF_CONTRAST_CRM WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if (staffContrastCrm != null) {
			if (!StrUtil.isEmpty(staffContrastCrm.getEmpeeAcct())) {
				sb.append(" AND EMPEE_ACCT LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffContrastCrm.getEmpeeAcct()) + "%");
			}
			if (!StrUtil.isEmpty(staffContrastCrm.getCertName())) {
				sb.append(" AND CERT_NAME LIKE ?");
				params.add("%" + StringEscapeUtils.escapeSql(staffContrastCrm.getCertName()) + "%");
			}
		}
		return this.jdbcFindPageInfo(sb.toString(), params, currentPage,
				pageSize, StaffContrastCrm.class);
	}
}









