package cn.ffcs.uom.common.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.dao.ForeignKeyDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;

/**
 * 组织实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings("serial")
public class ForeignKey extends UomEntity implements Serializable {

	/**
	 * 外键表标识.
	 */
	public Long getFkId() {
		return super.getId();
	}

	public void setFkId(Long fkId) {
		super.setId(fkId);
	}

	/**
	 * 实体类.
	 **/
	@Getter
	@Setter
	private String javaCode;
	/**
	 * 表名.
	 **/
	@Getter
	@Setter
	private String tableName;
	/**
	 * 外键表名.
	 **/
	@Getter
	@Setter
	private String fkTableName;
	/**
	 * 关联外键实体属性名.
	 **/
	@Getter
	@Setter
	private String fkJavaCode;
	/**
	 * 关联外键字段名.
	 **/
	@Getter
	@Setter
	private String fkColumnName;

	/**
	 * 构造方法
	 */
	public ForeignKey() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OperateLog
	 */
	public static ForeignKey newInstance() {
		return new ForeignKey();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static ForeignKeyDao repository() {
		return (ForeignKeyDao) ApplicationContextUtil.getBean("foreignKeyDao");
	}

}
