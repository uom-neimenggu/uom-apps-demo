package cn.ffcs.uom.bpm.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.bpm.dao.BmpSystemConfigDao;
import cn.ffcs.uom.bpm.manager.BmpSystemConfigManager;
import cn.ffcs.uom.bpm.model.QaUnPrincipal;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.StrUtil;

@Service("bmpSystemConfigManager")
public class BmpSystemConfigManagerImpl implements BmpSystemConfigManager {

	@Autowired
	private BmpSystemConfigDao bmpSystemConfigDao;
	
	@Override
	public List<QaUnPrincipal> qryPrincipal(BusinessSystem businessSystem) {
		// TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select a.* from qa_un_principal a,qa_un_buss_principal b where a.principal_id=b.principal_id and b.status_cd=? and a.status_cd=? and b.system_code=? ");
        List<Object> params = new ArrayList<Object>();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(businessSystem.getSystemCode());
		return bmpSystemConfigDao.jdbcFindList(sql.toString(), params, QaUnPrincipal.class);
	}

	@Override
	public void delPrincipal(BusinessSystem businessSystem, QaUnPrincipal principal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<QaUnPrincipal> qryPrincipal(QaUnPrincipal principal) {
		// TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select * from qa_un_principal where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StrUtil.isNullOrEmpty(principal.getPrincipalName())) {
            sql.append("and PRINCIPAL_NAME=?");
            params.add(principal.getPrincipalName());
        }
        if (!StrUtil.isNullOrEmpty(principal.getCellNum())) {
            sql.append("and CELL_NUM = ?");
            params.add(principal.getCellNum());
        }
        if (!StrUtil.isNullOrEmpty(principal.getStatusCd())) {
            sql.append("AND STATUS_CD=?");
            params.add(principal.getStatusCd());
        }
		return bmpSystemConfigDao.jdbcFindList(sql.toString(), params, QaUnPrincipal.class);

	}

	@Override
	public void delPrincipal(QaUnPrincipal principal) {
		// TODO Auto-generated method stub
		bmpSystemConfigDao.removeObject(QaUnPrincipal.class,principal.getPrincipalId());
	}

	@Override
	public void savePrincipal(QaUnPrincipal qaUnPrincipal) {
		// TODO Auto-generated method stub
		bmpSystemConfigDao.saveObject(qaUnPrincipal);
	}
	
}
