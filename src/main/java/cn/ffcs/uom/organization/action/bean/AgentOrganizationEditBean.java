package cn.ffcs.uom.organization.action.bean;

import lombok.Getter;
import lombok.Setter;

import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import cn.ffcs.uom.organization.action.OrganizationInfoExt;
import cn.ffcs.uom.party.component.PartyInfoExt;

public class AgentOrganizationEditBean {
	@Getter
	@Setter
	private Window agentOrganizationEditWindow;
	@Getter
	@Setter
	private Listbox addType;
	@Getter
	@Setter
	private OrganizationInfoExt organizationInfoExt;
	@Getter
	@Setter
	private PartyInfoExt partyInfoExt;
	@Getter
	@Setter
	private Groupbox addTypeGorupbox;
}
