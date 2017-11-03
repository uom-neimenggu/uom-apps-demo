package cn.ffcs.uom.gridUnit.component;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.gridUnit.model.GridUnit;
import cn.ffcs.uom.organization.constants.OrganizationConstant;
import cn.ffcs.uom.telcomregion.constants.TelecomRegionConstants;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

@Controller
@Scope("prototype")
public class GridUnitBandboxExt extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/gridUnit/comp/grid_unit_bandbox_ext.zul";
	/**
	 * 选择的网格单元
	 */
	private GridUnit gridUnit;
	/**
	 * 网格单元列表
	 */
	@Getter
	@Setter
	private GridUnitListboxExt gridUnitListboxExt;
	/**
	 * 是否是配置等页面（忽略数据权，电信管理区域默认中国）
	 */
	@Getter
	@Setter
	private Boolean isConfigPage = false;

	private boolean isLoaded = false;

	/**
	 * 构造函数
	 */
	public GridUnitBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		gridUnitListboxExt.getBean().getGridUnitListboxPaging().setPageSize(10);
		gridUnitListboxExt.getBean().getGridUnitBandboxDiv().setVisible(true);

		/**
		 * 监听事件
		 */
		this.gridUnitListboxExt.addForward(
				OrganizationConstant.ON_GIRD_UNIT_SELECT, this,
				"onSelectGridUnitResponse");
		this.gridUnitListboxExt.addForward(
				OrganizationConstant.ON_CLEAN_GRID_UNIT, this,
				"onCleanGridUnitResponse");
		this.gridUnitListboxExt.addForward(
				OrganizationConstant.ON_CLOSE_GRID_UNIT, this,
				"onCloseGridUnitResponse");
		/**
		 * 添加点击查询事件
		 */
		this.addEventListener("onOpen", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				isLoaded = true;
			}
		});

	}

	/**
	 * 创建接受参数,设置网格单元类型
	 */
	public void onCreate() {
		/**
		 * 公开页面（数据权忽略电信管理区域）
		 */
		if (isConfigPage) {
			Map map = new HashMap();
			TelcomRegion permissionTelcomRegion = new TelcomRegion();
			permissionTelcomRegion
					.setTelcomRegionId(TelecomRegionConstants.ROOT_TELECOM_REGION_ID);
			map.put("configTelcomRegion", permissionTelcomRegion);
			Events.postEvent(OrganizationConstant.ON_SET_CONFIG_PAGE_REQUEST,
					this.gridUnitListboxExt, map);
		}
	}

	public Object getAssignObject() {
		return getGridUnit();
	}

	public GridUnit getGridUnit() {
		return this.gridUnit;
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof GridUnit) {
			setGridUnit((GridUnit) assignObject);
		}
	}

	public void setGridUnit(GridUnit gridUnit) {
		this.setValue(gridUnit == null ? "" : gridUnit.getGridName());
		this.gridUnit = gridUnit;
	}

	/**
	 * 选择网格单元
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectGridUnitResponse(final ForwardEvent event)
			throws Exception {
		gridUnit = (GridUnit) event.getOrigin().getData();
		if (gridUnit != null) {
			this.setValue(gridUnit.getGridName());
		}
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanGridUnitResponse(final ForwardEvent event)
			throws Exception {
		this.setGridUnit(null);
		this.close();
		Events.postEvent(Events.ON_CHANGING, this, null);
	}

	/**
	 * 关闭窗口
	 * 
	 * @param eventt
	 * @throws Exception
	 */
	public void onCloseGridUnitResponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}
}
