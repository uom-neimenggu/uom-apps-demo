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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.OrganizationPositionExt;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.OrganizationRelationConstant;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.position.model.OrgPosition;
import cn.ffcs.uom.staff.model.Staff;

@Controller
@Scope("prototype")
public class OrganizationPositionBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/organization/comp/organization_position_bandbox_ext.zul";
	/**
	 * positionMainWin
	 */
	@Getter
	@Setter
	private OrganizationPositionExt organizationPositionExt;

	/**
	 * 组织岗位
	 */
	private OrgPosition orgPosition;

	/**
	 * 员工
	 */
	@Setter
	private Staff staff;
	/**
	 * 组织
	 */
	@Setter
	private Organization organization;

	/**
	 * 操作类型
	 */
	@Setter
	private String opType;

	/**
	 * 构造函数
	 */
	public OrganizationPositionBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.organizationPositionExt.addForward(
				OrganizationConstant.ON_SELECT_ORGANIZATION_POSITION_REQUEST,
				this,
				OrganizationConstant.ON_SELECT_ORGANIZATION_POSITION_RESPONSE);

		this.organizationPositionExt.addForward(
				OrganizationConstant.ON_CLEAN_ORGANIZATION_POSITION_REQUEST,
				this,
				OrganizationConstant.ON_CLEAN_ORGANIZATION_POSITION_RESPONSE);

		this.organizationPositionExt.addForward(
				OrganizationConstant.ON_CLOSE_ORGANIZATION_POSITION_REQUEST,
				this,
				OrganizationConstant.ON_CLOSE_ORGANIZATION_POSITION_RESPONSE);
	}

	/**
	 * 创建初始化
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		this.organizationPositionExt.setBandboxButtonValid(false, false, true,
				true);
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		Map map = new HashMap();
		if (!StrUtil.isEmpty(opType)) {
			if ("orgPosition".equals(opType)) {
				map.put("organization", organization);
			} else if ("staffPosition".equals(opType)) {
				map.put("staff", staff);
			}
			map.put("opType", opType);
			Events
					.postEvent(
							OrganizationRelationConstant.ON_STAFF_OR_ORG_QUERY_POSITION,
							this.organizationPositionExt, map);
		}
	}

	public Object getAssignObject() {
		return getOrgPosition();
	}

	public OrgPosition getOrgPosition() {
		return this.orgPosition;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof Organization) {
			setOrgPosition((OrgPosition) assignObject);
		}
	}

	public void setOrgPosition(OrgPosition orgPosition) {
		this.setValue(orgPosition == null ? "" : orgPosition.getPositionName());
		this.orgPosition = orgPosition;
	}

	/**
	 * 选择组织岗位
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectOrganizationPositionResponse(final ForwardEvent event)
			throws Exception {
		orgPosition = (OrgPosition) event.getOrigin().getData();
		if (orgPosition != null) {
			this.setValue(orgPosition.getPositionName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanOrganizationPositionResponse(final ForwardEvent event)
			throws Exception {
		this.setOrgPosition(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCloseOrganizationPositionResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}
}
