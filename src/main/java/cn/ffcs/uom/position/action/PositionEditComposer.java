package cn.ffcs.uom.position.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.position.action.bean.PositionEditBean;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.manager.PositionManager;
import cn.ffcs.uom.position.model.Position;

/**
 * 岗位编辑Composer.
 * 
 * @author yh
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class PositionEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * positionEditBean.
	 */
	private PositionEditBean bean = new PositionEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("positionManager")
	private PositionManager positionManager;
	/**
	 * 岗位.
	 */
	private Position position;
	/**
	 * 父岗位.
	 */
	private Position parentPosition;
	/**
	 * 修改岗位.
	 */
	private Position oldPosition;

	private boolean isNewInstance;

	String positionCd;

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
	public void onCreate$positionEditWindow() throws Exception {

		opType = StrUtil.strnull(arg.get("opType"));

		if ("addRootNode".equals(opType)) {
			this.bindRootCombobox();
		} else {
			this.bindCombobox();
		}

		if ("addRootNode".equals(opType)) {
			this.bean.getPositionEditWindow().setTitle("新增岗位类型");
			isNewInstance = true;
		} else if ("addChildNode".equals(opType)) {
			this.bean.getPositionEditWindow().setTitle("岗位新增");
			parentPosition = (Position) arg.get("position");
			this.bean.getPositionType().setDisabled(true);
			ListboxUtils.selectByCodeValue(this.bean.getPositionType(),
					parentPosition.getPositionType());
			isNewInstance = true;
		} else {
			if ("view".equals(opType)) {
				this.bean.getPositionEditWindow().setTitle("岗位查看");
				this.bean.getOkButton().setVisible(false);
				this.bean.getCancelButton().setVisible(false);
			} else {
				this.bean.getPositionEditWindow().setTitle("岗位修改");
			}
			oldPosition = (Position) arg.get("position");
			if (oldPosition != null) {
				this.bean.getPositionDesc().setValue(
						oldPosition.getPositionDesc());
				this.bean.getPositionCode().setValue(
						oldPosition.getPositionCode());
				this.bean.getPositionName().setValue(
						oldPosition.getPositionName());
				ListboxUtils.selectByCodeValue(this.bean.getPositionType(),
						oldPosition.getPositionType());
				isNewInstance = false;
			}
		}
	}

	/**
	 * 选择树
	 * 
	 * @throws Exception
	 */
	public void onSelect$positionType() throws Exception {
		String positionTypeName = this.bean.getPositionType().getSelectedItem()
				.getLabel();

		if (positionTypeName != null && !StrUtil.isEmpty(positionTypeName)) {
			this.bean.getPositionName().setValue(positionTypeName);
		} else {
			this.bean.getPositionName().setValue(null);
		}

	}

	/**
	 * 保存.
	 */
	public void onOk() {
		if (StrUtil.isEmpty(this.bean.getPositionName().getValue())) {
			ZkUtil.showError("岗位名称不能为空,请填写", "提示信息");
			return;
		}
		if (this.bean.getPositionType().getSelectedItem().getValue() == null) {
			ZkUtil.showError("岗位类型不能为空,请填写", "提示信息");
			return;
		}

		if ("addRootNode".equals(opType)) {

			position = Position.newInstance();
			position.setParentPositionId(0L);
			positionCd = this.bean.getPositionType().getSelectedItem()
					.getValue().toString();

			if (!StrUtil.isEmpty(positionCd)) {
				position.setPositionCode(positionCd + "0000");
			}

		} else if ("addChildNode".equals(opType)) {
			position = Position.newInstance();
			position.setParentPositionId(parentPosition.getPositionId());

			if (parentPosition != null
					&& !StrUtil.isEmpty(parentPosition.getPositionCode())) {

				if (parentPosition.getPositionCode().trim().substring(2, 4)
						.equals("00")) {// 用于判断是否在第一级岗位上新增子节点

					Position positionOne = positionManager
							.jdbcFindPosition(parentPosition);

					if (positionOne != null
							&& !StrUtil.isEmpty(positionOne.getPositionCode())) {

						int positionOneCd = Integer.parseInt(positionOne
								.getPositionCode().trim().substring(2, 4)) + 1;

						if (positionOneCd > 99) {

							ZkUtil.showError("二级编码超过99,不能进行添加!", "提示信息");

						} else {
							if (positionOneCd < 10) {
								positionCd = positionOne.getPositionCode()
										.trim().substring(0, 2)
										+ "0"
										+ positionOneCd
										+ positionOne.getPositionCode().trim()
												.substring(4, 6);
							} else {
								positionCd = positionOne.getPositionCode()
										.trim().substring(0, 2)
										+ positionOneCd
										+ positionOne.getPositionCode().trim()
												.substring(4, 6);
							}
						}
					} else {
						positionCd = parentPosition.getPositionCode().trim()
								.substring(0, 2)
								+ "0100";
					}

				} else {// 用于判断是否在第二级岗位上新增子节点

					Position positionTwo = positionManager
							.jdbcFindPosition(parentPosition);

					if (positionTwo != null
							&& !StrUtil.isEmpty(positionTwo.getPositionCode())) {

						int positionTwoCd = Integer.parseInt(positionTwo
								.getPositionCode().trim().substring(4, 6)) + 1;

						if (positionTwoCd > 99) {

							ZkUtil.showError("三级编码超过99,不能进行添加!", "提示信息");

						} else {
							if (positionTwoCd < 10) {
								positionCd = positionTwo.getPositionCode()
										.trim().substring(0, 4)
										+ "0" + positionTwoCd;
							} else {
								positionCd = positionTwo.getPositionCode()
										.trim().substring(0, 4)
										+ positionTwoCd;
							}
						}
					} else {
						positionCd = parentPosition.getPositionCode().trim()
								.substring(0, 4)
								+ "01";
					}

				}
			}

			position.setPositionCode(positionCd);

		} else if ("mod".equals(opType)) {
			position = oldPosition;
		}

		position.setPositionName(this.bean.getPositionName().getValue());
		position.setPositionType(this.bean.getPositionType().getSelectedItem()
				.getValue().toString());
		position.setPositionDesc(this.bean.getPositionDesc().getValue());

		if (isNewInstance) {
			positionManager.savePosition(position);
		} else {
			positionManager.updatePosition(position);
		}
		// 抛出成功事件
		Events.postEvent("onOK", bean.getPositionEditWindow(), position);
		bean.getPositionEditWindow().onClose();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		bean.getPositionEditWindow().onClose();
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
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void bindRootCombobox() throws Exception {
		List<NodeVo> positionTypeList = positionManager.getValuesList();
		ListboxUtils.rendererForEdit(bean.getPositionType(), positionTypeList);
	}

	/**
	 * |获取员工编码，以85开头，位数为10位 .
	 * 
	 * @param stffCd
	 * @return
	 * @author zhulintao
	 * @date 2013-07-03
	 */
	@SuppressWarnings("unused")
	private String getPositionCodeByFun() {
		String positionCd = positionManager.getSeqPositionCode();
		if (!StrUtil.isNullOrEmpty(positionCd)) {
			int legh = 8 - positionCd.length();
			if (legh >= 0) {
				String px = "85";
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < legh; i++) {
					sb.append("0");
				}
				sb.append(positionCd);
				return px + sb.toString();
			}
		}
		return null;
	}

}
