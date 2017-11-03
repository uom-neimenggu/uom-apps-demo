package cn.ffcs.uom.staffrole.component;

import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

import cn.ffcs.uom.common.treechooser.constants.Constants;
import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.staffrole.model.StaffRole;

/**
 * @author zengzh
 * @date 2013-06-08 modified from BaseTreeitemRenderer.java removed all
 *       non-render function
 */
public class StaffRoleTreeitemRenderer implements TreeitemRenderer {

	private StaffRoleTreeBandboxExt bandbox;

	public StaffRoleTreeitemRenderer(StaffRoleTreeBandboxExt bandbox) {
		this.bandbox = bandbox;
	}

	public void render(final Treeitem item, final Object data) throws Exception {
		final TreeNodeImpl ite = (TreeNodeImpl) data;
		StaffRole node = (StaffRole) ite.getEntity();

		// set checkable range
		if (Constants.RANGE_END.equals(bandbox.getRange())) {
			item.setCheckable(node.isEnd());
		}

		//
		item.setValue(ite);
		item.setOpen(true);

		//
		final Treecell tcNamn = new Treecell(ite.getLable());
		Treerow tr = null;
		if (item.getTreerow() == null) {
			tr = new Treerow();
			tr.setParent(item);
		} else {
			tr = item.getTreerow();
			tr.getChildren().clear();
		}
		tcNamn.setParent(tr);
		tcNamn.setStyle("white-space:nowrap;");
	}
}
