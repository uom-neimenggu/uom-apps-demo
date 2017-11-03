package cn.ffcs.uom.hisQuery.action;
import java.awt.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.Events;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ComboboxUtils;
import cn.ffcs.uom.common.util.ListboxUtils;
import cn.ffcs.uom.common.util.PubUtil;
import cn.ffcs.uom.common.vo.NodeVo;
import cn.ffcs.uom.hisQuery.action.bean.OrganizationDetailBean;
import cn.ffcs.uom.hisQuery.manager.OrgHisManager;
import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.organization.model.OrgContactInfo;


/**
 * @author yahui
 *
 */
@Controller
@Scope("prototype")
public class OrganizationDetailComposer extends BasePortletComposer {
	/**
	 * 序列化.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * bean
	 */
	private OrganizationDetailBean bean = new OrganizationDetailBean();
	
	/**
	 * Manager.
	 */
	@Autowired
	@Qualifier("orgHisManager")
	private OrgHisManager orgHisManager;
	
	/**
	 *组织标识.
	 **/
	@Getter
	@Setter
	private Long orgId;
	
	/**
	 * 生效时间
	 */
	private Date effDate;
	/**
	 * 失效时间
	 */
	private Date expDate;
	/**
	 * 组织.
	 */
	private Organization organization;
	
	private OrgContactInfo organizationContactInfo;
	
    @Override
    public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
        super.doAfterCompose(comp);
        Components.wireVariables(comp, bean);
    }
	
	/**
	 * window初始化.
	 * 
	 * @throws Exception
	 *             异常
	 */
	public void onCreate$organizationDetailWindow() throws Exception {
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
		List<NodeVo> orgTypeList = UomClassProvider.getValuesList(
				"TreeOrgRelaTypeRule", "orgType");
		ListboxUtils.rendererForEdit(this.bean.getOrgType(), orgTypeList);

		List<NodeVo> existTypeList = UomClassProvider.getValuesList(
				"TreeOrgRelaTypeRule", "existType");
		ListboxUtils.rendererForEdit(this.bean.getExistType(), existTypeList);

		List<NodeVo> orgLevelList = UomClassProvider.getValuesList(
				"TreeOrgRelaTypeRule", "orgLevel");
		ComboboxUtils.rendererForEdit(this.bean.getOrgLevel(), orgLevelList);


		List<NodeVo> relaCdList = UomClassProvider.getValuesList(
				"OrganizationRelation", "relaCd");
		ComboboxUtils.rendererForEdit(this.bean.getRelaCd(), relaCdList);
	}
	/**
	 * 填充信息
	 * @throws Exception
	 */
	public void bindBean() throws Exception{
		bean.getOrganizationDetailWindow().setTitle("组织详情");
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		orgId = (Long)arg.get("orgId");
		effDate = (Date)arg.get("effDate");
		expDate = (Date)arg.get("expDate");
		parmsMap.put("orgId", orgId);
		parmsMap.put("effDate", effDate);
		parmsMap.put("expDate", expDate);
		organization = orgHisManager.queryOrgByParams(parmsMap);
		
		if(organization != null){
			PubUtil.fillBeanFromPo(organization, bean);
			organizationContactInfo = organization.getOrganizationContactInfo();
			if(organizationContactInfo!=null){
				PubUtil.fillBeanFromPo(organizationContactInfo, this.bean);
			}
		}
				
	}
	/**
	 * 关闭窗口
	 * @throws Exception
	 */
	public void onOk() throws Exception{
		Events.postEvent(Events.ON_OK, bean.getOrganizationDetailWindow(),
				organization);
		bean.getOrganizationDetailWindow().onClose();
	}


}
