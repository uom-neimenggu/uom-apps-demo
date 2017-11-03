package cn.ffcs.uom.dataPermission.action;

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
import cn.ffcs.uom.dataPermission.action.bean.RoleTelcomRegionEditBean;
import cn.ffcs.uom.dataPermission.constants.RoleTelcomRegionConstant;
import cn.ffcs.uom.dataPermission.manager.AroleTelcomRegionManager;
import cn.ffcs.uom.dataPermission.model.AroleTelcomRegion;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class RoleTelcomRegionEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private RoleTelcomRegionEditBean bean = new RoleTelcomRegionEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("aroleTelcomRegionManager")
	private AroleTelcomRegionManager aroleTelcomRegionManager;

	/**
	 * 组织关系.
	 */
	private AroleTelcomRegion aroleTelcomRegion;

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
	public void onCreate$roleTelcomRegionEditWindow() throws Exception {
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
			this.bean.getRoleTelcomRegionEditWindow().setTitle("新增关联电信管理区域");
			aroleTelcomRegion = (AroleTelcomRegion) arg
					.get("aroleTelcomRegion");
		}
	}

	/**
	 * 保存.
	 */
	public void onOk() throws Exception {
		AroleTelcomRegion atr = null;
		if ("add".equals(opType)) {
			atr = new AroleTelcomRegion();
		}
		// 填充对象
		TelcomRegion telcomRegion = bean.getTelcomRegionTreeBandbox().getTelcomRegion();
		if (telcomRegion == null || telcomRegion.getTelcomRegionId() == null) {
			ZkUtil.showError("关联电信管理区域不能为空", "提示信息");
			return;
		}
		atr.setTelcomRegionId(telcomRegion.getTelcomRegionId());
		/**
		 * 现在默认是包含下级
		 */
		atr.setFlag(RoleTelcomRegionConstant.INCLUDE_SUB_YES);
		atr.setAroleId(aroleTelcomRegion.getAroleId());
		this.aroleTelcomRegionManager.addRoleTelcomRegion(atr);
		Events.postEvent(Events.ON_OK,
				bean.getRoleTelcomRegionEditWindow(), atr);
		bean.getRoleTelcomRegionEditWindow().onClose();
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getRoleTelcomRegionEditWindow().onClose();
	}
}
