package cn.ffcs.uom.common.zkplus.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.GenericComposer;

import cn.ffcs.raptornuke.plugin.common.exception.RtManagerException;
import cn.ffcs.raptornuke.plugin.common.util.ClassUtil;

/**
 * ComposerUtil.
 * 
 * @author wuyx
 * @version Revision 1.0.0
 * 
 */
public final class ComposerUtil {

	/**
	 * dep.
	 */
	private static final int DEP = 1000; // 最深查找层次

	/**
	 * 私有构造函数.
	 * 
	 */
	private ComposerUtil() {
	}

	/**
	 * 存放compser到组件.
	 * 
	 * @param comp
	 *            Component
	 * @param composer
	 *            Composer
	 *            <p>
	 */
	public static void setComposer(final Component comp, final Composer composer) {
		comp.setAttribute(ZkConstants.TAG_COMPOSER, composer);
	}

	/**
	 * 获取存放compser到组件.
	 * 
	 * @param comp
	 *            存放Composer的组件
	 * @return Composer
	 */
	public static Composer getComposer(final Component comp) {
		if (comp == null) {
			return null;
		}
		return (Composer) comp.getAttribute(ZkConstants.TAG_COMPOSER, false);
	}

	/**
	 * 获取存放compser到组件.
	 * 
	 * @param oComp
	 *            存放composer组件
	 * @return Composer
	 */
	public static Composer getComposer(final Object oComp) {
		return ComposerUtil.getComposer((Component) oComp);
	}

	/**
	 * 获取存放compser到组件.
	 * 
	 * @param comp
	 *            存放Composer的组件
	 * @return Composer
	 */
	public static Composer getSupComposer(final Component comp) {
		if (comp == null) {
			return null;
		}
		// 
		Component theComp = comp;
		for (int i = 0; i < ComposerUtil.DEP; i++) {
			if (theComp.hasAttribute(ZkConstants.TAG_COMPOSER)) {
				return ComposerUtil.getComposer(theComp);
			}
			theComp = theComp.getParent();
		}
		return null;
	}

	/**
	 * 获取存放compser到组件.
	 * 
	 * @param comp
	 *            存放Composer的组件
	 * @param clz
	 *            Composer Class
	 * @return Composer
	 */
	public static Composer getSupComposer(final Component comp, final Class clz) {
		if (comp == null) {
			return null;
		}
		// 判断属于对象的返回
		Component theComp = comp;
		for (int i = 0; i < ComposerUtil.DEP; i++) {
			if (theComp == null) {
				return null;
			} else if (theComp.hasAttribute(ZkConstants.TAG_COMPOSER)) {
				final Composer composer = ComposerUtil.getComposer(theComp);
				if (clz.isAssignableFrom(composer.getClass())) { // 属于指定类的对象返回
					return composer;
				}
			}
			theComp = theComp.getParent();
		}
		return null;
	}

	/**
	 * 获取存放compser到组件.
	 * 
	 * @param comp
	 *            存放Composer的组件
	 * @param clz
	 *            Composer Class
	 * @return Component
	 */
	public static Component getSupExt(final Component comp, final Class clz) {
		if (comp == null) {
			return null;
		}
		// 判断属于对象的返回
		Component theComp = comp;
		for (int i = 0; i < ComposerUtil.DEP; i++) {
			if (theComp == null) {
				return null;
			} else {
				if (clz.isAssignableFrom(theComp.getClass())) { // 属于指定类的对象返回
					return theComp;
				}
			}
			theComp = theComp.getParent();
		}
		return null;
	}

	/**
	 * 实例化composer.
	 * 
	 * @param clazzStr
	 *            String
	 * @return Object
	 * @author wuyx 2011-2-22 wuyx
	 */
	@SuppressWarnings("unchecked")
	public static GenericComposer newComposer(String clazzStr) {
		return (GenericComposer) ClassUtil.newInstance(clazzStr);
	}

	/**
	 * 绑定控件composer.
	 * 
	 * @param comp
	 *            Component
	 * @param composer
	 *            GenericComposer
	 * @author wuyx 2011-8-2 wuyx
	 */
	public static void bindComposer(Component comp, GenericComposer composer) {
		composer.bindComponent(comp);
		try {
			composer.doAfterCompose(comp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RtManagerException(e);
		}
	}

	/**
	 * bindComposer.
	 * 
	 * @param comp
	 *            Component
	 * @param clazzStr
	 *            String
	 * @param parentComp
	 *            Component
	 * @author wuyx 2011-8-2 wuyx
	 */
	public static Component bindParent(Component comp, Component parentComp) {
		if (parentComp != null && comp != null) {
			comp.setParent(parentComp);
		}
		return comp;
	}

	/**
	 * bindComposer.
	 * 
	 * @param comp
	 *            Component
	 * @param clazzStr
	 *            String
	 * @param parentComp
	 *            Component
	 * @author wuyx 2011-8-2 wuyx
	 */
	public static void bindComposer(Component comp, String clazzStr,
			Component parentComp) {
		if (parentComp != null && comp != null) {
			comp.setParent(parentComp);
		}
		ComposerUtil.bindComposer(comp, ComposerUtil.newComposer(clazzStr));
	}

}
