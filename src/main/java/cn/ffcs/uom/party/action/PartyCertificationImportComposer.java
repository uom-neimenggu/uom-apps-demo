package cn.ffcs.uom.party.action;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.CertUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.party.action.bean.PartyCertificationImportBean;
import cn.ffcs.uom.party.constants.PartyCertificationConstant;
import cn.ffcs.uom.party.manager.PartyManager;
import cn.ffcs.uom.party.model.Party;
import cn.ffcs.uom.party.model.PartyCertification;
import cn.ffcs.uom.party.vo.PartyCertificationImportVo;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年4月26日
 * @功能说明：参与人证件批量导入页面
 *
 */
@Controller
@Scope("prototype")
public class PartyCertificationImportComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private PartyCertificationImportBean bean = new PartyCertificationImportBean();

	/**
	 * 上传的文件
	 */
	private Media media = null;

	private static final int[] totalColumn = { 7 };

	/**
	 * 等待操作的批量数据
	 */
	private List<PartyCertificationImportVo> waitUpLoadPartyCertificationInfoList = new ArrayList<PartyCertificationImportVo>();

	/**
	 * manager
	 */
	private PartyManager partyManager = (PartyManager) ApplicationContextUtil
			.getBean("partyManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
	}

	// 创建页面
	public void onCreate$partyCertificationImportWindow() {
		// 初始化页面bean
		bindBean();
	}

	private void bindBean() {
		// 设定页面标题
		bean.getPartyCertificationImportWindow().setTitle("参与人证件批量导入");
	}

	/**
	 * 文件上传 .
	 * 
	 * @author zhanglu
	 * 
	 */
	public void onUpload$fileupload(ForwardEvent event) {
		// 获取上传的文件内容
		media = ((UploadEvent) event.getOrigin()).getMedia();
	}

	private String getValidateMsg(int rowNumber, int colNumber,
			String validateType, StringBuffer sb) {
		String partyCertificationUsedMax = UomClassProvider
				.getSystemConfig("partyCertificationUsedMax");
		String fieldErrorCertAlreadyUseStr = PartyCertificationConstant.FIELD_ERROR_CERT_ALREADY_USE_STR
				.replace("%", partyCertificationUsedMax);
		sb.setLength(0);
		if (PartyCertificationConstant.NULL_OR_EMPTY.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(PartyCertificationConstant.NULL_OR_EMPTY_STR)
					.append("的信息； ").toString();
		} else if (PartyCertificationConstant.LENGTH_LIMIT.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(PartyCertificationConstant.LENGTH_LIMIT_STR)
					.append("的信息； ").toString();
		} else if (PartyCertificationConstant.FIELD_ERROR.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(PartyCertificationConstant.FIELD_ERROR_STR)
					.append("的信息； ").toString();
		} else if (PartyCertificationConstant.FIELD_NOT_EXIST
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(PartyCertificationConstant.FIELD_NOT_EXIST_STR)
					.append("； ").toString();
		} else if (PartyCertificationConstant.FIELD_ERROR_VAL
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(PartyCertificationConstant.FIELD_ERROR_VAL_STR)
					.append("； ").toString();
		} else if (PartyCertificationConstant.DEFAULT_CERTIFICATION_NOTEXIST
				.equals(validateType)) {
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(PartyCertificationConstant.DEFAULT_CERTIFICATION_NOTEXIST_STR)
					.append("； ").toString();
		} else if (PartyCertificationConstant.CERTIFICATION_NOTEXIST
				.equals(validateType)) {
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(PartyCertificationConstant.CERTIFICATION_NOTEXIST_STR)
					.append("； ").toString();
		} else if (PartyCertificationConstant.ONE_DEFAULT_CERTIFICATION
				.equals(validateType)) {
			return sb
					.append("文件第")
					.append((rowNumber + 2))
					.append("行，第")
					.append((colNumber + 1))
					.append("列，出现错误：导入")
					.append(PartyCertificationConstant.ONE_DEFAULT_CERTIFICATION_STR)
					.append("； ").toString();
		} else if (PartyCertificationConstant.FIELD_ERROR_CERT_ALREADY_USE
				.equals(validateType)) {
			return sb.append("文件第").append((rowNumber + 2)).append("行，第")
					.append((colNumber + 1)).append("列，出现错误：导入")
					.append(fieldErrorCertAlreadyUseStr).append("； ")
					.toString();
		}

		return "";
	}

	public int checkUpLoadData(
			List<PartyCertificationImportVo> waitUpLoadPartyCertificationInfoList,
			List<String> checkInfoList, String[][] objDataArray, int totalColumn)
			throws Exception {

		// 循环遍历所有数据，校验所有的数据
		int errorDataCount = 0;
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < objDataArray.length; ++i) {
			// 一个临时变量
			PartyCertificationImportVo partyCertificationImportVo = new PartyCertificationImportVo();

			for (int j = 0; j < totalColumn; ++j) {
				// 当前的数据
				String s = "";
				// 获取当前的一个单元格的数据
				if (objDataArray[i][j] != null) {
					// 去掉空格
					s = objDataArray[i][j].trim();
				}
				String strs[] = null;
				// 提取数据所有的数据用-划分
				if (!StrUtil.isEmpty(s)) {
					strs = s.split("-");
				}
				// switch校验所有的数据
				switch (j) {
				case 0: // 操作类型，非空，且指定操作方式
					if (!StrUtil.isNullOrEmpty(s)) {
						if (strs.length == 1) {
							// 未知操作类型,FIELD_ERROR_STR = "格式不正确"
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											j,
											PartyCertificationConstant.FIELD_ERROR,
											sb));
							break;
						}

						// 给出的s必须是规定的add,edit,del这三个,否则就是未知操作类型
						if (strs[1].equals("add") || strs[1].equals("edit")
								|| strs[1].equals("del")) {
							// 获取操作类型
							partyCertificationImportVo.setOperation(strs[1]);
						} else {
							// 未知操作类型,FIELD_ERROR_STR = "格式不正确"
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											j,
											PartyCertificationConstant.FIELD_ERROR,
											sb));
						}
					} else {
						// 为空
						++errorDataCount;
						checkInfoList.add(getValidateMsg(i, j,
								PartyCertificationConstant.NULL_OR_EMPTY, sb));
					}
					break;
				case 1: // 变更原因，非空
					if (!StrUtil.isNullOrEmpty(s)) {
						partyCertificationImportVo.setReason(s);
					} else {
						// 为空
						++errorDataCount;
						checkInfoList.add(getValidateMsg(i, j,
								PartyCertificationConstant.NULL_OR_EMPTY, sb));
					}
					break;
				case 2: // 员工账号，非空且存在参与人
					if (!StrUtil.isNullOrEmpty(s)) {
						// 根据账号查询是否存在参与人
						Party party = partyManager.getPartyByStaffAccount(s);

						if (party == null) {
							// 参与人不存在
							++errorDataCount;
							checkInfoList.add(getValidateMsg(i, j,
									PartyCertificationConstant.FIELD_NOT_EXIST,
									sb));
						} else {
							// 参与人存在
							partyCertificationImportVo.setStaffAccount(s);
						}
					} else {
						// 为空
						++errorDataCount;
						checkInfoList.add(getValidateMsg(i, j,
								PartyCertificationConstant.NULL_OR_EMPTY, sb));
					}
					break;
				case 3: // 证件名，非空
					if (!StrUtil.isNullOrEmpty(s)) {
						partyCertificationImportVo.setCertName(s);
					} else {
						// 为空
						++errorDataCount;
						checkInfoList.add(getValidateMsg(i, j,
								PartyCertificationConstant.NULL_OR_EMPTY, sb));
					}
					break;
				case 4: // 证件号码，非空
					if (!StrUtil.isNullOrEmpty(s)) {
						partyCertificationImportVo.setCertNumber(s);
					} else {
						// 为空
						++errorDataCount;
						checkInfoList.add(getValidateMsg(i, j,
								PartyCertificationConstant.NULL_OR_EMPTY, sb));
					}
					break;
				case 5: // 发证机关，可空，
					if (!StrUtil.isNullOrEmpty(s)) {
						partyCertificationImportVo.setCertOrg(s);
					}
					break;
				case 6: // 证件地址，可空
					if (!StrUtil.isNullOrEmpty(s)) {
						partyCertificationImportVo.setCertAddress(s);
					}
					break;
				default:
					break;
				}
			}

			// 一行值校验结束
			waitUpLoadPartyCertificationInfoList
					.add(partyCertificationImportVo);
		}
		// 所有数据校验结束
		return errorDataCount;
	}

	/**
	 * 提交文件 .
	 * 
	 * @author zhanglu 2017年4月27日
	 */
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
			// 读取数据
			String[][] objDataArray = FileUtil.readExcel(media, 1, 1);
			// 验证导入文件是否有数据
			if (objDataArray == null || objDataArray.length == 0) {
				ZkUtil.showError("导入文件没有数据！", "错误信息");
				return;
			}

			// 吧从execl获取的数据提取全部出来，放到一个vo中，在逻辑中验证数据的正确性
			this.waitUpLoadPartyCertificationInfoList.clear();
			// 统计错误数量
			int errorDataCount = 0;
			// 验证信息列表定义
			List<String> checkInfoList = new ArrayList<String>();

			// 进行数据校验,得到出错的个数
			errorDataCount = checkUpLoadData(
					waitUpLoadPartyCertificationInfoList, checkInfoList,
					objDataArray, totalColumn[0]);

			// 后续全部数据进行联合校验，就是第几行数据有问题
			int length = waitUpLoadPartyCertificationInfoList.size();
			StringBuffer sb = new StringBuffer();
			// 对vo中的数据进行分类，新增，修改，删除三块
			// 首先执行删除，然后修改，最后新增，
			List<PartyCertification> addPartyCertificationList = new ArrayList<PartyCertification>();
			List<PartyCertification> editPartyCertificationList = new ArrayList<PartyCertification>();
			List<PartyCertification> delPartyCertificationList = new ArrayList<PartyCertification>();

			if (errorDataCount == 0) {
				for (int i = 0; i < length; ++i) {
					// 获取第i行数据
					PartyCertificationImportVo partyCertificationImportVoTemp = waitUpLoadPartyCertificationInfoList
							.get(i);
					// 根据账号查询是否存在参与人
					Party party = partyManager
							.getPartyByStaffAccount(partyCertificationImportVoTemp
									.getStaffAccount());

					// 判断是新增，修改，还是删除
					if (partyCertificationImportVoTemp.getOperation().equals(
							"add")) {
						boolean isRealName = CertUtil.checkIdCard(
								partyCertificationImportVoTemp.getCertNumber(),
								partyCertificationImportVoTemp.getCertName());

						if (!isRealName) {
							// 未通过实名校验
							++errorDataCount;
							checkInfoList.add(getValidateMsg(i, -1,
									PartyCertificationConstant.FIELD_ERROR_VAL,
									sb));
							continue;
						}

						boolean certIsNotExist = partyManager
								.checkIsExistCertificate(partyCertificationImportVoTemp
										.getCertNumber());
						if (!certIsNotExist) { // 证件已达到使用上限
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											PartyCertificationConstant.FIELD_ERROR_CERT_ALREADY_USE,
											sb));
							continue;
						}

						PartyCertification partyCertification = new PartyCertification();
						partyCertification
								.setReason(partyCertificationImportVoTemp
										.getReason());
						partyCertification
								.setCertAddress(partyCertificationImportVoTemp
										.getCertAddress());
						partyCertification
								.setCertName(partyCertificationImportVoTemp
										.getCertName());
						partyCertification
								.setCertNumber(partyCertificationImportVoTemp
										.getCertNumber());
						partyCertification
								.setCertOrg(partyCertificationImportVoTemp
										.getCertOrg());
						partyCertification.setPartyId(party.getPartyId());
						partyCertification.setIsRealName("1");
						partyCertification.setCertSort("2");
						partyCertification.setCertType("1");
						partyCertification.setIdentityCardId(1L);
						partyCertification.setIsNeedUpdate(false);

						addPartyCertificationList.add(partyCertification);
					} else if (partyCertificationImportVoTemp.getOperation()
							.equals("edit")) {
						boolean isRealName = CertUtil.checkIdCard(
								partyCertificationImportVoTemp.getCertNumber(),
								partyCertificationImportVoTemp.getCertName());

						if (!isRealName) {
							// 未通过实名校验
							++errorDataCount;
							checkInfoList.add(getValidateMsg(i, -1,
									PartyCertificationConstant.FIELD_ERROR_VAL,
									sb));
							continue;
						}

						// 1、数据库中是否存在这样一条参与人证件
						PartyCertification queryPartyCertification = new PartyCertification();
						queryPartyCertification.setPartyId(party.getPartyId());
						queryPartyCertification.setCertType("1");
						queryPartyCertification.setCertSort("1");

						List<PartyCertification> partyCertificationList = partyManager
								.getPartyCertificationList(queryPartyCertification);
						if (partyCertificationList == null
								|| partyCertificationList.isEmpty()) {
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											PartyCertificationConstant.DEFAULT_CERTIFICATION_NOTEXIST,
											sb));
							continue;
						}

						boolean certIsNotExist = partyManager
								.checkIsExistCertificate(partyCertificationImportVoTemp
										.getCertNumber());
						if (!certIsNotExist) { // 证件已达到使用上限
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											PartyCertificationConstant.FIELD_ERROR_CERT_ALREADY_USE,
											sb));
							continue;
						}

						PartyCertification partyCertification = partyCertificationList
								.get(0);
						partyCertification
								.setReason(partyCertificationImportVoTemp
										.getReason());
						partyCertification
								.setCertAddress(partyCertificationImportVoTemp
										.getCertAddress());
						partyCertification
								.setCertName(partyCertificationImportVoTemp
										.getCertName());
						partyCertification
								.setCertNumber(partyCertificationImportVoTemp
										.getCertNumber());
						partyCertification
								.setCertOrg(partyCertificationImportVoTemp
										.getCertOrg());
						partyCertification.setIsRealName("1");

						editPartyCertificationList.add(partyCertification);
					} else if (partyCertificationImportVoTemp.getOperation()
							.equals("del")) {
						// 数据库中是否存在这样一条参与人证件
						PartyCertification queryPartyCertification = new PartyCertification();
						queryPartyCertification.setPartyId(party.getPartyId());
						queryPartyCertification.setCertType("1");
						queryPartyCertification
								.setCertNumber(partyCertificationImportVoTemp
										.getCertNumber());

						List<PartyCertification> partyCertificationList = partyManager
								.getPartyCertificationList(queryPartyCertification);
						if (partyCertificationList == null
								|| partyCertificationList.isEmpty()) {
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											PartyCertificationConstant.CERTIFICATION_NOTEXIST,
											sb));
							continue;
						}

						// 数据库中该参与人下是否只有一条证件
						PartyCertification queryPartyCert = new PartyCertification();
						queryPartyCert.setPartyId(party.getPartyId());
						queryPartyCert.setCertType("1");

						List<PartyCertification> partyCertList = partyManager
								.getPartyCertificationList(queryPartyCert);
						if (partyCertList.size() == 1) {
							++errorDataCount;
							checkInfoList
									.add(getValidateMsg(
											i,
											-1,
											PartyCertificationConstant.ONE_DEFAULT_CERTIFICATION,
											sb));
							continue;
						}
						
						PartyCertification partyCertification = partyCertificationList.get(0);
						partyCertification.setReason(partyCertificationImportVoTemp.getReason());
						delPartyCertificationList.add(partyCertification);
					}
				}
			}

			if (errorDataCount > 0) {
				checkInfoList.add("导入文件错误条数共：" + errorDataCount
						+ "条，请修改以上错误后再导入。");
			} else {
				partyManager.saveOrEditOrDelPartyCertification(
						addPartyCertificationList, editPartyCertificationList,
						delPartyCertificationList);
			}

			if (checkInfoList != null && checkInfoList.size() > 0) {// 写出导入错误信息
				Map<String, Object> arg = new HashMap<String, Object>();
				arg.put("opType", "view");
				arg.put("infoList", checkInfoList);
				Window win = (Window) Executions.createComponents(
						"/pages/staff/staff_import.zul", null, arg);
				win.doModal();
			} else {
				ZkUtil.showInformation("导入成功！", "确定");
				Events.postEvent(SffOrPtyCtants.ON_OK,
						bean.getPartyCertificationImportWindow(), null);
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
		bean.getPartyCertificationImportWindow().onClose();
	}

	/**
	 * 下载划小单模板
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public void onDownloadPartyCertificationTemplate() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "party_certification_template.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("参与人证件导入模板.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("参与人证件导入模板.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload.save(
					new FileInputStream(httpRequest
							.getRealPath("/pages/party/doc/" + fileName)),
					"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载参与人证件导入模板失败。", "系统提示");
		}

	}
}
