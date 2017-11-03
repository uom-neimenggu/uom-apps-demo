package cn.ffcs.uom.webservices.constants;

public class WsConstants {
	/**
	 * 成功
	 */
	public final static String SUCCESS = "1";
	/**
	 * 失败
	 */
	public final static String FAILED = "0";
	/**
	 * 全量更新
	 */
	public final static String SYNC_ALL = "1";
	/**
	 * 增量更新
	 */
	public final static String SYNC_PART = "0";
	/**
	 * 初始化
	 */
	public final static Long TASK_INIT = -1L;
	/**
	 * 记录之前存在未通知成功的记录数
	 */
	public final static Long TASK_FRONT_ERROR = -2L;
	/**
	 * 成功
	 */
	public final static Long TASK_SUCCESS = 1L;
	/**
	 * 失败
	 */
	public final static Long TASK_FAILED = 0L;

	/**
	 * msgType错误
	 */
	public final static Long TASK_MSGTYPE_ERROR = -4L;

	/**
	 * msgType空
	 */
	public final static Long TASK_MSGTYPE_NULL = -3L;

	/**
	 * 方法反射错误
	 */
	public final static Long TASK_INVOKE_ERROR = -5L;

	/**
	 * FTP信息通知接口编码
	 */
	public final static String INTF_CODE_FTP_INFO_INFORM = "1300000001";
	/**
	 * FTP信息通知CRM渠道 msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_CRM_CHANNEL = "informFtpInfoToCrmChannel";
	/**
	 * FTP信息通知CRM msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_CRM = "informFtpInfoToCrm";
	/**
	 * FTP信息通知CPMIS msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_CPMIS = "informFtpInfoToCpmis";
	/**
	 * FTP信息通知1w msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_1W = "informFtpInfoTo1W";
	/**
	 * FTP信息通知知识库 msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_KMS = "informFtpInfoToKMS";
	/**
	 * FTP信息通知服务质量 msgType
	 */
	public final static String MSG_TYPE_FTP_INFORM_TO_SQMS = "informFtpInfoToSqms";
	/**
	 * FTP信息通知次数
	 */
	public final static Long LIMIT_TIMES_FTP = 3L;
	/**
	 * UOM系统编码
	 */
	public final static String SYSTEM_CODE_UOM = "13000";
	/**
	 * 邮件系统编码
	 */
	public final static String SYSTEM_CODE_UOM_EMAIL = "26100";
	/**
	 * 集团统一邮箱系统编码
	 */
	public final static String SYSTEM_CODE_GROUP_EMAIL = "26200";
	/**
	 * 网格全息编码
	 */
	public final static String SYSTEM_CODE_UOM_GRID = "26101";
	/**
	 * CRM系统编码
	 */
	public final static String SYSTEM_CODE_CRM = "13107";
	/**
	 * CPMIS系统编码
	 */
	public final static String SYSTEM_CODE_CPMIS = "13100";
	/**
	 * CRM渠道系统
	 */
	public final static String SYSTEM_CODE_CRM_CHANNEL = "13106";
	/**
	 * 1W智能客服系统
	 */
	public final static String SYSTEM_CODE_CRM_1W = "13105";
	/**
	 * 知识库系统
	 */
	public final static String SYSTEM_CODE_KMS = "13104";
	/**
	 * 服务质量系统
	 */
	public final static String SYSTEM_CODE_SQMS = "13201";
	/**
	 * 服务的用户密码
	 */
	public static String SERVICE_USERNAME = "UOM";
	public static String SERVICE_PASSWORD = "UOM-AH";
	/**
	 * 接口开关-开
	 */
	public static String INTF_SWITCH_OPEN = "1";
	/**
	 * 接口开关-关
	 */
	public static String INTF_SWITCH_CLOSE = "0";
	/**
	 * 短息平台地址
	 */
	public static String RONDOW_URL = "http://134.64.60.162:82/axis/services/SAS_Service";
	/**
	 * 短息平台webservice方法名
	 */
	public static String RONDOW_METHODNAME = "sendMessage";
	/**
	 * 短息平台系统标识
	 */
	public static String RONDOW_SYSNAME = "CSSS";
	/**
	 * 短息平台webservice固定标识
	 */
	public static String RONDOW_INTERFACEID = "saveShortMessage";
	/**
	 * 短信发送成功
	 */
	public static String RONDOW_RESULT_SUCCESS = "1";
	/**
	 * 短信发送失败
	 */
	public static String RONDOW_RESULT_FAIL = "0";
	/**
	 * 短信通知开关打开
	 */
	public static String SYSTEM_MESSAGE_SWITCH_OPEN = "1";
	/**
	 * 短信通知开关关闭
	 */
	public static String SYSTEM_MESSAGE_SWITCH_CLOSE = "0";
	/**
	 * 短信内容
	 */
	public static String RONDOW_MESSAGE = "随机码为：";
	/**
	 * OIP UOM系统sender值
	 */
	public static String OIP_SENDER = "1207";
	/**
	 * 短信平台 UOM系统业务ID
	 */
	public static String OIP_BUSINESS_ID = "5273";
	/**
	 * 短信平台在OIP注册的服务名，同步
	 */
	public static String OIP_SERVICE_CODE = "1102.sendMessage.SynReq";
	/**
	 * 短信平台OIP地址
	 */
	public static String OIP_SENDMESSAGE_URL = "oipSendMessageUrl";
	/**
	 * 集团4G渠道拨测地址
	 */
	public static String GROUP_CHANNEL_INFO_URL = "groupChannelInfoUrl";
	/**
	 * 员工类型开关打开
	 */
	public static String IDENTITY_CARD_SWITCH_OPEN = "1";
	/**
	 * 员工类型开关关闭
	 */
	public static String IDENTITY_CARD_SWITCH_CLOSE = "0";

	/**
	 * 网格全息在OIP注册的服务名
	 */
	public static String OIP_SERVICE_CODE_GRID = "";
	/**
	 * OIP HTTP JSON接口地址
	 */
	public static String OIP_HTTP_JSON_URL = "oipHttpJsonUrl";

	/**
	 * ITSM服务编码
	 */
	public static String STSM_OIP_SERVICE_CODE = "ITSM.UOMFileTransferNotice.SynReq";
	/**
	 * 网点查询ITSM地址
	 */
	public static String NETWORK_ITSM_URL = "networkItsmUrl";

	/**
	 * 渠道返回成功标识
	 */
	public static String CHANNEL_RESULT_SUCCESS_CODE = "0000";

	/**
	 * ActionCode标识 集团应答代码
	 */
	public static String CHANNEL_ACTION_CODE = "1";

	/**
	 * RspType标识 0表示成功
	 */
	public static String CHANNEL_RSP_TYPE_SUCCESS = "0";

	/**
	 * RspType标识 1表示失败
	 */
	public static String CHANNEL_RSP_TYPE_FAILED = "1";

	/**
	 * RspCode标识 主数据自定义应答代码
	 */
	public static String CHANNEL_RSP_CODE = "13000";

	/**
	 * 1表示渠道视图信息下发接口
	 */
	public static String INTERFACE_TYPE_1 = "1";

	/**
	 * 2表示渠道视图信息下发校验单接口
	 */
	public static String INTERFACE_TYPE_2 = "2";

}
