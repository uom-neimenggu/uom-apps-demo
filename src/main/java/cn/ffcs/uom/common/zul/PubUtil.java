package cn.ffcs.uom.common.zul;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Checkbox;

import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.common.util.NumericUtil;


@SuppressWarnings({"null","unchecked"})
public class PubUtil {
    
    /**
     * bandbox类型.
     */
    public static final String  BANDBOX_TYPE_ATTR  = "attr";
    /**
     * Name.
     */
    public static final String  BANDBOX_ATTR_NAME  = "Name";
    /**
     * BANDBOX_ATTR_VALUE.
     */
    public static final String  BANDBOX_ATTR_VALUE = "value";
    
    
    /**
     * OBJECT.
     */
    private static final String OBJECT             = "Object";
    /**
     * DATE.
     */
    private static final String DATE               = "Date";
    /**
     * INTEGER.
     */
    private static final String INTEGER            = "Integer";
    /**
     * LONG.
     */
    private static final String LONG               = "Long";
    /**
     * DOUBLE.
     */
    private static final String DOUBLE             = "Double";
    /**
     * STRING.
     */
    private static final String STRING             = "String";
    
    /**
     * PRIMITIVE.
     */
    private static final String PRIMITIVE          = "Primitive";
    
    /**
     * 用bean来填充po
     * .
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
                        Field beanField = beanFieldsMap.get(poFields[i].getName());
                        String beanFieldType = beanField.getGenericType().toString();
                        // 获取控件
                        Object oComp = getComp(bean, beanField.getName(), beanFieldType);
                        if (oComp != null) { // 存在匹配控件，进行赋值
                            Object oUiBeanVal = getCompValue(oComp, beanFieldType, beanField
                                .getName(), bean);
                            setPoValue(po, poFields[i].getName(), poFieldType, oUiBeanVal);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
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
     * @author wuyx
     *         2011-2-11 wuyx
     */
    public static Object getComp(Object uiBean, String compName, String compType) {
        if (null == uiBean) {
            
        } else if (compName == null || "".equals(compName)) {
            
        } else if (compType == null || "".equals(compType)) { // 控件类型
            
        }
        Object oComp = null;
        
        try {
            oComp = PropertyUtils.getProperty(uiBean, compName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return oComp instanceof Component ? oComp : null;
    }
    
    /**
     * -------------------------------------------
     * 获取控件值：
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
     * @author wuyx
     *         2011-2-11 wuyx
     */
    public static Object getCompValue(Object oComp, String compType, String compName, Object bean) {
        if (null == oComp) {
            
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
                return citem == null ? null
                    : citem.getValue();
            } else if ("class org.zkoss.zul.Listbox".equals(compType)) {
                Listitem litem = ((Listbox) oComp).getSelectedItem();
                return litem == null ? null
                    : litem.getValue();
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
            } /*else if ("class com.ffcs.crm2.common.ui.comp.AttrValueSelectExt".equals(compType)) {// 2012-03-31
                AttrValueSelectExt attrValueSelectExt = (AttrValueSelectExt) oComp;
                if (attrValueSelectExt != null && attrValueSelectExt.getAttrValue() != null) {
                    return attrValueSelectExt.getAttrValue().getAttrValue();
                } else {
                    return "";
                }
                
            } */else {
                // null
            }
        }
     // 其他非控件忽略
        return null;
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
            return ov == null ? null
                : ov + "";
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
        }
        return null;
    }
    
    /**
     * -------------------------------------------
     * 设置对象值：
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
    public static void setPoValue(Object po, String propName, String propType, Object val) {

        try {
            if (po == null) {
            } else if (propName == null || "".equals(propName)) {
            } else if (propType == null || "".equals(propType)) {
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
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (NoSuchMethodException e) {
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
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Long longVal = (val == null ? null: NumericUtil.toLong(val + ""));
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
        throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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
        throws ParseException, IllegalAccessException, InvocationTargetException,
        NoSuchMethodException {
        // Date dateVal = DateUtil.convertStringToDate(val + "");
        PropertyUtils.setProperty(po, propName, val);
    }
    
    /**
     * -------------------------------------------
     * 判断数据类型
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
     * 更新列表显示.
     * 
     * @param listbox
     *            列表
     * @param obj
     *            操作对象
     * @param opType
     *            操作类型
     */
    public static void reDisplayListbox(Listbox listbox, UomEntity obj, String opType) {
        if (obj == null) {
            return;
        }
        ListModelList model = (ListModelList) listbox.getModel();
        if (model == null) {
            model = new ListModelList();
        }
        if ("add".equals(opType)) {
            model.add(obj);
        }else if ("mod".equals(opType)) {
			boolean ret = true;
			for (int i = 0; model != null && i < model.getSize(); i++) {
				if (((UomEntity) model.get(i)).getId().equals(obj.getId())) {
					model.set(i, obj);
					ret = false;
					break;
				}
			}
			if (ret) {
				model.add(obj);
			}
        } else if ("del".equals(opType)) {
            if (model.contains(obj)) {
                model.remove(obj);
            }
        }
        ListModel dataList = new BindingListModelList(model, true);
        listbox.setModel(dataList);
    }
}
