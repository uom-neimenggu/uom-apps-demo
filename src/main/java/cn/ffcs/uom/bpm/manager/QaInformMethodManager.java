package cn.ffcs.uom.bpm.manager;

import java.util.List;

import cn.ffcs.uom.bpm.model.QaInformMethod;
import cn.ffcs.uom.businesssystem.model.BusinessSystem;


public interface QaInformMethodManager{

    public List<QaInformMethod> getInformMethod(QaInformMethod qaInformMethod);
    
    public List<QaInformMethod> getInformMethod(BusinessSystem businessSystem);
    
    public void saveInformMethod(QaInformMethod qaInformMethod);
    
    public void removeInformMehtod(QaInformMethod qaInformMethod);
    
}
