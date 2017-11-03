package cn.ffcs.uom.organization.manager.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.OperateLog;
import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.PlatformUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;
import cn.ffcs.uom.organization.constants.OrganizationTranConstant;
import cn.ffcs.uom.organization.constants.ProcessStatusConstant;
import cn.ffcs.uom.organization.dao.OrganizationTranDao;
import cn.ffcs.uom.organization.manager.OrganizationTranManager;
import cn.ffcs.uom.organization.model.OrganizationTran;
import cn.ffcs.uom.organization.model.UomGroupOrgTran;
import cn.ffcs.uom.systemconfig.model.SysClass;

@Service("organizationTranManager")
@Scope("prototype")
public class OrganizationTranManagerImpl implements OrganizationTranManager {
	@Resource
	private OrganizationTranDao organizationTranDao;

	@Override
	public PageInfo queryPageInfoByOrganizationTran(
			OrganizationTran organizationTran, int currentPage, int pageSize) {

		return organizationTranDao.queryPageInfoByOrganizationTran(
				organizationTran, currentPage, pageSize);
	}

	@Override
	public List<OrganizationTran> queryOrganizationTranList(
			OrganizationTran organizationTran, String pattern) {

		return organizationTranDao.queryOrganizationTranList(organizationTran,
				pattern);
	}

	@Override
	public void addOrganizationTran(OrganizationTran organizationTran) {
		organizationTran.add();
	}

	@Override
	public void updateOrganizationTran(OrganizationTran organizationTran) {
		organizationTran.update();
	}

	@Override
	public void removeOrganizationTran(OrganizationTran organizationTran) {
		organizationTran.remove();
	}

	@Override
	public PageInfo queryPageInfoByUomGroupOrgTran(
			UomGroupOrgTran uomGroupOrgTran, int currentPage, int pageSize) {

		return organizationTranDao.queryPageInfoByUomGroupOrgTran(
				uomGroupOrgTran, currentPage, pageSize);
	}

	@Override
	public List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran, String pattern) {

		return organizationTranDao.queryUomGroupOrgTranList(uomGroupOrgTran,
				pattern);
	}

	@Override
	public void addUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran) {
		uomGroupOrgTran.add();
	}

	@Override
	public void updateUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran) {
		uomGroupOrgTran.update();
	}

	@Override
	public void removeUomGroupOrgTran(UomGroupOrgTran uomGroupOrgTran) {
		uomGroupOrgTran.remove();
	}

    @Override
    public void addChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran) {
        Date nowDate = DateUtil.getNewDate();
        uomGroupOrgTran.setCreateDate(nowDate);
        uomGroupOrgTran.setEffDate(nowDate);
        uomGroupOrgTran.setExpDate(DateUtil.str2date("20991231", "yyyyMMdd"));
        uomGroupOrgTran.setStatusDate(nowDate);
        uomGroupOrgTran.setUpdateDate(nowDate);
//        uomGroupOrgTran.setStatusCd(BaseUnitConstants.ENTT_STATE_ACTIVE);
        Long staffId = PlatformUtil.getCurrentUserId();
        if (staffId != null) {
            uomGroupOrgTran.setCreateStaff(staffId);
        }
        //新增的时候，如果没有设置状态，那么这里就设置默认的有效
        if(uomGroupOrgTran.getStatusCd() == null) 
        {
            uomGroupOrgTran.setStatusCd(ProcessStatusConstant.ENTT_STATE_ACTIVE);
        }
        //设置状态添加进入数据库
        organizationTranDao.addObject(uomGroupOrgTran);
    }

    @Override
    public void updateChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran) {
    	uomGroupOrgTran.update();
    }

    @Override
    public void removeChannelPackAreaTran(UomGroupOrgTran uomGroupOrgTran) {
        // TODO Auto-generated method stub
        //删除数据吧数据设置为失效
        uomGroupOrgTran.remove();
    }
    
    public List<UomGroupOrgTran> queryChannelPackAreaTranList(UomGroupOrgTran uomGroupOrgTran)
    {
        //设置状态为包区网点的关系类型
        if(uomGroupOrgTran.getTranRelaType() == null)
        {
            //OrganizationTranConstant.CHANNEL_PACKAREA_RELATION
            uomGroupOrgTran.setTranRelaType(OrganizationTranConstant.CHANNEL_PACKAREA_RELATION);
        }
        
        return organizationTranDao.queryUomGroupOrgTranNotStatusCd1100List(uomGroupOrgTran);
    }

	@Override
	public List<UomGroupOrgTran> queryUomGroupOrgTranStoreAreaList(
			UomGroupOrgTran uomGroupOrgTran) {
		//设置状态为包区网点的关系类型
        if(uomGroupOrgTran.getTranRelaType() == null)
        {
            //OrganizationTranConstant.CHANNEL_PACKAREA_RELATION
            uomGroupOrgTran.setTranRelaType(OrganizationTranConstant.CHANNEL_PACKAREA_RELATION);
        }
        
		return organizationTranDao.queryUomGroupOrgTranStoreAreaNot1100List(uomGroupOrgTran);
	}
	
	@Override
	public List<UomGroupOrgTran> queryUomGroupOrgTranList(
			UomGroupOrgTran uomGroupOrgTran) {
		//设置状态为包区网点的关系类型
        if(uomGroupOrgTran.getTranRelaType() == null)
        {
            //OrganizationTranConstant.CHANNEL_PACKAREA_RELATION
            uomGroupOrgTran.setTranRelaType(OrganizationTranConstant.CHANNEL_PACKAREA_RELATION);
        }
        
		return organizationTranDao.queryUomGroupOrgTranList(uomGroupOrgTran);
	}
    
}
