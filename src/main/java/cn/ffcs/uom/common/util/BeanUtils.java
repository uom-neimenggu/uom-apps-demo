package cn.ffcs.uom.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

/**
 * <p>
 * Description:封闭Apache BeanUtils的方法。
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
public class BeanUtils {
	/**
	 * 将对象orig的属性的值复制到对象dest中拥有有相同属性名的属性
	 * 
	 * @param dest
	 *            目标对象
	 * @param orig
	 *            源对象
	 * @throws BaseException
	 */
	public static void copyProperties(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.BeanUtils.copyProperties(dest, orig);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * 复制对象
	 * 
	 * @param bean
	 *            目标对象
	 * @return @
	 */
	public static Object cloneBean(Object bean) {
		try {
			return org.apache.commons.beanutils.BeanUtils.cloneBean(bean);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

	}

	/**
	 * 深度复制对象
	 * 
	 * @param src
	 *            目标对象
	 * @return @
	 */
	public static Object deepClone(Object src) throws Throwable {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(src);
		oos.close();

		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);

		Object o = ois.readObject();
		ois.close();
		return o;
	}

	/**
	 * 得到对象的属性值
	 * 
	 * @param bean
	 *            目标对象
	 * @param name
	 *            属性名称
	 * @return @
	 */
	public static String getProperty(Object bean, String name) {
		try {
			return org.apache.commons.beanutils.BeanUtils.getProperty(bean,
					name);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}

	}

	/**
	 * 给对象添加属性值
	 * 
	 * @param bean
	 *            目标对象
	 * @param name
	 *            对象属性名称
	 * @param value
	 *            对象属性值 @
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			org.apache.commons.beanutils.BeanUtils.setProperty(bean, name,
					value);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}

	/**
	 * 复制一组对象
	 * 
	 * @param coll
	 *            目标对象 @
	 */
	public static Collection copy(Collection coll) {
		Collection to = null;
		try {
			to = (Collection) coll.getClass().newInstance();
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				Object tmp = BeanUtils.cloneBean(obj);
				to.add(tmp);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		return to;
	}

	/**
	 * 注册转化器
	 * 
	 * @date 2008-11-20
	 */
	private static boolean registered = false;

	public static void register() {
		if (registered == false) {
			ConvertUtils.register(new MySqlTimestampConverter(null),
					java.sql.Timestamp.class);
			ConvertUtils.register(new MySqlDateConverter(null),
					java.sql.Date.class);
			ConvertUtils.register(new MyDateConverter(null),
					java.util.Date.class);
			ConvertUtils.register(new MyIntegerConverter(null), Integer.class);
			ConvertUtils.register(new MyLongConverter(null), Long.class);
			ConvertUtils.register(new MyDoubleConverter(null), Double.class);
			registered = true;
		}

	}

	/**
	 * 注册新的BeanUtils时间类型转化器
	 * 
	 * 
	 */
	static {
		register();
	}

}

/**
 * <p>
 * Description:解决BeanUtils在copyProperties是，Date为空抛出ConversionException: No value
 * specified异常
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
class MyDateConverter implements Converter {

	public MyDateConverter() {
		this.defaultValue = null;
		this.useDefault = false;
	}

	public MyDateConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		this.useDefault = true;
	}

	private Object defaultValue = null;

	private boolean useDefault = true;

	public Object convert(Class type, Object value) {

		if (value == null || "".equals(value)) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("No value specified");
			}
		}

		if (value instanceof Date) {
			return (value);
		}
		try {
			return (DateUtil.convertStringToDate(String.valueOf(value)));
		} catch (Exception e) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
		}
	}
}

/**
 * <p>
 * Description:解决BeanUtils在copyProperties是，Date为空抛出ConversionException: No value
 * specified异常
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
class MySqlDateConverter implements Converter {

	public MySqlDateConverter() {
		this.defaultValue = null;
		this.useDefault = false;
	}

	public MySqlDateConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		this.useDefault = true;
	}

	private Object defaultValue = null;

	private boolean useDefault = true;

	public Object convert(Class type, Object value) {

		if (value == null || "".equals(value)) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException("No value specified");
			}
		}

		if (value instanceof java.sql.Date) {
			return (value);
		}

		try {
			return (java.sql.Date.valueOf(value.toString()));
		} catch (Exception e) {
			if (useDefault) {
				return (defaultValue);
			} else {
				throw new ConversionException(e);
			}
		}
	}
}

/**
 * <p>
 * Description:解决BeanUtils在copyProperties是，Timestamp为空抛出ConversionException: No
 * value specified异常
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
class MySqlTimestampConverter implements Converter {

	private Object defaultValue = null;
	private boolean useDefault = true;

	public MySqlTimestampConverter() {
		defaultValue = null;
		useDefault = false;
	}

	public MySqlTimestampConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		useDefault = true;
	}

	public Object convert(Class type, Object value) {
		try {
			if (value == null)
				if (useDefault)
					return defaultValue;
				else
					throw new ConversionException("No value specified");
			if (value instanceof Timestamp)
				return value;
			return Timestamp.valueOf(value.toString());
		} catch (Exception e) {
			if (useDefault)
				return defaultValue;
			else
				throw new ConversionException(e);
		}

	}

}

/**
 * <p>
 * Description:解决BeanUtils在copyProperties是，Long为空会自动转成0
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
class MyLongConverter implements Converter {

	private Object defaultValue = null;
	private boolean useDefault = true;

	public MyLongConverter() {
		defaultValue = null;
		useDefault = false;
	}

	public MyLongConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		useDefault = true;
	}

	public Object convert(Class type, Object value) {
		try {
			if (value == null)
				return null;
			if (value instanceof Long)
				return value;
			if (value instanceof Number)
				return new Long(((Number) value).longValue());
			return new Long(value.toString());
		} catch (Exception e) {
			if (useDefault)
				return defaultValue;
			else
				throw new ConversionException(e);
		}

	}

}

/**
 * <p>
 * Description:解决BeanUtils在copyProperties是，Integer为空会自动转成0
 * </p>
 * 
 * 
 * @version 1.0
 * 
 */
class MyIntegerConverter implements Converter {

	private Object defaultValue = null;
	private boolean useDefault = true;

	public MyIntegerConverter() {
		defaultValue = null;
		useDefault = false;
	}

	public MyIntegerConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		useDefault = true;
	}

	public Object convert(Class type, Object value) {
		try {
			if (value == null)
				return null;
			if (value instanceof Integer)
				return value;
			if (value instanceof Number)
				return new Integer(((Number) value).intValue());
			return new Integer(value.toString());
		} catch (Exception e) {
			if (useDefault)
				return defaultValue;
			else
				throw new ConversionException(e);
		}

	}

}

/**
 * *
 * <p>
 * Description:解决BeanUtils在copyProperties是，Double为空会自动转成0
 * </p>
 * 
 * 
 * @version 1.0
 */
class MyDoubleConverter implements Converter {

	private Object defaultValue = null;
	private boolean useDefault = true;

	public MyDoubleConverter() {
		defaultValue = null;
		useDefault = false;
	}

	public MyDoubleConverter(Object defaultValue) {
		this.defaultValue = defaultValue;
		useDefault = true;
	}

	public Object convert(Class type, Object value) {
		try {
			if (value == null)
				return null;
			if (value instanceof Double)
				return value;
			if (value instanceof Number)
				return new Double(((Number) value).intValue());
			return new Double(value.toString());
		} catch (Exception e) {
			if (useDefault)
				return defaultValue;
			else
				throw new ConversionException(e);
		}

	}
}