package cn.ffcs.uom.restservices.model;

import java.io.Serializable;
import java.sql.Timestamp;

import cn.ffcs.uom.common.model.UomEntity;

public class SystemMessageBefore extends UomEntity implements Serializable
{
	private static final long serialVersionUID = 1L;
	//private Long		systemMessageBeforeId;
	private String		systemCode;
	private String		eventName;
	private String		tableName;
	private Long		idValue;
	private String		systemMessageInfo;
	private String		operType;
	public Long getSystemMessageBeforeId()
	{
		return super.getId();
	}
	public void setSystemMessageBeforeId(Long systemMessageBeforeId)
	{
		super.setId(systemMessageBeforeId);
	}
	public String getSystemCode()
	{
		return systemCode;
	}
	public void setSystemCode(String systemCode)
	{
		this.systemCode = systemCode;
	}
	public String getEventName()
	{
		return eventName;
	}
	public void setEventName(String eventName)
	{
		this.eventName = eventName;
	}
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
	public Long getIdValue()
	{
		return idValue;
	}
	public void setIdValue(Long idValue)
	{
		this.idValue = idValue;
	}
	public String getSystemMessageInfo()
	{
		return systemMessageInfo;
	}
	public void setSystemMessageInfo(String systemMessageInfo)
	{
		this.systemMessageInfo = systemMessageInfo;
	}
	public String getOperType()
	{
		return operType;
	}
	public void setOperType(String operType)
	{
		this.operType = operType;
	}
}
