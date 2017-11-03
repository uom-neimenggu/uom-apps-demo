package cn.ffcs.uom.common.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.dao.ForeignKeyDao;
import cn.ffcs.uom.common.model.ForeignKey;
import cn.ffcs.uom.common.util.StrUtil;

@Repository("foreignKeyDao")
public class ForeignKeyDaoImpl extends BaseDaoImpl implements ForeignKeyDao {

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
	public ForeignKey jdbcFindForeignKey(String javaCode, String fkJavaCode) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM FOREIGN_KEY WHERE STATUS_CD = ?");
		List params = new ArrayList();
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

		if (!StrUtil.isEmpty(javaCode)) {
			sb.append(" AND JAVA_CODE = ?");
			params.add(javaCode.trim());
		}

		if (!StrUtil.isEmpty(fkJavaCode)) {
			sb.append(" AND FK_JAVA_CODE = ?");
			params.add(fkJavaCode.trim());
		}

		ForeignKey foreignKey = (ForeignKey) ForeignKey.repository()
				.jdbcFindObject(sb.toString(), params, ForeignKey.class);

		return foreignKey;

	}

}
