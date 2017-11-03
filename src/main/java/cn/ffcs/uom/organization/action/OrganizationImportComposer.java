package cn.ffcs.uom.organization.action;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zkoss.util.media.Media;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.uom.common.constants.SysLogConstrants;
import cn.ffcs.uom.common.key.WebKeys;
import cn.ffcs.uom.common.model.SysLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.service.LogService;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.GetipUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.organization.action.bean.OrganizationImportBean;
import cn.ffcs.uom.organization.manager.OrganizationManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.vo.OrganizationHXImportVo;
import cn.ffcs.uom.staff.model.ImportInfo;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class OrganizationImportComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private OrganizationImportBean bean = new OrganizationImportBean();

	List<ImportInfo> infoList = new ArrayList<ImportInfo>();

	/**
	 * 上传文件
	 */
	private Media media = null;

	/**
	 * 存放等待上传的代理商组织数据列表
	 */
	private List<Organization> waitUpLoadOrganizationList = new ArrayList<Organization>();
	/**
	 * 存放等待上传的营业网点组织数据列表
	 */
	private List<Organization> waitUpLoadBusinessOutletsOrgList = new ArrayList<Organization>();

	/**
	 * 存放等待上传的划小单元组织数据列表
	 */
	private List<OrganizationHXImportVo> waitUpLoadHXOrganizationInfoList = new ArrayList<OrganizationHXImportVo>();

	/**
	 * 存放上级组织的等级
	 */
	private List<String> waitLevelList = new ArrayList<String>();

	/**
	 * 文件列数
	 */
	private static final int[] totalColumn = { 91, 67, 25, 13 };

	/**
	 * Manager.
	 */
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

	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationImportWindow() throws Exception {
		bindBean();
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void bindBean() throws Exception {
		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"Organization", "importType");
		ListboxUtils.rendererForEdit(this.bean.getImportType(), orgTypeList);

		bean.getOrganizationImportWindow().setTitle("组织导入");
	}

	/**
	 * 开始导入
	 */
	public void onOk(ForwardEvent event) throws Exception {
		if (StrUtil.isNullOrEmpty(bean.getImportType().getSelectedItem())) {
			ZkUtil.showError("请选择导入类型!", "系统提示");
			return;
		}
		if (StrUtil.isNullOrEmpty(bean.getImportType().getSelectedItem()
				.getValue())) {
			ZkUtil.showError("请选择导入类型!", "系统提示");
			return;
		}
		if (null == media) {
			ZkUtil.showError("请选择要上传的文件!", "系统提示");
			return;
		}
		/**
		 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
		 */
		SysLog log = new SysLog();
		log.startLog(new Date(), SysLogConstrants.ORG);

		String fileName = media.getName();
		if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
			// 读取excel数据
			String[][] objDataArray = FileUtil.readExcel(media, 1, 1);
			// 验证导入文件是否有数据
			if (objDataArray == null || objDataArray.length == 0) {
				ZkUtil.showError("导入文件没有数据！", "错误信息");
				return;
			}
			// 清空等待上传的代理商组织与营业网点组织数据列表
			this.waitUpLoadOrganizationList.clear();
			this.waitUpLoadBusinessOutletsOrgList.clear();
			this.waitUpLoadHXOrganizationInfoList.clear();
			this.waitLevelList.clear(); // 添加的单元在组织中的层级

			// 验证信息列表定义
			List<String> checkInfoList = new ArrayList<String>();
			// 导入信息集合定义
			List<List<Map<String, Object>>> resultList = null;

			// 统计错误数据条数并写入等待上传的组织数据列表
			// 查找以前的三个类型
			int checkType = Integer.valueOf(bean.getImportType()
					.getSelectedItem().getValue().toString());

			int errorDataCount = 0;
			// 判断是营销2016,还是2017
			String markingType = (String) arg.get("marketing");
			markingType = "";
			if (checkType == 4) {
				errorDataCount = organizationManager.checkUpLoadHXData(
						waitLevelList,
						waitUpLoadHXOrganizationInfoList,
						checkInfoList,
						objDataArray,
						totalColumn[Integer.valueOf(bean.getImportType()
								.getSelectedItem().getValue().toString()) - 1],
						getCaseIndexArrayByType(Integer.valueOf(bean
								.getImportType().getSelectedItem().getValue()
								.toString())), markingType);
			} else {
				errorDataCount = organizationManager.checkUpLoadFileData(
						waitUpLoadOrganizationList,
						waitUpLoadBusinessOutletsOrgList,
						checkInfoList,
						objDataArray,
						totalColumn[Integer.valueOf(bean.getImportType()
								.getSelectedItem().getValue().toString()) - 1],
						getCaseIndexArrayByType(Integer.valueOf(bean
								.getImportType().getSelectedItem().getValue()
								.toString())));
			}

			if (errorDataCount > 0) {
				checkInfoList.add("导入文件错误条数共：" + errorDataCount
						+ "条，请修改以上错误后再导入。");
			} else {
				// 保存代理商和营业网点（这两个将会取消掉，所以最后只剩下划小单元） 划小单元
				resultList = organizationManager.saveAgentOrBusinessOutlets(
						waitLevelList,
						waitUpLoadOrganizationList,
						waitUpLoadBusinessOutletsOrgList,
						waitUpLoadHXOrganizationInfoList,
						Integer.valueOf(bean.getImportType().getSelectedItem()
								.getValue().toString()));
				/**
				 * 开始日志添加操作 添加日志到队列需要： 业务开始时间，日志消息类型，错误编码和描述
				 */
				Class clazz[] = { Organization.class };
				log.endLog(logService, clazz, SysLogConstrants.IMPORT,
						SysLogConstrants.INFO, "组织划小单元新增导入记录日志");
			}

			if (checkInfoList != null && checkInfoList.size() > 0) {// 写出导入错误信息
				Map arg = new HashMap();
				arg.put("opType", "view");
				arg.put("infoList", checkInfoList);
				Window win = (Window) Executions.createComponents(
						"/pages/staff/staff_import.zul", null, arg);
				win.doModal();
			} else if (resultList != null && resultList.size() > 0) {
				// 导入成功添加日志
				Map arg = new HashMap();
				arg.put("resultList", resultList);
				ZkUtil.showInformation("导入成功！", "确定");
				// Window win = (Window) Executions.createComponents(
				// "/pages/organization/organization_import_result.zul",
				// null, arg);
				// win.doModal();
			}
			onCancel();
		} else {
			ZkUtil.showError("导入的文件必须是以.xls或.xlsx结尾的EXCEL文件!", "系统提示");
			return;
		}
	}

	/**
	 * 文件上传，这里只是把文件上传上去，没有做任何处理，得到文件数据
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onUpload$fileupload(ForwardEvent event) throws Exception {
		media = ((UploadEvent) event.getOrigin()).getMedia();
	}

	public int[] getCaseIndexArrayByType(int type) {
		int[] caseIndexArray = null;
		;
		switch (type) {
		case 1:// 代理商、营业网点
			caseIndexArray = new int[totalColumn[0]];
			for (int i = 0; i < totalColumn[0]; i++) {
				caseIndexArray[i] = i;
			}
			break;
		case 2:// 代理商
			caseIndexArray = new int[totalColumn[1]];
			for (int i = 0; i < totalColumn[1]; i++) {
				caseIndexArray[i] = i;
			}
			break;
		case 3:// 营业网点
			caseIndexArray = new int[totalColumn[2]];
			for (int i = 0; i < totalColumn[2]; i++) {
				caseIndexArray[i] = i + totalColumn[1] - 1;// 第二列从67开始
			}
			caseIndexArray[0] = 91;// 第一列获取91验证case
			break;
		case 4:// 划小单元
			caseIndexArray = new int[totalColumn[3]];
			for (int i = 0; i < totalColumn[3]; i++) {
				caseIndexArray[i] = i;
			}
		}
		return caseIndexArray;
	}

	/**
	 * 取消
	 */
	public void onCancel() throws Exception {
		if (media != null) {
			media = null;
		}
		bean.getOrganizationImportWindow().onClose();
	}

	/**
	 * 下载代理商与营业网点模版
	 */
	public void onDownLoadTemplate1() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "template1.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("代理商与营业网点导入模版.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("代理商与营业网点导入模版.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload
					.save(new FileInputStream(httpRequest
							.getRealPath("/pages/organization/doc/" + fileName)),
							"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载代理商与营业网点导入模版失败。", "系统提示");
		}
	}

	/**
	 * 下载代理商
	 */
	public void onDownLoadTemplate2() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "template2.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("代理商导入模版.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("代理商导入模版.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload
					.save(new FileInputStream(httpRequest
							.getRealPath("/pages/organization/doc/" + fileName)),
							"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载代理商导入模版失败。", "系统提示");
		}
	}

	/**
	 * 下载营业网点模版
	 */
	public void onDownLoadTemplate3() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "template3.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("营业网点导入模版.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("营业网点导入模版.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload
					.save(new FileInputStream(httpRequest
							.getRealPath("/pages/organization/doc/" + fileName)),
							"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载营业网点导入模版失败。", "系统提示");
		}
	}

	/**
	 * 下载划小单模板
	 * 
	 * @throws Exception
	 */
	public void onDownLoadTemplate4() throws Exception {
		try {
			String charset = "UTF-8";
			// 服务器文件名
			String fileName = "template4.xls";
			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("划小单元导入模板V0.2.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("划小单元导入模板V0.2.xls".getBytes(charset),
						"ISO8859-1");
			}
			Filedownload
					.save(new FileInputStream(httpRequest
							.getRealPath("/pages/organization/doc/" + fileName)),
							"application/octet-stream", encodedName);
		} catch (Exception e) {
			ZkUtil.showError("下载划小单元导入模版失败。", "系统提示");
		}

	}
}
