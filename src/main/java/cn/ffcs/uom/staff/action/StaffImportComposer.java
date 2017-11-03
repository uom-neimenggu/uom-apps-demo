package cn.ffcs.uom.staff.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.ListModel;

import cn.ffcs.raptornuke.plugin.common.zk.ctrl.BasePortletComposer;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.staff.action.bean.StaffImportBean;
import cn.ffcs.uom.staff.constants.SffOrPtyCtants;
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
public class StaffImportComposer extends BasePortletComposer {

    private static final long serialVersionUID = 1L;
    
    private StaffImportBean bean = new StaffImportBean();
    
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
    public void onCreate$staffImportWindow() throws Exception {
        bindBean();
    }
    
    /**
     * 页面初始化
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void bindBean() throws Exception {
        String opType = StrUtil.strnull(arg.get("opType"));        
        if(SffOrPtyCtants.VIEW.equals(opType)){            
            bean.getStaffImportWindow().setTitle("导入结果信息");            
            List<String> iList = (List<String>) arg.get("infoList");
            if(null != iList && iList.size() > 0){
            	int count = iList.size();
            	for(int i = 0; i < count; i++){
            		ImportInfo iInfo = new ImportInfo();
            		iInfo.setInfoId((i+1)+"");
            		iInfo.setInfoContent(iList.get(i));
            		infoList.add(iInfo);
            	}
            }
            ListModel dataList = new BindingListModelList(infoList, true);
            this.bean.getInfoListbox().setModel(dataList);
            this.bean.getInfoListbox().setSizedByContent(true);
        }
    }
    
}
