package cn.ffcs.uom.bpm.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.bpm.comp.QaUnOppExecScriptTreeBandbox;

/**
 * <p> BPM主页面实体Bean</p>
 * @ClassName: BpmMainBean 
 * @Description: TODO 
 * @author Comsys-WCNGS 
 * @date 2016-5-12 上午9:43:39 * *
 */
public class BpmMainBean {
	
	@Getter
	@Setter
	private Window bpmMainWin;
    @Getter
    @Setter
    private Panel bpmPanel;
    @Getter
    @Setter
    private Panel hisBpmPanel;	
	@Getter
	@Setter
	private Tabbox tabBox;
	//选中的元素事件
	@Getter
	@Setter
	private Tab selectTab;
	
    @Getter
    @Setter
    private Listbox bpmInfoListbox;
    @Getter
    @Setter
    private Paging bpmInfoListPaging;
    
    @Getter
    @Setter
    private QaUnOppExecScriptTreeBandbox qaUnOppExecScriptTreeBandbox;
    
    @Getter
    @Setter
    private Listbox qaUnOppExecScriptListbox;
    
    @Getter
    @Setter
    private Listbox hisQaUnOppExecScriptListbox;
    
    @Getter
    @Setter
    private QaUnOppExecScriptTreeBandbox hisQaUnOppExecScriptTreeBandbox;
    
    @Getter
    @Setter
    private Listbox hisBpmInfoListbox;
    @Getter
    @Setter
    private Paging hisBpmInfoListPaging;
    @Getter
    @Setter
    private Datebox beginDate;
    @Getter
    @Setter  
    private Datebox endDate;
}
