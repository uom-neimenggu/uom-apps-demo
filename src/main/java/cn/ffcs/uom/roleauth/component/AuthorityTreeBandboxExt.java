package cn.ffcs.uom.roleauth.component;

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
import cn.ffcs.uom.roleauth.constants.RoleAuthConstants;
import cn.ffcs.uom.roleauth.model.StaffAuthority;

@Controller
@Scope("prototype")
public class AuthorityTreeBandboxExt extends Bandbox implements IdSpace {

	private static final long serialVersionUID = 1L;
	private final String zul = "/pages/role_auth/comp/authority_tree_bandbox_ext.zul";
	@Getter
	@Setter
	private AuthorityTree authorityTree;

	@Getter
	private List<StaffAuthority> authoritys;

	@Getter
	private StaffAuthority authority;
	
	public AuthorityTreeBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, this);
		Components.addForwards(this, this, '$');
	}
	
	public void setAuthoritys(List<StaffAuthority> authoritys) {
		String authName = "";
		if (authoritys != null && authoritys.size() > 0) {
			for (int i = 0; i < authoritys.size(); i++) {
				if (i == authoritys.size() - 1) {
					authName += authoritys.get(i).getAuthorityName();
				} else {
					authName += authoritys.get(i).getAuthorityName() + ",";
				}
			}
		}
		this.setValue(authName);
		this.authoritys = authoritys;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$okButton() {
		Set<Treeitem> itemsSet = this.authorityTree.getSelectedItems();
		List<StaffAuthority> list = new ArrayList<StaffAuthority>();
		if (itemsSet != null) {
			Iterator<Treeitem> it = itemsSet.iterator();
			while (it.hasNext()) {
				Treeitem ti = it.next();
				if (ti.getTreechildren() != null) {
					// 不是末级节点
					continue;
				}
				StaffAuthority sl = (StaffAuthority) ((TreeNodeImpl) ti.getValue())
						.getEntity();
				if(sl!=null && !RoleAuthConstants.IS_PARENT.equals(sl.getIsParent())){
					list.add(sl);
				}
			}
		}
		this.setAuthoritys(list);
		this.close();
	}
	
	public void onClick$cancelButton() {
		this.close();
	}
	
    public void setAuthority(StaffAuthority authority) {
        this.setValue(null == authority ? "" : authority.getAuthorityName());
        this.authority = authority;
    }
}
