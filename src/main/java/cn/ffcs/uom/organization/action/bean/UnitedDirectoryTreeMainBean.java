package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;

import cn.ffcs.uom.organization.action.UnitedDirectoryInfoEditExt;
import cn.ffcs.uom.organization.component.UnitedDirectoryTreeExt;

public class UnitedDirectoryTreeMainBean {

	@Getter
	@Setter
	private Tabbox leftTabbox;

	@Getter
	@Setter
	private Tabbox rightTabBox;

	/**
	 * 左侧选中的Tab
	 */
	@Getter
	@Setter
	private Tab leftSelectTab;

	/**
	 * 右侧选中的Tab
	 */
	@Getter
	@Setter
	private Tab rightSelectTab;

	@Getter
	@Setter
	private Tab leftTempTab;

	@Getter
	@Setter
	private Tab unitedDirectoryTab;

	/**
	 * 统一目录信息
	 */
	@Getter
	@Setter
	private Tab unitedDirectoryInfoTab;

	@Getter
	@Setter
	private Tabpanel leftTempTabpanel;

	@Getter
	@Setter
	private Tabpanel unitedDirectoryTabpanel;

	/**
	 * 统一目录树
	 */
	@Getter
	@Setter
	private UnitedDirectoryTreeExt unitedDirectoryTreeExt;

	/**
	 * 统一目录属性
	 */
	@Getter
	@Setter
	private UnitedDirectoryInfoEditExt unitedDirectoryInfoEditExt;

}
