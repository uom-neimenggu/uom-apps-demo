package cn.ffcs.uom.information.action;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import cn.ffcs.raptornuke.plugin.common.util.StringPool;
import cn.ffcs.raptornuke.plugin.common.util.Validator;
import cn.ffcs.raptornuke.plugin.common.web.bean.BaseBean;
import cn.ffcs.raptornuke.plugin.common.web.util.JSPPortletUtil;
import cn.ffcs.raptornuke.portal.kernel.util.ParamUtil;
/**
 * Portlet参数JSPBean
 */
public class PortletPreferencesBean extends BaseBean {

	private static final long serialVersionUID = 4666400574463067672L;

	public PortletPreferencesBean() {

	}
	/**
	 * 获取偏好参数值
	 */
	public String portletPreferencesValue(String key) {

		if (Validator.isNull(key))
			return StringPool.BLANK;
		PortletRequest portletRequest =
			JSPPortletUtil.getPortletRequest(getPageContext());
		if (portletRequest != null)
			return portletRequest.getPreferences().getValue(
				key, StringPool.BLANK);
		else
			return StringPool.BLANK;
	}

	public void doSaveInfoCenterUrl(
		ActionRequest actionRequest, ActionResponse actionResponse) {

		String name = "infoCenterUrl";
		String value = ParamUtil.getString(actionRequest, name);
		PortletPreferences preferences = actionRequest.getPreferences();

		try {
			preferences.setValue(name, value);
			preferences.store();
		}
		catch (Exception e) {
		}
	}

}
