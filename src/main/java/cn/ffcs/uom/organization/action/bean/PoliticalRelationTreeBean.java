package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

/**
 * 人力比较Bean.
 * 
 * @author faq
 **/
public class PoliticalRelationTreeBean {

	@Getter
	@Setter
	private Window politicalRelationTreeMainWin;
	@Getter
	@Setter
	private Tree politicalRelationTree;
	@Getter
	@Setter
	private Toolbar politicalRelationTreeToolbar;
	@Getter
	@Setter
	private Button okButton;
	@Getter
	@Setter
	private Button cancelButton;
	
}
