package cn.ffcs.uom.dataPermission.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Treecol;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.manager.OperateLogManager;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.dataPermission.action.bean.RoleProfessionalTreeEditBean;
import cn.ffcs.uom.dataPermission.manager.AroleProfessionalTreeManager;
import cn.ffcs.uom.dataPermission.model.AroleProfessionalTree;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.systemconfig.manager.OrgTreeConfigManager;

@Controller
@Scope("prototype")
public class RoleProfessionalTreeEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private RoleProfessionalTreeEditBean bean = new RoleProfessionalTreeEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	
	@Getter
	@Setter
	private Treecol treecol;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleProfessionalTreeManager")
	private AroleProfessionalTreeManager aroleProfessionalTreeManager;
	
	@Autowired
	@Qualifier("organizationRelationManager")
	private OrganizationRelationManager organizationRelationManager;
	@Autowired
	@Qualifier("operateLogManager")
	private OperateLogManager operateLogManager;
	@Autowired
	@Qualifier("orgTreeConfigManager")
	private OrgTreeConfigManager orgTreeConfigManager;

	/**
	 * 专业树角色配置实体类
	 */
	private AroleProfessionalTree aroleProfessionalTree;
	/**
	 * 已经存在的权限（回填和新增时校验用）
	 */
	private List<AroleProfessionalTree> aroleProfessionalTreeList;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$roleProfessionalTreeEditWindow() throws Exception {
		this.bindBean();
		bean.getOrgTreeRootNode().setVisible(false);
		List<NodeVo> orgTreeNameList = operateLogManager.getValuesListDw();
		ListboxUtils.rendererForEdit(bean.getOrgTreeNameListbox(),orgTreeNameList);
	}

	/**
	 * 选择组织树
	 * 
	 * @throws Exception
	 */
	public void onSelect$orgTreeNameListbox() throws Exception {
		Listitem listitem = bean.getOrgTreeNameListbox().getSelectedItem();
		if (listitem.getValue() != null) {
			aroleProfessionalTree.setOrgTreeId(Long.valueOf(listitem.getValue().toString()));
			bean.getOrgTreeRootNode().clear();
			treecol.setLabel(listitem.getLabel());
			aroleProfessionalTreeList = aroleProfessionalTreeManager.queryRoleProfessionalTreeList(aroleProfessionalTree);
			orgTreeConfigManager.loadProfessionalCheckTreeRootDw(bean.getOrgTreeRootNode(),listitem.getValue().toString(),aroleProfessionalTreeList);
			bean.getOrgTreeRootNode().setVisible(true);
		} else {
			bean.getOrgTreeRootNode().setVisible(false);
		}
	}
	
	/**
	 * .
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if ("add".equals(opType)) {
			this.bean.getRoleProfessionalTreeEditWindow().setTitle("专业树数据权配置");
			aroleProfessionalTree = (AroleProfessionalTree) arg.get("aroleProfessionalTree");
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		aroleProfessionalTreeManager.saveRoleProfessionalTree(bean.getOrgTreeRootNode(), aroleProfessionalTree,aroleProfessionalTreeList);
		Events.postEvent(Events.ON_OK,bean.getRoleProfessionalTreeEditWindow(), null);
		onCancel();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRoleProfessionalTreeEditWindow().onClose();
	}
}
