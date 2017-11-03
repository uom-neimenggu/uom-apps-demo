package cn.ffcs.uom.politicallocation.component;

import lombok.Getter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;

public class PoliticalLocationTreeBandbox extends Bandbox implements IdSpace {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/political_location/comp/political_location_tree_bandbox.zul";
	/**
	 * 对象树.
	 */
	private PoliticalLocationTree politicalLocationTree;

	/**
	 * .
	 */
	@Getter
	private PoliticalLocation politicalLocation;

	/**
	 * 构造函数.
	 */
	public PoliticalLocationTreeBandbox() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');

		this.init();
	}

	/**
	 * 界面初始化.
	 */
	public void init() {
		this.setReadonly(true);
		this.politicalLocationTree.addEventListener(Events.ON_SELECT,
				new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (PoliticalLocationTreeBandbox.this.politicalLocationTree
								.getSelectedCount() > 0) {
							Object data = PoliticalLocationTreeBandbox.this.politicalLocationTree
									.getSelectedItem().getValue();
							PoliticalLocationTreeBandbox.this
									.setPoliticalLocation((PoliticalLocation) ((TreeNodeImpl) data)
											.getEntity());
						}
						PoliticalLocationTreeBandbox.this.close();
					}
				});
	}

	/**
	 * 
	 * @param politicalLocation
	 */
	public void setPoliticalLocation(PoliticalLocation politicalLocation) {
		this.setValue(politicalLocation == null ? "" : politicalLocation
				.getLocationName());
		this.politicalLocation = politicalLocation;
	}

	/**
	 * 重新绑定
	 */
	public void politicalLocationTreeBindTree() {
		if (this.politicalLocationTree != null) {
			this.politicalLocationTree.bindTree();
		}
	}
}
