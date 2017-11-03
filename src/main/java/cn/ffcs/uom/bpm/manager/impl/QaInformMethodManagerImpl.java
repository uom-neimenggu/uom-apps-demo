package cn.ffcs.uom.bpm.manager.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ffcs.uom.bpm.dao.QaInformMethodDao;
import cn.ffcs.uom.bpm.manager.QaInformMethodManager;
import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.StrUtil;

@Service("qaInformMethodManager")
public class QaInformMethodManagerImpl implements QaInformMethodManager {
    
    @Autowired
    private QaInformMethodDao qaInformMethodDao;
    
    @Override
    public List<QaInformMethod> getInformMethod(QaInformMethod qaInformMethod) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select * from qa_inform_method where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StrUtil.isNullOrEmpty(qaInformMethod.getInformType())) {
            sql.append("and inform_type=?");
            params.add(StringEscapeUtils.escapeSql(qaInformMethod.getInformType()));
        }
        if (!StrUtil.isNullOrEmpty(qaInformMethod.getInformName())) {
            sql.append("and inform_Name like ?");
            params.add("%" + StringEscapeUtils.escapeSql(qaInformMethod.getInformName()) + "%");
        }
        if (!StrUtil.isNullOrEmpty(qaInformMethod.getStatusCd())) {
            sql.append("AND STATUS_CD=?");
            params.add(qaInformMethod.getStatusCd());
        }
        return this.qaInformMethodDao.jdbcFindList(sql.toString(), params, QaInformMethod.class);
    }
    
	@Override
	public List<QaInformMethod> getInformMethod(BusinessSystem businessSystem) {
		// TODO Auto-generated method stub
		String sql = "select a.* from qa_inform_method a,qa_systtem_inform_rel b where a.status_cd=? and b.status_cd=? and a.inform_method_id=b.inform_method_id and b.business_system_id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
        params.add(businessSystem.getBusinessSystemId());
        return this.qaInformMethodDao.jdbcFindList(sql.toString(), params, QaInformMethod.class);
	}
	
    @Override
    public void saveInformMethod(QaInformMethod qaInformMethod) {
        // TODO Auto-generated method stub
        Date nowDate = DateUtil.getNewDate();
        qaInformMethod.setCreateDate(nowDate);
        qaInformMethodDao.saveObject(qaInformMethod);
    }

    @Override
    public void removeInformMehtod(QaInformMethod qaInformMethod) {
        // TODO Auto-generated method stub
        qaInformMethodDao.removeObject(QaInformMethod.class, qaInformMethod.getInformMethodId());
    }

}
