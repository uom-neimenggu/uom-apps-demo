package cn.ffcs.uac.staff.dao;

import java.util.List;

import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uom.common.dao.BaseDao;

public interface UacCertDao extends BaseDao{
	public void addUacCert(UacCert uacCert);

	public void delUacCert(UacCert uacCert);

	public void updateUacCert(UacCert uacCert);

	public UacCert queryUacCert(UacCert uacCert);

	public List<UacCert> queryUacCertList(UacCert uacCert);
}
