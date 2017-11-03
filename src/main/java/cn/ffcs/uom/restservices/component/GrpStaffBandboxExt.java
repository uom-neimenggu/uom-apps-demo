package cn.ffcs.uom.restservices.component;

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

import cn.ffcs.uom.restservices.constants.ChannelInfoConstant;
import cn.ffcs.uom.restservices.model.GrpStaff;

@Controller
@Scope("prototype")
public class GrpStaffBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/restservices/comp/grp_staff_bandbox_ext.zul";
	/**
	 * 选择的集团员工
	 */
	@Getter
	@Setter
	private GrpStaff grpStaff;
	/**
	 * 集团员工列表
	 */
	@Getter
	@Setter
	private GrpStaffListboxExt grpStaffListboxExt;

	private boolean isLoaded = false;

	/**
	 * 构造函数
	 */
	public GrpStaffBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		grpStaffListboxExt.getBean().getGrpStaffListPaging().setPageSize(10);
		grpStaffListboxExt.getBean().getGrpStaffBandboxDiv().setVisible(true);

		/**
		 * 监听事件
		 */
		this.grpStaffListboxExt.addForward(
				ChannelInfoConstant.ON_SELECT_GRP_STAFF, this,
				"onSelectGrpStaffResponse");
		this.grpStaffListboxExt.addForward(
				ChannelInfoConstant.ON_CLEAN_GRP_STAFF, this,
				"onCleanGrpStaffResponse");
		this.grpStaffListboxExt.addForward(
				ChannelInfoConstant.ON_CLOSE_GRP_STAFF, this,
				"onCloseGrpStaffResponse");

		/**
		 * 添加点击查询事件
		 */
		this.addEventListener("onOpen", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (!isLoaded) {
					// 去除默认查询grpStaffListboxExt."onQueryGrpStaff"();
				}
				isLoaded = true;
			}
		});

	}

	/**
	 * 创建
	 */
	public void onCreate() {

	}

	public Object getAssignObject() {
		return getGrpStaff();
	}

	public GrpStaff getGrpStaff() {
		return this.grpStaff;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof GrpStaff) {
			setGrpStaff((GrpStaff) assignObject);
		}
	}

	public void setGrpStaff(GrpStaff grpStaff) {
		this.setValue(grpStaff == null ? "" : grpStaff.getStaffName());
		this.grpStaff = grpStaff;
	}

	/**
	 * 选择集团员工
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectGrpStaffResponse(final ForwardEvent event)
			throws Exception {
		grpStaff = (GrpStaff) event.getOrigin().getData();
		if (grpStaff != null) {
			this.setValue(grpStaff.getStaffName());
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
	public void onCleanGrpStaffResponse(final ForwardEvent event)
			throws Exception {
		this.setGrpStaff(null);
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 关闭窗口
	 * 
	 * @param eventt
	 * @throws Exception
	 */
	public void onCloseGrpStaffResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
