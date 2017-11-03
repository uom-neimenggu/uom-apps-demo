package cn.ffcs.uom.group.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.dao.GroupUomDao;
import cn.ffcs.uom.group.manager.GroupUomManager;
import cn.ffcs.uom.group.model.GroupUomOrg;
import cn.ffcs.uom.group.model.SyncGroupPrv;

@Service("groupUomManager")
@Scope("prototype")
public class GroupUomManagerImpl implements GroupUomManager {
	@Resource
	private GroupUomDao groupUomDao;

	@Override
	public PageInfo queryPageInfoByGroupUomOrg(GroupUomOrg groupUomOrg,
			int currentPage, int pageSize) {
		// TODO Auto-generated method stub
		return groupUomDao.queryPageInfoByGroupUomOrg(groupUomOrg, currentPage,
				pageSize);
	}

	@Override
	public List<GroupUomOrg> queryGroupUomOrgList(GroupUomOrg groupUomOrg) {
		// TODO Auto-generated method stub
		return groupUomDao.queryGroupUomOrgList(groupUomOrg);
	}

	@Override
	public void updateGroupUomOrgIsNull(GroupUomOrg groupUomOrg) {
		// TODO Auto-generated method stub
		groupUomDao.updateGroupUomOrgIsNull(groupUomOrg);
	}

	@Override
	public void addGroupUomOrg(GroupUomOrg groupUomOrg) {

		SyncGroupPrv syncGroupPrv = new SyncGroupPrv();
		syncGroupPrv.setGroupId(groupUomOrg.getId());
		syncGroupPrv.setPrvId(groupUomOrg.getOrgId());
		syncGroupPrv.setResType(groupUomOrg.getResType());
		syncGroupPrv.setDataType(groupUomOrg.getDataType());

		syncGroupPrv.addOnly();
	}

	@Override
	public void updateGroupUomOrg(GroupUomOrg groupUomOrg) {

		SyncGroupPrv syncGroupPrv = new SyncGroupPrv();
		syncGroupPrv.setResId(groupUomOrg.getResId());
		syncGroupPrv.setGroupId(groupUomOrg.getId());
		syncGroupPrv.setPrvId(groupUomOrg.getOrgId());
		syncGroupPrv.setResType(groupUomOrg.getResType());
		syncGroupPrv.setDataType(groupUomOrg.getDataType());
		syncGroupPrv.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
		syncGroupPrv.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));

		syncGroupPrv.updateOnly();
	}

	@Override
	public void removeGroupUomOrg(GroupUomOrg groupUomOrg) {

		SyncGroupPrv syncGroupPrv = new SyncGroupPrv();
		syncGroupPrv.setResId(groupUomOrg.getResId());
		syncGroupPrv.setGroupId(groupUomOrg.getId());
		syncGroupPrv.setPrvId(groupUomOrg.getOrgId());
		syncGroupPrv.setResType(groupUomOrg.getResType());
		syncGroupPrv.setDataType(groupUomOrg.getDataType());
		syncGroupPrv.setExpDate(DateUtil.getNewDate());

		syncGroupPrv.removeOnly();
	}

}
