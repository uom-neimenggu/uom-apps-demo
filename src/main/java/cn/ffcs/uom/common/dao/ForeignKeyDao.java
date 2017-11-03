package cn.ffcs.uom.common.dao;

import cn.ffcs.uom.common.model.ForeignKey;

public interface ForeignKeyDao extends BaseDao {
	/**
	 * 查询单个对象。
	 * 
	 * @param ForeignKey
	 *            数据对象
	 * @param javaCode
	 *            实体类
	 * @param fkJavaCode
	 *            实体类属性
	 * @return 对象数据
	 */
	public ForeignKey jdbcFindForeignKey(String javaCode, String fkJavaCode);
}
