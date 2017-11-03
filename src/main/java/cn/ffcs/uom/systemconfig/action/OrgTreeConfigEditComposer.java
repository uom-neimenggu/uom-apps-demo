package cn.ffcs.uom.systemconfig.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Treeitem;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.treechooser.model.Node;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.organization.manager.MdsionOrgRelationManager;
import cn.ffcs.uom.organization.model.MdsionOrgRelation;
import cn.ffcs.uom.systemconfig.action.bean.OrgTreeConfigEditBean;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;
import cn.ffcs.uom.systemconfig.model.OrgTreeConfig;

@Controller
@Scope("prototype")
public class OrgTreeConfigEditComposer extends BasePortletComposer {

	/**
	 * 页面bean
	 */
	private OrgTreeConfigEditBean bean = new OrgTreeConfigEditBean();
	/**
	 * manager
	 */
	private OrgTreeConfigManager orgTreeConfigManager = (OrgTreeConfigManager) ApplicationContextUtil.getBean("orgTreeConfigManager");
	
	private MdsionOrgRelationManager mdsionOrgRelationManager = (MdsionOrgRelationManager) ApplicationContextUtil.getBean("mdsionOrgRelationManager");
	/**
	 * 操作类型
	 */
	/*private String opType;*/
	/**
	 * 组织类型配置
	 */
	private List<OrgTreeConfig> orgTreeConfigList = null;
	/**
	 * 当前组织树信息
	 */
	private MdsionOrgRelation orgRela;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate$orgTreeConfigEditWin() throws Exception {
		this.bean.getOrgTreeConfigEditWin().setTitle("组织树配置修改");
		this.bindBean();
	}

	/**
	 * 绑定bean
	 * 
	 */
	private void bindBean() throws Exception {
		orgRela = (MdsionOrgRelation) arg.get("orgRela");
		if (orgRela == null) {
			ZkUtil.showError("参数错误", "提示信息");
			this.onCancel();
		}
		//组织关系回填
		Collection<Treeitem> orgRelaTreeitem = this.bean.getOrgRelaCd().getAttrValueTree().getItems();
		for (Treeitem ti : orgRelaTreeitem) {
			Node node = (Node) ((TreeNodeImpl) ti.getValue()).getEntity();
			if (node.getAttrValue().equals(orgRela.getRelaCdStr())){
				List<Pair<String, String>> resultArr = new ArrayList<Pair<String, String>>();
				resultArr.add(Pair.of(node.getAttrValue(),node.getAttrValueName()));
				this.bean.getOrgRelaCd().setResultArr(resultArr);
				this.bean.getOrgRelaCd().setValue(node.getAttrValueName());
			}
		}
		orgTreeConfigList = orgTreeConfigManager.findOrgType(orgRela.getOrgId());
		//组织类型回填
		Collection<Treeitem> orgTypeTreeitem = this.bean.getOrgTypeCd().getAttrValueTree().getItems();
		List<Pair<String, String>> resultArr = new ArrayList<Pair<String, String>>();
		StringBuffer sb = new StringBuffer();
		for (Treeitem ti : orgTypeTreeitem) {
			Node node = (Node) ((TreeNodeImpl) ti.getValue()).getEntity();
			for(OrgTreeConfig orgTreeConfig : orgTreeConfigList){
				if (node.getAttrValue().equals(orgTreeConfig.getOrgTypeCd())){
					resultArr.add(Pair.of(node.getAttrValue(),node.getAttrValueName()));
					sb.append(node.getAttrValueName()).append(",");
				}
			}
		}
		this.bean.getOrgTypeCd().setResultArr(resultArr);
		this.bean.getOrgTypeCd().setValue(StringUtils.removeEnd(sb.toString(), ","));
		//this.bean.getOrgRelaCd().setValue("上级管理机构");
		//this.bean.getOrgTypeCd().setValue("N0202020000,N0901000000");
	}

	/**
	 * 点击确定
	 * 
	 * @throws Exception
	 */
	public void onSubmit() throws Exception {
	    Long orgTreeId = orgRela.getOrgTreeIdByRoot(orgRela.getOrgId());
	    orgRela.setOrgTreeId(String.valueOf(orgTreeId));
		//orgTreeConfigList.
		List<Pair<String, String>> orgRelaResultArr = this.bean.getOrgRelaCd().getResultArr();
		List<Pair<String, String>> orgTypeResultArr = this.bean.getOrgTypeCd().getResultArr();
		if(orgRelaResultArr!=null&&orgTypeResultArr!=null){
			for (Pair pair : orgRelaResultArr) {
				orgRela.setRelaCd((String) pair.getLeft());
				mdsionOrgRelationManager.updateMdsionOrgRelType(orgRela);
			}
			for(OrgTreeConfig orgTreeConfig:orgTreeConfigList){
				boolean isExists = false;
				for (Pair pair : orgTypeResultArr) {
					if(orgTreeConfig.getOrgTypeCd().equals((String) pair.getLeft())){
						isExists = true;
					}
				}
				if(!isExists){//不存在删除
					orgTreeConfig.removeOnly();
				}
			}
			for (Pair pair : orgTypeResultArr) {
				boolean isExists = false;
				for(OrgTreeConfig orgTreeConfig:orgTreeConfigList){
					if(orgTreeConfig.getOrgTypeCd().equals((String) pair.getLeft())){
						isExists = true;
					}
				}
				if(!isExists){//不存在添加
					OrgTreeConfig otc = new OrgTreeConfig();
					otc.setOrgTreeId(orgRela.getOrgTreeIdByRoot(orgRela.getOrgId()));
					otc.setOrgTypeCd((String) pair.getLeft());
					otc.addOnly();
				}
			}
			Map arg = new HashMap();
			arg.put("orgRela", orgRela);
			Events.postEvent("onOK", this.self, arg);
			onCancel();
		}else{
			ZkUtil.showError("组织关系和组织类型不能为空!", "提示信息");
		}
		
	}

	/**
	 * 点击取消
	 * 
	 * @throws Exception
	 */
	public void onCancel() throws Exception {
		this.bean.getOrgTreeConfigEditWin().onClose();
	}
}
