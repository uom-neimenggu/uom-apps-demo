package cn.ffcs.uom.systemconfig.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.dao.AttrSpecDao;

/**
 *属性规格实体.
 * 
 * @author
 * 
 **/
public class AttrSpec extends UomEntity implements Serializable {
	/**
	 * 组件规格ID
	 */	
	public Long getAttrId() {
		return super.getId();
	}

	public void setAttrId(Long attrId) {
		super.setId(attrId);
	}
	/**
	 *归属类ID.
	 **/
	@Getter
	@Setter
	private Long classId;
	/**
	 *属性类型.
	 **/
	@Getter
	@Setter
	private String attrType;
	/**
	 *属性编码.
	 **/
	@Getter
	@Setter
	private String attrCd;
	/**
	 *属性名称.
	 **/
	@Getter
	@Setter
	private String attrName;
	/**
	 *JAVA编码.
	 **/
	@Getter
	@Setter
	private String javaCode;
	/**
	 *属性描述.
	 **/
	@Getter
	@Setter
	private String attrDesc;
	/**
	 *属性值类型.
	 **/
	@Getter
	@Setter
	private String attrValueType;
	/**
	 * 属性值
	 */
	private List<AttrValue> attrValueList;

	/**
	 * 默认构造方法
	 */
	public AttrSpec() {
		super();
	}

	public AttrSpec(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(AttrSpec.class);
			if (!StrUtil.isEmpty(seqName)) {
				this.setAttrId(AttrSpec.repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static AttrSpecDao repository() {
		return (AttrSpecDao) ApplicationContextUtil.getBean("attrSpecDao");
	}

	/**
	 * 获取当前属性值
	 * 
	 * @return
	 */
	public List<AttrValue> getAttrValueList() {
		if (this.attrValueList == null || this.attrValueList.size() == 0) {
			if (this.getAttrId() != null) {
				String sql = "SELECT * FROM ATTR_VALUE WHERE STATUS_CD=? AND ATTR_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getAttrId());
				attrValueList = AttrValue.repository().jdbcFindList(sql,
						params, AttrValue.class);
			}
		}
		return attrValueList;
	}

	/**
	 * 获取属性类型名称
	 * 
	 * @return
	 */
	public String getAttrTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"AttrSpec", "attrType", this.getAttrType(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}
}
