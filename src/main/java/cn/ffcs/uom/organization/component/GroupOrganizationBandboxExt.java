package cn.ffcs.uom.organization.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.GroupOrganizationListboxExt;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.model.GroupOrganization;

@Controller
@Scope("prototype")
public class GroupOrganizationBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/group_organization_bandbox_ext.zul";
	/**
	 * 选择的组织
	 */
	@Getter
	@Setter
	private GroupOrganization groupOrganization;
	/**
	 * 组织列表
	 */
	@Getter
	@Setter
	private GroupOrganizationListboxExt groupOrganizationListboxExt;

	/**
	 * 查找的组织类型列表
	 */
	@Getter
	@Setter
	private String orgTypeList;

	private boolean isLoaded = false;

	/**
	 * 构造函数
	 */
	public GroupOrganizationBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		groupOrganizationListboxExt.getBean().getGroupOrganizationListPaging()
				.setPageSize(10);
		groupOrganizationListboxExt.getBean().getGroupOrganizationBandboxDiv()
				.setVisible(true);

		/**
		 * 监听事件
		 */
		this.groupOrganizationListboxExt.addForward(
				OrganizationTranConstant.ON_SELECT_GROUP_ORGANIZATION, this,
				"onSelectGroupOrganizationResponse");
		this.groupOrganizationListboxExt.addForward(
				OrganizationTranConstant.ON_CLEAN_GROUP_ORGANIZATION, this,
				"onCleanGroupOrganizationResponse");
		this.groupOrganizationListboxExt.addForward(
				OrganizationTranConstant.ON_CLOSE_GROUP_ORGANIZATION, this,
				"onCloseGroupOrganizationResponse");

		/**
		 * 添加点击查询事件
		 */
		this.addEventListener("onOpen", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (!isLoaded) {
					// 去除默认查询organizationListboxExt.onQueryOrganization();
				}
				isLoaded = true;
			}
		});

	}

	/**
	 * 创建接受参数,设置组织类型
	 */
	public void onCreate() {

		/**
		 * 组织类型范围
		 */
		if (!StrUtil.isEmpty(this.orgTypeList)) {
			Map map = new HashMap();
			map.put("orgTypeList", this.orgTypeList);
			Events.postEvent(OrganizationConstant.ON_SET_ORGTYPE_REQUEST,
					this.groupOrganizationListboxExt, map);
		}

	}

	public Object getAssignObject() {
		return getGroupOrganization();
	}

	public GroupOrganization getGroupOrganization() {
		return this.groupOrganization;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof GroupOrganization) {
			setGroupOrganization((GroupOrganization) assignObject);
		}
	}

	public void setGroupOrganization(GroupOrganization groupOrganization) {
		this.setValue(groupOrganization == null ? "" : groupOrganization
				.getOrgName());
		this.groupOrganization = groupOrganization;
	}

	/**
	 * 选择组织
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectGroupOrganizationResponse(final ForwardEvent event)
			throws Exception {
		groupOrganization = (GroupOrganization) event.getOrigin().getData();
		if (groupOrganization != null) {
			this.setValue(groupOrganization.getOrgName());
		}
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanGroupOrganizationResponse(final ForwardEvent event)
			throws Exception {
		this.setGroupOrganization(null);
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 关闭窗口
	 * 
	 * @param eventt
	 * @throws Exception
	 */
	public void onCloseGroupOrganizationResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
