package cn.ffcs.uom.audit.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.audit.action.bean.StaffAuditExtBean;
import cn.ffcs.uom.audit.manager.StaffAuditManager;
import cn.ffcs.uom.audit.model.StaffAudit;
import cn.ffcs.uom.audit.model.StaffAuditBill;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.ExcelUtil;
import cn.ffcs.uom.common.util.FileUtil;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;

/**
 * 员工稽核查询 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhulintao
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2014-07-30
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffAuditExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffAuditExtBean bean = new StaffAuditExtBean();

	/**
	 * 组织结构查询
	 */
	private StaffAudit queryStaffAudit;
	/**
	 * 组织结构查询
	 */
	private StaffAuditBill queryStaffAuditBill;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * zul.
	 */
	private final String zul = "/pages/audit/comp/staff_audit_ext.zul";

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("staffAuditManager")
	private StaffAuditManager staffAuditManager = (StaffAuditManager) ApplicationContextUtil
			.getBean("staffAuditManager");

	public StaffAuditExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
		this.addForward("onQueryStaffAuditRequest", this,
				"onQueryStaffAuditResponse");
	}

	public void onCreate() throws Exception {
		this.bean.getUpdateDate().setValue(
				new java.sql.Date(new Date().getTime()));
		//onQueryStaffAudit();
		onQueryStaffItems();
	}

	public void onQueryStaffAuditResponse(ForwardEvent event) throws Exception {
		//onQueryStaffAudit();
		onQueryStaffItems();
	}

	/**
	 * 员工稽核查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQueryStaffAudit() throws Exception {
		queryStaffAudit = new StaffAudit();
		this.bean.getStaffAuditListboxPaging().setActivePage(0);
		if (this.bean.getSystemDomainListbox() != null
				&& this.bean.getSystemDomainListbox().getSelectedItem() != null) {
			queryStaffAudit.setSystemDomainId(Long.parseLong((String) this.bean
					.getSystemDomainListbox().getSelectedItem().getValue()));
		}
		queryStaffAudit.setUpdateDate(this.bean.getUpdateDate().getValue());
		this.onStaffAuditListboxPaging();
	}

	public void onQueryStaffItems() throws Exception {
		List<NodeVo> staffItemList = staffAuditManager.queryStaffAuditNodes();
        ListboxUtils.rendererForEdit(bean.getSystemDomainListbox(), staffItemList);
	}
    
	/**
	 * 重置
	 */
	public void onStaffAuditReset() {
		this.bean.getSystemDomainListbox().selectItem(null);
		this.bean.getUpdateDate().setValue(
				new java.sql.Date(new Date().getTime()));
	}

	/**
	 * 导出员工稽核统计清单
	 */
	@SuppressWarnings("deprecation")
	public void onExportStatisticsBill() throws Exception {
		try {
			// 设置字符集
			String charset = "UTF-8";
			// 项目根目录
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 服务器文件名
			String fileName = "staffAuditStatisticsBill.xls";
			// 时间字符串
			String dataStr = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmssS");

			if (!StrUtil.isEmpty(dataStr)) {
				fileName = dataStr + ".xls";
			}
			// 服务器文件存放相对路径
			String filePath = "/pages/audit/temp/";
			// 服务器文件存放真实路径
			String fileRealPath = httpRequest.getRealPath(filePath + fileName);

			File file = new File(fileRealPath);

			// 判断该文件是否存在，如果存在则删除
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}
			// 删除已经下载过的文件
			if (ExcelUtil.staffStatisticsList.size() > 0) {
				for (int i = 0; i < ExcelUtil.staffStatisticsList.size(); i++) {
					File oldFile = new File(
							ExcelUtil.staffStatisticsList.get(i));
					if (oldFile.exists()) {
						FileUtil.deletefile(ExcelUtil.staffStatisticsList
								.get(i));
					}
				}
				ExcelUtil.staffStatisticsList.clear();
			}

			String[] headers = { "系统域ID", "系统名", "组织名或区域名", "员工总数", "异常数",
					"员工合格率", "更新时间" };

			queryStaffAudit = new StaffAudit();
			if (this.bean.getSystemDomainListbox() != null
					&& this.bean.getSystemDomainListbox().getSelectedItem() != null) {
				queryStaffAudit.setSystemDomainId(Long
						.parseLong((String) this.bean.getSystemDomainListbox()
								.getSelectedItem().getValue()));
			}
			queryStaffAudit.setUpdateDate(this.bean.getUpdateDate().getValue());
			List staffAuditList = staffAuditManager
					.queryStaffAuditList(queryStaffAudit);

			if (staffAuditList == null || staffAuditList.size() <= 0) {
				Messagebox.show("员工稽核统计清单中没有数据,未下载！");
				return;
			}

			OutputStream out = new FileOutputStream(fileRealPath);

			ExcelUtil.exportExcel("员工稽核统计清单", headers, staffAuditList, out,
					"yyyy-MM-dd");

			// 清空缓冲区
			out.flush();
			// 关闭文件输出流
			out.close();

			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("员工稽核统计清单.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");

			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("员工稽核统计清单.xls".getBytes(charset),
						"ISO8859-1");
			}
			FileInputStream in = new FileInputStream(fileRealPath);
			Filedownload.save(in, "application/octet-stream", encodedName);
			// 关闭输入文件流
			// in.close();

			// 删除临时创建的文件
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}

			ExcelUtil.staffStatisticsList.add(fileRealPath);

		} catch (FileNotFoundException e) {
			ZkUtil.showError("下载员工稽核统计清单失败", "系统提示");
			e.getMessage();
		} catch (IOException e) {
			ZkUtil.showError("下载员工稽核统计清单失败", "系统提示");
			e.getMessage();
		} catch (Exception e) {
			ZkUtil.showError("下载员工稽核统计清单失败", "系统提示");
			e.getMessage();
		}
	}

	/**
	 * 导出员工稽核详细清单
	 */
	@SuppressWarnings("deprecation")
	public void onExportDetailsBill() throws Exception {
		try {
			// 设置字符集
			String charset = "UTF-8";
			// 项目根目录
			HttpServletRequest httpRequest = (HttpServletRequest) Executions
					.getCurrent().getNativeRequest();
			// 服务器文件名
			String fileName = "staffAuditDetailsBill.xls";
			// 时间字符串
			String dataStr = DateUtil.dateToStr(new Date(), "yyyyMMddHHmmssS");

			if (!StrUtil.isEmpty(dataStr)) {
				fileName = dataStr + ".xls";
			}
			// 服务器文件存放相对路径
			String filePath = "/pages/audit/temp/";
			// 服务器文件存放真实路径
			String fileRealPath = httpRequest.getRealPath(filePath + fileName);

			File file = new File(fileRealPath);

			// 判断该文件是否存在，如果存在则删除
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}
			// 删除已经下载过的文件
			if (ExcelUtil.staffDetailsList.size() > 0) {
				for (int i = 0; i < ExcelUtil.staffDetailsList.size(); i++) {
					File oldFile = new File(ExcelUtil.staffDetailsList.get(i));
					if (oldFile.exists()) {
						FileUtil.deletefile(ExcelUtil.staffDetailsList.get(i));
					}
				}
				ExcelUtil.staffDetailsList.clear();
			}

			String[] headers = { "系统域ID", "系统名", "组织名或区域名", "员工ID", "员工姓名",
					"员工编码", "员工账号", "员工UUID", "组织全称", "不规范内容", "更新时间" };

			queryStaffAuditBill = new StaffAuditBill();
			if (this.bean.getSystemDomainListbox() != null
					&& this.bean.getSystemDomainListbox().getSelectedItem() != null) {
				queryStaffAuditBill.setSystemDomainId(Long
						.parseLong((String) this.bean.getSystemDomainListbox()
								.getSelectedItem().getValue()));
			} else {
				Messagebox.show("未选择系统名,请选择后进行导出员工稽核详细清单!");
				return;
			}
			queryStaffAuditBill.setUpdateDate(this.bean.getUpdateDate()
					.getValue());
			List staffAuditBillList = staffAuditManager
					.queryStaffAuditBillList(queryStaffAuditBill);

			if (staffAuditBillList == null || staffAuditBillList.size() <= 0) {
				Messagebox.show("员工稽核详细清单中没有数据,未下载！");
				return;
			}

			OutputStream out = new FileOutputStream(fileRealPath);

			ExcelUtil.exportExcel("员工稽核详细清单", headers, staffAuditBillList, out,
					"yyyy-MM-dd");

			// 清空缓冲区
			out.flush();
			// 关闭文件输出流
			out.close();

			// 编码后文件名
			String encodedName = null;
			encodedName = URLEncoder.encode("员工稽核详细清单.xls", charset);
			// 将空格替换为+号
			encodedName = encodedName.replace("%20", "+");

			// 解决ie6 bug 或者是火狐浏览器
			if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
					|| Servlets.isGecko3(httpRequest)) {
				encodedName = new String("员工稽核详细清单.xls".getBytes(charset),
						"ISO8859-1");
			}
			FileInputStream in = new FileInputStream(fileRealPath);
			Filedownload.save(in, "application/octet-stream", encodedName);
			// 关闭输入文件流
			// in.close();

			// 删除临时创建的文件
			if (file.exists()) {
				FileUtil.deletefile(fileRealPath);
			}

			ExcelUtil.staffDetailsList.add(fileRealPath);

		} catch (FileNotFoundException e) {
			ZkUtil.showError("下载员工稽核详细清单失败", "系统提示");
			e.getMessage();
		} catch (IOException e) {
			ZkUtil.showError("下载员工稽核详细清单失败", "系统提示");
			e.getMessage();
		} catch (Exception e) {
			ZkUtil.showError("下载员工稽核详细清单失败", "系统提示");
			e.getMessage();
		}
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onStaffAuditListboxPaging() throws Exception {

		PageInfo pageInfo = this.staffAuditManager.queryPageInfoByStaffAudit(
				queryStaffAudit, this.bean.getStaffAuditListboxPaging()
						.getActivePage() + 1, this.bean
						.getStaffAuditListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getStaffAuditListbox().setModel(dataList);
		this.bean.getStaffAuditListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));
	}

	/**
	 * 设置页面坐标
	 * 
	 * @param string
	 * @throws SystemException
	 * @throws Exception
	 */
	public void setPagePosition(String page) throws Exception {
		boolean canExportStatistics = false;
		boolean canExportDetails = false;
		if (PlatformUtil.isAdmin()) {
			canExportStatistics = true;
			canExportDetails = true;
		} else if ("auditPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_AUDIT_STATISTICS_EXPORT)) {
				canExportStatistics = true;
			}
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.STAFF_AUDIT_DETAILS_EXPORT)) {
				canExportDetails = true;
			}
		}
		this.bean.getExportStatisticsButton().setVisible(canExportStatistics);
		this.bean.getExportDetailsButton().setVisible(canExportDetails);
	}

}
