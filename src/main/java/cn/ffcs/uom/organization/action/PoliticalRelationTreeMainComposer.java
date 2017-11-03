package cn.ffcs.uom.organization.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.UomZkUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.TreeNodeEntity;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.action.bean.PoliticalRelationTreeBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.organization.model.OrganizationRelation;

@Controller
@Scope("prototype")
public class PoliticalRelationTreeMainComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;
	
	PoliticalRelationTreeBean bean = new PoliticalRelationTreeBean();
	/**
	 * 默认根节点
	 */
	private MdsionOrgRelation orgRelaRoot;
	
	@Resource
	private MdsionOrgRelationManager mdsionOrgRelationManager;
	
	private Map<String,List<Checkbox>> checkboxMap = new HashMap<String,List<Checkbox>>();
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		UomZkUtil.autoFitHeight(comp);
		Components.wireVariables(comp, bean);
		
		orgRelaRoot = MdsionOrgRelation.newInstance();
		orgRelaRoot.setOrgId(OrganizationConstant.ROOT_TREE_ORG_ID);
		orgRelaRoot = mdsionOrgRelationManager.queryMdsionOrgRelation(orgRelaRoot);
		orgRelaRoot.setTreeLabel("中国电信股份有限公司");
		orgRelaRoot.setIsRoot(true);
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$politicalRelationTreeMainWin() throws Exception {
		bean.getPoliticalRelationTreeMainWin().setTitle("内部组织选择");
		Treeitem orgRelaRootTreeitem = loadOrgRelaRoot();
		mdsionOrgRelationManager.loadPoliticalTree(OrganizationConstant.ROOT_TREE_ORG_ID.toString(), orgRelaRootTreeitem,checkboxMap);
	}
	
	
	
	/**
	 * 加载顶层节点
	 */
	public Treeitem loadOrgRelaRoot(){
		Treechildren treechildren = bean.getPoliticalRelationTree().getTreechildren();
		Treeitem titem = new Treeitem();
		Treerow trow = new Treerow();
		Treecell tcell = new Treecell(orgRelaRoot.getLabel());
/*		if (organizationRelation.isCompany()) {tcell.setStyle("color:blue");}// 单位label样式
*/		tcell.setParent(trow);
		trow.setParent(titem);
		TreeNodeImpl treeNodeImpl = new TreeNodeImpl<TreeNodeEntity>(orgRelaRoot);
		titem.setValue(treeNodeImpl);
		titem.setParent(treechildren);
		return titem;
	}
	
	
	public void onOk() {
		StringBuffer sb = new StringBuffer();
		for (List<Checkbox> cbList : checkboxMap.values()) {
			for(Checkbox cb : cbList){
				if(cb.isChecked()){
					sb.append(cb.getValue()).append(",");
				}
			}
		}
		if(sb.length()>0){
			Events.postEvent("onOK",bean.getPoliticalRelationTreeMainWin(), sb.deleteCharAt(sb.length()-1).toString());
			bean.getPoliticalRelationTreeMainWin().onClose();
		}else{
			ZkUtil.showInformation("请选择要复制的组织！", "提示信息");
		}
	}
	
	public void onCancel() {
		bean.getPoliticalRelationTreeMainWin().onClose();
	}
}
