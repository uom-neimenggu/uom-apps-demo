package cn.ffcs.uom.dataPermission.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.dataPermission.action.bean.RoleOrganizationEditBean;
import cn.ffcs.uom.dataPermission.manager.AroleOrganizationManager;
import cn.ffcs.uom.dataPermission.model.AroleOrganization;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.manager.OrganizationRelationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationRelation;

@Controller
@Scope("prototype")
public class RoleOrganizationEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private RoleOrganizationEditBean bean = new RoleOrganizationEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleOrganizationManager")
	private AroleOrganizationManager aroleOrganizationManager;
	
	@Autowired
	@Qualifier("organizationRelationManager")
	private OrganizationRelationManager organizationRelationManager;

	/**
	 * 组织关系.
	 */
	private AroleOrganization aroleOrganization;

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
	public void onCreate$roleOrganizationEditWindow() throws Exception {
		this.bindBean();
	}

	/**
	 * .
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		if ("add".equals(opType)) {
			this.bean.getRoleOrganizationEditWindow().setTitle("新增关联组织");
			aroleOrganization = (AroleOrganization) arg
					.get("aroleOrganization");
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		AroleOrganization ao = null;
		if ("add".equals(opType)) {
			ao = new AroleOrganization();
		}
		// 填充对象
		Organization organization = bean.getOrganizationBandboxExt().getOrganization();
		if (organization == null || organization.getOrgId() == null) {
			ZkUtil.showError("关联组织不能为空", "提示信息");
			return;
		}
		/**
		 * 查询有几个根组织
		 */
		OrganizationRelation queryOrganizationRelation = new OrganizationRelation();
		queryOrganizationRelation
				.setRelaOrgId(OrganizationConstant.ROOT_ORG_ID);
		
		List<OrganizationRelation> rootOrgRelationList = organizationRelationManager
				.queryOrganizationRelationList(queryOrganizationRelation);
		/**
		 * 判断是否有多条上级树
		 */
		Map<Long, Boolean> map = new HashMap<Long, Boolean>();
		
		if (rootOrgRelationList != null && rootOrgRelationList.size() > 0) {
			for (OrganizationRelation rootOr : rootOrgRelationList) {
				map.put(rootOr.getOrgId(), rootOr.isSubOrganization(
						organization.getOrgId(), rootOr.getOrgId()));
				/**
				 * 如果选择的组织是根组织（指上级是0）
				 */
				if(organization.getOrgId().equals(rootOr.getOrgId())){
					map.put(rootOr.getOrgId(),true);
				}
			}
		}
		int count = 0;
		/**
		 * 选中组织的上级根组织id
		 */
		Long choiseOrgRootOrgId = null;
		/**
		 * 添加内部组织时会有两个根节点：9999999999【内部组织树】和9999999995【营销树】
		 * 用于排除添加内部组织节点时，受营销树的影响
		 * 如果是添加营销树根节点下面的子节点时，此方法不在适用，此时行排除内部组织树的影响，得重新写。
		 */
		Long choiseOrgTreeRootOrgId = null;
		for (OrganizationRelation rootOr : rootOrgRelationList) {
			Boolean result = map.get(rootOr.getOrgId());
			if (result != null && result) {
				count++;
				choiseOrgRootOrgId = rootOr.getOrgId();
				if (OrganizationConstant.ROOT_TREE_ORG_ID.equals(rootOr
						.getOrgId())) {
					choiseOrgTreeRootOrgId = OrganizationConstant.ROOT_TREE_ORG_ID;
				}
			}
		}
		if (count == 0) {
			ZkUtil.showError("该组织上级关系缺失，不能用来配置数据权", "提示信息");
			return;
		}
		if (count > 1) {
			if (OrganizationConstant.ROOT_TREE_ORG_ID
					.equals(choiseOrgTreeRootOrgId)) {
				choiseOrgRootOrgId = OrganizationConstant.ROOT_TREE_ORG_ID;
			} else {
				ZkUtil.showError("该组织存在多个上级组织根，不能用来配置数据权", "提示信息");
				return;
			}
		}
		/**
		 * 查看该角色id下配置了几个组织
		 */
		List<AroleOrganization> roleOrgList = aroleOrganizationManager
				.queryRoleOrganizationList(aroleOrganization);
		/**
		 * 已存在的组织根组织列表
		 */
		List<Long> existRootOrgIdList = new ArrayList();
		if (roleOrgList != null && roleOrgList.size() > 0) {
			// 判断该组织有几个根节点
			for (AroleOrganization aot : roleOrgList) {
				if (aot.getOrgId() != null) {
					for (OrganizationRelation rootOr : rootOrgRelationList) {
						if (rootOr.isSubOrganization(aot.getOrgId(), rootOr
								.getOrgId())
								|| aot.getOrgId().equals(rootOr.getOrgId())) {
							existRootOrgIdList.add(rootOr.getOrgId());
						}
					}
				}
			}
		}
		
		if (existRootOrgIdList != null && existRootOrgIdList.size() > 0) {
			if(existRootOrgIdList.contains(choiseOrgRootOrgId)){
				ZkUtil.showError("同一颗根组织树下只能配置一个组织", "提示信息");
				return;
			}
		}
		
		ao.setOrgId(organization.getOrgId());
		ao.setAroleId(aroleOrganization.getAroleId());
		this.aroleOrganizationManager.addRoleOrganization(ao);
		Events.postEvent(Events.ON_OK,
				bean.getRoleOrganizationEditWindow(), ao);
		bean.getRoleOrganizationEditWindow().onClose();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRoleOrganizationEditWindow().onClose();
	}
}
