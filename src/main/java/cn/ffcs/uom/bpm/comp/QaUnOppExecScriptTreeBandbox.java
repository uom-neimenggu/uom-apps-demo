package cn.ffcs.uom.bpm.comp;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.zkplus.zul.tree.node.impl.TreeNodeImpl;

public class QaUnOppExecScriptTreeBandbox extends Bandbox implements IdSpace {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * zul文件路径。
	 */
	private final String zul = "/pages/bpm/comp/qa_script_tree_bandbox.zul";
	/**
	 * 对象树.
	 */
	private QaUnOppExecScriptTree qaUnOppExecScriptTree;
	
	/**
	 * 限制树根节点展示值
	 */
	@Getter
	@Setter
	private String flag;

	/**
	 * .
	 */
	@Getter
	private QaUnOppExecScript qaUnOppExecScript;
	
	/**
	 * 构造函数.
	 */
	public QaUnOppExecScriptTreeBandbox() {
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
		
		if(!StrUtil.isNullOrEmpty(flag)) {
			this.qaUnOppExecScriptTree.setFlag(flag);
		}
		
		this.qaUnOppExecScriptTree.addEventListener(Events.ON_SELECT,
				new EventListener() {
					@SuppressWarnings("rawtypes")
					public void onEvent(Event event) throws Exception {
						List list = QaUnOppExecScriptTreeBandbox.this.qaUnOppExecScriptTree.getSelectedItem().getChildren();
						int size = list.size();
						
						if (QaUnOppExecScriptTreeBandbox.this.qaUnOppExecScriptTree
								.getSelectedCount() > 0 && size == 1) {
							Object data = QaUnOppExecScriptTreeBandbox.this.qaUnOppExecScriptTree
									.getSelectedItem().getValue();
							QaUnOppExecScriptTreeBandbox.this
									.setQaUnOppExecScript((QaUnOppExecScript) ((TreeNodeImpl) data)
											.getEntity());
							
							QaUnOppExecScriptTreeBandbox.this.close();
						}
					}
				});
	}

	/**
	 * 
	 * @param qaUnOppExecScript
	 */
	public void setQaUnOppExecScript(QaUnOppExecScript qaUnOppExecScript) {
		this.setValue(qaUnOppExecScript == null ? "" : qaUnOppExecScript.getExecKidName());
		this.qaUnOppExecScript = qaUnOppExecScript;
	}

	/**
	 * 重新绑定
	 */
	public void telcomRegionTreeBandboxBindTree() {
		if (this.qaUnOppExecScriptTree != null) {
			this.qaUnOppExecScriptTree.bindTree();
		}
	}
}
