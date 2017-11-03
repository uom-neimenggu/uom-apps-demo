package cn.ffcs.uom.staffrole.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staffrole.constants.StaffRoleConstants;
import cn.ffcs.uom.staffrole.model.StaffRole;

public class StaffRoleTreeBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;
	private final String zul = "/pages/staff_role/comp/staff_role_tree_bandbox_ext.zul";
	@Getter
	@Setter
	private StaffRoleTree staffRoleTree;
	@Getter
	@Setter
	private String mode;// 单选和多选两种模式
	@Getter
	@Setter
	private String range;// 只选择末级节点和任意选择节点两种模式
	@Getter
	private List<StaffRole> staffRoles;
	private List<StaffRole> resultArr;// 选择节点列表,left=节点标识,right=节点中文名
	@Getter
	private StaffRole staffRole;
	@Getter
	@Setter
	private Staff staff;
	@Getter
	@Setter
	private String resultText;// 返回文本

	public StaffRoleTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
		staffRoleTree.setStaffRoleTreeBandboxExt(this);
	}

	public void onCreate() {
		staffRoleTree.setMultiple(true);
		staffRoleTree.setCheckmark(true);
		Set<Treeitem> set = this.staffRoleTree.getSelectedItems();
		// ..实现初始化加载已选信息
		// TODO 加了监听box就会多出下面的空白
		// 写死高度像素值暂时解决
		this.addEventListener(Events.ON_OPEN, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				// set initial check state
				Collection<Treeitem> tis = staffRoleTree.getItems();
				for (Treeitem ti : tis) {
					StaffRole staffRole = (StaffRole) ((TreeNodeImpl) ti
							.getValue()).getEntity();
				}
				if (resultArr != null) {
					for (Treeitem ti : tis) {
						StaffRole staffRole = (StaffRole) ((TreeNodeImpl) ti
								.getValue()).getEntity();
						if (resultArr.contains(staffRole)) {
							ti.setSelected(true);
						} else {
							ti.setSelected(false);
						}
					}
				}
			}
		});
	}

	public void onChooseStaffResponse(ForwardEvent event) {
		Staff staff = (Staff) event.getOrigin().getData();
		// if (staff != null) {
		// } else {
		// }
	}

	public void setStaffRoles(List<StaffRole> staffRoles) {
		String roleName = "";
		if (staffRoles != null && staffRoles.size() > 0) {
			for (int i = 0; i < staffRoles.size(); i++) {
				if (i == staffRoles.size() - 1) {
					roleName += staffRoles.get(i).getRoleName();
				} else {
					roleName += staffRoles.get(i).getRoleName() + ",";
				}
			}
		}
		this.setValue(roleName);
		this.staffRoles = staffRoles;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.staffRoleTree.getSelectedItems();
		List<StaffRole> list = new ArrayList<StaffRole>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				if (ti.getTreechildren() != null) {
					// 不是末级节点
					continue;
				}
				StaffRole sl = (StaffRole) ((TreeNodeImpl) ti.getValue())
						.getEntity();
				if (sl != null
						&& !StaffRoleConstants.IS_PARENT.equals(sl
								.getIsParent())) {
					list.add(sl);
				}
			}
		}
		this.setStaffRoles(list);
		this.close();
	}

	public void onClick$cancelButton() {
		this.close();
	}

	public void setStaffRole(StaffRole staffRole) {
		this.setValue(null == staffRole ? "" : staffRole.getRoleName());
		this.staffRole = staffRole;
	}

	public List<StaffRole> getResultArr() {
		return resultArr;
	}

	public void setResultArr(List<StaffRole> resultArr) {
		this.resultArr = resultArr;
	}

	/**
	 * 快速设置初始值
	 * 
	 * @author 曾臻
	 * @date 2013-6-19
	 * @param codeList
	 *            编码列表
	 */
	public void setInitialValue(List<StaffRole> staffRoles) {
		/*
		 * List<Node> nodes = cache.getAllNodes(); Map<String, String> map = new
		 * HashMap<String, String>(); for (Node node : nodes)
		 * map.put(node.getAttrValue(), node.getAttrValueName());
		 */
		String str = "";
		resultArr = new ArrayList<StaffRole>();
		if (staffRoles != null && staffRoles.size() > 0) {
			for (StaffRole staffRole : staffRoles) {
				str += staffRole.getRoleName() + ",";
				resultArr.add(staffRole);
			}
		}

		/*
		 * for (String code : codeList) { String text = map.get(code); if (text
		 * == null) continue; str += text + ","; resultArr.add(Pair.of(code,
		 * text)); }
		 */
		resultText = StringUtils.removeEnd(str, ",");
		this.setText(resultText);
	}
}
