package cn.ffcs.uom.syslist.component;

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
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Treeitem;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.syslist.constants.SysListConstants;
import cn.ffcs.uom.syslist.model.SysList;

@Controller
@Scope("prototype")
public class SysListTreeBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;
	private final String zul = "/pages/syslist/comp/syslist_tree_bandbox_ext.zul";
	@Getter
	@Setter
	private SysListTree sysListTree;

	@Getter
	private List<SysList> sysLists;

	@Getter
	private SysList sysList;
	
	public SysListTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}
	
	public void setSysLists(List<SysList> sysLists) {
		String sysName = "";
		if (sysLists != null && sysLists.size() > 0) {
			for (int i = 0; i < sysLists.size(); i++) {
				if (i == sysLists.size() - 1) {
					sysName += sysLists.get(i).getSysName();
				} else {
					sysName += sysLists.get(i).getSysName() + ",";
				}
			}
		}
		this.setValue(sysName);
		this.sysLists = sysLists;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.sysListTree.getSelectedItems();
		List<SysList> list = new ArrayList<SysList>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				if (ti.getTreechildren() != null) {
					// 不是末级节点
					continue;
				}
				SysList sl = (SysList) ((TreeNodeImpl) ti.getValue())
						.getEntity();
				if(sl!=null && !SysListConstants.IS_PARENT.equals(sl.getIsParent())){
					list.add(sl);
				}
			}
		}
		this.setSysLists(list);
		this.close();
	}
	
	public void onClick$cancelButton() {
		this.close();
	}
	
    public void setSysList(SysList sysList) {
        this.setValue(null == sysList ? "" : sysList.getSysName());
        this.sysList = sysList;
    }
}
