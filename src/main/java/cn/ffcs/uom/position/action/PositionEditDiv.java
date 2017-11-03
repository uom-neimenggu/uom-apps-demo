package cn.ffcs.uom.position.action;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Div;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.position.action.bean.PositionEditExtBean;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.Position;

/**
 * 岗位管理显示列表控件 .
 * 
 * @版权：福富软件 版权所有 (c) 2013
 * @author Zhu Lintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-06-03
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class PositionEditDiv extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private PositionEditExtBean bean = new PositionEditExtBean();

	@Autowired
	@Qualifier("positionManager")
	private PositionManager positionManager = (PositionManager) ApplicationContextUtil
			.getBean("positionManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/position/comp/position_edit_ext.zul";

	/**
	 * 选中的 selectPosition.
	 */
	private Position selectPosition;

	/**
	 * 编辑中的editPosition.
	 */
	private Position editPosition;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;

	public PositionEditDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		bindCombobox();
		PositionEditDiv.this.addForward(PositionConstant.ON_SAVE_POSITION_NAME,
				PositionEditDiv.this, "onSavePosition");
		this.addForward(PositionConstant.ON_SELECT_TREE_POSITION, this,
				"onSelectPositionResponse");
		this.setPositionButtonValid(true, false, false);
	}

	/**
	 * 岗位选择.
	 * 
	 * @param event
	 *            事件
	 * @throws Exception
	 *             异常
	 */
	public void onSelectPositionResponse(final ForwardEvent event)
			throws Exception {
		this.selectPosition = (Position) event.getOrigin().getData();

		if (this.selectPosition == null
				|| this.selectPosition.getPositionId() == null) {
			this.selectPosition = new Position();
		}

		this.bean.getPositionCode().setValue(selectPosition.getPositionCode());
		this.bean.getPositionName().setValue(selectPosition.getPositionName());
		ListboxUtils.selectByCodeValue(this.bean.getPositionType(),
				selectPosition.getPositionType());
		this.bean.getStatusCdName().setValue(selectPosition.getStatusCdName());
		this.bean.getEffDateStr().setValue(selectPosition.getEffDateStr());
		this.bean.getExpDateStr().setValue(selectPosition.getExpDateStr());
		this.bean.getPositionDesc().setValue(selectPosition.getPositionDesc());

		this.bean.getPositionName().setDisabled(true);
		this.bean.getPositionDesc().setDisabled(true);
		this.setPositionButtonValid(true, false, false);
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> positionType = UomClassProvider.getValuesList("Position",
				"positionType");
		ListboxUtils.rendererForEdit(bean.getPositionType(), positionType);

	}

	/**
	 * 点击编辑
	 * 
	 * @throws Exception
	 */
	public void onEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.bean.getPositionName().setDisabled(false);
		this.bean.getPositionDesc().setDisabled(false);
		this.setPositionButtonValid(false, true, true);
	}

	/**
	 * 点击保存
	 * 
	 * @throws Exception
	 */
	public void onSave() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		String positionName = this.bean.getPositionName().getValue();
		String positionDesc = this.bean.getPositionDesc().getValue();

		if (this.bean.getPositionName() == null
				|| StrUtil.isEmpty(positionName)) {
			ZkUtil.showError("岗位名称不能为空", "提示信息");
			return;
		}

		this.editPosition = this.selectPosition;

		if (this.bean.getPositionDesc() == null
				|| StrUtil.isEmpty(positionDesc)) {
			this.editPosition.setPositionDesc("");
		} else {
			this.editPosition.setPositionDesc(positionDesc.trim());
		}

		this.editPosition.setPositionName(positionName.trim());
		positionManager.updatePosition(editPosition);
		this.bean.getPositionName().setDisabled(true);
		this.bean.getPositionDesc().setDisabled(true);
		this.setPositionButtonValid(true, false, false);
		/**
		 * 抛出保存事件，用来在岗位树中保存更改后的岗位名称
		 */
		Events.postEvent(PositionConstant.ON_SAVE_POSITION_NAME, this,
				editPosition);
	}

	/**
	 * 点击恢复
	 * 
	 * @throws Exception
	 */
	public void onRecover() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;

		this.bean.getPositionName().setDisabled(true);
		this.bean.getPositionDesc().setDisabled(true);
		this.setPositionButtonValid(true, false, false);
		this.bean.getPositionName().setValue(selectPosition.getPositionName());
		this.bean.getPositionDesc().setValue(selectPosition.getPositionDesc());
	}

	/**
	 * 设置属性按钮的状态.
	 * 
	 * @param canAdd
	 *            新增按钮
	 * @param canView
	 *            查看按钮
	 * @param canEdit
	 *            编辑按钮
	 * @param canDelete
	 *            删除按钮
	 */
	private void setPositionButtonValid(final Boolean canEdit,
			final Boolean canSave, final Boolean canRecover) {
		if (canEdit != null) {
			this.bean.getEditButton().setDisabled(!canEdit);
		}
		this.bean.getSaveButton().setDisabled(!canSave);
		this.bean.getRecoverButton().setDisabled(!canRecover);
	}
	
	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException 
	 * @throws Exception 
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canEdit = false;
		boolean canSave = false;
		boolean canRecover = false;

		if (PlatformUtil.isAdmin()) {
			canEdit = true;
			canSave = true;
			canRecover = true;
		} else if ("positionTreePage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_INFO_EDIT)) {
				canEdit = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_INFO_SAVE)) {
				canSave = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.POSITION_TREE_POSITION_INFO_RECOVER)) {
				canRecover = true;
			}
		}
		this.bean.getEditButton().setVisible(canEdit);
		this.bean.getSaveButton().setVisible(canSave);
		this.bean.getRecoverButton().setVisible(canRecover);
	}

}
