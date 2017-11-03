package cn.ffcs.uom.staff.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.util.StaticParameter;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.politicallocation.model.PoliticalLocation;
import cn.ffcs.uom.staff.action.bean.StaffDetailBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffExtendAttr;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 * 
 */
@Controller
@Scope("prototype")
public class StaffDetailWindowComposer extends BasePortletComposer {

	private static final long serialVersionUID = 1L;

	private StaffDetailBean bean = new StaffDetailBean();

	@Resource
	private StaticParameter staticParameter;

	private StaffManager staffManager = (StaffManager) ApplicationContextUtil
			.getBean("staffManager");

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
		bindCombobox();
		bindBean();
	}

	/**
	 * 绑定下拉框.
	 * 
	 * @throws Exception
	 */
	private void bindCombobox() throws Exception {
		List<NodeVo> liTp = UomClassProvider.getValuesList("Staff", "parttime");
		ListboxUtils.rendererForEdit(bean.getPartTime(), liTp);

		liTp = UomClassProvider.getValuesList("Staff", "staffPosition");
		ListboxUtils.rendererForEdit(bean.getStaffPosition(), liTp);
	}

	/**
	 * 页面初始化
	 * 
	 * @throws Exception
	 */
	public void bindBean() throws Exception {
		String opType = StrUtil.strnull(arg.get("opType"));

		if (SffOrPtyCtants.VIEW.equals(opType)) {

			bean.getStaffDetailWindow().setTitle("员工查看");

			Staff staff = (Staff) arg.get("staff");
			if(staff == null){
				return;
			}
			PoliticalLocation pl = new PoliticalLocation();
			pl.setLocationId(staff.getLocationId());
			pl.setLocationName(staff.getLocationName());
			bean.getPartyBandboxExt().setValue(
					staffManager.getPartyNameByStaffId(staff.getStaffId()));
			List<StaffExtendAttr> staffExtendAttrList = staffManager
					.getStaffExtendAttr(staff.getStaffId());
			if (null != staffExtendAttrList && staffExtendAttrList.size() > 0) {
				bean.getStaffExtendAttrExt()
						.setExtendValue(staffExtendAttrList);
			}
			String conNm = staticParameter.handling("Staff", "workProp",
					staff.getWorkProp());
			if (null != conNm) {
				bean.getWorkProp().setValue(conNm);
			}
			String staffProperty = staticParameter.handling("Staff", "staffProperty",
					staff.getStaffProperty());
			if (null != staffProperty) {
				bean.getStaffProperty().setValue(staffProperty);
			}
			PubUtil.fillBeanFromPo(staff, bean);
		}
	}

}
