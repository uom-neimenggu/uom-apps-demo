package cn.ffcs.uom.roleauth.constants;

public class RoleAuthConstants {
	/**
	 * 权限树的根节点
	 */
	public static Long ROOT_ROLE_AUTH_TREE = 0L;
	
	/**
	 * 是否是父节点
	 */
	public static String IS_PARENT = "1";
	
	/**
	 * 选择维护树上的权限
	 */
	public static final String ON_SELECT_TREE_AUTH = "onSelectTreeAuth";

	public static final String ON_SELECT_TREE_AUTH_RESPONSE = "onSelectTreeAuthResponse";
	
	/**
	 * 选择维护树上的权限
	 */
	public static final String ON_SAVE_AUTH_RESPONSE = "onSaveAuthResponse";
	
	/**
	 * 删除节点成功事件
	 */
	public static final String ON_DEL_NODE_OK = "onDelNodeOK";
	/**
	 * 修改权限保存事件
	 */
	public static final String ON_SAVE_AUTH = "onSaveAuth";
	public static final String ON_SAVE_AUTHN = "onSaveAuthN";
	/**
	 * 改变权限树样式
	 */
	public static final String ON_CHANGE_TREE_STYLE = "onChangeTreeStyle";

}
