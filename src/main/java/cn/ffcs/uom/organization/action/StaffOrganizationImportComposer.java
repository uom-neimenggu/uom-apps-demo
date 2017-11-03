package cn.ffcs.uom.organization.action;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.BeanUtils;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.StaffOrganizationImportBean;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.organization.constants.StaffOrganizationConstant;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.manager.StaffOrganizationManager;
import cn.ffcs.uom.organization.model.OrgType;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.StaffOrganization;
import cn.ffcs.uom.organization.vo.StaffOrganizationImportVo;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author xiaof
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2016年10月10日
 * @功能说明：员工组织关系批量导入页面
 *
 */
@Controller
@Scope("prototype")
public class StaffOrganizationImportComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private StaffOrganizationImportBean bean = new StaffOrganizationImportBean();

	/**
	 * 上传的文件
	 */
	private Media media = null;

	private static final int[] totalColumn = { 9 };

	/**
	 * 等待操作的批量数据
	 */
	private List<StaffOrganizationImportVo> waitUpLoadStaffOrganizationInfoList = new ArrayList<StaffOrganizationImportVo>();

	/**
	 * manager
	 */
	@Autowired
	@Qualifier("staffOrganizationManager")
	private StaffOrganizationManager staffOrganizationManager = (StaffOrganizationManager) ApplicationContextUtil
			.getBean("staffOrganizationManager");

	@Autowired
	@Qualifier("staffManager")
	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

	@Autowired
	@Qualifier("organizationManager")
	private OrganizationManager organizationManager = (OrganizationManager) ApplicationContextUtil
			.getBean("organizationManager");

	/**
	 * 日志服务队列
	 */
	@Autowired
	@Qualifier("logService")
	private LogService logService = (LogService) ApplicationContextUtil
			.getBean("logService");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	// 创建页面
	public void onCreate$staffOrganizationImportWindow() {
		// 初始化页面bean
		bindBean();
	}

	private void bindBean() {
		// 设定页面标题
		bean.getStaffOrganizationImportWindow().setTitle("员工组织关系批量导入");
	}

	/**
	 * 文件上传 .
	 * 
	 * @author xiaof 2016年10月10日 xiaof
	 */
	public void onUpload$fileupload(ForwardEvent event) {
		// 获取上传的文件内容
		media = ((UploadEvent) event.getOrigin()).getMedia();
	}

	private String getValidateMsg(int rowNumber, int colNumber,
			String validateType, StringBuffer sb) {
		sb.setLength(0);
		if (StaffOrganizationConstant.NULL_OR_EMPTY.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.NULL_OR_EMPTY_STR)
					.append("的信息； ").toString();
		} else if (StaffOrganizationConstant.FIELD_REPEAT.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.FIELD_REPEAT_STR)
					.append("的信息； ").toString();
		} else if (StaffOrganizationConstant.LENGTH_LIMIT.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.LENGTH_LIMIT_STR)
					.append("的信息； ").toString();
		} else if (StaffOrganizationConstant.FIELD_ERROR.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.FIELD_ERROR_STR)
					.append("的信息； ").toString();
		} else if (StaffOrganizationConstant.FIELD_NOT_EXIST
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.FIELD_NOT_EXIST_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.FIELD_ERROR_VAL
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.FIELD_ERROR_VAL_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.FIELD_EXIST_VAL
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.FIELD_EXIST_VAL_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.STAFF_EXIST_MANY_ACCOUNT
				.equals(validateType)) {
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(StaffOrganizationConstant.STAFF_EXIST_MANY_ACCOUNT_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.ORG_REL_RULE_ERROR
				.equals(validateType)) {
			StringBuffer sbtemp = new StringBuffer();
			sbtemp.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入").append("； ");
			// 吧字符串插入到前面
			return sb.insert(0, sbtemp.toString()).toString();
		} else if (StaffOrganizationConstant.ORG_REL_MANY_RELATION
				.equals(validateType)) {
			// ORG_REL_MANY_RELATION
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(StaffOrganizationConstant.ORG_REL_MANY_RELATION_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.ORG_REL_RALA_CD_1_CHANGE
				.equals(validateType)) {
			// ORG_REL_RALA_CD_1_CHANGE
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(StaffOrganizationConstant.ORG_REL_RALA_CD_1_CHANGE_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.ORG_REL_OTHER_REALTION
				.equals(validateType)) {
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(StaffOrganizationConstant.ORG_REL_OTHER_REALTION_STR)
					.append("； ").toString();
		} else if (StaffOrganizationConstant.ORG_REL_EXIST.equals(validateType)) {
			// ORG_REL_EXIST
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(StaffOrganizationConstant.ORG_REL_EXIST_STR)
					.append("； ").toString();
		}

		return "";
	}

	/**
	 * 提交文件 .
	 * 
	 * @author xiaof 2016年10月10日 xiaof
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onOk() {
		/*
		 * 1、判断上传文件是否为空 2、上传文件是否是execl文件
		 */
		if (media == null) {
			// 弹出提示框
			ZkUtil.showError("请选择要上传的文件!", "系统提示");
			return;
		}

		// 获取文件名，判断后缀是否是xls或者xlsx
		String fileName = media.getName();
		if (!(fileName.endsWith(".xls") | fileName.endsWith(".xlsx"))) {
			ZkUtil.showError("导入的文件必须是以.xls或.xlsx结尾的EXCEL文件!", "系统提示");
			return;
		}

		// 读取数据
		// 读取excel数据
		try {
			/**
			 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
			 */
			SysLog log = new SysLog();
			log.startLog(new Date(), SysLogConstrants.STAFF);
			// 获取当前操作用户
			// log.setUser(PlatformUtil.getCurrentUser());
			// 读取数据
			String[][] objDataArray = FileUtil.readExcel(media, 1, 1);
			// 验证导入文件是否有数据
			if (objDataArray == null || objDataArray.length == 0) {
				ZkUtil.showError("导入文件没有数据！", "错误信息");
				return;
			}

			// 吧从execl获取的数据提取全部出来，放到一个vo中，在逻辑中验证数据的正确性
			this.waitUpLoadStaffOrganizationInfoList.clear();
			// 统计错误数量
			int errorDataCount = 0;
			// 验证信息列表定义
			List<String> checkInfoList = new ArrayList<String>();
			// 进行数据校验,得到出错的个数
			errorDataCount = staffOrganizationManager.checkUpLoadData(
					waitUpLoadStaffOrganizationInfoList, checkInfoList,
					objDataArray, totalColumn[0]);

			// 后续全部数据进行联合校验，就是第几行数据有问题
			int length = waitUpLoadStaffOrganizationInfoList.size();
			StringBuffer sb = new StringBuffer();
			// 对vo中的数据进行分类，新增，修改，删除三块
			// 首先执行删除，然后修改，最后新增，
			List<StaffOrganization> addStaffOrganizationList = new ArrayList<StaffOrganization>();
			List<StaffOrganization> editStaffOrganizationList = new ArrayList<StaffOrganization>();
			List<StaffOrganization> editStaffOrganizationRelationList = new ArrayList<StaffOrganization>();
			List<StaffOrganization> delStaffOrganizationList = new ArrayList<StaffOrganization>();
			List<StaffOrganization> delStaffList = new ArrayList<StaffOrganization>();
			if (errorDataCount == 0) {
				for (int i = 0; i < length; ++i) {
					// 获取第i行数据
					StaffOrganizationImportVo staffOrganizationImportVoTemp = waitUpLoadStaffOrganizationInfoList
							.get(i);
					// 获取员工组织关系的员工和组织对象,一个账号只能是一个员工
					Staff staffTemp = new Staff();
					staffTemp.setStaffAccount(staffOrganizationImportVoTemp
							.getStaffAccount());
					List<Staff> staffs = staffManager.queryStaffList(staffTemp);
					if (staffs.size() != 1) {
						// 一个账号多个员工
						++errorDataCount;
						// -1表示一整行
						checkInfoList
								.add(getValidateMsg(
										i,
										-1,
										StaffOrganizationConstant.STAFF_EXIST_MANY_ACCOUNT,
										sb));
					}
					staffOrganizationImportVoTemp.setStaff(staffs.get(0));

					// 组织
					// 如果存在，判断员工账号是否存在,Organization表
					if (staffOrganizationImportVoTemp.getOrg() == null) {
						Organization orgTemp = new Organization();
						orgTemp.setOrgCode(staffOrganizationImportVoTemp
								.getOrgCode());
						// 根据组织编码查询是否存在这个组织
						orgTemp = organizationManager
								.queryOrganizationByOrgCode(orgTemp);
						staffOrganizationImportVoTemp.setOrg(orgTemp);
					}
					// 吧vo中的数据保存到StaffOrganziation中
					StaffOrganization staffOrganization = new StaffOrganization();
					// 吧vo复制到staffOrganization
					staffOrganization.setStaffId(staffOrganizationImportVoTemp
							.getStaff().getStaffId());
					staffOrganization.setOrgId(staffOrganizationImportVoTemp
							.getOrg().getOrgId());
					staffOrganization.setRalaCd(staffOrganizationImportVoTemp
							.getRalaCd());
					staffOrganization.setStaffSeq(staffOrganizationImportVoTemp
							.getStaffSeq());
					staffOrganization.setReason(staffOrganizationImportVoTemp
							.getReason());

					// 判断是新增，修改，还是删除，其实这里可以起三个线程分别进行添加，修改，删除
					if (staffOrganizationImportVoTemp.getOperation().equals(
							"add")) {
						// 1、一个员工只能有有一个员工组织关系 已经存在错误FIELD_EXIST_VAL
						/**
						 * 20140331一个员工只能和组织有一种关系
						 */
						StaffOrganization vo = new StaffOrganization();
						// 根据员工账号获取员工信息
						vo.setStaffId(staffOrganizationImportVoTemp.getStaff()
								.getStaffId());
						vo.setOrgId(staffOrganizationImportVoTemp.getOrg()
								.getOrgId());
						List<StaffOrganization> existlist = staffOrganizationManager
								.queryStaffOrganizationList(vo);
						if (existlist != null && existlist.size() > 0) {
							// 多条记录关系，重复
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											StaffOrganizationConstant.FIELD_REPEAT,
											sb));
						}
						// 2、添加员工组织关系规则校验
						String msgStr = staffOrganizationManager
								.doStaffOrgRelRule(
										staffOrganizationImportVoTemp
												.getStaff(), null,
										staffOrganizationImportVoTemp.getOrg());
						if (!StrUtil.isNullOrEmpty(msgStr)) {
							// 多条记录关系，重复
							++errorDataCount;
							sb.append(msgStr);
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											StaffOrganizationConstant.ORG_REL_RULE_ERROR,
											sb));
						}

						// 3、如果新增归属关系，则将已有的组织关系设置为兼职
						/**
						 * 如果新增的是归属关系，则将已有的归属关系改为兼职;
						 * 
						 */
						if (BaseUnitConstants.RALA_CD_1
								.equals(staffOrganizationImportVoTemp
										.getRalaCd())) {
							StaffOrganization queryStaffOrganization = new StaffOrganization();
							queryStaffOrganization
									.setStaffId(staffOrganizationImportVoTemp
											.getStaff().getStaffId());
							queryStaffOrganization
									.setRalaCd(BaseUnitConstants.RALA_CD_1);
							/**
							 * 已存在的归属关系列表：更改为兼职（先删除归属然后新增兼职）
							 */
							List<StaffOrganization> list = staffOrganizationManager
									.queryStaffOrganizationList(queryStaffOrganization);
							staffOrganization
									.setNeedUpdateStaffOrganizationlist(list);
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
								staffOrganization
										.setRalaCd(BaseUnitConstants.RALA_CD_1);
							}
						}

						// 4、吧关系添加到新增队列
						staffOrganization.setUserCode(staffOrganizationManager
								.getOrgUserCode());
						// 添加到队列
						addStaffOrganizationList.add(staffOrganization);
					} else if (staffOrganizationImportVoTemp.getOperation()
							.equals("edit")) {
						// 1、数据库中是否存在这样一条组织关系
						StaffOrganization queryStaffOrganization = new StaffOrganization();
						// 根据员工id和组织id来查询旧关系
						queryStaffOrganization
								.setStaffId(staffOrganizationImportVoTemp
										.getStaff().getId());
						queryStaffOrganization
								.setOrgId(staffOrganizationImportVoTemp
										.getOrg().getOrgId());
						// 查出旧关系
						List<StaffOrganization> staffOrganizationList = staffOrganizationManager
								.queryStaffOrganizationList(queryStaffOrganization);
						// 一个员工和一个组织关系应该只有一个
						if (staffOrganizationList.size() != 1) {
							// 无法确定对象或单个对象与单个组织存在多个关系
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											StaffOrganizationConstant.ORG_REL_MANY_RELATION,
											sb));
							// 当前信息不可改，直接跳出
							continue;
						}
						// 获取原来的员工关系
						StaffOrganization oldStaffOrganization = staffOrganizationList
								.get(0);
						staffOrganization = new StaffOrganization();
						// 查出旧对象之后，深拷贝给当前对象之后，修改相应新的属性值
						BeanUtils.copyProperties(staffOrganization,
								oldStaffOrganization);
						// 吧vo复制到staffOrganization
						staffOrganization
								.setStaffId(staffOrganizationImportVoTemp
										.getStaff().getStaffId());
						staffOrganization
								.setOrgId(staffOrganizationImportVoTemp
										.getOrg().getOrgId());
						staffOrganization
								.setRalaCd(staffOrganizationImportVoTemp
										.getRalaCd());
						staffOrganization
								.setStaffSeq(staffOrganizationImportVoTemp
										.getStaffSeq());
						staffOrganization
								.setReason(staffOrganizationImportVoTemp
										.getReason());

						// 2、OA账号是否存在
						String userCode = staffOrganization.getUserCode();
						if (StrUtil.isNullOrEmpty(userCode)) {
							staffOrganization
									.setUserCode(staffOrganizationManager
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
							 * 其实就是只改了序号和备注
							 */
							oldStaffOrganization.setStaffSeq(staffOrganization
									.getStaffSeq());
							oldStaffOrganization.setNote(staffOrganization
									.getNote());
							if (StrUtil.isEmpty(oldStaffOrganization
									.getUserCode())) {
								oldStaffOrganization
										.setUserCode(staffOrganizationManager
												.getOrgUserCode());
							}
							// 添加到修改队列
							editStaffOrganizationList.add(staffOrganization);
						} else {
							/**
							 * 20130911修改：如果非归属关系改为非归属关系，则修改；如果是非归属关系改为归属关系，
							 * 则将原来的归属关系改为兼职； 如果将归属关系修改为非归属关系，则判断弹出一个对话框
							 * 
							 */
							StaffOrganization queryStaffOrganizationTemp = new StaffOrganization();
							queryStaffOrganizationTemp
									.setStaffId(staffOrganization.getStaffId());
							List<StaffOrganization> listStaffOrganization = staffOrganizationManager
									.queryStaffOrganizationList(queryStaffOrganizationTemp);
							if (null != listStaffOrganization
									&& listStaffOrganization.size() == 1) {
								// 如果没有其他关系则强制修改为归属关系
								if (!BaseUnitConstants.RALA_CD_1
										.equals(oldStaffOrganization
												.getRalaCd())) {
									staffOrganization
											.setRalaCd(BaseUnitConstants.RALA_CD_1);
									List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
									needRemoveList.add(oldStaffOrganization);
									staffOrganization
											.setNeedRemoveList(needRemoveList);
									// 添加到修改队列
									// this.staffOrganizationManager
									// .updateStaffOrganizationRelation(staffOrganization);
									editStaffOrganizationRelationList
											.add(staffOrganization);
								}
							} else if (null != listStaffOrganization
									&& listStaffOrganization.size() > 1) {

								// 如果是非归属关系改为归属关系，则将原来的归属关系改为兼职；
								if (BaseUnitConstants.RALA_CD_1
										.equals(staffOrganization.getRalaCd())
										&& !BaseUnitConstants.RALA_CD_1
												.equals(oldStaffOrganization
														.getRalaCd())) {
									StaffOrganization querySo = new StaffOrganization();
									querySo.setStaffId(staffOrganization
											.getStaffId());
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
									staffOrganization
											.setNeedRemoveList(needRemoveList);

									// this.staffOrganizationManager
									// .updateStaffOrganizationRelation(staffOrganization);
									// 添加到修改队列
									editStaffOrganizationRelationList
											.add(staffOrganization);

								} else if (!BaseUnitConstants.RALA_CD_1
										.equals(oldStaffOrganization
												.getRalaCd())
										&& !BaseUnitConstants.RALA_CD_1
												.equals(staffOrganization
														.getRalaCd())) {
									// 非归属关系修改为非归属关系
									List<StaffOrganization> needRemoveList = new ArrayList<StaffOrganization>();
									needRemoveList.add(oldStaffOrganization);
									staffOrganization
											.setNeedRemoveList(needRemoveList);
									// 添加到修改队列
									editStaffOrganizationRelationList
											.add(staffOrganization);
								} else {
									// 如果将归属关系修改为非归属关系，则判断弹出一个对话框
									// 提示错误，吧归属改为非归属，只能单个修改
									++errorDataCount;
									checkInfoList
											.add(getValidateMsg(
													i,
													-1,
													StaffOrganizationConstant.ORG_REL_RALA_CD_1_CHANGE,
													sb));
								}
							}
						}
					} else if (staffOrganizationImportVoTemp.getOperation()
							.equals("del")) {
						// 删除一条关系，staffOrganization
						StaffOrganization queryStaffOrganization = new StaffOrganization();
						// 设置员工id号
						queryStaffOrganization.setStaffId(staffOrganization
								.getStaffId());
						queryStaffOrganization
								.setOrgId(staffOrganizationImportVoTemp
										.getOrg().getOrgId());
						queryStaffOrganization
								.setReason(staffOrganizationImportVoTemp
										.getReason());
						List<StaffOrganization> listStaffOrganization = staffOrganizationManager
								.queryStaffOrganizationList(queryStaffOrganization);

						// 这个组织要删除的话，必须存在对应这个组织的关系,就是这个组织编码在员工组织关系表中存在这个关系
						boolean orgCodeExit = false;
						for (StaffOrganization staffOrgCodeExit : listStaffOrganization) {
							if (staffOrgCodeExit.getOrgId().longValue() == staffOrganization
									.getOrgId().longValue()) {
								orgCodeExit = true;
								staffOrganization = staffOrgCodeExit;
								break;
							}
						}
						// 如果不存在这个org'的话，无法删除提示
						if (!orgCodeExit) {
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											StaffOrganizationConstant.ORG_REL_EXIST,
											sb));
							continue;
						}

						if (listStaffOrganization != null
								&& listStaffOrganization.size() == 1) {
							// 如果只有一条直接删除,添加进去删除队列
							// staffManager.delStaff(queryStaffOrganization.getStaff());
							// 删除是直接把员工删除掉
							// 判断是网点还是其他，如果是网点就直接删人，如果不是网点就只删关系
							// 获取对应组织的类型
							Organization targetOrganization = queryStaffOrganization
									.getOrganization();
							List<OrgType> orgTypeList = targetOrganization
									.getOrgTypeList();
							boolean isIntNetwork = false;// 内部网点
							boolean isExtNetwork = false;// 外部网点

							// 判断该组织是内部网点或外部网点
							if (orgTypeList != null && orgTypeList.size() > 0) {
								for (OrgType orgType : orgTypeList) {

									if (orgType
											.getOrgTypeCd()
											.equals(OrganizationConstant.ORG_TYPE_N0202010000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202030000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202040000)) {
										isIntNetwork = true;// 该组织是内部网点【目前不做区分内外部网点】
									}

									if (orgType
											.getOrgTypeCd()
											.equals(OrganizationConstant.ORG_TYPE_N0202020000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202050000)
											|| orgType
													.getOrgTypeCd()
													.equals(OrganizationConstant.ORG_TYPE_N0202060000)) {
										isExtNetwork = true;// 该组织是外部网点【目前不做区分内外部网点】
									}

								}
							}

							if (isIntNetwork) {
								// 内部网点,全删
								delStaffList.add(queryStaffOrganization);
							} else if (isExtNetwork) {
								// 内部网点,全删
								delStaffList.add(queryStaffOrganization);
							} else {
								// 其他的只删除关系
								delStaffOrganizationList.add(staffOrganization);
							}
						} else if (null != listStaffOrganization
								&& listStaffOrganization.size() == 2) {
							// 如果删除后只剩下一条其他关系，则将其设置为归属关系（先删除旧的兼职关系新增一条新的归属关系）
							StaffOrganization needUpdateStaffOrganization = null;
							for (StaffOrganization temp : listStaffOrganization) {
								if (temp.getStaffOrgId() != null
										&& staffOrganization.getStaffOrgId() != null
										&& !temp.getStaffOrgId().equals(
												staffOrganization
														.getStaffOrgId())) {
									needUpdateStaffOrganization = temp;
								}
							}
							if (needUpdateStaffOrganization != null) {
								staffOrganization
										.setRemoveNeedUpdateStaffOrganization(needUpdateStaffOrganization);
							}
							// staffOrganizationManager
							// .removeStaffOrganization(delSo);
							delStaffOrganizationList.add(staffOrganization);
						} else if (null != listStaffOrganization
								&& listStaffOrganization.size() > 2) {
							// 如果有多条关系，删除一条
							boolean isExistRala_CD_1 = false;
							for (StaffOrganization so : listStaffOrganization) {
								// 除本身外还存在直属关系
								if (BaseUnitConstants.RALA_CD_1.equals(so
										.getRalaCd())
										&& !so.getStaffOrgId().equals(
												staffOrganization
														.getStaffOrgId())) {
									isExistRala_CD_1 = true;
									break;
								}
							}
							// 如果剩下不止一条关系且不存在归属关系，则弹出对话框让用户设置一条记录为归属关系
							if (!isExistRala_CD_1) {
								// 除了本身还有直属关系
								++errorDataCount;
								checkInfoList
										.add(getValidateMsg(
												i,
												-1,
												StaffOrganizationConstant.ORG_REL_OTHER_REALTION,
												sb));
							} else {
								// staffOrganizationManager
								// .removeStaffOrganization(delSo);
								delStaffOrganizationList.add(staffOrganization);

							}
						}
					}
				}
			}

			if (errorDataCount > 0) {
				checkInfoList.add("导入文件错误条数共：" + errorDataCount
						+ "条，请修改以上错误后再导入。");
			} else {

				// 开始新增，修改与删除
				// 首先执行删除，然后修改，最后新增，
				// List<StaffOrganization> addStaffOrganizationList = new
				// ArrayList<StaffOrganization>();
				// List<StaffOrganization> editStaffOrganizationList = new
				// ArrayList<StaffOrganization>();
				// List<StaffOrganization> delStaffOrganizationList = new
				// ArrayList<StaffOrganization>();
				// List<StaffOrganization> delStaffList = new
				// ArrayList<StaffOrganization>();

				staffOrganizationManager.saveOrEditOrDelStaffOrganiaztion(
						addStaffOrganizationList, editStaffOrganizationList,
						editStaffOrganizationRelationList,
						delStaffOrganizationList, delStaffList);
				/**
				 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
				 */
				Class clazz[] = { StaffOrganization.class };
				log.endLog(logService, clazz, SysLogConstrants.IMPORT,
						SysLogConstrants.INFO, "员工组织关系导入记录日志");
			}

			if (checkInfoList != null && checkInfoList.size() > 0) {// 写出导入错误信息
				Map arg = new HashMap();
				arg.put("opType", "view");
				arg.put("infoList", checkInfoList);
				Window win = (Window) Executions.createComponents(
						"/pages/staff/staff_import.zul", null, arg);
				win.doModal();
			} else {
				ZkUtil.showInformation("导入成功！", "确定");
			}

			onCancel();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 取消
	 */
	public void onCancel() throws Exception {
		if (media != null) {
			media = null;
		}
		// 关闭操作界面
		bean.getStaffOrganizationImportWindow().onClose();
	}

	/**
	 * 下载划小单模板
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void onDownLoadTemplate5() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "template5.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("员工组织关系导入模板.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("员工组织关系导入模板.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload
					.save(new FileInputStream(httpRequest
							.getRealPath("/pages/organization/doc/" + fileName)),
							"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载员工组织关系导入模板失败。", "系统提示");
		}

	}
}
