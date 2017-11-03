package cn.ffcs.uac.staff.dao.impl;

import javax.annotation.Resource;

import lombok.Getter;
import lombok.Setter;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import cn.ffcs.uac.staff.dao.RemindInforDao;
import cn.ffcs.uom.common.dao.BaseDaoImpl;

@Repository
public class RemindInforDaoImpl extends BaseDaoImpl implements RemindInforDao {

	@Resource(name = "jdbcTemplate")
	@Getter
	@Setter
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void saveRemindInfo(String msg) {
		String sql = "insert into TAB_SMS_LOG(SMS_LOG_ID,SMS_ID,INFO_DATA, "
				+ " SMS_INFO,EFF_DATE,EXP_DATE,STATUS_CD,STATUS_DATE,CREATE_DATE,CREATE_STAFF) "
				+ " values(SEQ_SMS_LOG_ID.Nextval,SEQ_SMS_ID.NEXTVAL,'变更提醒',?,sysdate,sysdate,1000,sysdate,sysdate,1200) ";
		this.jdbcTemplate.update(sql, new Object[]{msg});
//		executeUpdateByJdbcAndParams
	}

}
