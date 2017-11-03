package cn.ffcs.uom.common.zkplus;

import java.lang.reflect.Field;

import org.zkoss.lang.Library;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.Attributes;

public class ZKRebuildUuid {

	public final static String CRM2ID = "_CRM2ID_";
	private final static String UUID_FIELD = "_uuid";
	/**
	 * 判断表示。
	 */
	private static Boolean _needRebuild = null;

	/**
	 * 
	 * 隐藏构造函数。
	 * 
	 * @author zhoupc 2011-5-10 zhoupc
	 */
	protected ZKRebuildUuid() {

	}

	/**
	 * 
	 * 获取判断.
	 * 
	 * @return Boolean true of false.
	 * @author zhoupc 2011-5-10 zhoupc
	 */
	public static boolean needReuild() {
		if (_needRebuild == null) {
			_needRebuild = Boolean.valueOf("true".equals(Library
					.getProperty(Attributes.UUID_RECYCLE_DISABLED)));
		}
		return _needRebuild.booleanValue();
	}

	/**
	 * 
	 * 重构组件uuid.
	 * 
	 * @param comp
	 *            组件
	 * @param id
	 *            标识
	 * @author zhoupc 2011-5-12 zhoupc
	 */
	public static void rebuildUuidById(Component comp, String id) {
		// 重构Uuid用于压力测试。
		if (ZKRebuildUuid.needReuild()) {
			// 替换横杠为下划线，避免zk不支持横杠作为UUID导致初始化报错 mod by huangjb 2012-1-7
			// id = id.replaceAll("-", "__");
			// 替换任何非单词字符为下划线，避免zk不支持横杠作为UUID导致初始化报错 mod by chenmh 2012-2-3
			id = id.replaceAll("\\W", "__");
			comp.setAttribute(ZKRebuildUuid.CRM2ID, id);
			Component parent = comp.getParent();
			if (parent != null) {
				int index = parent.getChildren().indexOf(comp);
				Component nextComp = null;
				if (index < parent.getChildren().size() - 1) {
					nextComp = (Component) parent.getChildren().get(index + 1);
				}
				parent.removeChild(comp);
				try {
					Field field = AbstractComponent.class
							.getDeclaredField(UUID_FIELD);
					if (field != null) {
						field.setAccessible(true);
						try {
							field.set(comp, null);
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						field.setAccessible(false);

					}
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (nextComp != null) {
					parent.insertBefore(comp, nextComp);
				} else {
					comp.setParent(parent);
				}
			}
		}
	}
}
