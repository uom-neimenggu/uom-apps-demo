package cn.ffcs.uac.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacCertDao;
import cn.ffcs.uac.staff.model.UacCert;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;

@Repository("uacCertDao")
public class UacCertDaoImpl extends BaseDaoImpl implements UacCertDao {
	@Override
	public void addUacCert(UacCert uacCert) {
		uacCert.add();
	}

	@Override
	public void delUacCert(UacCert uacCert) {
		uacCert.remove();
	}

	@Override
	public void updateUacCert(UacCert uacCert) {
		uacCert.update();
	}
	
	@Override
	public UacCert queryUacCert(UacCert uacCert) {
		List<UacCert> uacCertList = queryUacCertList(uacCert);
		if (null == uacCertList || uacCertList.size() == 0) {
			return null;
		}
		return uacCertList.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UacCert> queryUacCertList(UacCert uacCert) {
		if (uacCert != null) {
			StringBuilder hql = new StringBuilder(
					"from UacCert u where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			hql.append(" and u.statusCd = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uacCert.getCertName())) {
				hql.append(" AND u.certName = ?");
				params.add(StringEscapeUtils.escapeSql(uacCert.getCertName()));
			}

			if (!StrUtil.isNullOrEmpty(uacCert.getCertNumber())) {
				hql.append(" AND u.certNumber = ?");
				params.add(StringEscapeUtils.escapeSql(uacCert.getCertNumber()));
			}

			return getHibernateTemplate().find(hql.toString(),params.toArray());
		}

		return null;
	}
}
