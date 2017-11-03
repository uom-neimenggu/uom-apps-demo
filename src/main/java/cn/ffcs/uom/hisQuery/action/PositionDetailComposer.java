/**
 * 
 */
package cn.ffcs.uom.hisQuery.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.hisQuery.action.bean.PositionDetailBean;
import cn.ffcs.uom.hisQuery.manager.PositionHisManager;
import cn.ffcs.uom.position.model.Position;


/**
 * 岗位详情Composer.
 * 
 * @author 
 * @version Revision 1.0.0
 */
@Controller
@Scope("prototype")
public class PositionDetailComposer extends BasePortletComposer {
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean
	 */
	private PositionDetailBean bean = new PositionDetailBean();
	/**
	 * Manager.
	 */
	private PositionHisManager positionHisManager = (PositionHisManager)ApplicationContextUtil.getBean("positionHisManager");

	/**
	 * 岗位.
	 */
	private Position position;
	/**
	 * 岗位标识
	 */
	private Long positionId;
	/**
	 * 生效时间
	 */
	private Date effDate;
	/**
	 * 失效时间
	 */
	private Date expDate;

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
	public void onCreate$positionDetailWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();

	}

	/**
	 * 绑定combobox.
	 * 
	 * @throws Exception
	 *             异常
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> positionType = UomClassProvider.getValuesList("Position",
				"positionType");
		ListboxUtils.rendererForEdit(bean.getPositionType(), positionType);
	}

	public void bindBean() throws Exception{
		bean.getPositionDetailWindow().setTitle("岗位详情");
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		positionId = (Long)arg.get("positionId");
		effDate = (Date)arg.get("effDate");
		expDate = (Date)arg.get("expDate");
		paramsMap.put("positionId", positionId);
		paramsMap.put("effDate", effDate);
		paramsMap.put("expDate", expDate);
		
		position = positionHisManager.queryPositionDetail(paramsMap);
		if(position != null){
			PubUtil.fillBeanFromPo(position, this.bean);
			this.bean.getEffDate().setValue(position.getEffDate());
		}			
	}
	
	public void onOk() throws Exception{
		Events.postEvent(Events.ON_OK, bean.getPositionDetailWindow(),
				position);
		bean.getPositionDetailWindow().onClose();
	}
}
