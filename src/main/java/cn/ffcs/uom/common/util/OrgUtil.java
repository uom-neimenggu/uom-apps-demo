package cn.ffcs.uom.common.util;


import java.util.ArrayList;
import java.util.List;

import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Group;
import cn.ffcs.raptornuke.portal.model.Organization;
import cn.ffcs.raptornuke.portal.model.User;
import cn.ffcs.raptornuke.portal.service.GroupLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.OrganizationLocalServiceUtil;
import cn.ffcs.raptornuke.portal.service.UserLocalServiceUtil;
import cn.ffcs.uom.common.key.StaffOrgKeys;
import cn.ffcs.uom.common.vo.OrgVo;
import cn.ffcs.uom.common.vo.UserVo;

/**
 * 用户组织相关接口
 * @author 曾臻
 * @date 2012-11-8
 *
 */
public class OrgUtil {
	/**
	 * 获取当前用户
	 * @author 曾臻
	 * @date 2012-11-8
	 * @return
	 */
	public static UserVo getCurrentUser(){
		User user=null;
		user=(User)AjaxUtil.getSessionAttribute(StaffOrgKeys.CURRENT_USER);
		UserVo u=new UserVo();
		u.setUserId(user.getUserId());
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setScreenName(user.getScreenName());
		u.setLoginIp(user.getLoginIP());
		u.setEmail(user.getEmailAddress());
		u.setUserUuId(user.getUuid());
		Group g = user.getGroup();
		u.setGroupName(g.getName());
		u.setGroupId(g.getGroupId());
		u.setUuid(user.getUuid());
		
		return u;
	}
	/**
	 * 获取当前用户关联组织标识
	 * @author 曾臻
	 * @date 2013-3-28
	 * @param userUuid
	 * @return
	 * @throws PortalException
	 * @throws SystemException
	 */
	public static List<Long> getCurrentUserOrgIds() throws PortalException, SystemException{
		User user=(User)AjaxUtil.getSessionAttribute(StaffOrgKeys.CURRENT_USER);
		if(null==user)
			return null;
		
		List<Long> result=new ArrayList<Long>();
		for(Group group:user.getMyPlaces()){
			if(group.isGuest())
				result.add(-1l);
			else if(group.isOrganization())
				result.add(group.getClassPK());
		}
		return result;
	}
	/**
	 * 获取当前门户所对应的组织
	 * @author 曾臻
	 * @date 2012-11-8
	 * @return
	 * @throws SystemException 
	 * @throws PortalException 
	 * @throws NumberFormatException 
	 */
	public static OrgVo getCurrentOrg() throws NumberFormatException, PortalException, SystemException{
		OrgVo orgVo=new OrgVo();
		long orgId;
		String groupId=String.valueOf(AjaxUtil.getSessionAttribute(StaffOrgKeys.CURRENT_GROUP_ID));
		Group group=GroupLocalServiceUtil.getGroup(Long.valueOf(groupId));
		if(group.isGuest())
			orgVo.setOrgId(-1l);
		else if(group.isOrganization()){
			orgId=group.getClassPK();
			Organization o=OrganizationLocalServiceUtil.getOrganization(orgId);
			orgVo.setOrgId(o.getOrganizationId());
			orgVo.setName(o.getName());
			orgVo.setParentOrgId(o.getParentOrganizationId());
		}
		return orgVo;
	}
	/**
	 * 获取当前社区类型（公开、私有）
	 * @author 曾臻
	 * @date 2013-1-5
	 * @return
	 */
	public static String getCurrentGroupType(){
		String groupType=String.valueOf(AjaxUtil.getSessionAttribute(StaffOrgKeys.CURRENT_GROUP_TYPE));
		return groupType;
	}
}
