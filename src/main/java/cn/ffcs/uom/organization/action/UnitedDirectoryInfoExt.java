package cn.ffcs.uom.organization.action;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Div;

import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.organization.action.bean.UnitedDirectoryInfoExtBean;
import cn.ffcs.uom.organization.model.UnitedDirectory;

public class UnitedDirectoryInfoExt extends Div implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8024563184709088381L;

	private final String zul = "/pages/organization/united_directory_info_ext.zul";

	/**
	 * 页面bean
	 */
	@Getter
	private UnitedDirectoryInfoExtBean bean = new UnitedDirectoryInfoExtBean();
	/**
	 * 组织
	 */
	private UnitedDirectory unitedDirectory;

	/**
	 * 旧的组织
	 */
	private UnitedDirectory oldUnitedDirectory;

	/**
	 * 操作类型
	 */
	@Setter
	private String opType;

	/**
	 * 初始化
	 */
	public UnitedDirectoryInfoExt() {
		Executions.createComponents(this.zul, this, null);
		Components.wireVariables(this, bean);
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate() throws Exception {

	}

	/**
	 * 获取组织信息
	 * 
	 * @return
	 */
	public UnitedDirectory getUnitedDirectory() {
		if ("add".equals(opType)) {
			unitedDirectory = new UnitedDirectory();
		} else if ("mod".equals(opType)) {
			unitedDirectory = oldUnitedDirectory;
		} else {
			unitedDirectory = new UnitedDirectory();
		}

		String oldDeptname = unitedDirectory.getDeptname();
		/**
		 * 填充值
		 */
		this.fillPoFromBean(bean, unitedDirectory);

		String deptname = unitedDirectory.getDeptname();

		if (!StrUtil.isEmpty(oldDeptname) && !StrUtil.isEmpty(deptname)
				&& !oldDeptname.equals(deptname)) {
			// unitedDirectory.setIsChangeDeptname(true);
		}
		return unitedDirectory;
	}

	/**
	 * 设置组织信息
	 * 
	 * @param unitedDirectory
	 */
	public void setUnitedDirectory(UnitedDirectory unitedDirectory) {

		this.oldUnitedDirectory = unitedDirectory;

		if ("mod".equals(opType)) {
			this.fillBeanFromPo(unitedDirectory, this.bean);
		} else if ("show".equals(opType)) {
			this.fillBeanFromPo(unitedDirectory, this.bean);
		} else if ("clear".equals(opType)) {
			this.fillBeanFromPo(unitedDirectory, this.bean);
		} else if ("addUnitedDirectoryRootNode".equals(opType)) {
			this.fillBeanFromPo(unitedDirectory, this.bean);
		} else if ("addUnitedDirectoryChildNode".equals(opType)) {
			this.fillBeanFromPo(unitedDirectory, this.bean);
		}
	}

	/**
	 * 验证数据
	 * 
	 * @return
	 */
	public String getDoValidUnitedDirectoryInfo() {

		if (this.bean.getDeptname() == null
				|| StrUtil.isEmpty(this.bean.getDeptname().getValue())) {
			return "组织名称不能为空";
		} else {
			if (StrUtil.checkBlank(this.bean.getDeptname().getValue())) {
				return "组织名称中含有空格";
			}
		}
		return "";
	}

	/**
	 * 填充po
	 */
	private void fillPoFromBean(UnitedDirectoryInfoExtBean bean,
			UnitedDirectory unitedDirectory) {

		PubUtil.fillPoFromBean(bean, unitedDirectory);

	}

	/**
	 * 填充bean
	 */
	private void fillBeanFromPo(UnitedDirectory unitedDirectory,
			UnitedDirectoryInfoExtBean bean) {

		if (unitedDirectory != null) {
			PubUtil.fillBeanFromPo(unitedDirectory, bean);
		}
	}

	public void setUpdateUnitedDirectory(UnitedDirectory unitedDirectory) {
		if (unitedDirectory != null) {
			bean.getDeptname().setValue(unitedDirectory.getDeptname());
		}
	}
}
