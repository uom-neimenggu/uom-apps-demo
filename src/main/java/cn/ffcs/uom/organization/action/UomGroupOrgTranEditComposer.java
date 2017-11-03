package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.UomGroupOrgTranEditBean;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.manager.GroupOrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.GroupOrganization;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;

/**
 * 组织业务关系 Edit Composer .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author 朱林涛
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-6-17
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "rawtypes" })
public class UomGroupOrgTranEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private OrganizationTranManager organizationTranManager = (OrganizationTranManager) ApplicationContextUtil
			.getBean("organizationTranManager");

	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;

	@Autowired
	@Qualifier("groupOrganizationManager")
	private GroupOrganizationManager groupOrganizationManager;

	private UomGroupOrgTranEditBean bean = new UomGroupOrgTranEditBean();

	private UomGroupOrgTran uomGroupOrgTran;

	/**
	 * 区分组织树TAB
	 */
	@Getter
	@Setter
	private String variableOrgTreeTabName;

	private String opType;

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
	public void onCreate$uomGroupOrgTranEditComposer() {
		bindEvent();
		bindBean();
		bindCombobox();
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 */
	private void bindEvent() {
		UomGroupOrgTranEditComposer.this.bean.getUomGroupOrgTranEditComposer()
				.addEventListener("onUomGroupOrgTranChange",
						new EventListener() {
							public void onEvent(final Event event)
									throws Exception {
								if (!StrUtil.isNullOrEmpty(event.getData())) {
									UomGroupOrgTranEditComposer.this.arg = (HashMap) event
											.getData();
									bindBean();
								}
							}
						});
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() {

		opType = StrUtil.strnull(arg.get("opType"));
		uomGroupOrgTran = (UomGroupOrgTran) arg.get("uomGroupOrgTran");
		variableOrgTreeTabName = (String) arg.get("variableOrgTreeTabName");

		List<String> tranRelaTypeList = new ArrayList<String>();

		if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {

			tranRelaTypeList.add(uomGroupOrgTran.getTranRelaType());

			bean.getTranRelaType().setInitialValue(tranRelaTypeList);

		}

        if ("costTab".equals(variableOrgTreeTabName)) {// 针对财务树限定组织业务关系类型
        
            // tranRelaTypeList = new ArrayList<String>();
            //
            // tranRelaTypeList
            // .add(OrganizationTranConstant.UOM_FINACIAL_COMPANY_GROUP_COMPANY_CODE_ONE_TO_ONE);
            //
            // tranRelaTypeList
            // .add(OrganizationTranConstant.UOM_FINACIAL_PROFIT_GROUP_PROFIT_CENTER_ONE_TO_ONE);
            //
            // tranRelaTypeList
            // .add(OrganizationTranConstant.UOM_FINACIAL_COST_GROUP_COST_CENTER_ONE_TO_ONE);
            //
            // bean.getTranRelaType().setOptionNodes(tranRelaTypeList);
            
        } else if ("agentTab".equals(variableOrgTreeTabName)
            || "ibeTab".equals(variableOrgTreeTabName)) {
            
            tranRelaTypeList = new ArrayList<String>();
            
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
            
            tranRelaTypeList.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
            
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE);
            
            bean.getTranRelaType().setOptionNodes(tranRelaTypeList);
            
        } else if ("marketingTab".equals(variableOrgTreeTabName)) {
            
            tranRelaTypeList = new ArrayList<String>();
            
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
            
            bean.getTranRelaType().setOptionNodes(tranRelaTypeList);
            
        } else if ("organization".equals(variableOrgTreeTabName)) {
            // 如果是组织页面传过来的，设置对应的关系选中
            /*
             * 300205 统一用户网点组织与集团供应商对应关系[多对一]　　　供应商
             * 300101 统一用户与集团统一目录组织对应关系[一对一] 统一目录
             * 300206 统一用户营销组织与集团成本中心对应关系[多对一] 成本
             * 300204 统一用户网点组织与集团成本中心对应关系[多对一] 成本
             */
            String tranRelaType = uomGroupOrgTran.getTranRelaType();
            Map<Integer, String> groupOrgType = new HashMap<Integer, String>();
            if (tranRelaType
                .equals(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE)) {
                // 供应商 4
                groupOrgType.put(4, "4-供应商");
            } else if (tranRelaType.equals(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE)) {
                // 统一目录 5
                groupOrgType.put(5, "5-统一目录");
            } else if (tranRelaType
                .equals(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE)
                || tranRelaType
                    .equals(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE)) {
                // 成本中心 1
                groupOrgType.put(1, "1-成本");
            }
            
            // 绑定设定好的
            List<NodeVo> orgTypeList = new ArrayList();
            // 遍历map
            Set set = groupOrgType.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                NodeVo vo = new NodeVo();
                Integer value = (Integer) iter.next();
                vo.setId(String.valueOf(value));
                String attrValueName = groupOrgType.get(value);
                vo.setName(attrValueName);
                vo.setDesc(attrValueName);
                orgTypeList.add(vo);
            }
            
            ListboxUtils.rendererForEdit(bean.getTranOrg().getGroupOrganizationListboxExt()
                .getBean().getOrgType(), orgTypeList);
            
        } else {
            
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_NETWORK_GROUP_COST_CENTER_MANY_TO_ONE);
            tranRelaTypeList.add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_MANY_TO_ONE);
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_MARKETING_GROUP_COST_CENTER_MANY_TO_ONE);
            tranRelaTypeList
                .add(OrganizationTranConstant.UOM_NETWORK_GROUP_SUPPLIER_BANK_MANY_TO_ONE);
            tranRelaTypeList // 统一用户与集团统一目录组织对应关系[一对一] 300101
                .add(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE);
            bean.getTranRelaType().setOptionNodes(tranRelaTypeList);
        }

		if ("add".equals(opType)) {

			bean.getUomGroupOrgTranEditComposer().setTitle("跨域内外业务关系新增");

			if (!StrUtil.isEmpty(uomGroupOrgTran.getTranRelaType())) {
				bean.getTranRelaType().setDisabled(true);
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getOrgId())) {
				bean.getOrg().setDisabled(true);
				Organization org = organizationManager.getById(uomGroupOrgTran
						.getOrgId());
				bean.getOrg().setOrganization(org);
			}

			if (!StrUtil.isNullOrEmpty(uomGroupOrgTran.getTranOrgId())) {

				bean.getTranOrg().setDisabled(true);

				GroupOrganization groupOrganization = new GroupOrganization();
				groupOrganization.setOrgCode(uomGroupOrgTran.getTranOrgId());

				List<GroupOrganization> groupOrganizationList = groupOrganizationManager
						.queryGroupOrganizationList(groupOrganization);

				GroupOrganization tranOrg = null;

				if (groupOrganizationList != null
						&& groupOrganizationList.size() > 0) {

					tranOrg = groupOrganizationList.get(0);

				}

				bean.getTranOrg().setGroupOrganization(tranOrg);

			}

		} else if ("mod".equals(opType)) {

			bean.getUomGroupOrgTranEditComposer().setTitle("跨域内外业务关系修改");
			bean.getOrg().setDisabled(true);
			bean.getTranOrg().setDisabled(true);

			Organization org = organizationManager.getById(uomGroupOrgTran
					.getOrgId());
			bean.getOrg().setOrganization(org);

			GroupOrganization groupOrganization = new GroupOrganization();
			groupOrganization.setOrgCode(uomGroupOrgTran.getTranOrgId());

			List<GroupOrganization> groupOrganizationList = groupOrganizationManager
					.queryGroupOrganizationList(groupOrganization);

			GroupOrganization tranOrg = null;

			if (groupOrganizationList != null
					&& groupOrganizationList.size() > 0) {

				tranOrg = groupOrganizationList.get(0);

			}

			bean.getTranOrg().setGroupOrganization(tranOrg);

		}

	}

	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() {
	}

	/**
	 * 保存.
	 * 
	 * @throws Exception
	 */
	public void onOk() {

		String msg = this.getDoValidUomGroupOrgTran();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		if ("add".equals(opType)) {

			UomGroupOrgTran newUomGroupOrgTran = new UomGroupOrgTran();
			newUomGroupOrgTran.setOrgId(bean.getOrg().getOrganization()
					.getOrgId());
			newUomGroupOrgTran.setTranRelaType(bean.getTranRelaType()
					.getAttrValue());

			// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系
			List<UomGroupOrgTran> existlist = organizationTranManager
					.queryUomGroupOrgTranList(newUomGroupOrgTran, null);

			if (existlist != null && existlist.size() > 0) {
				ZkUtil.showInformation("已存在跨域内外业务关系,不能重复添加", "提示信息");
				return;
			}

			if (bean.getTranRelaType()
					.getAttrValue()
					.equals(OrganizationTranConstant.UOM_GROUP_DIRECTORY_ONE_TO_ONE)) {

				UomGroupOrgTran newUomGroupOrgTran1 = new UomGroupOrgTran();
				newUomGroupOrgTran1.setTranOrgId(bean.getTranOrg()
						.getGroupOrganization().getOrgCode());
				newUomGroupOrgTran1.setTranRelaType(bean.getTranRelaType()
						.getAttrValue());

				// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系
				List<UomGroupOrgTran> existlist1 = organizationTranManager
						.queryUomGroupOrgTranList(newUomGroupOrgTran1, null);

				if (existlist1 != null && existlist1.size() > 0) {
					ZkUtil.showInformation("业务组织已存在跨域内外业务关系,不能重复添加", "提示信息");
					return;
				}

			}

			newUomGroupOrgTran.setTranOrgId(bean.getTranOrg()
					.getGroupOrganization().getOrgCode());

			organizationTranManager.addUomGroupOrgTran(newUomGroupOrgTran);

			Events.postEvent("onOK", bean.getUomGroupOrgTranEditComposer(),
					newUomGroupOrgTran);

		} else if ("mod".equals(opType)) {

			UomGroupOrgTran newUomGroupOrgTran = new UomGroupOrgTran();
			newUomGroupOrgTran.setOrgId(bean.getOrg().getOrganization()
					.getOrgId());
			newUomGroupOrgTran.setTranOrgId(bean.getTranOrg()
					.getGroupOrganization().getOrgCode());
			newUomGroupOrgTran.setTranRelaType(bean.getTranRelaType()
					.getAttrValue());

			// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系
			List<UomGroupOrgTran> existlist = organizationTranManager
					.queryUomGroupOrgTranList(newUomGroupOrgTran, null);

			if (existlist != null && existlist.size() > 0) {
				for (UomGroupOrgTran oldUomGroupOrgTran : existlist) {
					if (uomGroupOrgTran.getOrgTranId().longValue() != oldUomGroupOrgTran
							.getOrgTranId().longValue()) {
						ZkUtil.showInformation("已存在跨域内外业务关系,不能重复添加", "提示信息");
						return;
					}
				}
			}

			organizationTranManager.removeUomGroupOrgTran(uomGroupOrgTran);
			organizationTranManager.addUomGroupOrgTran(newUomGroupOrgTran);

			Events.postEvent("onOK", bean.getUomGroupOrgTranEditComposer(),
					newUomGroupOrgTran);
		}

		bean.getUomGroupOrgTranEditComposer().onClose();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getUomGroupOrgTranEditComposer().onClose();
	}

	public String getDoValidUomGroupOrgTran() {

		if (StrUtil.isNullOrEmpty(bean.getTranRelaType())
				&& StrUtil.isNullOrEmpty(bean.getTranRelaType().getAttrValue())) {
			return "组织业务关系类型不能为空";
		}

		if (StrUtil.isNullOrEmpty(bean.getOrg().getOrganization().getOrgId())) {
			return "组织不能为空";
		}

		if (StrUtil.isNullOrEmpty(bean.getTranOrg().getGroupOrganization()
				.getOrgCode())) {
			return "业务组织不能为空";
		}

		return "";
	}

}
