package cn.ffcs.uom.telcomregion.component;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.telcomregion.model.TelcomRegion;

public class TelcomRegionTreeBandbox extends Bandbox implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/telcom_region/comp/telcom_region_tree_bandbox.zul";
	/**
	 * 对象树.
	 */
	private TelcomRegionTree telcomRegionTree;

	/**
	 * .
	 */
	@Getter
	private TelcomRegion telcomRegion;

	/**
	 * 是否是配置界面(忽略数据权时都可使用)
	 */
	@Getter
	@Setter
	private Boolean isConfigPage = false;

	/**
	 * 构造函数.
	 */
	public TelcomRegionTreeBandbox() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
	}

	/**
	 * 界面初始化.
	 */
	public void onCreate() {
		this.setReadonly(true);
		/**
		 * 配置界面重新绑定
		 */
		if (isConfigPage) {
			this.telcomRegionTree.setIsConfigPage(isConfigPage);
			telcomRegionTreeBandboxBindTree();
		}
		this.telcomRegionTree.addEventListener(Events.ON_SELECT,
				new EventListener() {
					@SuppressWarnings("rawtypes")
					public void onEvent(Event event) throws Exception {
						if (TelcomRegionTreeBandbox.this.telcomRegionTree
								.getSelectedCount() > 0) {
							Object data = TelcomRegionTreeBandbox.this.telcomRegionTree
									.getSelectedItem().getValue();
							TelcomRegionTreeBandbox.this
									.setTelcomRegion((TelcomRegion) ((TreeNodeImpl) data)
											.getEntity());
						}
						TelcomRegionTreeBandbox.this.close();
					}
				});
	}

	/**
	 * 
	 * @param politicalLocation
	 */
	public void setTelcomRegion(TelcomRegion telcomRegion) {
		this.setValue(telcomRegion == null ? "" : telcomRegion.getRegionName());
		this.telcomRegion = telcomRegion;
	}

	/**
	 * 重新绑定
	 */
	public void telcomRegionTreeBandboxBindTree() {
		if (this.telcomRegionTree != null) {
			this.telcomRegionTree.bindTree();
		}
	}
}
