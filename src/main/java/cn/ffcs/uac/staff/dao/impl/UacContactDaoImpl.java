package cn.ffcs.uac.staff.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.UacContactDao;
import cn.ffcs.uac.staff.model.UacContact;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.BaseDaoImpl;
import cn.ffcs.uom.common.util.StrUtil;


@Repository("uacContactDao")
public class UacContactDaoImpl extends BaseDaoImpl implements UacContactDao {
	@Override
	public void addUacContact(UacContact uacContact) {
		uacContact.add();
	}

	@Override
	public void delUacContact(UacContact uacContact) {
		uacContact.remove();
	}

	@Override
	public void updateUacContact(UacContact uacContact) {
		uacContact.update();
	}

	@Override
	public UacContact queryUacContact(UacContact uacContact) {
		List<UacContact> uacContactList = queryUacContactList(uacContact);
		if (null == uacContactList || uacContactList.size() == 0) {
			return null;
		}
		return uacContactList.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UacContact> queryUacContactList(UacContact uacContact) {
		if (uacContact != null) {
			StringBuilder hql = new StringBuilder(
					"from UacContact u where 1=1 ");

			List<Object> params = new ArrayList<Object>();

			hql.append(" and u.statusCd = ?");
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);

			if (!StrUtil.isNullOrEmpty(uacContact.getMobilePhone())) {
				hql.append(" AND u.mobilePhone = ?");
				params.add(StringEscapeUtils.escapeSql(uacContact.getMobilePhone()));
			}

			return getHibernateTemplate().find(hql.toString(),params.toArray());
		}

		return null;
	}
}
