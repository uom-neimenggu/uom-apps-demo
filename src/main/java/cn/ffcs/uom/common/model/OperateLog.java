package cn.ffcs.uom.common.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.dao.OperateLogDao;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.systemconfig.model.AttrValue;
import cn.ffcs.uom.systemconfig.model.SysClass;

/**
 * 组织实体.
 * 
 * @author
 * 
 **/
@SuppressWarnings("serial")
public class OperateLog extends UomEntity implements Serializable {

	/**
	 * 操作日志标识.
	 */
	public Long getOperateLogId() {
		return super.getId();
	}

	public void setOperateLogId(Long operateLogId) {
		super.setId(operateLogId);
	}

	/**
	 * 操作对象.
	 **/
	@Getter
	@Setter
	private String opeObject;
	/**
	 * 操作对象标识.
	 **/
	@Getter
	@Setter
	private Long opeObjectId;
	/**
	 * 操作原对象标识.
	 **/
	@Getter
	@Setter
	private Long opeOriObjectId;
	/**
	 * 操作类型.
	 **/
	@Getter
	@Setter
	private String opeType;
	/**
	 * 备注.
	 **/
	@Getter
	@Setter
	private String note;

	/**
	 * 构造方法
	 */
	public OperateLog() {
		super();
	}

	/**
	 * 创建对象实例.
	 * 
	 * @return OperateLog
	 */
	public static OperateLog newInstance() {
		return new OperateLog();
	}

	/**
	 * 获取dao
	 * 
	 * @return
	 */
	public static OperateLogDao repository() {
		return (OperateLogDao) ApplicationContextUtil.getBean("operateLogDao");
	}

	/**
	 * 获取操作类型名称
	 * 
	 * @return
	 */
	public String getOpeTypeName() {
		List<AttrValue> list = UomClassProvider.jdbcGetAttrValueByValue(
				"OperateLog", "opeType", this.getOpeType().trim(),
				BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0) {
			return list.get(0).getAttrValueName();
		}
		return "";
	}

	/**
	 * 获取物理表名称
	 * 
	 * @return
	 */
	public String getTableName() {
		List<SysClass> list = UomClassProvider
				.jdbcGetTableName(this.getOpeObject() != null ? this
						.getOpeObject().trim() : null,
						BaseUnitConstants.ENTT_STATE_ACTIVE);
		if (list != null && list.size() > 0
				&& list.get(0).getClassName() != null) {
			return list.get(0).getClassName().trim();
		}
		return "";

	}

	public static String gennerateBatchNumber() {

		String batchNumber = repository().getSeqBatchNumber();

		if (!StrUtil.isNullOrEmpty(batchNumber)) {

			int legh = 16 - batchNumber.length();

			if (legh >= 0) {

				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < legh; i++) {

					sb.append("0");

				}

				sb.append(batchNumber);

				return sb.toString();

			}

		}

		return null;
	}

}
