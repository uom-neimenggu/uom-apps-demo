package cn.ffcs.uom.common.treechooser.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.treechooser.model.Node;

/**
 * @author 曾臻
 *所有属性值的缓存
 */
public class NodeCache {

	private List<Node> cache;

	private Map<String,Boolean> hasChildrenMap;
	
	private TreeChooserBandbox component;
	
	
	public NodeCache(TreeChooserBandbox component) {
		this.component=component;
		hasChildrenMap=new HashMap<String,Boolean>();
	}
	
	/**
	 * 判断给定节点是否存在孩子（有缓存）
	 * @author 曾臻
	 * @date 2013-6-19
	 * @param code
	 * @param callback 若缓存中找不到，则通过此回调获取
	 * @return
	 */
	public boolean isHasChildren(String code,ICheckHasChildrenCallback callback){
		Boolean flag=hasChildrenMap.get(code);
		if(flag==null){
			flag=callback.checkHasChildren();
			hasChildrenMap.put(code, flag);
		}
		return flag;
	}
	/**
	 * 取得类和属性对应的所有编码
	 * 
	 * @author 曾臻
	 * @date 2013-6-6
	 * @return
	 */
	public List<Node> getAllNodes() {
		if (cache == null) {
			String sql = "SELECT C.* FROM SYS_CLASS A, ATTR_SPEC B, ATTR_VALUE C "
					+ "WHERE C.STATUS_CD = ? AND A.JAVA_CODE = ? AND B.JAVA_CODE = ? "
					+ "and a.class_id=b.class_id and b.attr_id=c.attr_id";
			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(component.getClassName());
			params.add(component.getAttrName());
			List<Node> list = DefaultDaoFactory.getDefaultDao().jdbcFindList(
					sql, params, Node.class);
			cache = list;
		}
		return cache;

	}
}
