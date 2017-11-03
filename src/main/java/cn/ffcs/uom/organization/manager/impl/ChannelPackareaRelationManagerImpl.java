package cn.ffcs.uom.organization.manager.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.dao.ChannelPackareaRelationDao;
import cn.ffcs.uom.organization.dao.OrganizationTranDao;
import cn.ffcs.uom.organization.manager.ChannelPackareaRelationManager;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.organization.vo.ChannelPackareaRelationVo;

@Service("channelPackareaRelationManager")
@Scope("prototype")
public class ChannelPackareaRelationManagerImpl implements
		ChannelPackareaRelationManager {

	@Resource(name = "channelPackareaRelationDao")
	private ChannelPackareaRelationDao channelPackareaRelationDao;

	@Resource(name = "organizationTranDao")
	private OrganizationTranDao organizationTranDao;

	@Override
	public PageInfo queryPageInfoByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo,
			int currentPage, int pageSize) {
		return channelPackareaRelationDao
				.queryPageInfoByChannelPackareaRelationVo(
						channelPackareaRelationVo, currentPage, pageSize);
	}

	@Override
	public List<Map<String, Object>> queryListByChannelPackareaRelationVo(
			ChannelPackareaRelationVo channelPackareaRelationVo) {
		return channelPackareaRelationDao
				.queryListByChannelPackareaRelationVo(channelPackareaRelationVo);
	}

	@Override
	public void removeUomGroupOrgTran(
			ChannelPackareaRelationVo channelPackareaRelationVo) {
		// TODO Auto-generated method stub
		if (channelPackareaRelationVo != null) {
			UomGroupOrgTran uomGroupOrgTran = (UomGroupOrgTran) organizationTranDao
					.getObject(UomGroupOrgTran.class,
							channelPackareaRelationVo.getId());
			uomGroupOrgTran.remove();
		}

	}

	@Override
	public UomGroupOrgTran queryUomGroupOrgTranById(Long id) {
		if (id == null)
			return null;

		return (UomGroupOrgTran) organizationTranDao.getObject(
				UomGroupOrgTran.class, id);
	}
}
