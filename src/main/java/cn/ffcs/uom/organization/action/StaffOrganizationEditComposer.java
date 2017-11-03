package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationEditBean;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 组织员工关系编辑Composer.
 * 
 * @author OUZHF
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class StaffOrganizationEditComposer extends BasePortletComposer {

	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private StaffOrganizationEditBean bean = new StaffOrganizationEditBean();

	/**
	 * 操作类型.
	 */
	private String opType = null; // 操作类型
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("staffOrganizationManager")
	private StaffOrganizationManager staffOrganizationManager;

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager;
	
	/**
     * 日志服务队列
     */
	@Autowired
    @Qualifier("logService")
    private LogService logService;

	/**
	 * 修改的组织关系
	 */
	private StaffOrganization oldStaffOrganization;

	/**
	 * 用于刷新的组织关系
	 */
	private StaffOrganization refreshStaffOrganization;

	/**
	 * 修改之前的关联关系
	 */
	private String oldRalaCd;

	private Staff objStaff;
	/**
	 * 是否是组织树页面
	 */
	private Boolean isOrgTreePage = false;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		bean.getStaffBandboxExt().addForward(
				SffOrPtyCtants.ON_STAFF_ORG_SELECT, comp,
				SffOrPtyCtants.ON_STAFF_ORG_RESPONSE);
		/**
		 * 在组织树界面的 “代理商管理界面－>组织员工”，“添加员工”只显示用工性质为“外部-代理商员工”的员工
		 */
		Events.postEvent(SffOrPtyCtants.ON_ENTER_AGENT_PAGE,
				bean.getStaffBandboxExt(), arg.get("isAgentTab"));
		/**
		 * 在组织树界面的 “内部经营实体管理界面－>组织员工”，“添加员工”只显示用工性质非“外部-代理商员工”的员工
		 */
		Events.postEvent(SffOrPtyCtants.ON_ENTER_IBE_PAGE,
				bean.getStaffBandboxExt(), arg.get("isIbeTab"));
	}

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$staffOrganizationEditWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> ralaCdList = UomClassProvider.getValuesList(
				"StaffOrganization", "ralaCd");
		ListboxUtils.rendererForEdit(this.bean.getRalaCd(), ralaCdList);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		opType = StrUtil.strnull(arg.get("opType"));
		oldStaffOrganization = (StaffOrganization) arg
				.get("oldStaffOrganization");
		/**
		 * 是否是组织树页面，是的话组织不可选
		 */
		isOrgTreePage = (Boolean) arg.get("isOrgTreePage");
		if (isOrgTreePage != null && isOrgTreePage) {
			this.bean.getOrg().setDisabled(true);
		}
		if ("add".equals(opType)) {
			this.bean.getStaffOrganizationEditWindow().setTitle("组织员工关系新增");
			objStaff = (Staff) arg.get("staff"); // wangy
			if (null != objStaff) {
				bean.getStaffBandboxExt().setValue(objStaff.getStaffName());
				bean.getStaffBandboxExt().setDisabled(true); // 在员工选定的情况下，对员工bandbox不可编辑
			}
			/**
			 * 新增默认排序号200
			 */
			this.bean.getStaffSeq().setValue(200);
		} else if ("mod".equals(opType)) {
			this.bean.getStaffOrganizationEditWindow().setTitle("组织员工关系修改");
			oldStaffOrganization = (StaffOrganization) arg
					.get("updateStaffOrganization");
			this.bean.getOrg().setDisabled(true);
			this.bean.getStaffBandboxExt().setDisabled(true);
			if (oldStaffOrganization != null) {
				oldRalaCd = oldStaffOrganization.getRalaCd();
				oldStaffOrganization.setReason("");
				PubUtil.fillBeanFromPo(oldStaffOrganization, this.bean);
				this.bean.getStaffBandboxExt().setStaff(
						oldStaffOrganization.getStaff());
				if (oldStaffOrganization.getStaffSeq() != null) {
					this.bean.getStaffSeq()
							.setValue(
									new Integer(oldStaffOrganization
											.getStaffSeq() + ""));
				}
			}
		}
		if (oldStaffOrganization != null
				&& oldStaffOrganization.getOrgId() != null) {
			Organization org = organizationManager.getById(oldStaffOrganization
					.getOrgId());
			bean.getOrg().setOrganization(org);
		}
	}

	/**
	 * 保存.
	 */
	@SuppressWarnings("unchecked")
	public void onOk() throws Exception {
		StaffOrganization staffOrganization = null;
		/**
         * 开始日志添加操作
         * 添加日志到队列需要：
         * 业务开始时间，日志消息类型，错误编码和描述
         */
        SysLog log = new SysLog();
        log.startLog(new Date(), SysLogConstrants.STAFF);
        //获取当前操作用户
//        log.setUser(PlatformUtil.getCurrentUser());
		if ("add".equals(opType)) {
		    
			staffOrganization = new StaffOrganization();
		} else if ("mod".equals(opType)) {
			/**
			 * 20131024修改的逻辑（修改员工，组织，关系类型3个有修改一个：先删除旧的，新增一条新的保持旧的数据的记录）
			 */
			staffOrganization = new StaffOrganization();
			BeanUtils.copyProperties(staffOrganization, oldStaffOrganization);
		}
		// 填充对象
		PubUtil.fillPoFromBean(bean, staffOrganization);
		Organization org = bean.getOrg().getOrganization();
		if (org != null) {
			staffOrganization.setOrgId(org.getOrgId());
		}
		Staff staff = null;
		if (null == objStaff) { // wangy
			staff = bean.getStaffBandboxExt().getStaff();
		} else {
			staff = objStaff;
		}
		if (staff != null) {
			staffOrganization.setStaffId(staff.getStaffId());
		}
		if (this.bean.getStaffSeq().getValue() != null) {
			staffOrganization.setStaffSeq(new Long(this.bean.getStaffSeq()
					.getValue() + ""));
		}
		String msg = this.doValidate(staffOrganization);
		if (!StrUtil.isNullOrEmpty(msg)) {
			ZkUtil.showError(msg, "提示信息");
			return;
		}
		if ("add".equals(opType)) {
			/**
			 * 20140331一个员工只能和组织有一种关系
			 */
			StaffOrganization vo = new StaffOrganization();
			vo.setStaffId(staffOrganization.getStaffId());
			vo.setOrgId(staffOrganization.getOrgId());
			List<StaffOrganization> existlist = staffOrganizationManager
					.queryStaffOrganizationList(vo);
			if (existlist != null && existlist.size() > 0) {
				ZkUtil.showError("该员工和该组织已存在关系,不能重复添加", "错误信息");
				return;
			}

			// 添加员工组织关系规则校验 zhulintao
			if (true) {
				String msgStr = staffOrganizationManager.doStaffOrgRelRule(
						staff, null, org);

				if (!StrUtil.isNullOrEmpty(msgStr)) {
					ZkUtil.showError(msgStr, "提示信息");
					return;
				}
			}

			/**
			 * 20130910如果新增的是归属关系，则将已有的归属关系改为兼职;
			 * 
			 */
			if (BaseUnitConstants.RALA_CD_1.equals(staffOrganization
					.getRalaCd())) {
				StaffOrganization queryStaffOrganization = new StaffOrganization();
				queryStaffOrganization.setStaffId(staffOrganization
						.getStaffId());
				queryStaffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);
				/**
				 * 已存在的归属关系列表：更改为兼职（先删除归属然后新增兼职）
				 */
				List<StaffOrganization> list = staffOrganizationManager
						.queryStaffOrganizationList(queryStaffOrganization);
				staffOrganization.setNeedUpdateStaffOrganizationlist(list);
			} else {
				/**
				 * 新增非归属关系：判读是否是唯一关系是的话改为归属关系
				 */
				StaffOrganization queryStaffOrganization = new StaffOrganization();
				queryStaffOrganization.setStaffId(staffOrganization
						.getStaffId());
				/**
				 * 员工存在的组织关系列表
				 */
				List<StaffOrganization> list = staffOrganizationManager
						.queryStaffOrganizationList(queryStaffOrganization);
				if (list != null && list.size() <= 0) {
					staffOrganization.setRalaCd(BaseUnitConstants.RALA_CD_1);
				}
			}
			staffOrganization.setUserCode(staffOrganizationManager
					.getOrgUserCode());
			this.staffOrganizationManager
					.addStaffOrganization(staffOrganization);
			/**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
			Class clazz[] = {StaffOrganization.class};
		    log.endLog(logService, clazz, SysLogConstrants.ADD, SysLogConstrants.INFO, "员工组织关系添加记录日志");
			Events.postEvent(Events.ON_OK,
					bean.getStaffOrganizationEditWindow(), staffOrganization);
			bean.getStaffOrganizationEditWindow().onClose();
		} else if ("mod".equals(opType)) {
		    /**
		     * 开始日志添加操作
		     * 添加日志到队列需要：
		     * 业务开始时间，日志消息类型，错误编码和描述
		     */
		    log.startLog(new Date(), SysLogConstrants.STAFF);
		    //获取当前操作用户，去你的，以后杜绝使用这个方法
//		    log.setUser(PlatformUtil.getCurrentUser());
			String userCode = staffOrganization.getUserCode();
			if (StrUtil.isNullOrEmpty(userCode)) {
				staffOrganization.setUserCode(staffOrganizationManager
						.getOrgUserCode());
			}
			/**
			 * 如果员工，组织，关联关系都没变直接做更新
			 */
			if (staffOrganization.getOrgId().equals(
					oldStaffOrganization.getOrgId())
					&& staffOrganization.getStaffId().equals(
							oldStaffOrganization.getStaffId())
					&& staffOrganization.getRalaCd().equals(
							oldStaffOrganization.getRalaCd())) {
				/**
				 * 其实就是只改了序号和备注,以及变更描述
				 */
				oldStaffOrganization.setStaffSeq(staffOrganization
						.getStaffSeq());
				oldStaffOrganization.setNote(staffOrganization.getNote());
				oldStaffOrganization.setReason(staffOrganization.getReason());
				
				if (StrUtil.isEmpty(oldStaffOrganization.getUserCode())) {
					oldStaffOrganization.setUserCode(staffOrganizationManager
							.getOrgUserCode());
				}
				this.staffOrganizationManager
						.updateStaffOrganization(oldStaffOrganization);
				Events.postEvent(Events.ON_OK,
						bean.getStaffOrganizationEditWindow(),
						oldStaffOrganization);
				bean.getStaffOrganizationEditWindow().onClose();
			} else {
				/**
				 * 20130911修改：如果非归属关系改为非归属关系，则修改；如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
				 * 如果将归属关系修改为非归属关系，则判断弹出一个对话框
				 * 
				 */
				StaffOrganization queryStaffOrganization = new StaffOrganization();
				queryStaffOrganization.setStaffId(staffOrganization
						.getStaffId());
				List<StaffOrganization> listStaffOrganization = staffOrganizationManager
						.queryStaffOrganizationList(queryStaffOrganization);
				if (null != listStaffOrganization
						&& listStaffOrganization.size() == 1) {
					// 如果没有其他关系则强制修改为归属关系
					if (!BaseUnitConstants.RALA_CD_1
							.equals(oldStaffOrganization.getRalaCd())) {
						staffOrganization
								.setRalaCd(BaseUnitConstants.RALA_CD_1);
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
						/**
					     * 开始日志添加操作
					     * 添加日志到队列需要：
					     * 业务开始时间，日志消息类型，错误编码和描述
					     */
						Class clazz[] = {StaffOrganization.class};
					    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "员工组织关系,没有其他关系则强制修改为归属关系记录日志");
					}
					ZkUtil.showInformation("该员工只有一条员工组织记录强制为归属关系", "提示信息");
					Events.postEvent(Events.ON_OK,
							bean.getStaffOrganizationEditWindow(),
							staffOrganization);
					bean.getStaffOrganizationEditWindow().onClose();
				} else if (null != listStaffOrganization
						&& listStaffOrganization.size() > 1) {
					// 如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
					if (BaseUnitConstants.RALA_CD_1.equals(staffOrganization
							.getRalaCd())
							&& !BaseUnitConstants.RALA_CD_1.equals(oldRalaCd)) {
						StaffOrganization querySo = new StaffOrganization();
						querySo.setStaffId(staffOrganization.getStaffId());
						querySo.setRalaCd(BaseUnitConstants.RALA_CD_1);
						/**
						 * 原来归属改非归属
						 */
						List<StaffOrganization> needUpdateToRela3List = this.staffOrganizationManager
								.queryStaffOrganizationList(querySo);
						staffOrganization
								.setNeedUpdateToRela3List(needUpdateToRela3List);
						/**
						 * 删除旧的 关系，复制新增归属关系
						 */
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
						/**
					     * 开始日志添加操作
					     * 添加日志到队列需要：
					     * 业务开始时间，日志消息类型，错误编码和描述
					     */
						Class clazz[] = {StaffOrganization.class};
					    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "员工组织关系,非归属关系改为归属关系，则将原来的归属关系改为兼职记录日志");
						Events.postEvent(Events.ON_OK,
								bean.getStaffOrganizationEditWindow(),
								staffOrganization);
						bean.getStaffOrganizationEditWindow().onClose();
					} else if (!BaseUnitConstants.RALA_CD_1.equals(oldRalaCd)
							&& !BaseUnitConstants.RALA_CD_1
									.equals(staffOrganization.getRalaCd())) {
						// 非归属关系修改为非归属关系
						List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
						needRemoveList.add(oldStaffOrganization);
						staffOrganization.setNeedRemoveList(needRemoveList);
						this.staffOrganizationManager
								.updateStaffOrganizationRelation(staffOrganization);
						/**
					     * 开始日志添加操作
					     * 添加日志到队列需要：
					     * 业务开始时间，日志消息类型，错误编码和描述
					     */
						Class clazz[] = {StaffOrganization.class};
					    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "员工组织关系,非归属关系修改为非归属关系记录日志");
						Events.postEvent(Events.ON_OK,
								bean.getStaffOrganizationEditWindow(),
								staffOrganization);
						bean.getStaffOrganizationEditWindow().onClose();
					} else {
						// 如果将归属关系修改为非归属关系，则判断弹出一个对话框
						refreshStaffOrganization = staffOrganization;
						final StaffOrganization innerStaffOrganization = staffOrganization;
						Map arg = new HashMap();
						arg.put("oldStaffOrganization", oldStaffOrganization);
						Window win = (Window) Executions
								.createComponents(
										"/pages/organization/staff_organization_relation_listbox.zul",
										this.self, arg);
						win.doModal();
						win.addEventListener(Events.ON_OK, new EventListener() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (null != event.getData()) {
									StaffOrganization chooseSo = (StaffOrganization) event
											.getData();
									if (chooseSo != null) {
									    /**
									     * 开始日志添加操作
									     * 添加日志到队列需要：
									     * 业务开始时间，日志消息类型，错误编码和描述
									     */
									    SysLog log = new SysLog();
									    log.startLog(new Date(), SysLogConstrants.STAFF);
									    //获取当前操作用户
//									    log.setUser(PlatformUtil.getCurrentUser());
										List<StaffOrganization> needUpdateToRela1List = new ArrayList<StaffOrganization>();
										needUpdateToRela1List.add(chooseSo);
										innerStaffOrganization
												.setNeedUpdateToRela1List(needUpdateToRela1List);
										List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
										needRemoveList
												.add(oldStaffOrganization);
										innerStaffOrganization
												.setNeedRemoveList(needRemoveList);
										staffOrganizationManager
												.updateStaffOrganizationRelation(innerStaffOrganization);
										/**
									     * 开始日志添加操作
									     * 添加日志到队列需要：
									     * 业务开始时间，日志消息类型，错误编码和描述
									     */
										Class clazz[] = {StaffOrganization.class};
									    log.endLog(logService, clazz, SysLogConstrants.EDIT, SysLogConstrants.INFO, "员工组织关系,归属关系修改为非归属关系记录日志");
										
										Events.postEvent(
												Events.ON_OK,
												bean.getStaffOrganizationEditWindow(),
												innerStaffOrganization);
										bean.getStaffOrganizationEditWindow()
												.onClose();
									}
								}
							}
						});
					}
				}
			}
		}
	}

	/**
	 * 取消.
	 */
	public void onCancel() {
		this.bean.getStaffOrganizationEditWindow().onClose();
	}

	/**
	 * 验证数据
	 * 
	 * @param staffOrganization
	 * @return
	 */
	private String doValidate(StaffOrganization staffOrganization) {
		if (staffOrganization.getOrgId() == null) {
			return "组织不能为空！";
		}
		if (staffOrganization.getStaffId() == null) {
			return "员工不能为空！";
		}
		if (staffOrganization.getRalaCd() == null) {
			return "关联类型不能为空！";
		}
		if (StrUtil.isNullOrEmpty(staffOrganization.getReason())) {
			return "变更原因不能为空！";
		}
		return null;
	}

	public void onSelectOrgStaffResponse(final ForwardEvent event) {
		// Staff staff = (Staff)event.getOrigin().getData();
		// if(null!=staff){
		// String pins = getChineseSpelling(staff.getStaffName());
		// bean.getUserCode().setValue(pins);
		// }
	}

	public void refreshStaffOrganizationListbox() {
		Events.postEvent(Events.ON_OK, bean.getStaffOrganizationEditWindow(),
				refreshStaffOrganization);
	}
}
