package cn.ffcs.uom.contrast.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.DiverseAttribute;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.NumericUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.contrast.action.bean.ContrastMainBean;
import cn.ffcs.uom.contrast.manager.ContrastManager;
import cn.ffcs.uom.contrast.model.Contrast;
import cn.ffcs.uom.ftpsyncfile.manager.BuildFileSqlManager;
import cn.ffcs.uom.organization.manager.OrgTreeManager;
import cn.ffcs.uom.organization.model.OrgTree;
import cn.ffcs.uom.publishLog.manager.PublishLogManager;

/**
 * 日志操作查询.
 * 
 * @author zhulintao
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class ContrastMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * bean.
	 */
	private ContrastMainBean bean = new ContrastMainBean();

	/**
	 * 选中的员工对照记录
	 */
	private Contrast contrast;
	/**
	 * 查询的员工对照记录
	 */
	private Contrast queryContrast;
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("contrastManager")
	private ContrastManager contrastManager = (ContrastManager) ApplicationContextUtil
			.getBean("contrastManager");

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Components.wireVariables(comp, bean);
		onContrastQueryRequest();
	}

	/**
	 * 员工对照表选中请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onContrastSelectRequest() throws Exception {
		if (this.bean.getContrastListbox().getSelectedIndex() != -1) {
			contrast = (Contrast) bean.getContrastListbox().getSelectedItem()
					.getValue();
		}
	}

	/**
	 * 组织树列表查询请求事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onContrastQueryRequest() throws Exception {
		queryContrast = new Contrast();
		this.bean.getContrastListboxPaging().setActivePage(0);
		this.onContrastQueryResponse();
	}

	public void onContrastReset() {
		// this.bean.getUomStaffId().setValue(null);
		// this.bean.setOssStaffId().setValue(null);
		this.bean.getUomNbr().setValue(null);
		this.bean.getUomAccount().setValue(null);
		this.bean.getOssNbr().setValue(null);
		this.bean.getOssAccount().setValue(null);
		this.bean.getOssName().setValue(null);
		this.bean.getOssCertNumber().setValue(null);
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public void onContrastQueryResponse() throws Exception {

		// queryContrast.setUomStaffId(this.bean.getUomStaffId().getValue());
		// queryContrast.setOssStaffId(this.bean.setOssStaffId().getValue());
		queryContrast.setUomNbr(this.bean.getUomNbr().getValue());
		queryContrast.setUomAccount(this.bean.getUomAccount().getValue());
		queryContrast.setOssNbr(this.bean.getOssNbr().getValue());
		queryContrast.setOssAccount(this.bean.getOssAccount().getValue());
		queryContrast.setOssName(this.bean.getOssName().getValue());
		queryContrast.setOssCertNumber(this.bean.getOssCertNumber().getValue());

		PageInfo pageInfo = this.contrastManager.queryPageInfoByContrast(
				queryContrast, this.bean.getContrastListboxPaging()
						.getActivePage() + 1, this.bean
						.getContrastListboxPaging().getPageSize());
		// ListModel dataList = new BindingListModelList(pageInfo.getDataList(),
		// true);
		// this.bean.getContrastListbox().setModel(dataList);
		this.bindDate(pageInfo.getDataList());
		this.bean.getContrastListboxPaging().setTotalSize(
				NumericUtil.nullToZero(pageInfo.getTotalCount()));

	}

	public void bindDate(List<Contrast> list) {

		ListboxUtils.clearListbox(this.bean.getContrastListbox());

		if (list != null && list.size() > 0) {

			Listbox listResult = this.bean.getContrastListbox();// 获取Listbox

			/*
			 * Listhead listHead = new Listhead();// 创建行标题
			 * listHead.setParent(listResult);// 设置父容器 listHead.appendChild(new
			 * Listheader("UOM员工ID")); listHead.appendChild(new
			 * Listheader("OSS员工ID")); listHead.appendChild(new
			 * Listheader("OSS员工名称")); listHead.appendChild(new
			 * Listheader("UOM员工账号")); listHead.appendChild(new
			 * Listheader("OSS员工账号")); listHead.appendChild(new
			 * Listheader("UOM员工工号")); listHead.appendChild(new
			 * Listheader("OSS员工工号")); listHead.appendChild(new
			 * Listheader("OSS员工证件号")); listHead.appendChild(new
			 * Listheader("创建时间"));
			 */

			for (Contrast contrast : list) {
				Listitem li = new Listitem();// 创建行
				li.setParent(listResult);// 设置父容器
				li.appendChild(new Listcell(contrast.getUomStaffId().toString()));// 添加列
				li.appendChild(new Listcell(contrast.getOssStaffId().toString()));// 添加列
				li.appendChild(new Listcell(contrast.getOssName()));// 添加列
				li.appendChild(new Listcell(contrast.getUomAccount()));// 添加列
				li.appendChild(new Listcell(contrast.getOssAccount()));// 添加列
				li.appendChild(new Listcell(contrast.getUomNbr()));// 添加列
				li.appendChild(new Listcell(contrast.getOssNbr()));// 添加列
				li.appendChild(new Listcell(contrast.getOssCertNumber()));// 添加列
				li.appendChild(new Listcell(DateUtil.dateToStr(
						contrast.getCreateDate(), "yyyy-MM-dd HH:mm:ss")));// 添加列
			}
		}
	}

	/**
	 * 组织树列表查询响应事件
	 * 
	 * @throws Exception
	 *             异常
	 */
	@SuppressWarnings("unused")
	public void onContrastListboxPaging() throws Exception {
		this.onContrastQueryResponse();
	}

}
