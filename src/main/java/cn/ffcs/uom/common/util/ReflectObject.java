package cn.ffcs.uom.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ffcs.uom.common.dao.BaseDaoHibernate;
import cn.ffcs.uom.common.model.DiverseAttribute;
import cn.ffcs.uom.common.model.ForeignKey;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.systemconfig.model.AttrSpec;
import cn.ffcs.uom.systemconfig.model.AttrValue;

/**
 * 实体类反射：比较具有相同数据结构的两条记录，找出相同字段，但值不同的字段，并将值反射到相应实体类的关联属性。 并以list形式保存找出值不同的字段
 * 
 * @author zhulintao
 * 
 */
@SuppressWarnings({ "unused" })
public class ReflectObject {

	/**
	 * 
	 * 当一个属性有多个值时，可以根据其编码获取对就的属性名称. 例如：岗位类型(positionType)有如下值01
	 * 表示管理岗，02表示技术岗，03表示其他 此时调用下面的函数(传入值为01)，可得到管理岗
	 * 
	 * @param className
	 *            类名
	 * @param propertyName
	 *            属性名
	 * @param propertyValue
	 *            属性值
	 * @param statusCd
	 *            状态
	 * @return AttrValue.getAttrValueName() 对应的属性值名
	 * @author: zhulintao
	 * @创建日期 2013-06-20
	 */
	public String getPropertyName(String className, String propertyName,
			String propertyValue, String statusCd) {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				className, propertyName, propertyValue, statusCd);

		if (list != null && list.size() == 1) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 根据类的属性返回对应表的字段名
	 * 
	 * @param classId
	 *            类ID
	 * @param statusCd
	 *            状态
	 * @param javaCode
	 *            类属性
	 * @return AttrSpec
	 * @author: zhulintao
	 * @创建日期 2013-06-20
	 */
	public String getColumnName(Long classId, String javaCode, String statusCd) {

		StringBuffer sb = new StringBuffer(
				"SELECT * FROM ATTR_SPEC WHERE STATUS_CD = ?");
		List params = new ArrayList();

		if (!StrUtil.isEmpty(statusCd)) {
			params.add(statusCd);
		}

		if (classId != null) {
			sb.append(" AND CLASS_ID = ?");
			params.add(classId);
		}

		if (!StrUtil.isEmpty(javaCode)) {
			sb.append(" AND JAVA_CODE = ?");
			params.add(javaCode.trim());
		}

		AttrSpec attrSpec = (AttrSpec) AttrSpec.repository().jdbcFindObject(
				sb.toString(), params, AttrSpec.class);
		if (attrSpec != null) {
			return attrSpec.getAttrName();
		}
		return "";
	}

	/**
	 * 填充DiverseAttribute类的相应属性，并返回该类的一个实例
	 * 
	 * @param attrName
	 * @param newValue
	 * @param oldValue
	 * @return diverseAttribute
	 */
	public DiverseAttribute diverseAttributeEntity(String attrName,
			String newValue, String oldValue, boolean difference) {
		DiverseAttribute diverseAttribute = new DiverseAttribute();
		diverseAttribute.setAttrName(attrName);
		diverseAttribute.setAttrNewVaule(newValue);
		diverseAttribute.setAttrOldVaule(oldValue);
		diverseAttribute.setDifference(difference);
		return diverseAttribute;
	}

	/**
	 * 处理实体类不同类型的属性，以list形式返回差异的属性值
	 * 
	 * @param newModel
	 * @param oldModel
	 * @return list
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public List<DiverseAttribute> reflect(Object newModel, Object oldModel)
			throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {

		try {

			List<DiverseAttribute> list = new ArrayList<DiverseAttribute>();

			Field[] parentFields = newModel.getClass().getSuperclass()
					.getDeclaredFields();// 获取实体父类的所有属性，返回parentFields数组
			Field[] subFields = newModel.getClass().getDeclaredFields(); // 获取实体子类的所有属性，返回subFields数组

			Field[] concatFields = null;// 合并父类、子类的属性

			if ((subFields != null && subFields.length > 0)
					&& (parentFields != null && parentFields.length > 0)) {
				concatFields = new Field[subFields.length + parentFields.length];// 合并父类、子类的属性
				System.arraycopy(subFields, 0, concatFields, 0,
						subFields.length);
				System.arraycopy(parentFields, 0, concatFields,
						subFields.length, parentFields.length);
			} else if ((subFields != null && subFields.length > 0)
					&& (parentFields == null || parentFields.length <= 0)) {
				concatFields = new Field[subFields.length];// 合并父类、子类的属性
				System.arraycopy(subFields, 0, concatFields, 0,
						subFields.length);
			} else if ((subFields == null || subFields.length <= 0)
					&& (parentFields != null && parentFields.length > 0)) {
				concatFields = new Field[parentFields.length];// 合并父类、子类的属性
				System.arraycopy(parentFields, 0, concatFields, 0,
						parentFields.length);
			}

			if (concatFields != null && concatFields.length > 0) {
				for (Field field : concatFields) { // 遍历所有属性

					String name = field.getName(); // 获取属性的名字
					String attrName = name;// 保存属性的名字

					name = name.substring(0, 1).toUpperCase()
							+ name.substring(1); // 将属性的首字符大写，方便构造get，set方法
					String type = field.getGenericType().toString(); // 获取属性的类型

					if (type.equals("class java.lang.Character")) { // 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Character newValue = (Character) m.invoke(newModel); // 调用getter方法获取属性值
						Character oldValue = null;

						if (oldModel != null) {
							oldValue = (Character) m.invoke(oldModel); // 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						String newValue = (String) m.invoke(newModel); // 调用getter方法获取属性值
						String oldValue = null;

						if (oldModel != null) {
							oldValue = (String) m.invoke(oldModel); // 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Byte")) { // 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Byte newValue = (Byte) m.invoke(newModel); // 调用getter方法获取属性值
						Byte oldValue = null;

						if (oldModel != null) {
							oldValue = (Byte) m.invoke(oldModel); // 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Short")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Short newValue = (Short) m.invoke(newModel);// 调用getter方法获取属性值
						Short oldValue = null;

						if (oldModel != null) {
							oldValue = (Short) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Integer")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Integer newValue = (Integer) m.invoke(newModel);// 调用getter方法获取属性值
						Integer oldValue = null;

						if (oldModel != null) {
							oldValue = (Integer) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Long")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Long newValue = (Long) m.invoke(newModel);// 调用getter方法获取属性值
						Long oldValue = null;

						if (oldModel != null) {
							oldValue = (Long) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Float")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Float newValue = (Float) m.invoke(newModel);// 调用getter方法获取属性值
						Float oldValue = null;

						if (oldModel != null) {
							oldValue = (Float) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Double")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Double newValue = (Double) m.invoke(newModel);// 调用getter方法获取属性值
						Double oldValue = null;

						if (oldModel != null) {
							oldValue = (Double) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.lang.Boolean")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);
						Boolean newValue = (Boolean) m.invoke(newModel);// 调用getter方法获取属性值
						Boolean oldValue = null;

						if (oldModel != null) {
							oldValue = (Boolean) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(), null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null, oldValue.toString(), true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										newValue.toString(),
										oldValue.toString(), false));
							}
						}

					}

					if (type.equals("class java.util.Date")) {// 如果type是类类型，则前面包含"class "，后面跟类名

						Method m = newModel.getClass().getMethod("get" + name);// 构造属性的getter方法
						Date newValue = (Date) m.invoke(newModel);// 调用getter方法获取属性值
						Date oldValue = null;

						if (oldModel != null) {
							oldValue = (Date) m.invoke(oldModel);// 调用getter方法获取属性值
						}

						if (!(newValue == null && oldValue == null)) {
							if (newValue != null && oldValue == null) {
								list.add(this.diverseAttributeEntity(attrName,
										this.DateConvertToString(newValue),
										null, true));
							} else if (newValue == null && oldValue != null) {
								list.add(this.diverseAttributeEntity(attrName,
										null,
										this.DateConvertToString(oldValue),
										true));
							} else if (!(newValue.equals(oldValue))) {// 比较差异项
								list.add(this.diverseAttributeEntity(attrName,
										this.DateConvertToString(newValue),
										this.DateConvertToString(oldValue),
										true));
							} else {
								list.add(this.diverseAttributeEntity(attrName,
										this.DateConvertToString(newValue),
										this.DateConvertToString(oldValue),
										false));
							}
						}

					}
				}
				return list;
			} else {
				list = null;
				return list;
			}
		} catch (Exception e) {
			List<DiverseAttribute> list = null;
			return list;
		}
	}

	public Date StringConvertToDate(String str) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return df.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String DateConvertToString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return df.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}