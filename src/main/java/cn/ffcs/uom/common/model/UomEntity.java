package cn.ffcs.uom.common.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import lombok.Getter;
import lombok.Setter;

import org.apache.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.exception.RtManagerException;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.restservices.util.DateAdapter;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.systemconfig.model.SysClass;

/**
 * 公共的8个字段
 * 
 * @author ZhaoF
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UomEntity {
	/**
	 * 日记器.
	 */
	@XmlTransient
	private static Logger logger = Logger.getLogger(UomEntity.class);
	/**
	 * 主键
	 */
	@Getter
	@Setter
	@XmlTransient
	private Long id;
	/**
	 * 生效时间.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private Date effDate;
	/**
	 * 失效时间.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private Date expDate;

	/**
	 * 状态.
	 **/
	@Getter
	@Setter
	@XmlElement(name = "STATUS_CD")
	private String statusCd;
	/**
	 * 状态时间.
	 **/
	@Getter
	@Setter
	@XmlElement(name = "STATUS_DATE")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date statusDate;
	/**
	 * 创建时间.
	 **/
	@Getter
	@Setter
	@XmlElement(name = "CREATE_DATE")
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date createDate;

	/**
	 * 创建员工.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private Long createStaff;
	/**
	 * 更新时间.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private Date updateDate;
	/**
	 * 更新员工.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private Long updateStaff;
	/**
	 * 批次号.
	 **/
	@Getter
	@Setter
	@XmlTransient
	private String batchNumber;

	/**
	 * 默认构造方法
	 */
	public UomEntity() {
	}

	/**
	 * 获取属性值
	 * 
	 * @return
	 */
	public String getStatusCdName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"UomEntity", "statusCd", this.getStatusCd(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& !StrUtil.isEmpty(this.getStatusCd())) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 新增
	 */
	public void add() {
		Date nowDate = DateUtil.getNewDate();
		this.setCreateDate(nowDate);
		this.setEffDate(nowDate);
		this.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setCreateStaff(staffId);
			//this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().addObject(this);
		/**
		 * 操作记录表
		 */
		OperateLog operateLog = new OperateLog();
		operateLog.setBatchNumber(this.getBatchNumber());
		operateLog.setCreateDate(nowDate);
		operateLog.setEffDate(nowDate);
		operateLog.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
		operateLog.setStatusDate(nowDate);
		operateLog.setUpdateDate(nowDate);
		operateLog.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		operateLog.setOpeType(BaseUnitConstants.OPE_TYPE_ADD);
		operateLog.setOpeObjectId(this.getId());
		SysClass sysClass = UomClassProvider.jdbcGetSysClass(this.getClass());
		if (sysClass != null) {
			operateLog.setOpeObject(sysClass.getTableName());
		}
		OperateLog.repository().addObject(operateLog);
	}

	/**
	 * 新增无需操作日志和历史
	 */
	public void addOnly() {
		Date nowDate = DateUtil.getNewDate();
		this.setCreateDate(nowDate);
		this.setEffDate(nowDate);
		this.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setCreateStaff(staffId);
			//this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().addObject(this);
	}

	/**
	 * 修改
	 */
	public void update() {
		Date nowDate = DateUtil.getNewDate();
		/**
		 * 保存历史
		 */
		Long hisId = this.saveUomEntityToHis(this, nowDate);
		/**
		 * 当前生效时间，状态时间，更新时间修改为当前操作时间
		 */
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setEffDate(nowDate);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().updateObject(this);
		/**
		 * 操作记录表
		 */
		OperateLog operateLog = new OperateLog();
		operateLog.setBatchNumber(this.getBatchNumber());
		operateLog.setCreateDate(nowDate);
		operateLog.setEffDate(nowDate);
		operateLog.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
		operateLog.setStatusDate(nowDate);
		operateLog.setUpdateDate(nowDate);
		operateLog.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		operateLog.setOpeType(BaseUnitConstants.OPE_TYPE_UPDATE);
		operateLog.setOpeObjectId(this.getId());
		operateLog.setOpeOriObjectId(hisId);
		SysClass sysClass = UomClassProvider.jdbcGetSysClass(this.getClass());
		if (sysClass != null) {
			operateLog.setOpeObject(sysClass.getTableName());
		}
		OperateLog.repository().addObject(operateLog);
		int i = 0;
		OperateLog opLog = null;
	}

	/**
	 * 只更新当前表
	 */
	public void updateOnly() {
		Date nowDate = DateUtil.getNewDate();
		/**
		 * 当前生效时间，状态时间，更新时间修改为当前操作时间
		 */
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setEffDate(nowDate);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().updateObject(this);
	}

	/**
	 * 删除
	 */
	public void remove() {
		Date nowDate = DateUtil.getNewDate();
		/**
		 * 保存历史
		 */
		Long hisId = this.saveUomEntityToHis(this, nowDate);
		/**
		 * 状态时间，更新时间，生效时间改为当前操作时间
		 */
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setEffDate(nowDate);
		this.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().updateObject(this);
		/**
		 * 操作记录表
		 */
		OperateLog operateLog = new OperateLog();
		operateLog.setBatchNumber(this.getBatchNumber());
		operateLog.setCreateDate(nowDate);
		operateLog.setEffDate(nowDate);
		operateLog.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
		operateLog.setStatusDate(nowDate);
		operateLog.setUpdateDate(nowDate);
		operateLog.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		operateLog.setOpeType(BaseUnitConstants.OPE_TYPE_DEL);
		operateLog.setOpeObjectId(this.getId());
		SysClass sysClass = UomClassProvider.jdbcGetSysClass(this.getClass());
		if (sysClass != null) {
			operateLog.setOpeObject(sysClass.getTableName());
		}
		OperateLog.repository().addObject(operateLog);
	}

	/**
	 * 只删除
	 */
	public void removeOnly() {
		Date nowDate = DateUtil.getNewDate();
		/**
		 * 状态时间，更新时间，生效时间改为当前操作时间
		 */
		this.setStatusDate(nowDate);
		this.setUpdateDate(nowDate);
		this.setEffDate(nowDate);
		this.setStatusCd(BaseUnitConstants.ENTT_STATE_INACTIVE);
		Long staffId = PlatformUtil.getCurrentUserId();
		if (staffId != null) {
			this.setUpdateStaff(staffId);
		}
		DefaultDaoFactory.getDefaultDao().updateObject(this);
	}

	/**
	 * 获取LocalSessionFactoryBean
	 * 
	 * @return
	 */
	public LocalSessionFactoryBean getSessionFactoryBean() {
		return (LocalSessionFactoryBean) ApplicationContextUtil
				.getBean("&sessionFactory");
	}

	/**
	 * 保存历史：状态修改，状态时间，修改时间，失效时间为当前时间
	 * 
	 * @param entity
	 * @param id
	 */
	private Long saveUomEntityToHis(Object entity, Date nowDate) {
		StringBuffer sql = new StringBuffer("insert into ");
		StringBuffer sqlTail = new StringBuffer("select ");
		SysClass sysClass = UomClassProvider.jdbcGetSysClass(this.getClass());
		if (sysClass == null) {
			logger.info(entity.getClass().getName() + "主数据未配置,历史表记录无法保存");
			return null;
		}
		sql.append(sysClass.getHisTableName()).append(" (");
		PersistentClass persistentClass = null;
		String entityName = entity.getClass().getName();
		Configuration cfg = this.getSessionFactoryBean().getConfiguration();
		persistentClass = cfg.getClassMapping(entityName);
		Table table = persistentClass.getTable();
		Iterator<?> iterator = table.getColumnIterator();
		Column column = null;
		while (iterator.hasNext()) {
			column = (Column) iterator.next();
			sql.append(column.getName()).append(", ");
			if (BaseUnitConstants.TABLE_CLOUMN_EXP_DATE
					.equals(column.getName())) {
				sqlTail.append("to_date('")
						.append(DateUtil.getYYYYMMDDHHmmss(nowDate))
						.append("','yyyyMMddHH24miss')").append(", ");
			} else if (BaseUnitConstants.TABLE_CLOUMN_EFF_DATE.equals(column
					.getName())) {// 处理同个时间内操作历史记录与原记录生效时间相等问题
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(nowDate);
				calendar.add(Calendar.SECOND, -1);
				sqlTail.append("to_date('")
						.append(DateUtil.getYYYYMMDDHHmmss(calendar.getTime()))
						.append("','yyyyMMddHH24miss')").append(", ");
			} else {
				sqlTail.append(column.getName()).append(", ");
			}
		}
		sqlTail.append("? from ").append(table.getName()).append(" where ")
				.append(table.getPrimaryKey().getColumn(0).getName())
				.append("=? ");
		sql.append("HIS_ID) ");
		sql.append(sqlTail);
		Long hisId = DefaultDaoFactory.getDefaultDao().jdbcGetSeqNextval(
				sysClass.getHisSeqName());

		int count = DefaultDaoFactory.getDefaultDao().getJdbcTemplate()
				.update(sql.toString(), hisId, this.getId());
		return hisId;
	}

	/**
	 * 获取更新员工
	 * 
	 * @return
	 */
	public String getUpdateStaffName() {
		if (this.updateStaff != null) {
			String sql = "SELECT * FROM RAPTORNUKE.USER_ WHERE USERID = ?";
			List<Map<String, Object>> list = DefaultDaoFactory.getDefaultDao()
					.getJdbcTemplate().queryForList(sql, this.updateStaff);
			if (list != null && list.size() > 0) {
				Map userMap = list.get(0);
				String firstname = userMap.get("FIRSTNAME") != null ? userMap
						.get("FIRSTNAME").toString() : "";
				String lastname = userMap.get("FIRSTNAME") != null ? userMap
						.get("LASTNAME").toString() : "";
				return firstname + lastname;
			}
		}
		return "";
	}

	/**
	 * 获取更新员工 空白用"初始化代替"
	 * 
	 * @return
	 */
	public String getUpdateStaffName2() {
		if (this.updateStaff != null) {
			String sql = "SELECT * FROM RAPTORNUKE.USER_ WHERE USERID = ?";
			List<Map<String, Object>> list = DefaultDaoFactory.getDefaultDao()
					.getJdbcTemplate().queryForList(sql, this.updateStaff);
			if (list != null && list.size() > 0) {
				Map userMap = list.get(0);
				String firstname = userMap.get("FIRSTNAME") != null ? userMap
						.get("FIRSTNAME").toString() : "";
				String lastname = userMap.get("FIRSTNAME") != null ? userMap
						.get("LASTNAME").toString() : "";
				return (firstname + lastname).equals("") ? "初始化"
						: (firstname + lastname);
			}
		}
		return "初始化";
	}
	
	/**
	 * 获取更新员工 空白用"初始化代替"
	 * 
	 * @return
	 */
	public String getCreateStaffName() {
		if (this.createStaff != null) {
			String sql = "SELECT * FROM RAPTORNUKE.USER_ WHERE USERID = ?";
			List<Map<String, Object>> list = DefaultDaoFactory.getDefaultDao()
					.getJdbcTemplate().queryForList(sql, this.createStaff);
			if (list != null && list.size() > 0) {
				Map userMap = list.get(0);
				String firstname = userMap.get("FIRSTNAME") != null ? userMap
						.get("FIRSTNAME").toString() : "";
				String lastname = userMap.get("FIRSTNAME") != null ? userMap
						.get("LASTNAME").toString() : "";
				return (firstname + lastname).equals("") ? "初始化"
						: (firstname + lastname);
			}
		}
		return "初始化";
	}
}
