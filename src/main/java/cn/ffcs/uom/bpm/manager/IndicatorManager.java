package cn.ffcs.uom.bpm.manager;

import java.util.List;

import cn.ffcs.uom.bpm.model.QaUnAssmCrt;
import cn.ffcs.uom.bpm.model.QaUnOppExecScript;
import cn.ffcs.uom.common.vo.PageInfo;

public interface IndicatorManager{
    
    public PageInfo qryIndicatorPage(QaUnOppExecScript execScript, int currentPage, int pageSize);
    public List<QaUnOppExecScript> qryIndicator(QaUnOppExecScript execScript);
    
    public void delIndicator(QaUnOppExecScript execScript);
    public void saveIndicator(QaUnOppExecScript execScript);
    
    public List<QaUnAssmCrt> qryQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt);
    public void delQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt);
    public void saveQaUnAssmCrt(QaUnAssmCrt qaUnAssmCrt);
}
