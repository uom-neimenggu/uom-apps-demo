package cn.ffcs.uom.ftpsyncfile.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *组织树同步表表字段配置实体.
 * 
 * @author
 * 
 **/
public class SyncTableColumnConfig extends UomEntity implements Serializable {
	/**
	 *组织树标识.
	 **/
	@Getter
	@Setter
	private Long orgTreeId;
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
	/**
	 *字段排序.
	 **/
	@Getter
	@Setter
	private Long columnSeq;
	
	public Long getSyncTableColumnConfigId() {
		return super.getId();
	}

	public void setSyncTableColumnConfigId(Long syncTableColumnConfigId) {
		super.setId(syncTableColumnConfigId);
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return SyncTableColumnConfig
	 */
	public static SyncTableColumnConfig newInstance() {
		return new SyncTableColumnConfig();
	}
}
