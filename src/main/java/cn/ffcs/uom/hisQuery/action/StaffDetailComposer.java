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
import cn.ffcs.uom.hisQuery.action.bean.StaffDetailBean;
import cn.ffcs.uom.hisQuery.manager.StaffHisManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

/**
 * 查看员工详细信息
 * @author yahui
 *
 */
@Controller
@Scope("prototype")
public class StaffDetailComposer extends BasePortletComposer {
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean
	 */
	private StaffDetailBean bean = new StaffDetailBean();
	/**
	 * Manager.
	 */
	private StaffHisManager staffHisManager = (StaffHisManager)ApplicationContextUtil.getBean("staffHisManager");
	/**
	 * 员工.
	 */
	private Staff staff;
	/**
	 * 员工账号
	 */
	private StaffAccount staffAccount;

	/**
	 * 生效时间
	 */
	private Date effDate;
	/**
	 * 失效时间
	 */
	private Date expDate;
	
	private Long staffId;
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
	public void onCreate$staffDetailWindow() throws Exception {
		this.bindCombobox();
		this.bindBean();
	}
	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		
		List<NodeVo> partTime = UomClassProvider.getValuesList(
				"Staff", "partTime");
		ListboxUtils.rendererForEdit(bean.getPartTime(), partTime);
		/*List<NodeVo> staffStatusCd = UomClassProvider.getValuesList(
				"UomEntity", "statusCd");
		ListboxUtils.rendererForEdit(bean.getStaffStatusCd(), staffStatusCd);
		List<NodeVo> accountStatusCd = UomClassProvider.getValuesList(
				"UomEntity", "statusCd");
		ListboxUtils.rendererForEdit(bean.getAccountStatusCd(), accountStatusCd);*/
	}
	/**
	 * 页面初始化
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		this.bean.getStaffDetailWindow().setTitle("员工详情");
		Map<String,Object> paramsMap = new HashMap<String, Object>();
		staffId = (Long)arg.get("staffId");
		effDate = (Date)arg.get("effDate");
		expDate = (Date)arg.get("expDate");
		paramsMap.put("positionId", staffId);
		paramsMap.put("effDate", effDate);
		paramsMap.put("expDate", expDate);
		staff = staffHisManager.queryStaffDetail(paramsMap);
		if(staff != null){
			PubUtil.fillBeanFromPo(staff, this.bean);
			staffAccount = staff.getStaffAccountHis();
			if(staffAccount != null){
				PubUtil.fillBeanFromPo(staffAccount, this.bean);
			}
		}			
	}
	/**
	 * 关闭页面
	 * @throws Exception
	 */
	public void onOk() throws Exception{
		Events.postEvent(Events.ON_OK, bean.getStaffDetailWindow(),
				staff);
		bean.getStaffDetailWindow().onClose();							
	}

}
