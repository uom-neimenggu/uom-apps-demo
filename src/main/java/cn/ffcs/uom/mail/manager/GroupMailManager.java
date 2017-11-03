package cn.ffcs.uom.mail.manager;

import cn.ffcs.uom.organization.model.Organization;
import cn.ffcs.uom.party.model.Party;

public interface GroupMailManager {

	public String groupMamilTimeStamp(String msgId);

	public String groupMailPackageInfo(String bizId, Party party,
			Organization organization);

	public String groupMailSendInfo(String bizId, String msgId, String json);

}
