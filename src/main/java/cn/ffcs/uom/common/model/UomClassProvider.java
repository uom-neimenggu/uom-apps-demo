package cn.ffcs.uom.common.model;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.model.AttrExtValue;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.systemconfig.model.SysClass;

public class UomClassProvider {
	/**
	 * 
	 * 根据属性名称， 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param valueName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @return AttrValue 对象
	 * @author: zhaof
	 * @创建日期 2012-09-09
	 */
	private static List<AttrValue> jdbcGetAttrValue(String className,
			String propertyName, String valueName, String value, String statusCd) {
		List<AttrValue> list = new ArrayList<AttrValue>();
		List<Object> params = new ArrayList<Object>();
		AttrValue attrValue = null;
		StringBuffer sb = new StringBuffer(
				"SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C WHERE 1=1 ");
		if (!StrUtil.isEmpty(statusCd)) {
			sb.append("AND C.STATUS_CD = ? ");
			params.add(statusCd);
		}
		if (!StrUtil.isEmpty(valueName)) {
			sb.append("AND C.ATTR_VALUE_NAME = ? ");
			params.add(valueName);
		}
		if (!StrUtil.isEmpty(value)) {
			sb.append("AND C.ATTR_VALUE = ? ");
			params.add(value);
		}
		sb.append("AND C.ATTR_ID = B.ATTR_ID AND A.CLASS_ID = B.CLASS_ID ");
		if (!StrUtil.isEmpty(className)) {
			sb.append("AND A.JAVA_CODE = ? ");
			params.add(className);
		}
		if (!StrUtil.isEmpty(propertyName)) {
			sb.append("AND B.JAVA_CODE = ? ");
			params.add(propertyName);
		}
		sb.append(" ORDER BY C.ATTR_VALUE_ID");
		list = DefaultDaoFactory.getDefaultDao().jdbcFindList(sb.toString(),
				params, AttrValue.class);
		return list;
	}

	/**
	 * 
	 * 根据属性值， 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @param statusCd
	 *            状态
	 * @return List<AttrExtValue> 对象
	 * @author: 朱林涛
	 * @创建日期 2015-09-17
	 */
	public static List<AttrExtValue> jdbcGetAttrExtValueByValue(
			String className, String propertyName, String value, String statusCd) {
		List<AttrExtValue> list = jdbcGetAttrExtValue(className, propertyName,
				null, value, statusCd);
		return list;
	}

	/**
	 * 
	 * 根据属性名称， 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param valueName
	 *            属性名称
	 * @param value
	 *            属性值
	 * @param statusCd
	 *            状态
	 * @return List<AttrExtValue> 对象
	 * @author: 朱林涛
	 * @创建日期 2015-09-17
	 */
	private static List<AttrExtValue> jdbcGetAttrExtValue(String className,
			String propertyName, String valueName, String value, String statusCd) {

		if (!StrUtil.isEmpty("className") && !StrUtil.isEmpty("propertyName")
				&& !StrUtil.isEmpty("value")) {

			List<AttrExtValue> list = new ArrayList<AttrExtValue>();
			List<Object> params = new ArrayList<Object>();

			StringBuffer sb = new StringBuffer(
					"SELECT D.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C, ATTR_EXT_VALUE D");
			sb.append(" WHERE A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID AND C.ATTR_VALUE_ID = D.ATTR_VALUE_ID");

			if (!StrUtil.isEmpty(statusCd)) {
				sb.append(" AND A.STATUS_CD = ? AND B.STATUS_CD = ? AND C.STATUS_CD = ? AND D.STATUS_CD = ?");
				params.add(statusCd);
				params.add(statusCd);
				params.add(statusCd);
				params.add(statusCd);
			}

			if (!StrUtil.isEmpty(className)) {
				sb.append(" AND A.JAVA_CODE = ? ");
				params.add(className);
			}

			if (!StrUtil.isEmpty(propertyName)) {
				sb.append(" AND B.JAVA_CODE = ? ");
				params.add(propertyName);
			}

			if (!StrUtil.isEmpty(valueName)) {
				sb.append(" AND C.ATTR_VALUE_NAME = ? ");
				params.add(valueName);
			}

			if (!StrUtil.isEmpty(value)) {
				sb.append(" AND C.ATTR_VALUE = ? ");
				params.add(value);
			}

			sb.append(" ORDER BY D.ATTR_EXT_VALUE_ID");

			list = DefaultDaoFactory.getDefaultDao().jdbcFindList(
					sb.toString(), params, AttrExtValue.class);

			return list;

		}

		return null;

	}

	/**
	 * 
	 * 根据属性值， 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            属性值
	 * @return AttrValue 对象
	 * @author: zhaof
	 * @创建日期 2012-09-09
	 */
	public static List<AttrValue> jdbcGetAttrValueByValue(String className,
			String propertyName, String value, String statusCd) {
		List<AttrValue> list = jdbcGetAttrValue(className, propertyName, null,
				value, null);
		return list;
	}

	/**
	 * 
	 * 根据属性名称， 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param valueName
	 *            属性名称
	 * @return AttrValue 对象
	 * @author: zhaof
	 * @创建日期 2012-09-09
	 */
	public static List<AttrValue> jdbcGetAttrValueByName(String className,
			String propertyName, String valueName, String statusCd) {
		List<AttrValue> list = jdbcGetAttrValue(className, propertyName,
				valueName, null, statusCd);
		return list;
	}

	/**
	 * 
	 * 获取属性取值元数据列表.
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @author: zhaof
	 * @创建日期 2012-09-09
	 */
	public static List<AttrValue> jdbcGetAttrValue(String className,
			String propertyName, String statusCd) {
		List<AttrValue> list = jdbcGetAttrValue(className, propertyName, null,
				null, statusCd);
		return list;
	}

	/**
	 * 方法功能: 根据属性名称， 获取属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	public static List<NodeVo> getValuesList(String className,
			String propertyName) {
		List<AttrValue> lstAttrValue = jdbcGetAttrValue(className,
				propertyName, BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<NodeVo> retAttrValues = new ArrayList();
		if (lstAttrValue != null) {
			for (AttrValue av : lstAttrValue) {
				if (av != null) {
					NodeVo vo = new NodeVo();
					vo.setId(av.getAttrValue());
					vo.setName(av.getAttrValueName());
					vo.setDesc(av.getAttrDesc());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	/**
	 * 方法功能: 根据属性名称，属性特定字符串开头， 获取属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	public static List<NodeVo> getValuesList(String className,
			String propertyName, String startStr) {
		List<AttrValue> lstAttrValue = jdbcGetAttrValue(className,
				propertyName, BaseUnitConstants.ENTT_STATE_ACTIVE);
		List<NodeVo> retAttrValues = new ArrayList();
		if (lstAttrValue != null) {
			for (AttrValue av : lstAttrValue) {
				if (av != null) {
					if (av.getAttrValue().startsWith(startStr)) {
						if (!av.getAttrValue().equals(startStr + "000000")) {
							NodeVo vo = new NodeVo();
							vo.setId(av.getAttrValue());
							vo.setName(av.getAttrValueName());
							vo.setDesc(av.getAttrDesc());
							retAttrValues.add(vo);
						}
					}
				}
			}
		}
		return retAttrValues;
	}

	/**
	 * 根据class获取类
	 * 
	 * @param c
	 * @return
	 */
	public static SysClass jdbcGetSysClass(Class c) {
		List<SysClass> list = new ArrayList<SysClass>();
		List<Object> params = new ArrayList<Object>();
		String javaCode = c.getName().substring(
				c.getName().lastIndexOf(".") + 1);
		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYS_CLASS A WHERE STATUS_CD=? AND A.JAVA_CODE=?");
		params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
		params.add(javaCode);
		list = DefaultDaoFactory.getDefaultDao().jdbcFindList(sb.toString(),
				params, SysClass.class);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据class获取seqName
	 * 
	 * @param c
	 * @return
	 */
	public static String jdbcGetSeqName(Class c) {
		SysClass sysClass = jdbcGetSysClass(c);
		if (sysClass != null) {
			return sysClass.getSeqName();
		}
		return "";
	}

	/**
	 * 获取同表父属性的AttrValue
	 * 
	 * @param className
	 * @param propertyName
	 * @param parentPropertyName
	 * @param parentValue
	 * @param statusCd
	 * @return
	 */
	public static List<AttrValue> jdbcGetAttrValueByParent(String className,
			String propertyName, String parentPropertyName, String parentValue,
			String statusCd) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer(
				"SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C WHERE 1=1 ");
		if (!StrUtil.isEmpty(statusCd)) {
			sb.append("AND C.STATUS_CD = ? ");
			params.add(statusCd);
		}
		if (!StrUtil.isEmpty(parentValue)) {
			sb.append("AND C.PARENT_VALUE_ID IN(SELECT C.ATTR_VALUE_ID FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C WHERE A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID AND A.JAVA_CODE = ? AND B.JAVA_CODE = ? AND C.STATUS_CD=? AND C.ATTR_VALUE = ?) ");
			params.add(className);
			params.add(parentPropertyName);
			params.add(statusCd);
			params.add(parentValue);
		}
		sb.append("AND A.CLASS_ID = B.CLASS_ID AND B.ATTR_ID = C.ATTR_ID ");
		if (!StrUtil.isEmpty(className)) {
			sb.append("AND A.JAVA_CODE = ? ");
			params.add(className);
		}
		if (!StrUtil.isEmpty(propertyName)) {
			sb.append("AND B.JAVA_CODE = ? ");
			params.add(propertyName);
		}
		List<AttrValue> list = DefaultDaoFactory.getDefaultDao().jdbcFindList(
				sb.toString(), params, AttrValue.class);
		return list;
	}

	/**
	 * 方法功能: 根据属性名称， 获取属性取值元数据列表 - 封装成前台下拉框需要的格式 id value
	 * 
	 */
	public static List<NodeVo> jdbcGetNodeVoListByParent(String className,
			String propertyName, String parentPropertyName, String parentValue,
			String statusCd) {
		List<AttrValue> lstAttrValue = jdbcGetAttrValueByParent(className,
				propertyName, parentPropertyName, parentValue, statusCd);
		List<NodeVo> retAttrValues = new ArrayList();
		if (lstAttrValue != null) {
			for (AttrValue av : lstAttrValue) {
				if (av != null) {
					NodeVo vo = new NodeVo();
					vo.setId(av.getAttrValue());
					vo.setName(av.getAttrValueName());
					vo.setDesc(av.getAttrDesc());
					retAttrValues.add(vo);
				}
			}
		}
		return retAttrValues;
	}

	/**
	 * 根据类的属性返回对应表的字段名
	 * 
	 * @param tableName
	 *            表名
	 * @param statusCd
	 *            状态
	 * @return SysClass
	 * @author: zhulintao
	 * @创建日期 2013-06-25
	 */
	public static List<SysClass> jdbcGetTableName(String tableName,
			String statusCd) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM SYS_CLASS WHERE STATUS_CD = ?");
		List params = new ArrayList();

		if (!StrUtil.isEmpty(statusCd)) {
			params.add(statusCd);
		}

		if (tableName != null) {
			sb.append("  AND TABLE_NAME = ?");
			params.add(tableName);
		}

		List<SysClass> list = DefaultDaoFactory.getDefaultDao().jdbcFindList(
				sb.toString(), params, SysClass.class);

		return list;
	}

	/**
	 * 获取地址
	 * 
	 * @param UrlName
	 *            配置在attrSpec的javaCode
	 * @return
	 */
	public static String getIntfUrl(String urlName) {
		List<AttrValue> list = jdbcGetAttrValue("IntfConfig", urlName,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValue();
		}
		return "";
	}

	/**
	 * 获取地址
	 * 
	 * @param UrlName
	 *            配置在attrSpec的javaCode
	 * @return
	 */
	public static boolean isOpenSwitch(String switchName) {
		boolean isOpen = false;
		List<AttrValue> list = jdbcGetAttrValue("IntfSwitch", switchName,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			if (BaseUnitConstants.SWITCH_OPEN
					.equals(list.get(0).getAttrValue())) {
				isOpen = true;
			}
		}
		return isOpen;
	}

	/**
	 * 获取系统配置
	 * 
	 * @param configName
	 * @return
	 */
	public static String getSystemConfig(String configName) {
		List<AttrValue> list = jdbcGetAttrValue("SystemConfig", configName,
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValue();
		}
		return "";
	}
}
