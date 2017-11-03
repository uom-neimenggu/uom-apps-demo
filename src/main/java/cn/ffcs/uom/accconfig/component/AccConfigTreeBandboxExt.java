package cn.ffcs.uom.accconfig.component;

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

import cn.ffcs.uom.accconfig.constants.AccConfigConstants;
import cn.ffcs.uom.accconfig.model.AccConfig;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;

@Controller
@Scope("prototype")
public class AccConfigTreeBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;
	private final String zul = "/pages/accconfig/comp/accconfig_tree_bandbox_ext.zul";
	@Getter
	@Setter
	private AccConfigTree accConfigTree;

	@Getter
	private List<AccConfig> accConfigs;

	@Getter
	private AccConfig accConfig;
	
	public AccConfigTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}
	
	public void setAccConfigs(List<AccConfig> accConfigs) {
		String sysName = "";
		if (accConfigs != null && accConfigs.size() > 0) {
			for (int i = 0; i < accConfigs.size(); i++) {
				if (i == accConfigs.size() - 1) {
					sysName += accConfigs.get(i).getAccName();
				} else {
					sysName += accConfigs.get(i).getAccName() + ",";
				}
			}
		}
		this.setValue(sysName);
		this.accConfigs = accConfigs;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.accConfigTree.getSelectedItems();
		List<AccConfig> list = new ArrayList<AccConfig>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				if (ti.getTreechildren() != null) {
					// 不是末级节点
					continue;
				}
				AccConfig sl = (AccConfig) ((TreeNodeImpl) ti.getValue())
						.getEntity();
				if(sl!=null && !AccConfigConstants.IS_PARENT.equals(sl.getIsParent())){
					list.add(sl);
				}
			}
		}
		this.setAccConfigs(list);
		this.close();
	}
	
	public void onClick$cancelButton() {
		this.close();
	}
	
    public void setAccConfig(AccConfig accConfig) {
        this.setValue(null == accConfig ? "" : accConfig.getAccName());
        this.accConfig = accConfig;
    }
}
