/**
 * 
 */
package cn.ffcs.uom.common.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import cn.ffcs.raptornuke.plugin.common.json.jackson.JsonUtil;
import cn.ffcs.raptornuke.portal.PortalException;
import cn.ffcs.raptornuke.portal.SystemException;
import cn.ffcs.raptornuke.portal.model.Organization;
import cn.ffcs.raptornuke.portal.model.OrganizationConstants;
import cn.ffcs.raptornuke.portal.service.OrganizationLocalServiceUtil;
import cn.ffcs.uom.common.util.AjaxUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.vo.OrgTreeNodeVo;

/**
 * @author 曾臻
 * @date 2012-12-19
 * 
 */
public class OrgTreeAction extends HttpServlet {

	private static final long serialVersionUID = 4899655772759277787L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 组织树缓存，采用惰性加载方式
	 * 1.每个节点包含了自己本身的信息，以及其孩子们的引用(children)，孩子节点不再包含更下级的孩子
	 * 但是有属性openable来表示其是否是目录节点。
	 */
	private Map<Long,OrgTreeNodeVo> buffer=new HashMap<Long,OrgTreeNodeVo>();
	
	
	/**
	 * 给定节点，连接成一棵树并返回
	 * @author 曾臻
	 * @date 2012-12-26
	 * @param ids
	 * @return
	 */
	public Object connectTree(long[] ids){
		try {
			long d=new Date().getTime();
			Set<Long> idset=new HashSet<Long>();
			for(long id:ids)
				idset.add(id);
			List<OrgTreeNodeVo> tree=new ArrayList<OrgTreeNodeVo>();
			for(long id:ids){
				List<Long> path=computePath(id);
				List<OrgTreeNodeVo> cur=tree;
				for(long id2:path){
					nextLevel:{
						for(OrgTreeNodeVo scan:cur){
							if(scan.getId()==id2){
								cur=scan.getChildren();
								break nextLevel;
							}
						}
						//add node
						OrgTreeNodeVo add=queryBufferOrg(id2);
						OrgTreeNodeVo _add;
						_add=(OrgTreeNodeVo)BeanUtils.cloneBean(add);
						_add.setChildren(new ArrayList<OrgTreeNodeVo>());
						_add.setExclusion(!idset.contains(id2));
						cur.add(_add);
						cur=_add.getChildren();
					}
				}
			}
			logger.info("connectTree:"+(new Date().getTime()-d)+"ms");
			String json=JsonUtil.objectArray2Json(tree);
			return AjaxUtil.createReturnValueSuccess(json==null?"[]":json);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}
	public Object queryRootSubNodes() {
		try {
			long rootId = OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID;
			OrgTreeNodeVo o=queryBufferOrg(rootId);
			//String json=JsonUtil.objectArray2Json(list);
			return AjaxUtil.createReturnValueSuccess(o.getChildren());
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}

	public Object querySubNodes(long parentId) {
		try {
			OrgTreeNodeVo o=queryBufferOrg(parentId);
			//String json=JsonUtil.objectArray2Json(list);
			return AjaxUtil.createReturnValueSuccess(o.getChildren());
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}
	
	
	/**
	 * 批量查询组织节点信息
	 * @author 曾臻
	 * @date 2012-12-26
	 * @param nodeIdArr
	 * @return
	 */
	public Object queryNodesBatch(long[] nodeIdArr){
		try {
			List<OrgTreeNodeVo> list=new ArrayList<OrgTreeNodeVo>(); 
			for(long id:nodeIdArr){
				OrgTreeNodeVo vo=queryBufferOrg(id);
				list.add(vo);
			}
			return AjaxUtil.createReturnValueSuccess(list);
		} catch (PortalException e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		} catch (SystemException e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}
	/**
	 * 查询所有后代组织至一个列表，用于添加包括所有下级组织时获取所有组织
	 * @author 曾臻
	 * @date 2012-12-26
	 * @param parentIds
	 * @return
	 */
	public Object listDescendantNodes(long[] parentIds){
		try {
			long d=new Date().getTime();
			List<OrgTreeNodeVo> list=new ArrayList<OrgTreeNodeVo>();
			for(long id:parentIds){
				_listDescendantNodes(id, list);
			}
			
			//remove repeated obj
			Map<Long,OrgTreeNodeVo> map=new HashMap<Long,OrgTreeNodeVo>();
			for(OrgTreeNodeVo vo:list)
				map.put(vo.getId(),vo);
			list.clear();
			list.addAll(map.values());
			logger.info("listDescendantNodes:"+(new Date().getTime()-d)+"ms");
			String json=JsonUtil.objectArray2Json(list);
			return AjaxUtil.createReturnValueSuccess(json==null?"[]":json);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}
	
	public Object listDescendantNodes2(long[] parentIds){
		try {
			List<OrgTreeNodeVo> list=new ArrayList<OrgTreeNodeVo>();
			for(long id:parentIds){
				_listDescendantNodes(id, list);
			}
			
			//remove repeated obj
			Map<Long,OrgTreeNodeVo> map=new HashMap<Long,OrgTreeNodeVo>();
			for(OrgTreeNodeVo vo:list)
				map.put(vo.getId(),vo);
			list.clear();
			list.addAll(map.values());
			return AjaxUtil.createReturnValueSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxUtil.createReturnValueError();
		}
	}
	private void _listDescendantNodes(long parentId,List<OrgTreeNodeVo> list) throws SystemException, PortalException{
		OrgTreeNodeVo o=queryBufferOrg(parentId);
		list.add(o);
		for(OrgTreeNodeVo vo:o.getChildren())
			_listDescendantNodes(vo.getId(), list);
	}

	/**
	 * 清除缓存，当组织同步后需要调用本方法
	 * @author 曾臻
	 * @date 2012-12-26
	 */
	public void clearBuffer(){
		buffer.clear();
	}
	/**
	 * 缓存读取下级组织
	 * @author 曾臻
	 * @date 2012-12-26
	 * @param parentId
	 * @return
	 * @throws SystemException 
	 * @throws PortalException 
	 */
	private OrgTreeNodeVo queryBufferOrg(long parentId) throws SystemException, PortalException{
		//负数节点作特殊处理
		if(parentId==-1){
			OrgTreeNodeVo org=new OrgTreeNodeVo();
			org.setId(parentId);
			org.setLabel("默认");
			org.setOpenable(false);
			org.setChildren(new ArrayList<OrgTreeNodeVo>());
			return org;
		}
		
		//正常节点
		OrgTreeNodeVo org=buffer.get(parentId);
		if(org==null){
			if(parentId== OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID){
				org=new OrgTreeNodeVo();
				org.setId(0);
				org.setLabel("组织树");
				org.setOpenable(true);
			}else{
				Organization org2=OrganizationLocalServiceUtil.getOrganization(parentId);
				org=convert(org2);
			}
			
			long companyId = PlatformUtil.getCompanyId();
			int total = OrganizationLocalServiceUtil.searchCount(companyId, parentId, "", null, null,
					null, null, true);
			List<Organization> results = OrganizationLocalServiceUtil.search(companyId, parentId, "",
					null, null, null, null, true, 0, total);
			
			List<OrgTreeNodeVo> kids=new ArrayList<OrgTreeNodeVo>();
			org.setChildren(kids);
			for(Organization o:results){
				OrgTreeNodeVo kid=convert(o);
				kids.add(kid);
			}
			buffer.put(parentId,org);
		}
		return org;
	}
	/**
	 * 计算给定节点的路径（从根节点开始到该节点为止）
	 * 注意：给定节点路径上的节点必须已被缓存
	 * @author 曾臻
	 * @date 2012-12-26
	 * @param id
	 * @return
	 * @throws PortalException 
	 * @throws SystemException 
	 */
	private List<Long> computePath(Long id) throws SystemException, PortalException{
		List<Long> result=new ArrayList<Long>();
		do{
			result.add(id);
			id=queryBufferOrg(id).getParentId();
		}while(id!=OrganizationConstants.DEFAULT_PARENT_ORGANIZATION_ID);
		Collections.reverse(result);
		return result;
	}
	private OrgTreeNodeVo convert(Organization o) throws SystemException{
		long companyId = PlatformUtil.getCompanyId();
		int childrenCount = OrganizationLocalServiceUtil.searchCount(companyId,
				o.getOrganizationId(), "", null, null, null, null, true);
		
		OrgTreeNodeVo kid=new OrgTreeNodeVo();
		kid.setId(o.getOrganizationId());
		kid.setLabel(o.getName());
		kid.setOpenable(childrenCount>0);
		kid.setParentId(o.getParentOrganizationId());
		return kid;
	}
}
