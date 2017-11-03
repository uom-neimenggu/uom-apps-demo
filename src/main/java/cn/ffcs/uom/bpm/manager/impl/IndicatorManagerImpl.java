package cn.ffcs.uom.bpm.manager.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.ffcs.uom.bpm.dao.IndicatorDao;
import cn.ffcs.uom.bpm.manager.IndicatorManager;
import cn.ffcs.uom.bpm.model.QaUnAssmCrt;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.common.vo.PageInfo;

@Repository("indicatorManager")
public class IndicatorManagerImpl implements IndicatorManager {
    
    @Autowired
    private IndicatorDao indicatorDao;
    
    @Override
    public PageInfo qryIndicatorPage(QaUnOppExecScript execScript, int currentPage, int pageSize) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select * from QA_UN_OPP_EXEC_SCRIPT where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StrUtil.isNullOrEmpty(execScript.getExecName())) {
            sql.append("and EXEC_NAME like ?");
            params.add("%" + StringEscapeUtils.escapeSql(execScript.getExecName()) + "%");
        }
        if (!StrUtil.isNullOrEmpty(execScript.getStatusCd())) {
            sql.append("and STATUS_CD = ?");
            params.add(execScript.getStatusCd());
        }
        return indicatorDao.jdbcFindPageInfo(sql.toString(), params, currentPage, pageSize,
            QaUnOppExecScript.class);
    }

    @Override
    public void delIndicator(QaUnOppExecScript execScript) {
        // TODO Auto-generated method stub
        indicatorDao.removeObject(QaUnOppExecScript.class, execScript.getExecSctIdenti());
    }

    @Override
    public List<QaUnAssmCrt> qryQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select * from QA_UN_ASSM_CRT where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StrUtil.isNullOrEmpty(qaUnAssmCrt.getExecSctIdenti())) {
            sql.append("and EXEC_SCT_IDENTI = ?");
            params.add(qaUnAssmCrt.getExecSctIdenti());
        }
        if (!StrUtil.isNullOrEmpty(qaUnAssmCrt.getStatusCd())) {
            sql.append("and STATUS_CD = ?");
            params.add(qaUnAssmCrt.getStatusCd());
        }
        return indicatorDao.jdbcFindList(sql.toString(), params, QaUnAssmCrt.class);
    }

    @Override
    public List<QaUnOppExecScript> qryIndicator(QaUnOppExecScript execScript) {
        // TODO Auto-generated method stub
        StringBuffer sql = new StringBuffer("select * from QA_UN_OPP_EXEC_SCRIPT where 1=1 ");
        List<Object> params = new ArrayList<Object>();
        if (!StrUtil.isNullOrEmpty(execScript.getExecName())) {
            sql.append("and EXEC_NAME like ?");
            params.add("%" + StringEscapeUtils.escapeSql(execScript.getExecName())+ "%");
        }
        if (!StrUtil.isNullOrEmpty(execScript.getStatusCd())) {
            sql.append("and STATUS_CD = ?");
            params.add(execScript.getStatusCd());
        }
        return indicatorDao.jdbcFindList(sql.toString(), params, QaUnOppExecScript.class);
    }

    @Override
    public void saveIndicator(QaUnOppExecScript execScript) {
        // TODO Auto-generated method stub
        indicatorDao.saveObject(execScript);
    }

    @Override
    public void delQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt) {
        // TODO Auto-generated method stub
        indicatorDao.removeObject(QaUnAssmCrt.class, qaUnAssmCrt.getAssmCrtId());
    }

    @Override
    public void saveQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt) {
        // TODO Auto-generated method stub
        indicatorDao.saveObject(qaUnAssmCrt);
    }
    
}
