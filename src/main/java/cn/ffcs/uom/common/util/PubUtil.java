package cn.ffcs.uom.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.api.Checkbox;
import org.zkoss.zul.impl.InputElement;

import cn.ffcs.raptornuke.plugin.common.exception.RtManagerException;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUiUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.systemconfig.model.AttrSpec;

/**
 * 公共操作 .
 * 
 * @author zfz
 * @version Revision 1.0.0
 */
public final class PubUtil {
	/**
	 * log.
	 */
	public static Logger log = Logger.getLogger(PubUtil.class);
	/**
	 * bandbox类型.
	 */
	public static final String BANDBOX_TYPE_ATTR = "attr";
	/**
	 * Name.
	 */
	public static final String BANDBOX_ATTR_NAME = "Name";
	/**
	 * BANDBOX_ATTR_VALUE.
	 */
	public static final String BANDBOX_ATTR_VALUE = "value";
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getLogger(PubUtil.class);
	/**
	 * PRIMITIVE.
	 */
	private static final String PRIMITIVE = "Primitive";
	/**
	 * OBJECT.
	 */
	private static final String OBJECT = "Object";
	/**
	 * DATE.
	 */
	private static final String DATE = "Date";
	/**
	 * INTEGER.
	 */
	private static final String INTEGER = "Integer";
	/**
	 * LONG.
	 */
	private static final String LONG = "Long";
	/**
	 * DOUBLE.
	 */
	private static final String DOUBLE = "Double";
	/**
	 * STRING.
	 */
	private static final String STRING = "String";

	/**
	 * .
	 */
	private PubUtil() {

	}

	/**
	 * 用bean来填充po .
	 * 
	 * @param bean
	 *            Object
	 * @param po
	 *            Object
	 * @return boolean
	 */
	public static boolean fillPoFromBean(Object bean, Object po) {
		Field[] beanFields = bean.getClass().getDeclaredFields();
		Field[] poFields = po.getClass().getDeclaredFields();
		try {
			// map
			Map<String, Field> beanFieldsMap = new HashMap<String, Field>();
			for (Field beanField : beanFields) {
				beanFieldsMap.put(beanField.getName(), beanField);
			}
			for (int i = 0; i < poFields.length; i++) {
				String poFieldName = poFields[i].getName();
				if (poFieldName.equals("serialVersionUID")) {
					continue;
				}
				String poFieldType = poFields[i].getGenericType().toString();
				if (poFieldType.equals("class java.lang.String")
						|| poFieldType.equals("class java.lang.Long")
						|| poFieldType.equals("class java.lang.Double")
						|| poFieldType.equals("class java.lang.Integer")
						|| poFieldType.equals("class java.util.Date")) {

					if (beanFieldsMap.containsKey(poFields[i].getName())) { // 成员变量名称相关
						Field beanField = beanFieldsMap.get(poFields[i]
								.getName());
						String beanFieldType = beanField.getGenericType()
								.toString();
						// 获取控件
						Object oComp = getComp(bean, beanField.getName(),
								beanFieldType);
						if (oComp != null) { // 存在匹配控件，进行赋值
							Object oUiBeanVal = getCompValue(oComp,
									beanFieldType, beanField.getName(), bean);
							setPoValue(po, poFields[i].getName(), poFieldType,
									oUiBeanVal);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
		}
		return true;
	}

	/**
	 * 用po来填充bean .
	 * 
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @return boolean
	 */
	public static boolean fillBeanFromPo(Object po, Object bean) {
		return fillBeanFromPo(po, bean, false, true);
	}

	/**
	 * 用po来填充bean.
	 * 
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @param useAttrFlag
	 *            boolean
	 * @return boolean
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static boolean fillBeanFromPo(Object po, Object bean,
			boolean useAttrFlag, boolean isSetNull) {
		try {
			Field[] beanFields = bean.getClass().getDeclaredFields();
			Field[] poFields = null;
			String className = po.getClass().getSimpleName();
			if ((className + "").indexOf("_$$_javassist_") > -1) {
				poFields = po.getClass().getSuperclass().getDeclaredFields();
			} else {
				poFields = po.getClass().getDeclaredFields();
			}
			for (int i = 0; i < beanFields.length; i++) {
				String beanFieldType = beanFields[i].getGenericType()
						.toString();
				if (isBasicZulType(beanFieldType)) {
					// map
					Map<String, Field> poFieldsMap = new HashMap<String, Field>();
					for (Field poField : poFields) {
						poFieldsMap.put(poField.getName(), poField);
					}
					if (poFieldsMap.containsKey(beanFields[i].getName())) { // 成员变量名称相关
						String poFieldName = beanFields[i].getName();
						Field poField = poFieldsMap.get(poFieldName);
						// 获取控件
						Object oComp = getComp(bean, beanFields[i].getName(),
								beanFieldType);
						if (oComp != null) { // 存在匹配控件，进行赋值
							setCompValue(oComp, beanFieldType,
									getPoValue(po, poField.getName()),
									poFieldName, po, bean, useAttrFlag,
									isSetNull);
						}
					}
				}
			}
			return true;
		} catch (Exception ex) {
			log.error("绑定报错", ex);
			return false;
		}
	}

	/**
	 * 判断是否zul基本类型.
	 * 
	 * @param type
	 *            String
	 * @return boolean
	 * @author huangjb 2011-2-9 huangjb
	 */
	private static boolean isBasicZulType(String type) {
		boolean flag = false;
		if (type != null
				&& (type.equals("class org.zkoss.zul.Textbox")
						|| type.equals("class org.zkoss.zul.Combobox")
						|| type.equals("class org.zkoss.zul.Listbox")
						|| type.equals("class org.zkoss.zul.Datebox")
						|| type.equals("class org.zkoss.zul.Intbox")
						|| type.equals("class org.zkoss.zul.Bandbox")
						|| type.equals("class org.zkoss.zul.Doublebox")
						|| type.equals("class org.zkoss.zul.Checkbox")
						|| type.equals("class org.zkoss.zul.Label") || type
							.equals("class org.zkoss.zul.Longbox"))) {
			flag = true;
		}
		return flag;
	}

	/**
	 * fillPoFromEditorBean.
	 * 
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @return boolean
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static boolean fillPoFromEditorBean(Object po, Object bean) {
		return fillPoFromBean(bean, po);
	}

	/**
	 * 用PO来填充编辑窗体Bean.
	 * 
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @return boolean
	 */
	public static boolean fillEditorBeanFromPo(Object po, Object bean) {
		return fillBeanFromPo(po, bean, true, true);
	}

	/**
	 * 对象值复制，要求对象域名称一致、类型一致。 .
	 * 
	 * @param orig
	 *            源对象
	 * @param dest
	 *            目标对象
	 * @return boolean
	 */
	public static boolean copyProperties(Object orig, Object dest) {
		try {
			Field[] origFields = orig.getClass().getDeclaredFields();
			Field[] destFields = dest.getClass().getDeclaredFields();
			for (int i = 0; i < destFields.length; i++) {
				String destFieldName = destFields[i].getName();
				String destFieldType = destFields[i].getGenericType()
						.toString();
				for (int k = 0; k < origFields.length; k++) {
					String origFieldName = origFields[k].getName();
					String origFieldType = origFields[k].getGenericType()
							.toString();
					if (destFieldName.equals(origFieldName)
							&& destFieldType.equals(origFieldType)) {
						Object origFieldValue = PropertyUtils.getProperty(orig,
								origFields[k].getName());
						PropertyUtils.setProperty(dest, destFieldName,
								origFieldValue);
						break;
					}
				}
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * 清除表单数据.
	 * 
	 * @param comp
	 *            组件
	 */
	@SuppressWarnings("unchecked")
	public static void clearComponent(Component comp) {
		List<Component> childrenList = (List<Component>) comp.getChildren();
		if (childrenList == null || childrenList.size() < 1) {
			return;
		}
		for (Component children : childrenList) {
			if (children instanceof InputElement) {
				InputElement inputElement = (InputElement) children;
				inputElement.setText("");
			} else if (children instanceof Listbox) {
				if ("select".equals(((Listbox) children).getMold())) {
					// 2011-06-02 刘壮飞 增加对下拉框的赋值
					if (((Listbox) children).getSelectedCount() > 0) {
						((Listbox) children).setSelectedIndex(0);
					}
				} else {
					PubUtil.clearListbox(((Listbox) children));
				}
			}
			// else if (children instanceof Grid) {
			// clearGrid((Grid) children);
			// }
			else {
				clearComponent(children);
			}
		}
	}

	/**
	 * .
	 * 
	 * @param comp
	 * @author zfz 2011-3-21 zfz
	 */
	public static void disableComponent(Component comp) {
		Collection<Component> childrenList = (Collection<Component>) comp
				.getFellows();
		if (childrenList == null || childrenList.size() < 1) {
			return;
		}
		for (Component children : childrenList) {
			if (children instanceof Button) {
				Button btn = (Button) children;
				btn.setDisabled(true);
			}
			if (children instanceof InputElement) {
				InputElement ipt = (InputElement) children;
				ipt.setReadonly(true);
			}
		}
	}

	/**
	 * .
	 * 
	 * @param comp
	 * @author zfz 2011-3-21 zfz
	 */
	public static void disableComponent(Component comp, boolean disable) {
		Collection<Component> childrenList = (Collection<Component>) comp
				.getFellows();
		if (childrenList == null || childrenList.size() < 1) {
			return;
		}
		for (Component children : childrenList) {
			if (children instanceof Button) {
				Button btn = (Button) children;
				btn.setDisabled(disable);
			}
			if (children instanceof InputElement) {
				InputElement ipt = (InputElement) children;
				ipt.setReadonly(disable);
			}
		}
	}

	/**
	 * 页面只读设置.
	 * 
	 * @param comp
	 *            comp
	 * @param readOnly
	 *            readOnly
	 * @author zhengcl 2011-3-24 zhengcl
	 */
	public static void readOnlyComponent(Component comp, boolean readOnly) {
		Collection<Component> childrenList = (Collection<Component>) comp
				.getFellows();
		if (childrenList == null || childrenList.size() < 1) {
			return;
		}
		for (Component children : childrenList) {
			if (children instanceof InputElement) {
				InputElement ipt = (InputElement) children;
				ipt.setReadonly(readOnly);
			}
			if (children instanceof Listbox) {
				Listbox lis = (Listbox) children;
				lis.setDisabled(readOnly);
			}
			if (children instanceof Bandbox) {
				Bandbox ban = (Bandbox) children;
				ban.setDisabled(readOnly);
			}
			if (children instanceof Button) {
				Button btn = (Button) children;
				btn.setVisible(!readOnly);
			}
			if (children instanceof Combobox) {
				Combobox cob = (Combobox) children;
				cob.setDisabled(readOnly);
			}
			if (children instanceof Datebox) {
				Datebox dat = (Datebox) children;
				dat.setDisabled(readOnly);
			}
		}
	}

	/**
	 * 对页面指定控件设置只读.
	 * 
	 * @param comp
	 * @param compType
	 * @param readOnly
	 * @author zhengcl 2011-4-18 zhengcl
	 */
	public static void readOnlyComponentByType(Component comp, String compType,
			boolean readOnly) {
		Collection<Component> childrenList = (Collection<Component>) comp
				.getFellows();
		if (childrenList == null || childrenList.size() < 1) {
			return;
		}
		for (Component children : childrenList) {
			if (compType.equals("InputElement")
					&& children instanceof InputElement) {
				InputElement ipt = (InputElement) children;
				ipt.setReadonly(readOnly);
			}
			if (compType.equals("Listbox") && children instanceof Listbox) {
				Listbox lis = (Listbox) children;
				lis.setDisabled(readOnly);
			}
			if (compType.equals("Bandbox") && children instanceof Bandbox) {
				Bandbox ban = (Bandbox) children;
				ban.setDisabled(readOnly);
			}
			if (compType.equals("Button") && children instanceof Button) {
				Button btn = (Button) children;
				btn.setVisible(!readOnly);
			}
			if (compType.equals("Combobox") && children instanceof Combobox) {
				Combobox cob = (Combobox) children;
				cob.setDisabled(readOnly);
			}
			if (compType.equals("Datebox") && children instanceof Datebox) {
				Datebox dat = (Datebox) children;
				dat.setDisabled(readOnly);
			}
		}
	}

	/**
	 * ------------------------------------------- 获取控件对象：
	 * -------------------------------------------
	 */

	/**
	 * getComp.
	 * 
	 * @param uiBean
	 *            Object
	 * @param compName
	 *            String
	 * @param compType
	 *            String
	 * @return Object
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static Object getComp(Object uiBean, String compName, String compType) {
		if (logger.isDebugEnabled()) {
			logger.debug("==getComp compName:" + compName + ",compType:"
					+ compType + ",ui bean:" + uiBean);
		}
		if (uiBean == null) {
			throw new RtManagerException("待获取控件bean为空，请确认", PubUtil.class,
					"getCompValue", "uiBeanNull");
		} else if (compName == null || "".equals(compName)) {
			throw new RtManagerException("待获取控件compName为空，请确认", PubUtil.class,
					"getCompValue", "compNameNull");

		} else if (compType == null || "".equals(compType)) { // 控件类型
			throw new RtManagerException("控件类型为空，请确认", PubUtil.class,
					"getCompValue", "compTypeNull");
		}
		Object oComp = null;
		try {
			oComp = PropertyUtils.getProperty(uiBean, compName);
		} catch (IllegalAccessException e) {
			throw new RtManagerException(
					"控件uiBean:@uiBean,compName:@compName,compType@compType,错误，IllegalAccessException，请确认",
					PubUtil.class, "setPoValue", "IllegalAccessException",
					"@uiBean", uiBean, "@compName", compName, "@compType",
					compType);
		} catch (InvocationTargetException e) {
			throw new RtManagerException(
					"填充uiBean:@uiBean,compName:@compName,compType@compType,错误，InvocationTargetException，请确认",
					PubUtil.class, "setPoValue", "InvocationTargetException",
					"@uiBean", uiBean, "@compName", compName, "@compType",
					compType);
		} catch (NoSuchMethodException e) {
			throw new RtManagerException(
					"填充uiBean:@uiBean,compName:@compName,compType@compType,错误，没有赋值方法，请确认",
					PubUtil.class, "setPoValue", "NoSuchMethodException",
					"@uiBean", uiBean, "@compName", compName, "@compType",
					compType);
		}
		return oComp instanceof Component ? oComp : null;
	}

	/**
	 * ------------------------------------------- 获取控件值：
	 * -------------------------------------------
	 */
	/**
	 * getCompValue.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param compName
	 *            String
	 * @param bean
	 *            Object
	 * @return Object
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static Object getCompValue(Object oComp, String compType,
			String compName, Object bean) {
		if (logger.isDebugEnabled()) {
			logger.debug("getCompValue comp:" + oComp + ",compType:" + compType);
		}
		if (oComp == null) {
			throw new RtManagerException("待获取控件为空compType@compType，请确认",
					PubUtil.class, "setPoValue", "compNull", "@compType",
					compType);
		} else if (oComp instanceof Component) { // 控件
			if ("class org.zkoss.zul.Textbox".equals(compType)) {
				return ((Textbox) oComp).getValue();
			} else if ("class org.zkoss.zul.Intbox".equals(compType)) {
				return ((Intbox) oComp).getValue();
			} else if ("class org.zkoss.zul.Longbox".equals(compType)) {
				return ((Longbox) oComp).getValue();
			} else if ("class org.zkoss.zul.Doublebox".equals(compType)) {
				return ((Doublebox) oComp).getValue();
			} else if ("class org.zkoss.zul.Combobox".equals(compType)) {
				Comboitem citem = ((Combobox) oComp).getSelectedItem();
				return citem == null ? null : citem.getValue();
			} else if ("class org.zkoss.zul.Listbox".equals(compType)) {
				Listitem litem = ((Listbox) oComp).getSelectedItem();
				return litem == null ? null : litem.getValue();
			} else if ("class org.zkoss.zul.Bandbox".equals(compType)) {
				Bandbox bbComp = (Bandbox) oComp;
				String bandName = getBeanVal(bean, compName + BANDBOX_ATTR_NAME);
				if (bbComp.hasAttribute(BANDBOX_ATTR_VALUE) || bandName != null) { // 优先采用attrbute的value
					return bbComp.getAttribute(BANDBOX_ATTR_VALUE);
				} else {
					return bbComp.getValue();
				}
			} else if ("class org.zkoss.zul.Datebox".equals(compType)) {
				return ((Datebox) oComp).getValue();
			} else if ("class org.zkoss.zul.Checkbox".equals(compType)) {
				Checkbox checkBox = (Checkbox) oComp;
				if (checkBox.isChecked()) {
					return ((Checkbox) oComp).getValue();
				} else {
					return "";
				}
			} else {
				// null
			}
		}
		// 其他非控件忽略
		return null;
	}

	/**
	 * ------------------------------------------- 设置控件值：
	 * -------------------------------------------
	 */
	/**
	 * .
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 * @param propName
	 *            String
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @param useAttrFlag
	 *            boolean
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static void setCompValue(Object oComp, String compType, Object val,
			String propName, Object po, Object bean, boolean useAttrFlag,
			boolean isSetNull) {
		if (logger.isDebugEnabled()) {
			logger.debug("setCompValue comp:" + oComp + ",compType:" + compType
					+ ",val:" + val);
		}
		if (oComp == null) {
			throw new RtManagerException(
					"待设置控件为空compType@compType,val@val，请确认", PubUtil.class,
					"setPoValue", "compNull", "@compType", compType, "@val",
					val);
		} else if (oComp instanceof Component) { // 控件
			if (StrUtil.strnull(val).equals("") && !isSetNull) {
				return;
			}
			if ("class org.zkoss.zul.Textbox".equals(compType)) {
				setCompValueTextbox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Intbox".equals(compType)) {
				setCompValueIntbox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Longbox".equals(compType)) {
				setCompValueLongbox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Doublebox".equals(compType)) {
				setCompValueDoublebox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Combobox".equals(compType)) {
				setCompValueCombobox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Listbox".equals(compType)) {
				setCompValueListbox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Label".equals(compType)) {
				setCompValueLabel(oComp, compType, val);
			} else if ("class org.zkoss.zul.Bandbox".equals(compType)) {
				setCompValueBandbox(oComp, compType, val, propName, po, bean,
						useAttrFlag);
			} else if ("class org.zkoss.zul.Datebox".equals(compType)) {
				setCompValueDatebox(oComp, compType, val);
			} else if ("class org.zkoss.zul.Checkbox".equals(compType)) {
				setCompValueCheckbox(oComp, compType, val);
			} else {
				// null
			}
		}
		// 其他非控件忽略
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueTextbox(Object oComp, String compType,
			Object val) {
		try {
			Textbox textbox = ((Textbox) oComp);
			textbox.setValue(val == null ? null : val + "");
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueCheckbox(Object oComp, String compType,
			Object val) {
		if ("Y".equals(val)) {
			((Checkbox) oComp).setChecked(true);
		} else {
			((Checkbox) oComp).setChecked(false);
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueLabel(Object oComp, String compType,
			Object val) {
		((Label) oComp).setValue(val == null ? null : val + "");
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueIntbox(Object oComp, String compType,
			Object val) {
		try {
			int ival = (val == null ? null : NumericUtil.toInt(val + ""));
			((Intbox) oComp).setValue(ival);
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueLongbox(Object oComp, String compType,
			Object val) {
		try {
			Long lval = (val == null ? null : NumericUtil.toLong(val + ""));
			((Longbox) oComp).setValue(lval);
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueDoublebox(Object oComp, String compType,
			Object val) {
		try {
			Double lval = (val == null ? null : NumericUtil.toDouble(val + ""));
			((Doublebox) oComp).setValue(lval);
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueDatebox(Object oComp, String compType,
			Object val) {
		try {
			Date dval = null;
			if (val instanceof Date) {
				dval = (Date) val;
			} else {
				dval = DateUtil.strToDate(val + "");
			}
			((Datebox) oComp).setValue(dval);
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueListbox(Object oComp, String compType,
			Object val) {
		try {
			ListboxUtils.selectByCodeValue((Listbox) oComp, val == null ? null
					: val + "");
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueText.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 */
	public static void setCompValueCombobox(Object oComp, String compType,
			Object val) {
		try {
			ComboboxUtils.setSelected((Combobox) oComp, val == null ? null
					: val + "");
		} catch (Exception ex) {
		}
	}

	/**
	 * setCompValueBandbox.
	 * 
	 * @param oComp
	 *            Object
	 * @param compType
	 *            String
	 * @param val
	 *            Object
	 * @param propName
	 *            String
	 * @param po
	 *            Object
	 * @param bean
	 *            Object
	 * @param useAttrFlag
	 *            boolean
	 * @author wuyx 2011-2-11 wuyx
	 */
	public static void setCompValueBandbox(Object oComp, String compType,
			Object val, String propName, Object po, Object bean,
			boolean useAttrFlag) {
		try {
			Bandbox box = (Bandbox) oComp;
			boolean isExitProp = false;
			try {
				PropertyUtils.getProperty(po, propName + BANDBOX_ATTR_NAME);
				isExitProp = true;
			} catch (Exception e) {
			}
			if (useAttrFlag || isExitProp) {
				Object nameval = getPoValue(po, propName + BANDBOX_ATTR_NAME);
				String bandName = getBeanVal(bean, propName + BANDBOX_ATTR_NAME);
				box.setAttribute(BANDBOX_ATTR_VALUE, val);
				box.setValue(nameval == null ? "" : nameval + "");
			} else {
				box.setValue(val == null ? "" : val + "");
			}
		} catch (Exception ex) {
		}
	}

	/**
	 * ------------------------------------------- 获取对象值：
	 * -------------------------------------------
	 */

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @return Object
	 */
	public static Object getPoValue(Object po, String propName) {
		if (logger.isDebugEnabled()) {
			logger.debug("==getComp propName:" + propName + ",po:" + po);
		}
		if (po == null) {
			throw new RtManagerException("待获取控件po为空，请确认", PubUtil.class,
					"getCompValue", "poNull");
		} else if (propName == null || "".equals(propName)) {
			throw new RtManagerException("待获取控件propName为空，请确认", PubUtil.class,
					"getCompValue", "propNameNull");

		}
		Object oVal = null;
		try {
			oVal = PropertyUtils.getProperty(po, propName);
		} catch (IllegalAccessException e) {
			throw new RtManagerException(
					"控件po:@po,propName:@propName,错误，取值IllegalAccessException，请确认",
					PubUtil.class, "setPoValue", "IllegalAccessException",
					"@po", po, "@propName", propName);
		} catch (InvocationTargetException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,错误，取值InvocationTargetException，请确认",
					PubUtil.class, "setPoValue", "InvocationTargetException",
					"@po", po, "@propName", propName);
		} catch (NoSuchMethodException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,错误，没有取值方法，请确认", PubUtil.class,
					"setPoValue", "NoSuchMethodException", "@po", po,
					"@propName", propName);
		}
		return oVal;
	}

	/**
	 * ------------------------------------------- 设置对象值：
	 * -------------------------------------------
	 */

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 */
	public static void setPoValue(Object po, String propName, String propType,
			Object val) {
		if (logger.isDebugEnabled()) {
			logger.debug("setPoValue propName:" + propName + ",propType:"
					+ propType + ",val:" + val + ",po:" + po);
		}
		try {
			if (po == null) {
				throw new RtManagerException("待填充po为空，请确认", PubUtil.class,
						"setPoValue", "poNull");
			} else if (propName == null || "".equals(propName)) {
				throw new RtManagerException("待填充propName为空，请确认",
						PubUtil.class, "setPoValue", "propNameNull");
			} else if (propType == null || "".equals(propType)) {
				throw new RtManagerException("待填充propType为空，请确认",
						PubUtil.class, "setPoValue", "propTypeNull");
			} else if ("class java.lang.String".equals(propType)) {
				setPoValueString(po, propName, val);
			} else if ("class java.lang.Integer".equals(propType)) {
				setPoValueInteger(po, propName, val);
			} else if ("class java.lang.Long".equals(propType)) {
				setPoValueLong(po, propName, val);
			} else if ("class java.lang.Double".equals(propType)) {
				setPoValueDouble(po, propName, val);
			} else if ("class java.util.Date".equals(propType)) {
				setPoValueDate(po, propName, val);
			} else {
				// null
			}
		} catch (ParseException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,propType@propType,val:@val错误，ParseException，请确认",
					PubUtil.class, "setPoValue", "ParseException", "@po", po,
					"@propName", propName, "@propType", propType, "@val", val);
		} catch (IllegalAccessException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,propType@propType,val:@val错误，IllegalAccessException，请确认",
					PubUtil.class, "setPoValue", "IllegalAccessException",
					"@po", po, "@propName", propName, "@propType", propType,
					"@val", val);
		} catch (InvocationTargetException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,propType@propType,val:@val错误，InvocationTargetException，请确认",
					PubUtil.class, "setPoValue", "InvocationTargetException",
					"@po", po, "@propName", propName, "@propType", propType,
					"@val", val);
		} catch (NoSuchMethodException e) {
			throw new RtManagerException(
					"填充po:@po,propName:@propName,propType@propType,val:@val错误，没有赋值方法，请确认",
					PubUtil.class, "setPoValue", "NoSuchMethodException",
					"@po", po, "@propName", propName, "@propType", propType,
					"@val", val);
		}
	}

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void setPoValueString(Object po, String propName, Object val)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		final String tp = getValType(val);
		if (tp == null) {
			PropertyUtils.setProperty(po, propName, null);
		} else if (PRIMITIVE.equals(tp)) {
			PropertyUtils.setProperty(po, propName, tp + "");
		} else {
			PropertyUtils.setProperty(po, propName, val.toString());
		}
	}

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void setPoValueInteger(Object po, String propName, Object val)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// Integer integerVal = NumericUtil.nullToIntegerZero(val);
		PropertyUtils.setProperty(po, propName, val);
	}

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void setPoValueLong(Object po, String propName, Object val)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Long longVal = (val == null ? null : NumericUtil.toLong(val + ""));
		PropertyUtils.setProperty(po, propName, longVal);
	}

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void setPoValueDouble(Object po, String propName, Object val)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		// Double doubleVal = NumericUtil.nullToDoubleZero(val);
		PropertyUtils.setProperty(po, propName, val);
	}

	/**
	 * setPoValue.
	 * 
	 * @param po
	 *            待设置值对象
	 * @param propName
	 *            属性名称
	 * @param propType
	 *            属性类型
	 * @throws ParseException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static void setPoValueDate(Object po, String propName, Object val)
			throws ParseException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		// Date dateVal = DateUtil.convertStringToDate(val + "");
		PropertyUtils.setProperty(po, propName, val);
	}

	/**
	 * ------------------------------------------- 判断数据类型
	 * -------------------------------------------
	 */
	public static String getValType(Object val) {
		if (val == null) {
			return null;
		} else if (val instanceof String) {
			return STRING;
		} else if (val instanceof Long) {
			return LONG;
		} else if (val instanceof Double) {
			return DOUBLE;
		} else if (val instanceof Integer) {
			return INTEGER;
		} else if (val instanceof Date) {
			return DATE;
		} else if (val instanceof Object) {
			return OBJECT;
		}
		return PRIMITIVE;
	}

	/**
	 * getBeanVal.
	 * 
	 * @param bean
	 *            Object
	 * @param propName
	 *            propName
	 * @return String
	 */
	public static String getBeanVal(Object bean, String propName) {
		Object ov;
		try {
			ov = PropertyUtils.getProperty(bean, propName);
			return ov == null ? null : ov + "";
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
		return null;
	}

	/**
	 * 清除listbox.
	 * 
	 * @param listbox
	 */
	public static void clearListbox(Listbox listbox) {
		while (listbox.getItemCount() > 0) {
			listbox.removeItemAt(listbox.getItemCount() - 1);
		}
	}

	/**
	 * 清除listbox.
	 * 
	 * @param listbox
	 */
	public static void clearGrid(Grid grid) {
		// List list = grid.getRows().getChildren();
		grid.getRows().getChildren().clear();
		// if (list != null && list.size() > 0) {
		//
		// }
	}

	/**
	 * 方法功能: 把textbox置为只读.
	 * 
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-5-20 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void setTextboxReadOnly(Component cmp) {
		Iterator it = cmp.getFellows().iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof Textbox) {
				((Textbox) obj).setReadonly(true);
			}
		}
	}

	/**
	 * 根据str字符串内容定位树(查询结果提示). .
	 * 
	 * @param tree
	 *            树
	 * @param str
	 *            字符串内容
	 * @param strBuf
	 *            查询结果
	 * @author g.huangwch 2011-9-20 g.huangwch
	 */
	public static void searchTree(Tree tree, String str, StringBuffer strBuf) {

		// 2011-09-29 linzhiqiang ttp4484 重置 树
		reSetTreeitem(tree);
		// setAllOpen(tree); //2011-10-18 linzhiqiang 废弃这个方法，影响效率

		int searchAmount = 0;
		if (tree.getAttribute("lastSelectedIndex") == null) {
			tree.setAttribute("lastSelectedIndex", -1);
		}
		int lastSelectedIndex = NumericUtil.nullToIntegerZero(tree
				.getAttribute("lastSelectedIndex"));
		Collection<Treeitem> treeitems = tree.getItems();
		if (treeitems == null) {
			strBuf.append("没有找到！");
			return;
		}
		int loopIdx = 0;
		while (loopIdx < 2) {
			int i = 0;
			boolean selectedFlag = false;
			for (Treeitem treeitem : treeitems) {
				if (!treeitem.isDisabled()) {
					// 2011-10-18 linzhiqiang 设置打开
					treeitem.setOpen(true);
					if (i > lastSelectedIndex || loopIdx == 1) {
						Treecell treecell = (Treecell) treeitem.getTreerow()
								.getChildren().get(0);
						if (treecell.getLabel() != null
								&& treecell.getLabel().indexOf(str) >= 0) {
							treeitem.setSelected(true);
							selectedFlag = true;
							tree.setAttribute("lastSelectedIndex", i);
							setTreeitemOpen(treeitem);
							searchAmount++;
						}
					}

				}

				// 2011-09-29 林志强 ttp4540 隐藏搜索结果中不相关的结果
				if (!treeitem.isSelected()) {
					treeitem.setVisible(false);
				}
				if (!treeitem.isDisabled() && treeitem.isSelected()) {
					setParentVisiable(treeitem);
					// 2011-10-25 linzhiqiang 设置关联选择
					setRelaParentSelect(treeitem, true);
				}

				i++;
			}

			if (selectedFlag) {
				break;
			}
			loopIdx++;
		}

		if (searchAmount > 0) {
			strBuf.append("找到:").append(searchAmount).append("个");
		} else {
			strBuf.append("没有找到！");
		}
	}

	/**
	 * 方法功能: .递归设置 显示 的属性
	 * 
	 * @param treeitem
	 * @author: 林志强
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-9-29 林志强 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void setParentVisiable(Treeitem treeitem) {
		Treeitem item = (Treeitem) treeitem.getParentItem();
		if (item != null) {
			item.setVisible(true);
			// item.setOpen(true);
			setParentVisiable(item);
		}
	}

	/**
	 * 方法功能: 将相关的父选项勾选或取消.
	 * 
	 * @param treeitem
	 * @param flag
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-25 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void setRelaParentSelect(Treeitem treeitem, boolean flag) {
		Treeitem item = (Treeitem) treeitem.getParentItem();
		if (item != null && item.isVisible() && !item.isDisabled()) {
			if (flag) {
				item.setSelected(flag);
				item.setOpen(flag);
				setRelaParentSelect(item, flag);
			} else {
				if (!hasBrother(item)) {
					item.setSelected(flag);
					item.setOpen(flag);
					setRelaParentSelect(item, flag);
				}
			}

		}
	}

	/**
	 * 方法功能: 判断是否有兄弟结点是选中状态.
	 * 
	 * @param treeitem
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-25 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static boolean hasBrother(Treeitem treeitem) {
		Collection<Treeitem> items = treeitem.getTreechildren().getItems();
		int level = treeitem.getLevel();
		if (items != null || items.size() > 0) {
			for (Treeitem item : items) {
				if (item.getLevel() == (level + 1) && item.isSelected())
					return true;
			}
		}
		return false;
	}

	/**
	 * 方法功能: 选中相关的子项 .
	 * 
	 * @param treeitem
	 * @param flag
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-25 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void setRelaChildSelect(Treeitem treeitem, boolean flag) {
		if (treeitem == null || treeitem.getTreechildren() == null) {
			return;
		}
		Collection<Treeitem> items = treeitem.getTreechildren().getItems();
		if (items != null || items.size() > 0) {
			if (flag) {
				treeitem.setOpen(flag);
			}
			for (Treeitem item : items) {
				if ((item.isVisible() && !item.isDisabled())
						|| (!flag && item.isSelected())) {
					item.setSelected(flag);
					Treechildren children = item.getTreechildren();
					if (children != null && children.getItemCount() > 0) {
						if (flag != item.isOpen()) {
							item.setOpen(flag);
						}
					}

				}
			}
			if (!flag) {
				treeitem.setOpen(flag);
			}
		}
	}

	/**
	 * 方法功能: 将整颗树全部打开.
	 * 
	 * @param tree
	 * @author: 林志强
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-9-30 林志强 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void setAllOpen(Tree tree) {
		Collection<Treeitem> treeitems = tree.getItems();
		for (Treeitem treeitem : treeitems) {
			if (!treeitem.isDisabled()) {
				treeitem.setOpen(true);
			}
		}
	}

	/**
	 * 方法功能: .将树 除了 标记位 其余的状态重置
	 * 
	 * @param tree
	 * @author: 林志强
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-9-29 林志强 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static void reSetTreeState(Tree tree) {
		Collection<Treeitem> treeitems = tree.getItems();
		if (treeitems == null) {
			return;
		}
		for (Treeitem treeitem : treeitems) {
			treeitem.setSelected(false);
			treeitem.setOpen(false);
			treeitem.setVisible(true);
		}
	}

	/**
	 * 根据str字符串内容定位树.
	 * 
	 * @param tree
	 *            树
	 * @param str
	 *            字符串内容
	 * @author zfz 2011-4-23 zfz
	 */
	public static void searchTree(Tree tree, String str) {
		// reSetTreeitem(tree);
		// setAllOpen(tree);

		if (tree.getAttribute("lastSelectedIndex") == null) {
			tree.setAttribute("lastSelectedIndex", -1);
		}
		int lastSelectedIndex = NumericUtil.nullToIntegerZero(tree
				.getAttribute("lastSelectedIndex"));
		Collection<Treeitem> treeitems = tree.getItems();
		if (treeitems == null) {
			return;
		}
		int loopIdx = 0;
		while (loopIdx < 2) {
			int i = 0;
			boolean selectedFlag = false;
			for (Treeitem treeitem : treeitems) {
				if (!treeitem.isDisabled()) {
					if (i > lastSelectedIndex || loopIdx == 1) {
						Treecell treecell = (Treecell) treeitem.getTreerow()
								.getChildren().get(0);
						if (treecell.getLabel() != null
								&& treecell.getLabel().indexOf(str) >= 0) {
							treeitem.setSelected(true);
							selectedFlag = true;
							tree.setAttribute("lastSelectedIndex", i);
							setTreeitemOpen(treeitem);
							break;
						}
					}
				}
				i++;
			}
			if (selectedFlag) {
				break;
			}
			loopIdx++;
		}
	}

	/**
	 * 设置树项的展开(从本身树项到顶级根目录树项都展开) .
	 * 
	 * @param treeitem
	 *            treeitem
	 * @author g.huangwch 2011-9-7 g.huangwch
	 */
	private static void setTreeitemOpen(Treeitem treeitem) {
		if (!treeitem.isOpen()) {
			treeitem.setOpen(true);
			if (treeitem.getParentItem() != null) {
				setTreeitemOpen(treeitem.getParentItem());
			}
		}
	}

	/**
	 * 将树的树项设置不选中及缩起树项 .
	 * 
	 * @param tree
	 *            tree
	 * @author g.huangwch 2011-9-20 g.huangwch
	 */
	public static void reSetTreeitem(Tree tree) {
		Collection<Treeitem> treeitems = tree.getItems();
		if (treeitems == null) {
			return;
		}
		for (Treeitem treeitem : treeitems) {
			treeitem.setSelected(false);
			tree.setAttribute("lastSelectedIndex", -1);
			treeitem.setOpen(false);

			// 2011-09-29 linzhiqiang ttp4540 设置显示属性
			treeitem.setVisible(true);
		}
	}

	/**
	 * .
	 * 
	 * @param listbox
	 * @param str
	 * @author zhangfzh 2011-6-21 zhangfzh
	 */
	public static void searchListbox(Listbox listbox, String str) {
		if (listbox.getAttribute("lastSelectedIndex") == null) {
			listbox.setAttribute("lastSelectedIndex", -1);
		}
		int lastSelectedIndex = NumericUtil.nullToIntegerZero(listbox
				.getAttribute("lastSelectedIndex"));
		Collection<Listitem> listitems = listbox.getItems();
		if (listitems == null) {
			return;
		}
		int loopIdx = 0;
		while (loopIdx < 2) {
			int i = 0;
			boolean selectedFlag = false;
			for (Listitem listitem : listitems) {
				if (i > lastSelectedIndex || loopIdx == 1) {
					for (Listcell listcell : (List<Listcell>) listitem
							.getChildren()) {
						if (listcell.getLabel().indexOf(str) >= 0) {
							listitem.setSelected(true);
							selectedFlag = true;
							listbox.setAttribute("lastSelectedIndex", i);
							return;
						}
					}
				}
				i++;
			}
			loopIdx++;
		}
	}

	public static void searchListbox(Listbox listbox, String str, int cellIndex) {
		if (listbox.getAttribute("lastSelectedIndex") == null) {
			listbox.setAttribute("lastSelectedIndex", -1);
		}
		int lastSelectedIndex = NumericUtil.nullToIntegerZero(listbox
				.getAttribute("lastSelectedIndex"));
		Collection<Listitem> listitems = listbox.getItems();
		if (listitems == null) {
			return;
		}
		int loopIdx = 0;
		while (loopIdx < 2) {
			int i = 0;
			boolean selectedFlag = false;
			for (Listitem listitem : listitems) {
				if (i > lastSelectedIndex || loopIdx == 1) {
					List<Listcell> cells = (List<Listcell>) listitem
							.getChildren();
					if (cells.get(cellIndex) != null
							&& cells.get(cellIndex).getLabel().indexOf(str) >= 0) {
						listitem.setSelected(true);
						selectedFlag = true;
						listbox.setAttribute("lastSelectedIndex", i);
						return;
					}
				}

				i++;
			}
			loopIdx++;
		}
	}

	/**
	 * .
	 * 
	 * @param combobox
	 * @param clearItem
	 * @author zhangfzh 2011-7-4 zhangfzh
	 */
	public static void clearComboitem(Combobox combobox, String clearItem) {
		List<String> clearItems = new ArrayList<String>();
		clearItems.add(clearItem);
		clearComboitem(combobox, clearItems);
	}

	/**
	 * .
	 * 
	 * @param combobox
	 * @param clearItems
	 * @author zhangfzh 2011-7-4 zhangfzh
	 */
	public static void clearComboitem(Combobox combobox, List<String> clearItems) {
		List<Comboitem> items = combobox.getItems();
		if (items == null) {
			return;
		}
		for (String clearItem : clearItems) {
			for (Comboitem item : items) {
				if (clearItem.equals(item.getValue())) {
					item.detach();
					break;
				}
			}
		}
	}

	/**
	 * 方法功能: 获取att_spec指定的字段field值为code的列表.
	 * 
	 * @param code
	 * @return
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-10-27 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static List<AttrSpec> getAttrSpecFieldList(List<AttrSpec> list,
			String field, String code) {
		List<AttrSpec> rList = new ArrayList<AttrSpec>();
		if (list == null || list.size() < 1 || code == null) {
			return list;
		}
		for (AttrSpec spec : list) {
			try {
				Object obj = PropertyUtils.getNestedProperty(spec, field);
				if (obj != null && obj.toString().indexOf(code + ";") != -1) {
					rList.add(spec);
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
		}

		return rList;
	}

	/**
	 * 方法功能: 将 oriMap中值后面加上sep.
	 * 
	 * @param tarMap
	 * @param oriMap
	 * @param sep
	 *            分隔符
	 * @return
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-11-2 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Map appendMapVal(Map oriMap, String sep) {
		if (oriMap == null || oriMap.size() < 1) {
			return oriMap;
		}
		Iterator it = oriMap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if (oriMap.get(key) != null && oriMap.get(key) instanceof String) {
				oriMap.put(key, oriMap.get(key) + sep);
			}
		}
		return oriMap;
	}

	/**
	 * 方法功能: 将 tarMap与oriMap中key相同的值叠加起来.
	 * 
	 * @param tarMap
	 * @param oriMap
	 * @param sep
	 *            分隔符
	 * @return
	 * @author: chenmq
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-11-2 chenmq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Map<Object, Object> appendMapVal(Map tarMap, Map oriMap,
			String sep) {
		if (tarMap == null || tarMap.size() < 1) {
			return tarMap;
		}
		Iterator it = tarMap.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			if (oriMap.get(key) != null && oriMap.get(key) instanceof String) {
				tarMap.put(key, tarMap.get(key) + sep + oriMap.get(key) + sep);
			}
		}
		return tarMap;
	}

	/**
	 * 方法功能: 将字节数组 转化成字符串 如 ｛1，2，3｝ 转化成 123.
	 * 
	 * @param b
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2011-12-24 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String byteArrayToString(byte[] b) {
		StringBuffer str = new StringBuffer();
		if (b != null && b.length > 0) {
			for (int i = 0; i < b.length; i++) {
				str.append(b[i]);
			}
		}
		return str.toString();
	}

	/**
	 * 方法功能: post页面信息.
	 * 
	 * @param pageUrl
	 * @param content
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-1-6 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String postPage(String pageUrl, String content) {

		String res = "";
		try {
			URL url;
			HttpURLConnection urlConn;
			DataOutputStream printout;
			BufferedReader input;
			url = new URL(pageUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestMethod("POST");
			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 发送request
			urlConn.setRequestProperty("Content-Length", content.length() + "");
			printout = new DataOutputStream(urlConn.getOutputStream());
			byte[] pp = content.getBytes("GBK");
			printout.writeBytes(content);
			printout.flush();
			printout.close();
			// 取response
			input = new BufferedReader(new InputStreamReader(
					urlConn.getInputStream()));
			String str = "";
			int count;
			char[] chs = new char[1024000];
			while ((count = input.read(chs)) != -1) {
				str += new String(chs);
			}
			res = str.trim();
			input.close();
		} catch (Exception ex) {

		}
		return res;
	}

	/**
	 * 方法功能: 创建随机数.
	 * 
	 * @param size
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-4-17 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String createRandom(int size) {
		String randNum = "";

		for (int i = 0; i < size; i++) {
			int rand = (int) (Math.random() * 10);
			randNum += rand;
		}
		return randNum.substring(0, size);
	}

	/**
	 * 列表中创建label单元，并赋值.
	 */
	public static void createLabelCell(Listitem item, String value, String width) {
		Listcell lc = new Listcell();
		Label lb = new Label();
		if (!StrUtil.isEmpty(value)) {
			lb.setValue(value);
		}
		if (!StrUtil.isEmpty(width)) {
			lb.setWidth(width);
		} else {
			lb.setWidth("98%");
		}
		lb.setParent(lc);
		lc.setParent(item);
	}

	/**
	 * 列表中创建Textbox单元，并赋值.
	 */
	public static Textbox createTextCell(Listitem item, String value) {
		return createTextCell(item, value, null);
	}

	public static Textbox createTextCell(Listitem item, String value,
			String width) {
		Listcell lc = new Listcell();
		Textbox tb = new Textbox();
		if (!StrUtil.isEmpty(value)) {
			tb.setValue(value);
		}
		if (!StrUtil.isEmpty(width)) {
			tb.setWidth(width);
		} else {
			tb.setWidth("98%");
		}
		tb.setParent(lc);
		lc.setParent(item);
		return tb;
	}

	/**
	 * 方法功能: 生成文本框并添加监听时间.
	 * 
	 * @param item
	 * @param value
	 * @param width
	 * @param orgEvent
	 * @param targetEvent
	 * @param readOnly
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-2-3 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static Textbox createTextCell(Listitem item, String value,
			String width, String orgEvent, Component target,
			String targetEvent, boolean readOnly) {
		Listcell lc = new Listcell();

		Textbox tb = new Textbox();
		if (!StrUtil.isEmpty(value)) {
			tb.setValue(value);
		}
		if (!StrUtil.isEmpty(width)) {
			tb.setWidth(width);
		} else {
			tb.setWidth("98%");
		}

		tb.setReadonly(readOnly);
		tb.addForward(orgEvent, target, targetEvent);
		tb.setParent(lc);
		lc.setParent(item);

		lc.setParent(item);
		return tb;
	}

	/**
	 * 列表中创建intTextbox单元，并赋值.
	 */
	public static void createIntTextCell(Listitem item, int value) {
		createIntTextCell(item, value, null);
	}

	public static void createIntTextCell(Listitem item, int value, String width) {
		Listcell lc = new Listcell();
		Intbox intBox = new Intbox();
		if (!StrUtil.isEmpty(String.valueOf(value))) {
			intBox.setValue(value);
		}
		if (!StrUtil.isEmpty(width)) {
			intBox.setWidth(width);
		} else {
			intBox.setWidth("98%");
		}
		intBox.setParent(lc);
		lc.setParent(item);
	}

	/**
	 * 列表中创建Combobox单元，并赋值.
	 */
	public static Listcell createComboboxCell(Listitem item,
			List<NodeVo> comboboxValueList, String value) {
		return createComboboxCell(item, comboboxValueList, value, null);
	}

	public static Listcell createComboboxCell(Listitem item,
			List<NodeVo> comboboxValueList, String value, String width) {
		Listcell lc = new Listcell();
		Combobox cb = new Combobox();
		ComboboxUtils.rendererForEdit(cb, comboboxValueList);
		cb.setValue(value);
		if (!StrUtil.isEmpty(width)) {
			cb.setWidth(width);
		} else {
			cb.setWidth("99%");
		}
		cb.setReadonly(true);
		cb.setParent(lc);
		lc.setParent(item);
		return lc;
	}

	public static Listcell createComboboxCell(Listitem item,
			List<NodeVo> comboboxValueList, String value, String width,
			boolean disable, boolean readonly) {
		Listcell lc = new Listcell();
		Combobox cb = new Combobox();
		cb.setReadonly(readonly);
		cb.setDisabled(disable);
		ComboboxUtils.rendererForEdit(cb, comboboxValueList);
		ComboboxUtils.setSelected(cb, value);
		if (!StrUtil.isEmpty(width)) {
			cb.setWidth(width);
		} else {
			cb.setWidth("99%");
		}
		cb.setReadonly(true);
		cb.setParent(lc);
		lc.setParent(item);
		return lc;
	}

	/**
	 * 从编辑单元获取输入框值.
	 * 
	 * @param cell
	 *            Listcell
	 * @return String
	 */
	public static String getTextValueFromCell(Listcell cell) {
		String value = "";
		Object valObj = ((Textbox) cell.getFirstChild()).getValue();
		if (valObj != null) {
			value = valObj + "";
		}
		return value;
	}

	/**
	 * 方法功能: 获取控件的attribute值.
	 * 
	 * @param cell
	 * @param attr
	 * @return
	 * @author: linzhiqiang
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-2-3 linzhiqiang 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static String getTextAttrFromCell(Listcell cell, String attr) {
		String value = "";
		Object valObj = ((Textbox) cell.getFirstChild()).getAttribute(attr);
		if (valObj != null) {
			value = valObj + "";
		}
		return value;
	}

	/**
	 * 从编辑单元获取int输入框值.
	 * 
	 * @param cell
	 *            Listcell
	 * @return String
	 */
	public static int getIntTextValueFromCell(Listcell cell) {
		int value;
		return ((Intbox) cell.getFirstChild()).getValue();
	}

	/**
	 * 从编辑单元获取选择框选中值.
	 * 
	 * @param cell
	 *            Listcell
	 * @return String
	 */
	public static String getComboboxSelValueFromCell(Listcell cell) {
		String value = "";
		Comboitem selComboitem = ((Combobox) cell.getFirstChild())
				.getSelectedItem();// .getValue();
		if (selComboitem != null && selComboitem.getValue() != null) {
			value = selComboitem.getValue() + "";
		}
		return value;
	}

	/**
	 * 
	 * 方法功能:对象差异值比较(显示对象差异的内容) .
	 * 
	 * @param orig
	 *            源对象
	 * @param dest
	 *            目标对象
	 * @return List<Map<String, Object>>
	 * @author: huangwc
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-7-25 huangwc 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static List<Map<String, Object>> compareProperties(Object orig,
			Object dest) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Field[] origFields = orig.getClass().getDeclaredFields();
			Field[] destFields = dest.getClass().getDeclaredFields();
			for (int i = 0; i < destFields.length; i++) {
				String destFieldName = destFields[i].getName();
				String destFieldType = destFields[i].getGenericType()
						.toString();
				Object destFieldValue = PropertyUtils.getProperty(dest,
						destFields[i].getName());
				for (int k = 0; k < origFields.length; k++) {
					String origFieldName = origFields[k].getName();
					String origFieldType = origFields[k].getGenericType()
							.toString();
					if (destFieldName.equals(origFieldName)
							&& destFieldType.equals(origFieldType)) {
						Object origFieldValue = PropertyUtils.getProperty(orig,
								origFields[k].getName());
						if (!StrUtil.strnull(destFieldValue).equals(
								StrUtil.strnull(origFieldValue))) {
							Map<String, Object> myMap = new HashMap<String, Object>();
							myMap.put("FieldName", destFieldName);
							myMap.put("FieldType", destFieldType);
							myMap.put("OrigFieldValue", origFieldValue);
							myMap.put("DestFieldValue", destFieldValue);
							list.add(myMap);
						}
						break;
					}
				}
			}
			return list;
		} catch (Exception ex) {
			ex.printStackTrace();
			return list;
		}
	}

	/**
	 * 
	 * 方法功能: 判断IP是否在IP段内.
	 * 
	 * @param ipSection
	 * @param ip
	 * @return
	 * @author: lzq
	 * @修改记录： ==============================================================<br>
	 *        日期:2012-11-9 lzq 创建方法，并实现其功能
	 *        ==============================================================<br>
	 */
	public static boolean ipIsValid(String ipSection, String ip) {
		if (ipSection == null)
			throw new NullPointerException("IP段不能为空！");
		if (ip == null)
			throw new NullPointerException("IP不能为空！");
		ipSection = ipSection.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))
			return false;
		int idx = ipSection.indexOf('-');
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}

}
