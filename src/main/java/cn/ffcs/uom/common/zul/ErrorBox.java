package cn.ffcs.uom.common.zul;

import org.zkoss.zk.au.out.AuWrongValue;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author wuyx
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2011-7-20
 * @功能说明：
 * 
 */
public class ErrorBox extends AbstractComponent {
    
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * newInstance.
     * 
     * @return ErrorBox
     * @author wuyx
     * 2011-7-20 wuyx 
     */
    public static ErrorBox newInstance() {
        return new ErrorBox();
    }
    
    /**
     * .
     * 
     * @param c Component
     * @param message String
     * @author wuyx
     * 2011-7-20 wuyx 
     */
    public void showError(Component c, String message) {
        AuWrongValue awv = new AuWrongValue(c, message);
        this.response(awv);
    }
    
}
