package cn.ffcs.uom.organization.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.organization.action.bean.OrganizationImportResultBean;
import cn.ffcs.uom.staff.model.ImportInfo;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2011
 * @author Wong
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2013-5-23
 * @功能说明：
 *
 */
@Controller
@Scope("prototype")
public class OrganizationImportResultComposer extends BasePortletComposer {

    private static final long serialVersionUID = 1L;
    
    private OrganizationImportResultBean bean = new OrganizationImportResultBean();
    
    List<ImportInfo> infoList = new ArrayList<ImportInfo>();
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Components.wireVariables(comp, bean);
    }
    
    /**
     * window初始化.
     * @throws Exception
     *             异常
     */
    public void onCreate$organizationImportResultWindow() throws Exception {
        bindBean();
    }
    
    /**
     * 页面初始化
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void bindBean() throws Exception {
        bean.getOrganizationImportResultWindow().setTitle("导入成功"); 
        StringBuffer sb = new StringBuffer();
        List<List<Map<String,Object>>> resultList = (List<List<Map<String,Object>>>) arg.get("resultList");
    	for(List<Map<String,Object>> list : resultList){
    		if(list!=null)
    		for(Map<String,Object> map : list){
    			if(map!=null){
	    			Set<Entry<String, Object>> set = map.entrySet();
	    			for (Iterator<Entry<String, Object>> it = set.iterator(); it.hasNext();) {
			            Map.Entry<String,Object> entry = (Map.Entry<String,Object>) it.next();
			            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\t");
	    			}
	    			sb.append("\r\n\r\n");
    			}
    		}
    	}
    	this.bean.getOrganizationImportInfo().setReadonly(true);
    	this.bean.getOrganizationImportInfo().setValue(sb.toString());
    }
    
}
