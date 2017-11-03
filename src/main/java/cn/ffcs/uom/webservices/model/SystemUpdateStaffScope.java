package cn.ffcs.uom.webservices.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.webservices.dao.SystemUpdateStaffScopeDao;

/**
 *员工系统可修改字段范围实体.
 * 
 * @author
 * 
 **/
public class SystemUpdateStaffScope extends UomEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
	 *系统编码.
	 **/
	@Getter
	@Setter
	private String systemCode;
	/**
	 *表名.
	 **/
	@Getter
	@Setter
	private String tableName;
	/**
	 *字段名.
	 **/
	@Getter
	@Setter
	private String columnName;

	public Long getSystemUpdateStaffScopeId() {
		return super.getId();
	}

	public void setSystemUpdateStaffScopeId(Long systemUpdateStaffScopeId) {
		super.setId(systemUpdateStaffScopeId);
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static SystemUpdateStaffScopeDao repository() {
		return (SystemUpdateStaffScopeDao) ApplicationContextUtil
				.getBean("systemUpdateStaffScopeDao");
	}

	/**
	 * 是否配置
	 * 
	 * @param list
	 * @param tableName
	 * @param columnName
	 * @return
	 */
	public static boolean includes(List<SystemUpdateStaffScope> list,
			String tableName, String columnName) {
		if (list != null && list.size() > 0) {
			for (SystemUpdateStaffScope systemUpdateStaffScope : list) {
				if (tableName.equals(systemUpdateStaffScope.getTableName())
						&& columnName.equals(systemUpdateStaffScope
								.getColumnName())) {
					return true;
				}
			}
		}
		return false;
	}
}
