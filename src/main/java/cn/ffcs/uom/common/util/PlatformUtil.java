/**
 * 
 */
package cn.ffcs.uom.common.util;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.ffcs.raptornuke.plugin.common.zk.util.ZkUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.kernel.util.JavaConstants;
import cn.ffcs.raptornuke.portal.model.Role;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.raptornuke.portal.security.permission.PermissionChecker;
import cn.ffcs.raptornuke.portal.service.permission.PortletPermissionUtil;
import cn.ffcs.raptornuke.portal.theme.ThemeDisplay;
import cn.ffcs.raptornuke.portal.util.PortalUtil;
import cn.ffcs.uom.common.key.WebKeys;

/**
 * 门户平台相关工具
 * 
 * @author 曾臻
 * @date 2012-11-14
 * 
 */
public class PlatformUtil {
	public static PortletRequest getPortletRequest() {
		HttpServletRequest req = AjaxUtil.getHttpRequest();
		PortletRequest pr = (PortletRequest) req
				.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
		return pr;
	}

	public static long getCompanyId() {
		HttpServletRequest req = AjaxUtil.getHttpRequest();
		long companyId = PortalUtil.getCompanyId(req);
		return companyId;
	}

	/**
	 * 判断是否有权限
	 * 
	 * @author 曾臻
	 * @date 2013-6-18
	 * @return
	 * @throws SystemException 
	 * @throws PortalException 
	 */
	public static boolean checkPermission(ThemeDisplay themeDisplay,String portletId, String action) throws PortalException, SystemException {
		PermissionChecker permissionChecker = themeDisplay
				.getPermissionChecker();
		long plid = themeDisplay.getPlid();
		return PortletPermissionUtil.contains(permissionChecker, plid,
				portletId, action);
	}
	
	public static boolean checkPermissionDialog(IPortletInfoProvider portletInfoProvider,String action) throws PortalException, SystemException{
		if (!PlatformUtil.checkPermission(
				portletInfoProvider.getThemeDisplay(),
				portletInfoProvider.getPortletId(), action)) {
			ZkUtil.showExclamation("您没有权限进行此操作！", "警告");
			return false;
		}
		return true;
	}

	public static boolean checkHasPermission(
			IPortletInfoProvider portletInfoProvider, String action)
			throws PortalException, SystemException {
		if (!PlatformUtil.checkPermission(
				portletInfoProvider.getThemeDisplay(), portletInfoProvider
						.getPortletId(), action)) {
			return false;
		}
		return true;
	}
	/**
	 * 获取当前用户
	 * 
	 * @return
	 */
	public static User getCurrentUser() {
		try {
			HttpServletRequest req = (HttpServletRequest) ZkUtil
					.getNativeRequest();
			if (req != null) {
				HttpSession session = req.getSession();
				User user = (User) session
						.getAttribute(WebKeys.RAPTORNUKE_SHARED_USER);
				return user;
			}
		} catch (Exception e) {
			//未登录会抛异常
		}
		return null;
	}
	
	/**
	 * 获取当前用户标识
	 * 
	 * @return
	 */
	public static Long getCurrentUserId() {
		User user = getCurrentUser();
		if (user != null) {
			return user.getUserId();
		}
		return null;
	}
	/**
	 * 是否是管理员
	 * @return
	 */
	public static boolean isAdmin() {
		User user = getCurrentUser();
		if (user != null) {
			List<Role> roleList = user.getRoles();
			if(roleList != null && !roleList.isEmpty()) {
				int count = 0;
				for(Role role : roleList) {
					if("Administrator".equals(role.getName()) || "Power User".equals(role.getName())) {
						count++;
					}
				}
				
				if(count == 2) {
					return true;
				}
			}
		}
		return false;
	}
}
