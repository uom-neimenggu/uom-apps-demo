package cn.ffcs.uom.staff.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.staff.action.bean.StaffOrgTranResultMainBean;
import cn.ffcs.uom.staff.model.StaffOrgTranTemp;
import cn.ffcs.uom.webservices.constants.WsConstants;

/**
 * 员工组织业务关系导入结果.
 * 
 * @author 朱林涛
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
@SuppressWarnings({ "unused" })
public class StaffOrgTranResultMainComposer extends BasePortletComposer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean.
	 */
	private StaffOrgTranResultMainBean bean = new StaffOrgTranResultMainBean();

	/**
	 * list
	 */
	private List<StaffOrgTranTemp> staffOrgTranTempList;

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
	@SuppressWarnings("unchecked")
	public void onCreate$staffOrgTranResultMainWin() throws Exception {

		this.bean.getStaffOrgTranResultMainWin().setTitle("员工组织业务关系导入结果页面");

		staffOrgTranTempList = (List<StaffOrgTranTemp>) arg
				.get("staffOrgTranTempList");

		if (staffOrgTranTempList != null && staffOrgTranTempList.size() > 0) {

			this.bindDate(staffOrgTranTempList);// 向ListBox中填充数据，解决样式延迟加载的问题

		} else {
			this.bean.getStaffOrgTranResultDiv().setVisible(true);
			this.bean.getStaffOrgTranResultListboxDiv().setVisible(false);
		}

	}

	public void bindDate(List<StaffOrgTranTemp> staffOrgTranTempList) {

		Div div = (Div) this.bean.getStaffOrgTranResultListboxDiv();// 获取Div
		div.setStyle("height:436px;overflow:auto;");

		Panel panel = new Panel();
		panel.setParent(div);

		Panelchildren panelChildren = new Panelchildren();
		panelChildren.setParent(panel);

		Listbox listResult = new Listbox();
		listResult.setParent(panelChildren);

		listResult.setRows(staffOrgTranTempList.size());

		Listhead listHead = new Listhead();// 创建行标题
		listHead.setSizable(true);// 行可调整
		listHead.setParent(listResult);// 设置父容器

		Listheader dataSourceListheader = new Listheader("数据来源");
		dataSourceListheader.setWidth("90px");

		Listheader operationTypeListheader = new Listheader("操作类型");
		operationTypeListheader.setWidth("70px");

		Listheader staffNameListheader = new Listheader("员工姓名");
		staffNameListheader.setWidth("80px");

		Listheader staffAccountListheader = new Listheader("员工工号");
		staffAccountListheader.setWidth("90px");

		Listheader orgCodeListheader = new Listheader("组织编码/全息网格标识");
		orgCodeListheader.setWidth("140px");

		Listheader resultListheader = new Listheader("操作结果");
		resultListheader.setWidth("70px");

		Listheader errMsgListheader = new Listheader("备  注");

		listHead.appendChild(dataSourceListheader);
		listHead.appendChild(operationTypeListheader);
		listHead.appendChild(staffNameListheader);
		listHead.appendChild(staffAccountListheader);
		listHead.appendChild(orgCodeListheader);
		listHead.appendChild(resultListheader);
		listHead.appendChild(errMsgListheader);

		for (StaffOrgTranTemp staffOrgTranTemp : staffOrgTranTempList) {
			Listitem li = new Listitem();// 创建行
			li.setParent(listResult);// 设置父容器
			li.appendChild(new Listcell(staffOrgTranTemp.getDataSourceName()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getOperationTypeName()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getStaffName()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getStaffAccount()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getOrgCode()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getResultName()));// 添加列
			li.appendChild(new Listcell(staffOrgTranTemp.getErrMsg()));// 添加列

			if (WsConstants.TASK_FAILED.equals(staffOrgTranTemp.getResult())) {// 数据操作失败，则用黄底色显示
				li.setStyle("background-color:yellow;");
			}

		}

	}

}
