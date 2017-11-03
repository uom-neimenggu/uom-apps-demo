package cn.ffcs.uom.position.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.treechooser.component.ICheckHasChildrenCallback;
import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.position.model.CtPositionNode;

/**
 * @author 曾臻
 *所有属性值的缓存
 */
public class CtPositionNodeCache {

	private List<CtPositionNode> cache;

	private Map<String,Boolean> hasChildrenMap;
	
	private CtPositionTreeBandbox component;
	
	
	public CtPositionNodeCache(CtPositionTreeBandbox component) {
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
	public List<CtPositionNode> getAllNodes() {
		if (cache == null) {
			String sql = "SELECT * FROM ct_position WHERE STATUS_CD = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			List<CtPositionNode> list = CtPositionNode.repository().jdbcFindList(
					sql, params, CtPositionNode.class);
			cache = list;
		}
		return cache;

	}
}
