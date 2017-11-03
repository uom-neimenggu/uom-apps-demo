package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.OrganizationTranEditBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

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
public class OrganizationTranEditComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private OrganizationTranManager organizationTranManager = (OrganizationTranManager) ApplicationContextUtil
			.getBean("organizationTranManager");
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;

	private OrganizationTranEditBean bean = new OrganizationTranEditBean();

	private OrganizationTran organizationTran;

	/**
	 * 是否是聚合营销2015tab
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
	public void onCreate$organizationTranEditComposer() {
		bindEvent();
		bindBean();
		bindCombobox();
	}

	/**
	 * 监听事件 .
	 * 
	 * @throws Exception
	 * @author Wong 2013-5-25 Wong
	 */
	private void bindEvent() {
		OrganizationTranEditComposer.this.bean
				.getOrganizationTranEditComposer().addEventListener(
						"onOrganizationTranChange", new EventListener() {
							public void onEvent(final Event event)
									throws Exception {
								if (!StrUtil.isNullOrEmpty(event.getData())) {
									OrganizationTranEditComposer.this.arg = (HashMap) event
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
		organizationTran = (OrganizationTran) arg.get("organizationTran");
		variableOrgTreeTabName = (String) arg.get("variableOrgTreeTabName");

		List<String> tranRelaTypeList = new ArrayList<String>();

		if (!StrUtil.isEmpty(organizationTran.getTranRelaType())) {

			tranRelaTypeList.add(organizationTran.getTranRelaType());

			bean.getTranRelaType().setInitialValue(tranRelaTypeList);

			if ("politicalTab".equals(variableOrgTreeTabName)) {

				tranRelaTypeList = new ArrayList<String>();

				tranRelaTypeList
						.add(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE);

				bean.getTranRelaType().setOptionNodes(tranRelaTypeList);

			} else {

				tranRelaTypeList
						.add(OrganizationTranConstant.DEPARTMENT_NETWORK_MANY_TO_ONE);
				bean.getTranRelaType().setOptionNodes(tranRelaTypeList);
			}

		}

		if ("add".equals(opType)) {

			bean.getOrganizationTranEditComposer().setTitle("域内业务关系新增");

			if (!StrUtil.isEmpty(organizationTran.getTranRelaType())) {
				bean.getTranRelaType().setDisabled(true);
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getOrgId())) {
				bean.getOrg().setDisabled(true);
				Organization org = organizationManager.getById(organizationTran
						.getOrgId());
				bean.getOrg().setOrganization(org);
			}

			if (!StrUtil.isNullOrEmpty(organizationTran.getTranOrgId())) {
				bean.getTranOrg().setDisabled(true);
				Organization tranOrg = organizationManager
						.getById(organizationTran.getTranOrgId());
				bean.getTranOrg().setOrganization(tranOrg);
			}

		} else if ("mod".equals(opType)) {

			bean.getOrganizationTranEditComposer().setTitle("域内业务关系修改");
			bean.getOrg().setDisabled(true);
			bean.getTranOrg().setDisabled(true);

			Organization org = organizationManager.getById(organizationTran
					.getOrgId());
			bean.getOrg().setOrganization(org);

			Organization tranOrg = organizationManager.getById(organizationTran
					.getTranOrgId());
			bean.getTranOrg().setOrganization(tranOrg);

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

		String msg = this.getDoValidOrganizationTran();

		if (!StrUtil.isEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}

		OrganizationTran newOrganizationTran = new OrganizationTran();
		newOrganizationTran
				.setOrgId(bean.getOrg().getOrganization().getOrgId());
		newOrganizationTran.setTranRelaType(bean.getTranRelaType()
				.getAttrValue());

		// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系
		List<OrganizationTran> existlist = organizationTranManager
				.queryOrganizationTranList(newOrganizationTran, null);

		newOrganizationTran.setTranOrgId(bean.getTranOrg().getOrganization()
				.getOrgId());

		if ("add".equals(opType)) {

			if (existlist != null && existlist.size() > 0) {
				ZkUtil.showInformation("已存在域内业务关系,不能重复添加", "提示信息");
				return;
			}

			organizationTranManager.addOrganizationTran(newOrganizationTran);

		} else if ("mod".equals(opType)) {

			// 使用模糊查询 同一第二大类下，两个组织之间只能有一种业务关系
			List<OrganizationTran> existlist1 = organizationTranManager
					.queryOrganizationTranList(newOrganizationTran, null);

			if (existlist1 != null && existlist1.size() > 0) {
				for (OrganizationTran oldOrganizationTran : existlist1) {
					if (organizationTran.getOrgTranId().longValue() != oldOrganizationTran
							.getOrgTranId().longValue()) {
						ZkUtil.showInformation("已存在域内业务关系,不能重复添加", "提示信息");
						return;
					}
				}
			}

			organizationTranManager.removeOrganizationTran(organizationTran);
			organizationTranManager.addOrganizationTran(newOrganizationTran);
		}

		Events.postEvent("onOK", bean.getOrganizationTranEditComposer(),
				newOrganizationTran);

		bean.getOrganizationTranEditComposer().onClose();

	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getOrganizationTranEditComposer().onClose();
	}

	public String getDoValidOrganizationTran() {

		if (StrUtil.isNullOrEmpty(bean.getTranRelaType())
				&& StrUtil.isNullOrEmpty(bean.getTranRelaType().getAttrValue())) {
			return "组织业务关系类型不能为空";
		}

		if (StrUtil.isNullOrEmpty(bean.getOrg().getOrganization().getOrgId())) {
			return "组织不能为空";
		}

		if (StrUtil.isNullOrEmpty(bean.getTranOrg().getOrganization()
				.getOrgId())) {
			return "业务组织不能为空";
		}

		return "";
	}

}
