package cn.ffcs.uom.systemconfig.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class NumberSegmentConfigMainBean {
	@Getter
	@Setter
	private Window numberSegmentConfigMainWin;
	@Getter
	@Setter
	private Textbox numberSegment;
	@Getter
	@Setter
	private Button findNumberSegmentButton;
	@Getter
	@Setter
	private Listbox numberSegmentListBox;
	@Getter
	@Setter
	private Paging numberSegmentPaging;
	@Getter
	@Setter
	private Button addNumberSegmentConfigButton;
	@Getter
	@Setter
	private Button editNumberSegmentConfigButton;
	@Getter
	@Setter
	private Button delNumberSegmentConfigButton;
}
