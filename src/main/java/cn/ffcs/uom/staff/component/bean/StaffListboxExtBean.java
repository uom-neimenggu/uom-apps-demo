package cn.ffcs.uom.staff.component.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

import cn.ffcs.uom.organization.component.OrganizationRelationTreeBandboxExt;

public class StaffListboxExtBean {

	/**
	 * ListBox
	 */
	@Getter
	@Setter
	private Listbox staffListbox;

	@Getter
	@Setter
	private Listbox pageListbox;

	/**
	 * 分页插件
	 */
	@Getter
	@Setter
	private Paging staffListboxPaging;

	/**
	 * 员工新增按钮.
	 */
	@Getter
	@Setter
	private Button addStaffButton;

	/**
	 * 员工批量新增按钮.
	 */
	@Getter
	@Setter
	private Button staffAddBatchButton;
	/**
	 * 员工编辑按钮.
	 */
	@Getter
	@Setter
	private Button editStaffButton;
	/**
	 * 个人信息编辑按钮.
	 */
	@Getter
	@Setter
	private Button editIndividualButton;
	/**
	 * 员工删除按钮.
	 */
	@Getter
	@Setter
	private Button delStaffButton;
	/**
	 * 员工选择按钮.
	 */
	@Getter
	@Setter
	private Button selectStaffButton;
	/**
	 * 重置密码按钮.
	 */
	@Getter
	@Setter
	private Toolbarbutton resetPwdButton;
	/**
	 * 设置角色按钮.
	 */
	@Getter
	@Setter
	private Toolbarbutton setStaffRoleButton;
	@Getter
	@Setter
	private Div staffWindowDiv;

	@Getter
	@Setter
	private Div staffBandboxDiv;

	@Getter
	@Setter
	private Div staffBatchDiv;
	/*******************************
	 * 
	 * *****************************
	 */
	@Getter
	@Setter
	private Textbox staffCode;

	@Getter
	@Setter
	private Textbox staffAccount;

	@Getter
	@Setter
	private Textbox staffName;

	/**
	 * 更新
	 */
	@Getter
	@Setter
	private Button updateStaffButton;

	/**
	 * 查看
	 */
	@Getter
	@Setter
	private Button viewButton;

	/**
	 * 导入
	 */
	@Getter
	@Setter
	private Button uploadButton;
	/**
	 * 模板下载
	 */
	@Getter
	@Setter
	private Button downloadButton;
	/**
	 * 组织树label
	 */
	@Getter
	@Setter
	private Label organizationLab;
	/**
	 * 组织树
	 */
	@Getter
	@Setter
	private OrganizationRelationTreeBandboxExt organizationRelationTreeBandboxExt;
}
