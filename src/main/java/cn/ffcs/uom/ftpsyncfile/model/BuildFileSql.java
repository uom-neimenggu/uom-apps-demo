package cn.ffcs.uom.ftpsyncfile.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *文件生成配置表实体.
 * 
 * @author
 * 
 **/
public class BuildFileSql extends UomEntity implements Serializable {
	/**
	 *对象名称.
	 **/
	@Getter
	@Setter
	private String objectName;
	/**
	 *对象中文名称.
	 **/
	@Getter
	@Setter
	private String objectNameCn;
	/**
	 *全量SQL.
	 **/
	@Getter
	@Setter
	private String fullSql;
	/**
	 *全量占位符.
	 **/
	@Getter
	@Setter
	private String placeHolderFl;
	/**
	 *全量参数.
	 **/
	@Getter
	@Setter
	private String parameterFl;
	/**
	 *增量SQL.
	 **/
	@Getter
	@Setter
	private String increaseSql;
	/**
	 *增量占位符.
	 **/
	@Getter
	@Setter
	private String placeHolderIs;
	/**
	 *增量参数.
	 **/
	@Getter
	@Setter
	private String parameterIs;

	/**
	 * 
	 * @return
	 */
	public Long getBuildFileSqlId() {
		return super.getId();
	}

	/**
	 * 
	 * @param buildFileSqlId
	 */
	public void setBuildFileSqlId(Long buildFileSqlId) {
		super.setId(buildFileSqlId);
	}

}
