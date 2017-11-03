package cn.ffcs.uom.ftpsyncfile.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *组织树同步表字段别名配置实体.
 * 
 * @author
 * 
 **/
public class SyncTableColumnAliasConfig extends UomEntity implements
		Serializable {
	/**
	 *组织树标识.
	 **/
	@Getter
	@Setter
	private Long treeId;
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
	/**
	 *别名.
	 **/
	@Getter
	@Setter
	private String aliasName;

	public Long getSyncTableColumnAliasConfig() {
		return super.getId();
	}

	public void setSyncTableColumnAliasConfig(Long syncTableColumnAliasConfig) {
		super.setId(syncTableColumnAliasConfig);
	}

	/**
	 * 获取该字段的配置名
	 * 
	 * @param columnName
	 * @param list
	 */
	public static String getAliasNameByTableNameAndColunmName(
			String columnName, String tableName,
			List<SyncTableColumnAliasConfig> list) {
		if (list != null && list.size() > 0) {
			for (SyncTableColumnAliasConfig stcac : list) {
				if (stcac.getTableName().equals(tableName)
						&& stcac.getColumnName().equals(columnName)) {
					return stcac.getAliasName();
				}
			}
		}
		return null;
	}
}
