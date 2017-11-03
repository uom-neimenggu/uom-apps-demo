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
import cn.ffcs.uom.systemconfig.dao.SysClassDao;

/**
 *类实体.
 * 
 * @author
 * 
 **/
public class SysClass extends UomEntity implements Serializable {
	/**
	 *类主键.
	 **/
	public Long getClassId() {
		return super.getId();
	}

	public void setClassId(Long classId) {
		super.setId(classId);
	}

	/**
	 *类名.
	 **/
	@Getter
	@Setter
	private String className;
	/**
	 *JAVA类名.
	 **/
	@Getter
	@Setter
	private String javaCode;
	/**
	 *物理表类型.
	 **/
	@Getter
	@Setter
	private String tableType;
	/**
	 *物理表名.
	 **/
	@Getter
	@Setter
	private String tableName;
	/**
	 *是否实体类.
	 **/
	@Getter
	@Setter
	private Long isEntity;

	public Long getIsEntity() {
		return isEntity;
	}

	/**
	 *描述信息.
	 **/
	@Getter
	@Setter
	private String classDesc;
	/**
	 *序列名称.
	 **/
	@Getter
	@Setter
	private String seqName;
	/**
	 *历史表.
	 **/
	@Getter
	@Setter
	private String hisTableName;
	/**
	 *历史表序列.
	 **/
	@Getter
	@Setter
	private String hisSeqName;
	/**
	 *属性规格列表.
	 **/
	@Getter
	@Setter
	private List<AttrSpec> attrSpecList;

	/**
	 * 创建对象实例.
	 * 
	 * @return SysClass
	 */
	public SysClass() {
		super();
	}

	public SysClass(boolean hasId) {
		super();
		if (hasId) {
			String seqName = UomClassProvider.jdbcGetSeqName(SysClass.class);
			if (!StrUtil.isEmpty(seqName)) {
				this.setClassId(SysClass.repository().jdbcGetSeqNextval(seqName));
			}
		}
	}

	/**
	 * 获取仓库
	 * 
	 * @return
	 */
	public static SysClassDao repository() {
		return (SysClassDao) ApplicationContextUtil.getBean("sysClassDao");
	}

	/**
	 * 
	 * @return
	 */
	public List<AttrSpec> getAttrSpecList() {
		if (attrSpecList == null || attrSpecList.size() == 0) {
			if (this.getClassId() != null) {
				String sql = "select * from attr_spec a where a.status_cd=? and a.class_id=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getClassId());
				attrSpecList = AttrSpec.repository().jdbcFindList(sql, params,
						AttrSpec.class);
			}
		}
		return attrSpecList;
	}
}
