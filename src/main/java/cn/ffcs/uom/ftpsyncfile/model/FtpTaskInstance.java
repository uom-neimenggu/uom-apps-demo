package cn.ffcs.uom.ftpsyncfile.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.constants.BaseUnitConstants;
import cn.ffcs.uom.common.model.DefaultDaoFactory;
import cn.ffcs.uom.common.model.UomEntity;
import cn.ffcs.uom.ftpsyncfile.constants.FtpFileConstant;

/**
 *FTP文件上传任务实例实体.
 * 
 * @author
 * 
 **/
public class FtpTaskInstance extends UomEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 310800405662789518L;

	public Long getFtpTaskInstanceId() {
		return super.getId();
	}

	public void setFtpTaskInstanceId(Long ftpTaskInstanceId) {
		super.setId(ftpTaskInstanceId);
	}

	/**
	 *发布树标识.
	 **/
	@Getter
	@Setter
	private Long treeId;
	/**
	 *增量类型.
	 **/
	@Getter
	@Setter
	private String syncType;
	/**
	 *上次发布时间.
	 **/
	@Getter
	@Setter
	private Date lastDate;
	/**
	 *本次发布时间.
	 **/
	@Getter
	@Setter
	private Date thisDate;
	/**
	 *本地文件路径.
	 **/
	@Getter
	@Setter
	private String localPath;
	/**
	 *远程文件路径.
	 **/
	@Getter
	@Setter
	private String remotePath;
	/**
	 *通知地址（由于ftp登录进去的用户路径是不一样的）.
	 **/
	@Getter
	@Setter
	private String noticePath;
	/**
	 *实例状态.
	 **/
	@Getter
	@Setter
	private String result;
	/**
	 * 任务实例具体信息
	 */
	@Setter
	private List<FtpTaskInstanceInfo> ftpTaskInstanceInfoList;

	/**
	 * 获取任务列表
	 * 
	 * @return
	 */
	public List<FtpTaskInstanceInfo> getFtpTaskInstanceInfoList() {
		if (ftpTaskInstanceInfoList == null
				|| ftpTaskInstanceInfoList.size() <= 0) {
			if (this.getFtpTaskInstanceId() != null) {
				String sql = "SELECT * FROM  FTP_TASK_INSTANCE_INFO A WHERE A.STATUS_CD=? AND A.FTP_TASK_INSTANCE_ID=?";
				List params = new ArrayList();
				params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
				params.add(this.getFtpTaskInstanceId());
				ftpTaskInstanceInfoList = DefaultDaoFactory.getDefaultDao()
						.jdbcFindList(sql, params, FtpTaskInstanceInfo.class);
			}
		}
		return ftpTaskInstanceInfoList;
	}

	/**
	 * 之前是是否有同一颗树未成功通知的ftp实例
	 * 
	 * @return
	 */
	public Boolean hasPrevUnInformFtpTaskInstance() {
		if (this.getFtpTaskInstanceId() != null && this.getTreeId() != null) {
			Boolean result = false;
			String sql = "SELECT * FROM  FTP_TASK_INSTANCE A WHERE A.STATUS_CD = ? AND A.RESULT<? AND A.TREE_ID = ? AND A.FTP_TASK_INSTANCE_ID < ?";
			List params = new ArrayList();
			params.add(BaseUnitConstants.ENTT_STATE_ACTIVE);
			params.add(FtpFileConstant.FTP_TASK_INSTANCE_INFORM);
			params.add(this.getTreeId());
			params.add(this.getFtpTaskInstanceId());
			List<FtpTaskInstance> list = DefaultDaoFactory.getDefaultDao()
					.jdbcFindList(sql, params, FtpTaskInstance.class);
			if (list != null && list.size() > 0) {
				result = true;
			}
			return result;
		}
		return null;
	}
}
