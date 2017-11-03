package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.AttrValueDao;

/**
 *属性值规格实体.
 * 
 * @author
 * 
 **/
public class AttrValue extends UomEntity implements Serializable {
	/**
	 *属性值ID.
	 **/
	public Long getAttrValueId() {
		return super.getId();
	}

	public void setAttrValueId(Long attrValueId) {
		super.setId(attrValueId);
	}
	/**
	 * 上级属性值
	 */
	@Getter
	@Setter
	private Long parentValueId;
	/**
	 *规格ID.
	 **/
	@Getter
	@Setter
	private Long attrId;
	/**
	 *属性值.
	 **/
	@Getter
	@Setter
	private String attrValue;
	/**
	 *属性值名称.
	 **/
	@Getter
	@Setter
	private String attrValueName;
	/**
	 *属性值描述.
	 **/
	@Getter
	@Setter
	private String attrDesc;

	/**
	 * 
	 *默认构造方法.
	 * 
	 **/
	public AttrValue() {
		super();
	}

	public AttrValue(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(AttrValue.class);
			if (!StrUtil.isEmpty(seqName)) {
				 this.setAttrValueId(AttrValue.repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static AttrValueDao repository() {
		return (AttrValueDao) ApplicationContextUtil.getBean("attrValueDao");
	}

	/**
	 * 获取使用到改配置的记录数 返回-1时表示不是实体表
	 * 
	 * @return
	 */
	public int getUseRecordCount() {
		AttrSpec attrSpec = null;
		if (this.getAttrId() != null) {
			attrSpec = (AttrSpec) AttrSpec.repository().getObject(
					AttrSpec.class, this.getAttrId());
		}
		SysClass sysClass = null;
		if (attrSpec != null && attrSpec.getClassId() != null) {
			sysClass = (SysClass) SysClass.repository().getObject(
					SysClass.class, attrSpec.getClassId());
		}
		if (sysClass != null && !StrUtil.isEmpty(sysClass.getTableName())) {
			StringBuffer sb = new StringBuffer("select count(*) from ");
			sb.append(sysClass.getTableName());
			sb.append(" where ");
			sb.append(attrSpec.getAttrCd());
			sb.append("=");
			sb.append("'");
			sb.append(this.getAttrValue());
			sb.append("'");
			return AttrValue.repository().getJdbcTemplate().queryForInt(
					sb.toString());
		}
		return -1;
	}
}
