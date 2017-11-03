package cn.ffcs.uac.staff.dao;

import java.util.List;

import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uom.common.dao.BaseDao;

public interface UacContactDao extends BaseDao{
	public void addUacContact(UacContact uacContact);

	public void delUacContact(UacContact uacContact);

	public void updateUacContact(UacContact uacContact);
	
	public UacContact queryUacContact(UacContact uacContact);

	public List<UacContact> queryUacContactList(UacContact uacContact);
}
