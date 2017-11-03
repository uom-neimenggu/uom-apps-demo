package cn.ffcs.uom.report.component;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Messagebox;

import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.uom.common.key.ActionKeys;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ExportExcelNew;
import cn.ffcs.uom.common.util.IPortletInfoProvider;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.report.bean.FullStaffQuarterReportExtBean;
import cn.ffcs.uom.report.manager.TBaobQuarterManager;
import cn.ffcs.uom.report.model.TBaobQuarter;
import cn.ffcs.uom.report.model.TBaobUnit;

/**
 *  全口径人员情况统计按季人数统计 .
 * 
 * @版权：福富软件 版权所有 (c) 2014
 * @author zhanglu
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017-05-09
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class FullStaffQuarterReportExt extends Div implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6092385957399905208L;

	/**
	 * bean.
	 */
	private FullStaffQuarterReportExtBean bean = new FullStaffQuarterReportExtBean();

	private TBaobQuarter queryTBaobQuarter;

	@Getter
	@Setter
	private IPortletInfoProvider portletInfoProvider;

	/**
	 * zul.
	 */
	private final String zul = "/pages/report/comp/full_staff_quarter_report_ext.zul";

	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("tBaobQuarterManager")
	private TBaobQuarterManager tBaobQuarterManager = (TBaobQuarterManager) ApplicationContextUtil
			.getBean("tBaobQuarterManager");

	public FullStaffQuarterReportExt() throws Exception {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	public void onCreate() throws Exception {
		this.bean.getDate().setValue(
				new java.sql.Date(new Date().getTime()));
	}

	/**
	 * 查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onQuery() throws Exception {
		this.bean.getReportListboxPaging().setActivePage(0);
		this.onListboxPaging();
	}
    
	/**
	 * 重置
	 */
	public void onReset() {
		this.bean.getDate().setValue(
				new java.sql.Date(new Date().getTime()));
	}

	/**
	 * 导出
	 */
	public void onExport() throws Exception {
		// 设置字符集
				String charset = "UTF-8";
				// 项目根目录
				HttpServletRequest httpRequest = (HttpServletRequest) Executions
						.getCurrent().getNativeRequest();
				
				queryTBaobQuarter = new TBaobQuarter();
				String reportDate = new SimpleDateFormat("yyyy-MM").format(this.bean
						.getDate().getValue());
				queryTBaobQuarter.setReportDate(reportDate);

				List<Map<String, Object>> tBaobQuarterList = tBaobQuarterManager.queryTBaobQuarterList(queryTBaobQuarter);

				if (tBaobQuarterList == null || tBaobQuarterList.size() <= 0) {
					Messagebox.show("全口径人员按季人数统计中没有数据,未下载！");
					return;
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				ExportExcelNew.exportExcel("全口径人员按季人数统计", tBaobQuarterList, out);
				byte[] content = out.toByteArray();

				// 清空缓冲区
				out.flush();
				// 关闭文件输出流
				out.close();

				// 编码后文件名
				String encodedName = null;
				encodedName = URLEncoder.encode("全口径人员按季人数统计.xlsx", charset);
				// 将空格替换为+号
				encodedName = encodedName.replace("%20", "+");

				// 解决ie6 bug 或者是火狐浏览器
				if (encodedName.length() > 150 || Servlets.isGecko(httpRequest)
						|| Servlets.isGecko3(httpRequest)) {
					encodedName = new String("全口径人员按季人数统计.xlsx".getBytes(charset),
							"ISO8859-1");
				}

				Filedownload.save(content, "application/octet-stream", encodedName);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void onListboxPaging() throws Exception {
		queryTBaobQuarter = new TBaobQuarter();
		String reportDate = new SimpleDateFormat("yyyy-MM").format(this.bean.getDate().getValue());
 		queryTBaobQuarter.setReportDate(reportDate);
		
		PageInfo pageInfo = this.tBaobQuarterManager.queryTBaobQuarterPageInfo(
				queryTBaobQuarter, this.bean.getReportListboxPaging()
						.getActivePage() + 1, this.bean
						.getReportListboxPaging().getPageSize());
		ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
				true);
		this.bean.getReportListbox().setModel(dataList);
		this.bean.getReportListboxPaging().setTotalSize(
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
		boolean canExport = false;
		if (PlatformUtil.isAdmin()) {
			canExport = true;
		} else if ("staffReportPage".equals(page)) {
			if (PlatformUtil.checkHasPermission(getPortletInfoProvider(),
					ActionKeys.FULL_STAFF_QUARTER_REPORT_EXPORT)) {
				canExport = true;
			}
		}
		
		this.bean.getExportButton().setVisible(canExport);
	}

}
