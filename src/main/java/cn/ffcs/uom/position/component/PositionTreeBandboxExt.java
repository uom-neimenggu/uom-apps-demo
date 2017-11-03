/**
 * 
 */
package cn.ffcs.uom.position.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Treeitem;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.position.model.Position;

/**
 * @author yahui
 * 
 */
@Controller
@Scope("prototype")
public class PositionTreeBandboxExt extends Bandbox implements IdSpace {
	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/position/comp/position_tree_bandbox_ext.zul";

	@Getter
	@Setter
	private PositionTree positionTree;
	/**
	 * 选择岗位
	 */
//	@Getter
//	private Position position;
	
	/**
	 * 选择岗位列表
	 */
	@Getter
	private List<Position> positionList;

	@Getter
	@Setter
	private boolean endpoint;

	/**
	 * 构造函数
	 */
	public PositionTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听树选择事件
		 */
//		this.positionTree.addForward(Events.ON_SELECT, this,
//				"onSelectPositionReponse");
	}

//	public Object getAssignObject() {
//		return this.getPosition();
//	}
//
//	public void setAssignObject(Object assignObject) {
//		if (assignObject == null || assignObject instanceof Position) {
//			this.setPosition((Position) assignObject);
//		}
//	}
//
//	public void setPosition(Position position) {
//		this.setValue(position == null ? "" : position.getPositionName());
//		this.position = position;
//	}

	/**
	 * 多选：设置岗位
	 */
	public void setPositionList(List<Position> positionList) {
		String positionName = "";
		if (positionList != null && positionList.size() > 0) {
			for (int i = 0; i < positionList.size(); i++) {
				if (i == positionList.size() - 1) {
					positionName += positionList.get(i).getPositionName();
				} else {
					positionName += positionList.get(i).getPositionName() + ",";
				}
			}
		}
		this.setValue(positionName);
		this.positionList = positionList;
	}
	
	/**
	 * 选择岗位
	 * 
	 * @param event
	 * @throws Exception
	 */
//	public void onSelectPositionReponse(final ForwardEvent event)
//			throws Exception {
//		if (this.positionTree.getSelectedItem() != null) {
//			if (endpoint) {
//				Treeitem ti = this.positionTree.getSelectedItem();
//				if (ti.getTreechildren() != null) {
//					ZkUtil.showError("只能选择根节点", "提示信息");
//					return;
//				}
//			}
//			Object data = this.positionTree.getSelectedItem().getValue();
//			this.setPosition((Position) ((TreeNodeImpl) data).getEntity());
//		}
//		this.close();
//	}

	/**
	 * 点击确定按钮
	 */
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.positionTree.getSelectedItems();
		List<Position> list = new ArrayList<Position>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				if (ti.getTreechildren() != null) {
					// 不是末级节点
					continue;
				}
				Position position = (Position) ((TreeNodeImpl) ti.getValue())
						.getEntity();
				if(position!=null){
					list.add(position);
				}
			}
		}
		this.setPositionList(list);
		this.close();
	}

	/**
	 * 点击取消按钮
	 */
	public void onClick$cancelButton() {
		this.close();
	}
}
