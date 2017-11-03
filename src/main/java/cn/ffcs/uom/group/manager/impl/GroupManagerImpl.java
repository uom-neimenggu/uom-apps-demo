package cn.ffcs.uom.group.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.group.dao.GroupDao;
import cn.ffcs.uom.group.manager.GroupManager;
import cn.ffcs.uom.group.model.Group;
import cn.ffcs.uom.staff.manager.StaffManager;
import cn.ffcs.uom.staff.model.Staff;
import cn.ffcs.uom.staff.model.StaffAccount;

@Service("groupManager")
@Scope("prototype")
public class GroupManagerImpl implements GroupManager {
	@Resource
	private GroupDao groupDao;
	@Resource
	private StaffManager staffManager;

	@Override
	public PageInfo queryPageInfoByGroup(Group group, int currentPage,
			int pageSize) {
		// TODO Auto-generated method stub
		return groupDao.queryPageInfoByGroup(group, currentPage, pageSize);
	}

	@Override
	public List<Group> queryGroupList(Group group) {
		// TODO Auto-generated method stub
		return groupDao.queryGroupList(group);
	}

	@Override
	public void updateGroup(Group group) {
		// TODO Auto-generated method stub
		groupDao.updateGroup(group);
	}

	@Override
	public void updateGroupIsNull(Group group) {
		// TODO Auto-generated method stub
		groupDao.updateGroupIsNull(group);
	}

	@Override
	public void updateGroupProofread(Group group, Staff staff) {

		if (!StrUtil.isNullOrEmpty(staff) && !StrUtil.isNullOrEmpty(group)) {

			List<Group> oldGroupList = new ArrayList<Group>();

			List<StaffAccount> oldStaffAccountList = new ArrayList<StaffAccount>();

			List<StaffAccount> newStaffAccountList = new ArrayList<StaffAccount>();

			Group queryGroup = new Group();

			StaffAccount queryStaffAccount = new StaffAccount();

			if (staff.getStaffId() != null) {
				queryGroup.setReserv_Col2(staff.getStaffId().toString());
				oldGroupList = this.queryGroupList(queryGroup);
				if (oldGroupList != null && oldGroupList.size() > 0) {
					this.updateGroupIsNull(queryGroup);
				}

				group.setReserv_Col2(staff.getStaffId().toString());
				this.updateGroup(group);

			}

			if (!StrUtil.isNullOrEmpty(group.getLoginName())) {

				queryStaffAccount.setGuid(group.getLoginName());

				oldStaffAccountList = staffManager
						.queryStaffAccountList(queryStaffAccount);

				if (oldStaffAccountList != null
						&& oldStaffAccountList.size() > 0) {
					for (StaffAccount oldStaffAccount : oldStaffAccountList) {
						oldStaffAccount.setGuid("");
						oldStaffAccount.update();
					}
				}

				queryStaffAccount = new StaffAccount();

				queryStaffAccount.setStaffId(staff.getStaffId());

				newStaffAccountList = staffManager
						.queryStaffAccountList(queryStaffAccount);
				if (newStaffAccountList != null
						&& newStaffAccountList.size() > 0) {
					for (StaffAccount newStaffAccount : newStaffAccountList) {
						newStaffAccount.setGuid(group.getLoginName());
						newStaffAccount.update();
					}
				}

			}
		}

	}
}
