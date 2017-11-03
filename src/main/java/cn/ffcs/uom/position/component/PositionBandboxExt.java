/**
 * 
 */
package cn.ffcs.uom.position.component;

import lombok.Getter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Bandbox;

import cn.ffcs.uom.position.action.PositionListboxDiv;
import cn.ffcs.uom.position.constants.PositionConstant;
import cn.ffcs.uom.position.model.Position;

/**
 * @author yahui
 * 
 */
@Controller
@Scope("prototype")
public class PositionBandboxExt extends Bandbox implements IdSpace {
	/**
     * .
     */
    private static final long serialVersionUID = 1L;
    /**
	 * zul文件路径。
	 */
	private final String zul = "/pages/position/comp/position_bandbox_ext.zul";
	/**
	 * positionMainWin
	 */
	private PositionListboxDiv positionListboxDiv;
	/**
	 * 岗位
	 */
	@Getter
	private Position position;

	/**
	 * 构造函数
	 */
	public PositionBandboxExt() {
		Executions.createComponents(this.zul, this, null);
		// 2. Wire variables (optional)
		Components.wireVariables(this, this);
		// 3. Wire event listeners (optional)
		Components.addForwards(this, this, '$');
		/**
		 * 监听事件
		 */
		this.positionListboxDiv.addForward(PositionConstant.ON_SELECT_POSITION,
				this, "onSelectPositionReponse");
		this.positionListboxDiv.addForward(PositionConstant.ON_CLEAN_POSITION,
				this, "onCleanPositionReponse");
		this.positionListboxDiv.addForward(PositionConstant.ON_CLOSE_POSITION,
				this, "onClosePositionReponse");
		/**
		 * 设置按钮
		 */
		positionListboxDiv.setPositionWindowDivVisible(false);
		positionListboxDiv.setPositionBandboxDivVisible(true);
	}

	public Object getAssignObject() {
		return this.getPosition();
	}

	public void setAssignObject(Object assignObject) {
		if (assignObject == null || assignObject instanceof Position) {
			this.setPosition((Position) assignObject);
		}
	}

	public void setPosition(Position position) {
		this.setValue(position == null ? "" : position.getPositionName());
		this.position = position;
	}

	/**
	 * 选择岗位
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onSelectPositionReponse(final ForwardEvent event)
			throws Exception {
		position = (Position) event.getOrigin().getData();
		if (position != null) {
			this.setValue(position.getPositionName());
		}
		this.close();
	}

	/**
	 * 清空内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCleanPositionReponse(final ForwardEvent event)
			throws Exception {
		this.setPosition(null);
		this.close();
	}

	/**
	 * 关闭窗口
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClosePositionReponse(final ForwardEvent event)
			throws Exception {
		this.close();
	}

}
