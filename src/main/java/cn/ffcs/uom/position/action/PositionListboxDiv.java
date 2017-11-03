package cn.ffcs.uom.position.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.position.action.bean.PositionListboxBean;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.OrgPosition;
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
public class PositionListboxDiv extends Div implements IdSpace {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;

	private PositionListboxBean bean = new PositionListboxBean();

	@Autowired
	@Qualifier("positionManager")
	private PositionManager positionManager = (PositionManager) ApplicationContextUtil
			.getBean("positionManager");
	/**
	 * zul.
	 */
	private final String zul = "/pages/position/comp/position_listbox.zul";

	/**
	 * position.
	 */
	private Position position;

	/**
	 * 查询position.
	 */
	private Position queryPosition;
	/**
	 * 组织岗位关系
	 */
	private List<OrgPosition> orgPositionList;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * 操作类型
	 * 
	 * @throws Exception
	 */
	private String opType;

	public PositionListboxDiv() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.onQueryPosition();
		bindCombobox();
		this.setPositionButtonValid(true, false, false, false);
	}

	public void onQueryPositionResponse(final ForwardEvent event)
			throws Exception {
		this.bean.getPositionListboxPaging().setActivePage(0);
		queryPosition = (Position) event.getOrigin().getData();
		this.onQueryPosition();
		if (this.bean.getPositionListbox().getItemCount() == 0) {
			this.setPositionButtonValid(true, false, false, false);
		}
	}

	/**
	 * 岗位选择.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionSelectRequest() throws Exception {
		if (this.bean.getPositionListbox().getSelectedIndex() != -1) {
			position = (Position) bean.getPositionListbox().getSelectedItem()
					.getValue();
			this.setPositionButtonValid(true, true, true, true);
			Events.postEvent(PositionConstant.ON_SELECT_POSITION, this,
					position);
		}

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
	 * 分页.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionListboxPaging() throws Exception {
		queryPosition = Position.newInstance();
		this.queryPosition(queryPosition);
		this.setPositionButtonValid(true, false, false, false);
	}

	/**
	 * 新增岗位.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionAdd() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openPositionEditWin("add");
	}

	/**
	 * 查看岗位.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionView() throws Exception {
		this.openPositionEditWin("view");
	}

	/**
	 * 修改岗位 .
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionEdit() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		this.openPositionEditWin("mod");
	}

	/**
	 * 删除岗位.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onPositionDel() throws Exception {
		if (!PlatformUtil.checkPermissionDialog(getPortletInfoProvider(),
				ActionKeys.DATA_OPERATING))
			return;
		ZkUtil.showQuestion("确定要删除吗?", "提示信息", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Integer result = (Integer) event.getData();
				if (result == Messagebox.OK) {
					if (position == null || position.getPositionId() == null) {
						ZkUtil.showError("请选择你要删除的记录", "提示信息");
						return;
					} else {
						orgPositionList = position.getOrgPositionList();
						if (orgPositionList != null
								&& orgPositionList.size() > 0) {
							ZkUtil.showError("你选择的岗位存在于组织，请先从组织删除该岗位", "提示信息");
							return;
						}
						positionManager.removePosition(position);
						cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
								bean.getPositionListbox(), position, "del");
						// queryPosition = Position.newInstance();
						// queryPosition(queryPosition);
					}
				}
			}
		});

	}

	/**
	 * 打开岗位编辑窗口.
	 * 
	 * @param opType
	 *            操作类型
	 * @throws Exception
	 *             异常
	 */
	private void openPositionEditWin(String type) throws Exception {
		Map arg = new HashMap();
		this.opType = type;
		arg.put("opType", opType);
		if (opType.equals("mod") || opType.equals("view")) {
			arg.put("position", position);
		}
		Window win = (Window) Executions.createComponents(
				"/pages/position/position_edit.zul", this, arg);
		win.doModal();
		win.addEventListener("onOK", new EventListener() {
			public void onEvent(Event event) throws Exception {
				setPositionButtonValid(true, false, false, false);
				if ("add".equals(opType)) {
					cn.ffcs.uom.common.zul.PubUtil.reDisplayListbox(
							bean.getPositionListbox(),
							(Position) event.getData(), "add");
				} else if ("mod".equals(opType)) {

					ListModelList model = (ListModelList) bean
							.getPositionListbox().getModel();
					for (int i = 0; i < model.getSize(); i++) {
						Position position = (Position) model.get(i);
						if (position.getPositionId().equals(
								((Position) event.getData()).getPositionId())) {
							model.set(i, position);
							ListModel dataList = new BindingListModelList(
									model, true);
							bean.getPositionListbox().setModel(dataList);
						}
					}
					// queryPosition = Position.newInstance();
					// queryPosition(queryPosition);
				}
			}
		});
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
	private void setPositionButtonValid(final Boolean canAdd,
			final Boolean canView, final Boolean canEdit,
			final Boolean canDelete) {
		if (canAdd != null) {
			this.bean.getAddPositionButton().setDisabled(!canAdd);
		}
		this.bean.getViewPositionButton().setDisabled(!canView);
		this.bean.getEditPositionButton().setDisabled(!canEdit);
		this.bean.getDelPositionButton().setDisabled(!canDelete);
	}

	/**
	 * 查询岗位.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void queryPosition(Position position) throws Exception {
		PageInfo pageInfo = this.positionManager.queryPageInfoByQuertPosition(
				queryPosition, this.bean.getPositionListboxPaging()
						.getActivePage() + 1, this.bean
						.getPositionListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getPositionListbox().setModel(dataList);
		this.bean.getPositionListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 按条件查询
	 * 
	 * @throws Exception
	 */
	public void onQueryPosition() throws Exception {
		queryPosition = Position.newInstance();
		if (this.bean.getPositionCode() != null
				&& !StrUtil.isEmpty(this.bean.getPositionCode().getValue())) {
			queryPosition.setPositionCode(this.bean.getPositionCode()
					.getValue());
		}
		if (this.bean.getPositionName() != null
				&& !StrUtil.isEmpty(this.bean.getPositionName().getValue())) {
			queryPosition.setPositionName(this.bean.getPositionName()
					.getValue());
		}
		if (this.bean.getPositionType().getSelectedItem() != null
				&& this.bean.getPositionType() != null) {
			queryPosition.setPositionType((String) this.bean.getPositionType()
					.getSelectedItem().getValue());
		}
		this.queryPosition(queryPosition);
	}

	/**
	 * 重置
	 */
	public void onResetPosition() {
		this.bean.getPositionCode().setValue(null);
		this.bean.getPositionName().setValue(null);
		ListboxUtils.selectByCodeValue(this.bean.getPositionType(), null);
	}

	/**
	 * 关闭窗口
	 */
	public void onClosePosition() {
		Events.postEvent(PositionConstant.ON_CLOSE_POSITION, this, null);

	}

	/**
	 * 清除岗位
	 */
	public void onCleanPosition() {
		this.bean.getPositionListbox().clearSelection();
		Events.postEvent(PositionConstant.ON_CLEAN_POSITION, this, null);

	}

	/**
	 * Window按钮可见.
	 * 
	 * @param visible
	 */
	public void setPositionWindowDivVisible(boolean visible) {
		bean.getPositionWindowDiv().setVisible(visible);
	}

	/**
	 * 设置bandbox按钮
	 * 
	 * @param visible
	 */
	public void setPositionBandboxDivVisible(boolean visible) {
		bean.getPositionBandboxDiv().setVisible(visible);
	}
}
