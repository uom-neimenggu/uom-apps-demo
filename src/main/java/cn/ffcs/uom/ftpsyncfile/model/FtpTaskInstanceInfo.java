package cn.ffcs.uom.ftpsyncfile.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import cn.ffcs.uom.common.model.UomEntity;

/**
 *FTP任务实例文件信息实体.
 * 
 * @author
 * 
 **/
public class FtpTaskInstanceInfo extends UomEntity implements Serializable {

	public Long getFtpTaskInstanceInfoId() {
		return super.getId();
	}

	public void setFtpTaskInstanceInfoId(Long ftpTaskInstanceInfoId) {
		super.setId(ftpTaskInstanceInfoId);
	}

	/**
	 *FTP任务实例标识.
	 **/
	@Getter
	@Setter
	private Long ftpTaskInstanceId;
	/**
	 *文件名称.
	 **/
	@Getter
	@Setter
	private String fileName;
	/**
	 *文件记录数.
	 **/
	@Getter
	@Setter
	private Long dataCount;

}
