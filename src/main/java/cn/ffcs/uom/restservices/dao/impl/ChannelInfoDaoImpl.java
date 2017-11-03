package cn.ffcs.uom.restservices.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.restservices.model.GrpBusiStore;
import cn.ffcs.uom.restservices.model.GrpBusiStoreAttr;
import cn.ffcs.uom.restservices.model.GrpChannel;
import cn.ffcs.uom.restservices.model.GrpChannelAttr;
import cn.ffcs.uom.restservices.model.GrpChannelBusiStoreRela;
import cn.ffcs.uom.restservices.model.GrpChannelCustomAttr;
import cn.ffcs.uom.restservices.model.GrpChannelOperatorsRela;
import cn.ffcs.uom.restservices.model.GrpChannelRela;
import cn.ffcs.uom.restservices.model.GrpOperators;
import cn.ffcs.uom.restservices.model.GrpOperatorsAttr;
import cn.ffcs.uom.restservices.model.GrpOperatorsCustomAttr;
import cn.ffcs.uom.restservices.model.GrpStaff;
import cn.ffcs.uom.restservices.model.GrpStaffAttr;
import cn.ffcs.uom.restservices.model.GrpStaffChannelRela;
import cn.ffcs.uom.restservices.model.GrpStaffCustomAttr;

@Repository("channelInfoDao")
public class ChannelInfoDaoImpl extends BaseDaoImpl implements ChannelInfoDao {

	/**
	 * 根据经营主体编码查询经营主体
	 * 
	 * @param operatorsNbr
	 */
	@Override
	public GrpOperators queryGrpOperatorsByOperatorsNbr(String operatorsNbr) {
		if (!StrUtil.isNullOrEmpty(operatorsNbr)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM GRP_OPERATORS T WHERE T.OPERATORS_NBR = ?";
			params.add(operatorsNbr);
			GrpOperators grpOperators = jdbcFindObject(sql, params,
					GrpOperators.class);
			return grpOperators;
		}
		return null;
	}

	/**
	 * 根据经营场所编码查询经营场所
	 * 
	 * @param busiStoreNbr
	 */
	@Override
	public GrpBusiStore queryGrpBusiStoreByBusiStoreNbr(String busiStoreNbr) {
		if (!StrUtil.isNullOrEmpty(busiStoreNbr)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM GRP_BUSI_STORE T WHERE T.BUSI_STORE_NBR = ?";
			params.add(busiStoreNbr);
			GrpBusiStore grpBusiStore = jdbcFindObject(sql, params,
					GrpBusiStore.class);
			return grpBusiStore;
		}
		return null;
	}

	/**
	 * 查询经营主体信息
	 * 
	 * @param grpOperators
	 * @return
	 */
	@Override
	public List<GrpOperators> queryGrpOperatorsList(GrpOperators grpOperators) {

		if (grpOperators != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_OPERATORS WHERE STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpOperators.getGrpOperatorsId())) {
				sql.append(" AND GRP_OPERATORS_ID = ?");
				params.add(grpOperators.getGrpOperatorsId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsNbr())) {
				sql.append(" AND OPERATORS_NBR = ?");
				params.add(grpOperators.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsName())) {
				sql.append(" AND OPERATORS_NAME LIKE ? ");
				params.add("%" + grpOperators.getOperatorsName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCertType())) {
				sql.append(" AND CERT_TYPE = ?");
				params.add(grpOperators.getCertType());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCertNbr())) {
				sql.append(" AND CERT_NBR = ?");
				params.add(grpOperators.getCertNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsSname())) {
				sql.append(" AND OPERATORS_SNAME LIKE ?");
				params.add("%" + grpOperators.getOperatorsSname() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getLegalRepr())) {
				sql.append(" AND LEGAL_REPR LIKE ?");
				params.add("%" + grpOperators.getLegalRepr() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getTelephone())) {
				sql.append(" AND TELEPHONE LIKE ?");
				params.add("%" + grpOperators.getTelephone() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getContact())) {
				sql.append(" AND CONTACT = ?");
				params.add(grpOperators.getContact());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getEmail())) {
				sql.append(" AND EMAIL LIKE ?");
				params.add("%" + grpOperators.getEmail() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsAreaGrade())) {
				sql.append(" AND OPERATORS_AREA_GRADE = ?");
				params.add(grpOperators.getOperatorsAreaGrade());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getParentOperNbr())) {
				sql.append(" AND PARENT_OPER_NBR = ?");
				params.add(grpOperators.getParentOperNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOrgId())) {
				sql.append(" AND ORG_ID = ?");
				params.add(grpOperators.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCommonRegionId())) {
				sql.append(" AND COMMON_REGION_ID = ?");
				params.add(grpOperators.getCommonRegionId());
			}

			sql.append(" ORDER BY GRP_OPERATORS_ID DESC");

			return super.jdbcFindList(sql.toString(), params,
					GrpOperators.class);

		}

		return null;

	}

	/**
	 * 分面查询经营主体信息
	 * 
	 * @param grpOperators
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpOperators(GrpOperators grpOperators,
			int currentPage, int pageSize) {

		if (grpOperators != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT o.* FROM GRP_OPERATORS o WHERE o.STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpOperators.getGrpOperatorsId())) {
				sql.append(" AND o.GRP_OPERATORS_ID = ?");
				params.add(grpOperators.getGrpOperatorsId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsNbr())) {
				sql.append(" AND o.OPERATORS_NBR = ?");
				params.add(grpOperators.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsName())) {
				sql.append(" AND o.OPERATORS_NAME LIKE ? ");
				params.add("%" + grpOperators.getOperatorsName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCertType())) {
				sql.append(" AND o.CERT_TYPE = ?");
				params.add(grpOperators.getCertType());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCertNbr())) {
				sql.append(" AND o.CERT_NBR = ?");
				params.add(grpOperators.getCertNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsSname())) {
				sql.append(" AND o.OPERATORS_SNAME LIKE ?");
				params.add("%" + grpOperators.getOperatorsSname() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getLegalRepr())) {
				sql.append(" AND o.LEGAL_REPR LIKE ?");
				params.add("%" + grpOperators.getLegalRepr() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getTelephone())) {
				sql.append(" AND o.TELEPHONE LIKE ?");
				params.add("%" + grpOperators.getTelephone() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getContact())) {
				sql.append(" AND o.CONTACT = ?");
				params.add(grpOperators.getContact());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getEmail())) {
				sql.append(" AND o.EMAIL LIKE ?");
				params.add("%" + grpOperators.getEmail() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOperatorsAreaGrade())) {
				sql.append(" AND o.OPERATORS_AREA_GRADE = ?");
				params.add(grpOperators.getOperatorsAreaGrade());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getParentOperNbr())) {
				sql.append(" AND o.PARENT_OPER_NBR = ?");
				params.add(grpOperators.getParentOperNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getOrgId())) {
				sql.append(" AND o.ORG_ID = ?");
				params.add(grpOperators.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperators.getCommonRegionId())) {
				sql.append(" AND o.COMMON_REGION_ID = ?");
				params.add(grpOperators.getCommonRegionId());
			}

			sql.append(" ORDER BY o.GRP_OPERATORS_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpOperators.class);

		}

		return null;

	}

	/**
	 * 根据渠道编码查询渠道
	 * 
	 * @param channelNbr
	 */
	@Override
	public GrpChannel queryGrpChannelByChannelNbr(String channelNbr) {
		if (!StrUtil.isNullOrEmpty(channelNbr)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM GRP_CHANNEL T WHERE T.CHANNEL_NBR = ?";
			params.add(channelNbr);
			GrpChannel grpChannel = jdbcFindObject(sql, params,
					GrpChannel.class);
			return grpChannel;
		}
		return null;
	}

	/**
	 * 查询渠道信息
	 * 
	 * @param grpChannel
	 * @return
	 */
	@Override
	public List<GrpChannel> queryGrpChannelList(GrpChannel grpChannel) {

		if (grpChannel != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_CHANNEL WHERE STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpChannel.getGrpChannelId())) {
				sql.append(" AND GRP_CHANNEL_ID = ?");
				params.add(grpChannel.getGrpChannelId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpChannel.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelName())) {
				sql.append(" AND CHANNEL_NAME LIKE ? ");
				params.add("%" + grpChannel.getChannelName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelClass())) {
				sql.append(" AND CHANNEL_CLASS = ?");
				params.add(grpChannel.getChannelClass());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelTypeCd())) {
				sql.append(" AND CHANNEL_TYPE_CD = ?");
				params.add(grpChannel.getChannelTypeCd());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getOrgId())) {
				sql.append(" AND ORG_ID = ?");
				params.add(grpChannel.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getCommonRegionId())) {
				sql.append(" AND COMMON_REGION_ID = ?");
				params.add(grpChannel.getCommonRegionId());
			}

			sql.append(" ORDER BY GRP_CHANNEL_ID");

			return super.jdbcFindList(sql.toString(), params, GrpChannel.class);

		}

		return null;

	}

	/**
	 * 分页查询渠道信息
	 * 
	 * @param grpChannel
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpChannel(GrpChannel grpChannel,
			int currentPage, int pageSize) {

		if (grpChannel != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT c.* FROM GRP_CHANNEL c WHERE c.STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpChannel.getGrpChannelId())) {
				sql.append(" AND c.GRP_CHANNEL_ID = ?");
				params.add(grpChannel.getGrpChannelId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelNbr())) {
				sql.append(" AND c.CHANNEL_NBR = ?");
				params.add(grpChannel.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelName())) {
				sql.append(" AND c.CHANNEL_NAME LIKE ? ");
				params.add("%" + grpChannel.getChannelName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelClass())) {
				sql.append(" AND c.CHANNEL_CLASS = ?");
				params.add(grpChannel.getChannelClass());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getChannelTypeCd())) {
				sql.append(" AND c.CHANNEL_TYPE_CD = ?");
				params.add(grpChannel.getChannelTypeCd());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getOrgId())) {
				sql.append(" AND c.ORG_ID = ?");
				params.add(grpChannel.getOrgId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannel.getCommonRegionId())) {
				sql.append(" AND c.COMMON_REGION_ID = ?");
				params.add(grpChannel.getCommonRegionId());
			}

			sql.append(" ORDER BY c.GRP_CHANNEL_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpChannel.class);

		}

		return null;

	}

	/**
	 * 根据员工编码查询员工
	 * 
	 * @param salesCode
	 */
	@Override
	public GrpStaff queryGrpStaffBySalesCode(String salesCode) {
		if (!StrUtil.isNullOrEmpty(salesCode)) {
			List<Object> params = new ArrayList<Object>();
			String sql = "SELECT T.* FROM GRP_STAFF T WHERE T.SALES_CODE = ?";
			params.add(salesCode);
			GrpStaff grpStaff = jdbcFindObject(sql, params, GrpStaff.class);
			return grpStaff;
		}
		return null;
	}

	/**
	 * 查询员工信息
	 * 
	 * @param grpStaff
	 * @return
	 */
	@Override
	public List<GrpStaff> queryGrpStaffList(GrpStaff grpStaff) {

		if (grpStaff != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_STAFF WHERE STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpStaff.getGrpStaffId())) {
				sql.append(" AND GRP_STAFF_ID = ?");
				params.add(grpStaff.getGrpStaffId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getSalesCode())) {
				sql.append(" AND SALES_CODE = ?");
				params.add(grpStaff.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getStaffCode())) {
				sql.append(" AND STAFF_CODE = ? ");
				params.add(grpStaff.getStaffCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getStaffName())) {
				sql.append(" AND STAFF_NAME LIKE ?");
				params.add("%" + grpStaff.getStaffName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getCertNumber())) {
				sql.append(" AND CERT_NUMBER LIKE ?");
				params.add("%" + grpStaff.getCertNumber() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getMobilePhone())) {
				sql.append(" AND MOBILE_PHONE LIKE ?");
				params.add("%" + grpStaff.getMobilePhone() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getEmail())) {
				sql.append(" AND E_MAIL LIKE ?");
				params.add("%" + grpStaff.getEmail() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getOrgId())) {
				sql.append(" AND ORG_ID = ?");
				params.add(grpStaff.getOrgId());
			}

			sql.append(" ORDER BY GRP_STAFF_ID");

			return super.jdbcFindList(sql.toString(), params, GrpStaff.class);

		}

		return null;

	}

	/**
	 * 查询员工信息
	 * 
	 * @param grpStaff
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpStaff(GrpStaff grpStaff, int currentPage,
			int pageSize) {
		if (grpStaff != null) {
			StringBuffer sql = new StringBuffer(
					"SELECT A.* FROM GRP_STAFF A,GRP_STAFF_CUSTOM_ATTR B");
			sql.append(" WHERE A.SALES_CODE = B.SALES_CODE AND A.STATUS_CD NOT IN (?,?,?) AND B.ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			String groupChannelInfoAttrId = UomClassProvider
					.getSystemConfig("groupChannelInfoAttrId");

			String groupChannelInfoAttrValue = UomClassProvider
					.getSystemConfig("groupChannelInfoAttrValue");

			if (!StrUtil.isEmpty(groupChannelInfoAttrId)
					&& !StrUtil.isEmpty(groupChannelInfoAttrValue)) {
				sql.append(" AND B.ATTR_ID = ? AND B.ATTR_VALUE = ?");
				params.add(groupChannelInfoAttrId);
				params.add(groupChannelInfoAttrValue);
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getGrpStaffId())) {
				sql.append(" AND A.GRP_STAFF_ID = ?");
				params.add(grpStaff.getGrpStaffId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getSalesCode())) {
				sql.append(" AND A.SALES_CODE = ?");
				params.add(grpStaff.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getStaffCode())) {
				sql.append(" AND A.STAFF_CODE = ? ");
				params.add(grpStaff.getStaffCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getStaffName())) {
				sql.append(" AND A.STAFF_NAME LIKE ?");
				params.add("%" + grpStaff.getStaffName() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getCertNumber())) {
				sql.append(" AND A.CERT_NUMBER LIKE ?");
				params.add("%" + grpStaff.getCertNumber() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getMobilePhone())) {
				sql.append(" AND A.MOBILE_PHONE LIKE ?");
				params.add("%" + grpStaff.getMobilePhone() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getEmail())) {
				sql.append(" AND A.E_MAIL LIKE ?");
				params.add("%" + grpStaff.getEmail() + "%");
			}

			if (!StrUtil.isNullOrEmpty(grpStaff.getOrgId())) {
				sql.append(" AND A.ORG_ID = ?");
				params.add(grpStaff.getOrgId());
			}

			sql.append(" ORDER BY A.GRP_STAFF_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpStaff.class);

		}

		return null;

	}

	/**
	 * 根据经营主体扩展属性查询经营主体扩展属性
	 * 
	 * @param grpOperatorsAttr
	 */
	@Override
	public GrpOperatorsAttr queryGrpOperatorsAttr(
			GrpOperatorsAttr grpOperatorsAttr) {

		if (!StrUtil.isNullOrEmpty(grpOperatorsAttr)
				&& !StrUtil.isNullOrEmpty(grpOperatorsAttr.getOperatorsNbr())
				&& !StrUtil.isNullOrEmpty(grpOperatorsAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_OPERATORS_ATTR WHERE OPERATORS_NBR = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpOperatorsAttr.getOperatorsNbr());
			params.add(grpOperatorsAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpOperatorsAttr.class);

		}

		return null;
	}

	/**
	 * 查询经营主体扩展属性信息
	 * 
	 * @param grpOperatorsAttr
	 * @return
	 */
	@Override
	public List<GrpOperatorsAttr> queryGrpOperatorsAttrList(
			GrpOperatorsAttr grpOperatorsAttr) {

		if (grpOperatorsAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_OPERATORS_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil
					.isNullOrEmpty(grpOperatorsAttr.getGrpOperatorsAttrId())) {
				sql.append(" AND GRP_OPERATORS_ATTR_ID = ?");
				params.add(grpOperatorsAttr.getGrpOperatorsAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperatorsAttr.getOperatorsNbr())) {
				sql.append(" AND OPERATORS_NBR = ?");
				params.add(grpOperatorsAttr.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperatorsAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpOperatorsAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperatorsAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpOperatorsAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_OPERATORS_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpOperatorsAttr.class);

		}

		return null;

	}

	/**
	 * 根据经营主体自定义扩展属性查询经营主体扩展属性
	 * 
	 * @param grpOperatorsCustomAttr
	 */
	@Override
	public GrpOperatorsCustomAttr queryGrpOperatorsCustomAttr(
			GrpOperatorsCustomAttr grpOperatorsCustomAttr) {

		if (!StrUtil.isNullOrEmpty(grpOperatorsCustomAttr)
				&& !StrUtil.isNullOrEmpty(grpOperatorsCustomAttr
						.getOperatorsNbr())
				&& !StrUtil.isNullOrEmpty(grpOperatorsCustomAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_OPERATORS_CUSTOM_ATTR WHERE OPERATORS_NBR = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpOperatorsCustomAttr.getOperatorsNbr());
			params.add(grpOperatorsCustomAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpOperatorsCustomAttr.class);

		}

		return null;
	}

	/**
	 * 查询经营主体扩展属性信息
	 * 
	 * @param grpOperatorsCustomAttr
	 * @return
	 */
	@Override
	public List<GrpOperatorsCustomAttr> queryGrpOperatorsCustomAttrList(
			GrpOperatorsCustomAttr grpOperatorsCustomAttr) {

		if (grpOperatorsCustomAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_OPERATORS_CUSTOM_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpOperatorsCustomAttr
					.getGrpOperatorsCustomAttrId())) {
				sql.append(" AND GRP_OPERATORS_CUSTOM_ATTR_ID = ?");
				params.add(grpOperatorsCustomAttr.getGrpOperatorsCustomAttrId());
			}

			if (!StrUtil
					.isNullOrEmpty(grpOperatorsCustomAttr.getOperatorsNbr())) {
				sql.append(" AND OPERATORS_NBR = ?");
				params.add(grpOperatorsCustomAttr.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpOperatorsCustomAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpOperatorsCustomAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpOperatorsCustomAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpOperatorsCustomAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_OPERATORS_CUSTOM_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpOperatorsCustomAttr.class);

		}

		return null;

	}

	/**
	 * 根据经营场所扩展属性查询经营场所扩展属性
	 * 
	 * @param grpBusiStoreAttr
	 */
	@Override
	public GrpBusiStoreAttr queryGrpBusiStoreAttr(
			GrpBusiStoreAttr grpBusiStoreAttr) {

		if (!StrUtil.isNullOrEmpty(grpBusiStoreAttr)
				&& !StrUtil.isNullOrEmpty(grpBusiStoreAttr.getBusiStoreNbr())
				&& !StrUtil.isNullOrEmpty(grpBusiStoreAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_BUSI_STORE_ATTR WHERE BUSI_STORE_NBR = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpBusiStoreAttr.getBusiStoreNbr());
			params.add(grpBusiStoreAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpBusiStoreAttr.class);

		}

		return null;
	}

	/**
	 * 查询经营场所扩展属性信息
	 * 
	 * @param grpBusiStoreAttr
	 * @return
	 */
	@Override
	public List<GrpBusiStoreAttr> queryGrpBusiStoreAttrList(
			GrpBusiStoreAttr grpBusiStoreAttr) {

		if (grpBusiStoreAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_BUSI_STORE_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil
					.isNullOrEmpty(grpBusiStoreAttr.getGrpBusiStoreAttrId())) {
				sql.append(" AND GRP_BUSI_STORE_ATTR_ID = ?");
				params.add(grpBusiStoreAttr.getGrpBusiStoreAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpBusiStoreAttr.getBusiStoreNbr())) {
				sql.append(" AND BUSI_STORE_NBR = ?");
				params.add(grpBusiStoreAttr.getBusiStoreNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpBusiStoreAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpBusiStoreAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpBusiStoreAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpBusiStoreAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_BUSI_STORE_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpBusiStoreAttr.class);

		}

		return null;

	}

	/**
	 * 根据销售点扩展属性查询销售点扩展属性
	 * 
	 * @param grpChannelAttr
	 */
	@Override
	public GrpChannelAttr queryGrpChannelAttr(GrpChannelAttr grpChannelAttr) {

		if (!StrUtil.isNullOrEmpty(grpChannelAttr)
				&& !StrUtil.isNullOrEmpty(grpChannelAttr.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_CHANNEL_ATTR WHERE CHANNEL_NBR = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpChannelAttr.getChannelNbr());
			params.add(grpChannelAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpChannelAttr.class);

		}

		return null;
	}

	/**
	 * 查询销售点扩展属性信息
	 * 
	 * @param grpChannelAttr
	 * @return
	 */
	@Override
	public List<GrpChannelAttr> queryGrpChannelAttrList(
			GrpChannelAttr grpChannelAttr) {

		if (grpChannelAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_CHANNEL_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpChannelAttr.getGrpChannelAttrId())) {
				sql.append(" AND GRP_CHANNEL_ATTR_ID = ?");
				params.add(grpChannelAttr.getGrpChannelAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelAttr.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpChannelAttr.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpChannelAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpChannelAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_CHANNEL_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpChannelAttr.class);

		}

		return null;

	}

	/**
	 * 根据销售点自定义扩展属性查询销售点扩展属性
	 * 
	 * @param grpChannelCustomAttr
	 */
	@Override
	public GrpChannelCustomAttr queryGrpChannelCustomAttr(
			GrpChannelCustomAttr grpChannelCustomAttr) {

		if (!StrUtil.isNullOrEmpty(grpChannelCustomAttr)
				&& !StrUtil.isNullOrEmpty(grpChannelCustomAttr.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelCustomAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_CHANNEL_CUSTOM_ATTR WHERE CHANNEL_NBR = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpChannelCustomAttr.getChannelNbr());
			params.add(grpChannelCustomAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpChannelCustomAttr.class);

		}

		return null;
	}

	/**
	 * 查询销售点自定义扩展属性信息
	 * 
	 * @param grpChannelCustomAttr
	 * @return
	 */
	@Override
	public List<GrpChannelCustomAttr> queryGrpChannelCustomAttrList(
			GrpChannelCustomAttr grpChannelCustomAttr) {

		if (grpChannelCustomAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_CHANNEL_CUSTOM_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpChannelCustomAttr
					.getGrpChannelCustomAttrId())) {
				sql.append(" AND GRP_CHANNEL_CUSTOM_ATTR_ID = ?");
				params.add(grpChannelCustomAttr.getGrpChannelCustomAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelCustomAttr.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpChannelCustomAttr.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelCustomAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpChannelCustomAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelCustomAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpChannelCustomAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_CHANNEL_CUSTOM_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpChannelCustomAttr.class);

		}

		return null;

	}

	/**
	 * 根据员工扩展属性查询员工扩展属性
	 * 
	 * @param grpStaffAttr
	 */
	@Override
	public GrpStaffAttr queryGrpStaffAttr(GrpStaffAttr grpStaffAttr) {

		if (!StrUtil.isNullOrEmpty(grpStaffAttr)
				&& !StrUtil.isNullOrEmpty(grpStaffAttr.getSalesCode())
				&& !StrUtil.isNullOrEmpty(grpStaffAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_STAFF_ATTR WHERE SALES_CODE = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpStaffAttr.getSalesCode());
			params.add(grpStaffAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpStaffAttr.class);

		}

		return null;
	}

	/**
	 * 查询员工扩展属性信息
	 * 
	 * @param grpStaffAttr
	 * @return
	 */
	@Override
	public List<GrpStaffAttr> queryGrpStaffAttrList(GrpStaffAttr grpStaffAttr) {

		if (grpStaffAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_STAFF_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpStaffAttr.getGrpStaffAttrId())) {
				sql.append(" AND GRP_STAFF_ATTR_ID = ?");
				params.add(grpStaffAttr.getGrpStaffAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffAttr.getSalesCode())) {
				sql.append(" AND SALES_CODE = ?");
				params.add(grpStaffAttr.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpStaffAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpStaffAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_STAFF_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpStaffAttr.class);

		}

		return null;

	}

	/**
	 * 根据员工自定义扩展属性查询员工扩展属性
	 * 
	 * @param grpStaffCustomAttr
	 */
	@Override
	public GrpStaffCustomAttr queryGrpStaffCustomAttr(
			GrpStaffCustomAttr grpStaffCustomAttr) {

		if (!StrUtil.isNullOrEmpty(grpStaffCustomAttr)
				&& !StrUtil.isNullOrEmpty(grpStaffCustomAttr.getSalesCode())
				&& !StrUtil.isNullOrEmpty(grpStaffCustomAttr.getAttrId())) {

			String sql = "SELECT * FROM GRP_STAFF_CUSTOM_ATTR WHERE SALES_CODE = ? AND ATTR_ID = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpStaffCustomAttr.getSalesCode());
			params.add(grpStaffCustomAttr.getAttrId());

			return jdbcFindObject(sql, params, GrpStaffCustomAttr.class);

		}

		return null;
	}

	/**
	 * 查询员工扩展属性信息
	 * 
	 * @param grpStaffCustomAttr
	 * @return
	 */
	@Override
	public List<GrpStaffCustomAttr> queryGrpStaffCustomAttrList(
			GrpStaffCustomAttr grpStaffCustomAttr) {

		if (grpStaffCustomAttr != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_STAFF_CUSTOM_ATTR WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpStaffCustomAttr
					.getGrpStaffCustomAttrId())) {
				sql.append(" AND GRP_STAFF_CUSTOM_ATTR_ID = ?");
				params.add(grpStaffCustomAttr.getGrpStaffCustomAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffCustomAttr.getSalesCode())) {
				sql.append(" AND SALES_CODE = ?");
				params.add(grpStaffCustomAttr.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffCustomAttr.getAttrId())) {
				sql.append(" AND ATTR_ID = ? ");
				params.add(grpStaffCustomAttr.getAttrId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffCustomAttr.getAttrValue())) {
				sql.append(" AND ATTR_VALUE = ?");
				params.add(grpStaffCustomAttr.getAttrValue());
			}

			sql.append(" ORDER BY GRP_STAFF_CUSTOM_ATTR_ID");

			return super.jdbcFindList(sql.toString(), params,
					GrpStaffCustomAttr.class);

		}

		return null;

	}

	/**
	 * 查询渠道经营主体关系
	 * 
	 * @param grpChannelOperatorsRela
	 */
	@Override
	public GrpChannelOperatorsRela queryGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela) {

		if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela)
				&& !StrUtil.isNullOrEmpty(grpChannelOperatorsRela
						.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelOperatorsRela
						.getOperatorsNbr())
				&& !StrUtil
						.isNullOrEmpty(grpChannelOperatorsRela.getRelaType())) {

			String sql = "SELECT * FROM GRP_CHANNEL_OPERATORS_RELA WHERE CHANNEL_NBR = ? AND OPERATORS_NBR = ? AND RELA_TYPE = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpChannelOperatorsRela.getChannelNbr());
			params.add(grpChannelOperatorsRela.getOperatorsNbr());
			params.add(grpChannelOperatorsRela.getRelaType());

			return jdbcFindObject(sql, params, GrpChannelOperatorsRela.class);

		}

		return null;
	}

	/**
	 * 查询渠道经营场所关系
	 * 
	 * @param grpChannelBusiStoreRela
	 */
	@Override
	public GrpChannelBusiStoreRela queryGrpChannelBusiStoreRela(
			GrpChannelBusiStoreRela grpChannelBusiStoreRela) {

		if (!StrUtil.isNullOrEmpty(grpChannelBusiStoreRela)
				&& !StrUtil.isNullOrEmpty(grpChannelBusiStoreRela
						.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelBusiStoreRela
						.getBusiStoreNbr())
				&& !StrUtil
						.isNullOrEmpty(grpChannelBusiStoreRela.getRelaType())) {

			String sql = "SELECT * FROM GRP_CHANNEL_BUSI_STORE_RELA WHERE CHANNEL_NBR = ? AND BUSI_STORE_NBR = ? AND RELA_TYPE = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpChannelBusiStoreRela.getChannelNbr());
			params.add(grpChannelBusiStoreRela.getBusiStoreNbr());
			params.add(grpChannelBusiStoreRela.getRelaType());

			return jdbcFindObject(sql, params, GrpChannelBusiStoreRela.class);

		}

		return null;
	}

	/**
	 * 查询渠道关联经营主体信息
	 * 
	 * @param grpChannelOperatorsRela
	 * @return
	 */
	@Override
	public List<GrpChannelOperatorsRela> queryGrpChannelOperatorsRelaList(
			GrpChannelOperatorsRela grpChannelOperatorsRela) {

		if (grpChannelOperatorsRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_CHANNEL_OPERATORS_RELA WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela
					.getGrpChannelOperatorsRelaId())) {
				sql.append(" AND GRP_CHANNEL_OPERATORS_RELA_ID = ?");
				params.add(grpChannelOperatorsRela
						.getGrpChannelOperatorsRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpChannelOperatorsRela.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela
					.getOperatorsNbr())) {
				sql.append(" AND OPERATORS_NBR = ? ");
				params.add(grpChannelOperatorsRela.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela.getRelaType())) {
				sql.append(" AND RELA_TYPE = ?");
				params.add(grpChannelOperatorsRela.getRelaType());
			}

			sql.append(" ORDER BY GRP_CHANNEL_OPERATORS_RELA_ID DESC");

			return super.jdbcFindList(sql.toString(), params,
					GrpChannelOperatorsRela.class);

		}

		return null;

	}

	/**
	 * 分页查询渠道关联经营主体信息
	 * 
	 * @param grpChannelOperatorsRela
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpChannelOperatorsRela(
			GrpChannelOperatorsRela grpChannelOperatorsRela, int currentPage,
			int pageSize) {

		if (grpChannelOperatorsRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT A.*,B.CHANNEL_NAME,C.OPERATORS_NAME FROM GRP_CHANNEL_OPERATORS_RELA A,GRP_CHANNEL B,GRP_OPERATORS C");
			sql.append(" WHERE A.CHANNEL_NBR = B.CHANNEL_NBR AND A.OPERATORS_NBR = C.OPERATORS_NBR AND A.ACTION != ? AND B.STATUS_CD NOT IN (?,?,?) AND C.STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela
					.getGrpChannelOperatorsRelaId())) {
				sql.append(" AND A.GRP_CHANNEL_OPERATORS_RELA_ID = ?");
				params.add(grpChannelOperatorsRela
						.getGrpChannelOperatorsRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela.getChannelNbr())) {
				sql.append(" AND A.CHANNEL_NBR = ?");
				params.add(grpChannelOperatorsRela.getChannelNbr());
			}

			if (!StrUtil
					.isNullOrEmpty(grpChannelOperatorsRela.getChannelName())) {
				sql.append(" AND B.CHANNEL_NAME = ?");
				params.add(grpChannelOperatorsRela.getChannelName());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela
					.getOperatorsNbr())) {
				sql.append(" AND A.OPERATORS_NBR = ? ");
				params.add(grpChannelOperatorsRela.getOperatorsNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelOperatorsRela.getRelaType())) {
				sql.append(" AND A.RELA_TYPE = ?");
				params.add(grpChannelOperatorsRela.getRelaType());
			}

			sql.append(" ORDER BY A.GRP_CHANNEL_OPERATORS_RELA_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpChannelOperatorsRela.class);

		}

		return null;

	}

	/**
	 * 查询店中商关系
	 * 
	 * @param grpChannelRela
	 */
	@Override
	public GrpChannelRela queryGrpChannelRela(GrpChannelRela grpChannelRela) {

		if (!StrUtil.isNullOrEmpty(grpChannelRela)
				&& !StrUtil.isNullOrEmpty(grpChannelRela.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelRela.getRelaChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpChannelRela.getRelaType())) {

			String sql = "SELECT * FROM GRP_CHANNEL_RELA WHERE CHANNEL_NBR = ? AND RELA_CHANNEL_NBR = ? AND RELA_TYPE = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpChannelRela.getChannelNbr());
			params.add(grpChannelRela.getRelaChannelNbr());
			params.add(grpChannelRela.getRelaType());

			return jdbcFindObject(sql, params, GrpChannelRela.class);

		}

		return null;
	}

	/**
	 * 查询店中商信息
	 * 
	 * @param grpChannelRela
	 * @return
	 */
	@Override
	public List<GrpChannelRela> queryGrpChannelRelaList(
			GrpChannelRela grpChannelRela) {

		if (grpChannelRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_CHANNEL_RELA WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getGrpChannelRelaId())) {
				sql.append(" AND GRP_CHANNEL_RELA_ID = ?");
				params.add(grpChannelRela.getGrpChannelRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpChannelRela.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getRelaChannelNbr())) {
				sql.append(" AND RELA_CHANNEL_NBR = ? ");
				params.add(grpChannelRela.getRelaChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getRelaType())) {
				sql.append(" AND RELA_TYPE = ?");
				params.add(grpChannelRela.getRelaType());
			}

			sql.append(" ORDER BY GRP_CHANNEL_RELA_ID DESC");

			return super.jdbcFindList(sql.toString(), params,
					GrpChannelRela.class);

		}

		return null;

	}

	/**
	 * 分页查询店中商信息
	 * 
	 * @param grpChannelRela
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpChannelRela(
			GrpChannelRela grpChannelRela, int currentPage, int pageSize) {

		if (grpChannelRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT A.*,B.CHANNEL_NAME FROM GRP_CHANNEL_RELA A LEFT JOIN GRP_CHANNEL B ON A.CHANNEL_NBR = B.CHANNEL_NBR WHERE A.ACTION != ? AND B.STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getGrpChannelRelaId())) {
				sql.append(" AND A.GRP_CHANNEL_RELA_ID = ?");
				params.add(grpChannelRela.getGrpChannelRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getChannelNbr())) {
				sql.append(" AND A.CHANNEL_NBR = ?");
				params.add(grpChannelRela.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getChannelName())) {
				sql.append(" AND B.CHANNEL_NAME = ?");
				params.add(grpChannelRela.getChannelName());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getRelaChannelNbr())) {
				sql.append(" AND A.RELA_CHANNEL_NBR = ? ");
				params.add(grpChannelRela.getRelaChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpChannelRela.getRelaType())) {
				sql.append(" AND A.RELA_TYPE = ?");
				params.add(grpChannelRela.getRelaType());
			}

			sql.append(" ORDER BY A.GRP_CHANNEL_RELA_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpChannelRela.class);

		}

		return null;

	}

	/**
	 * 查询销售点与销售员关系
	 * 
	 * @param grpStaffChannelRela
	 */
	@Override
	public GrpStaffChannelRela queryGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela) {

		if (!StrUtil.isNullOrEmpty(grpStaffChannelRela)
				&& !StrUtil.isNullOrEmpty(grpStaffChannelRela.getSalesCode())
				&& !StrUtil.isNullOrEmpty(grpStaffChannelRela.getChannelNbr())
				&& !StrUtil.isNullOrEmpty(grpStaffChannelRela.getRelaType())) {

			String sql = "SELECT * FROM GRP_STAFF_CHANNEL_RELA WHERE SALES_CODE = ? AND CHANNEL_NBR = ? AND RELA_TYPE = ?";

			List<Object> params = new ArrayList<Object>();
			params.add(grpStaffChannelRela.getSalesCode());
			params.add(grpStaffChannelRela.getChannelNbr());
			params.add(grpStaffChannelRela.getRelaType());

			return jdbcFindObject(sql, params, GrpStaffChannelRela.class);

		}

		return null;
	}

	/**
	 * 查询销售点与销售员关系
	 * 
	 * @param grpStaffChannelRela
	 * @return
	 */
	@Override
	public List<GrpStaffChannelRela> queryGrpStaffChannelRelaList(
			GrpStaffChannelRela grpStaffChannelRela) {

		if (grpStaffChannelRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT * FROM GRP_STAFF_CHANNEL_RELA WHERE ACTION != ?");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela
					.getGrpStaffChannelRelaId())) {
				sql.append(" AND GRP_STAFF_CHANNEL_RELA_ID = ?");
				params.add(grpStaffChannelRela.getGrpStaffChannelRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getSalesCode())) {
				sql.append(" AND SALES_CODE = ? ");
				params.add(grpStaffChannelRela.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getChannelNbr())) {
				sql.append(" AND CHANNEL_NBR = ?");
				params.add(grpStaffChannelRela.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getRelaType())) {
				sql.append(" AND RELA_TYPE = ?");
				params.add(grpStaffChannelRela.getRelaType());
			}

			sql.append(" ORDER BY GRP_STAFF_CHANNEL_RELA_ID DESC");

			return super.jdbcFindList(sql.toString(), params,
					GrpStaffChannelRela.class);

		}

		return null;

	}

	/**
	 * 分页查询销售点与销售员关系
	 * 
	 * @param grpStaffChannelRela
	 * @return
	 */
	@Override
	public PageInfo queryPageInfoByGrpStaffChannelRela(
			GrpStaffChannelRela grpStaffChannelRela, int currentPage,
			int pageSize) {

		if (grpStaffChannelRela != null) {

			StringBuffer sql = new StringBuffer(
					"SELECT A.*,B.STAFF_NAME,C.CHANNEL_NAME FROM GRP_STAFF_CHANNEL_RELA A,GRP_STAFF B,GRP_CHANNEL C");
			sql.append(" WHERE A.SALES_CODE = B.SALES_CODE AND A.CHANNEL_NBR = C.CHANNEL_NBR AND A.ACTION != ? AND B.STATUS_CD NOT IN (?,?,?) AND C.STATUS_CD NOT IN (?,?,?)");

			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_DEL);
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);
			params.add(BaseUnitConstants.ENTT_STATE_INACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_UNACTIVE);
			params.add(BaseUnitConstants.ENTT_STATE_FILE);

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela
					.getGrpStaffChannelRelaId())) {
				sql.append(" AND A.GRP_STAFF_CHANNEL_RELA_ID = ?");
				params.add(grpStaffChannelRela.getGrpStaffChannelRelaId());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getSalesCode())) {
				sql.append(" AND A.SALES_CODE = ? ");
				params.add(grpStaffChannelRela.getSalesCode());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getStaffName())) {
				sql.append(" AND B.STAFF_NAME = ? ");
				params.add(grpStaffChannelRela.getStaffName());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getChannelNbr())) {
				sql.append(" AND A.CHANNEL_NBR = ?");
				params.add(grpStaffChannelRela.getChannelNbr());
			}

			if (!StrUtil.isNullOrEmpty(grpStaffChannelRela.getRelaType())) {
				sql.append(" AND A.RELA_TYPE = ?");
				params.add(grpStaffChannelRela.getRelaType());
			}

			sql.append(" ORDER BY A.GRP_STAFF_CHANNEL_RELA_ID");

			return super.jdbcFindPageInfo(sql.toString(), params, currentPage,
					pageSize, GrpStaffChannelRela.class);

		}

		return null;

	}

	@Override
	public String selectDual1(String str)
	{
		JdbcTemplate template = this.getJdbcTemplate();
		String result = (String) template.queryForObject(str, String.class);
		//查询出第一个数据
		return result;
	}

}
