package cn.ffcs.uom.restservices.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import cn.ffcs.uom.common.model.UomClassProvider;
import cn.ffcs.uom.common.util.ApplicationContextUtil;
import cn.ffcs.uom.common.util.DateUtil;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.common.util.JaxbUtil;
import cn.ffcs.uom.common.util.JsonValidator;
import cn.ffcs.uom.common.util.StrUtil;
import cn.ffcs.uom.restservices.dao.ChannelInfoDao;
import cn.ffcs.uom.restservices.dao.GrpIntfLogDao;
import cn.ffcs.uom.restservices.manager.ChannelInfoManager;
import cn.ffcs.uom.restservices.model.ContractRootInParam;
import cn.ffcs.uom.restservices.model.ContractRootOutParam;
import cn.ffcs.uom.restservices.model.GrpIntfLog;
import cn.ffcs.uom.restservices.model.OipError;
import cn.ffcs.uom.restservices.model.ResponseOutParam;
import cn.ffcs.uom.restservices.model.TcpContOutParam;
import cn.ffcs.uom.restservices.model.UomModelStorageOutParam;
import cn.ffcs.uom.restservices.restdemo.User;
import cn.ffcs.uom.restservices.vo.ChannelConfigVo;
import cn.ffcs.uom.restservices.vo.ChannelInfoVo;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

@Path("/channelInfoService")
public class ChannelInfoService {

	private ChannelInfoManager channelInfoManager = (ChannelInfoManager) ApplicationContextUtil
			.getBean("channelInfoManager");
	
	private ChannelInfoDao channelInfoDao = (ChannelInfoDao) ApplicationContextUtil
			.getBean("channelInfoDao");
	//渠道日志
	private GrpIntfLogDao grpIntfLogDao = (GrpIntfLogDao) ApplicationContextUtil.getBean("grpIntfLogDao");
	
	@POST
	@Path("/getRestServiceIsOn")
	@Produces(MediaType.TEXT_XML)
	public String getRestServiceIsOn(String str)
	{
		String result = "参数为空";
		if(!StrUtil.isNullOrEmpty(str))
		{
			//如果匹配成功
			result = channelInfoDao.selectDual1("select 1 from dual");
		}
		return result;
	}

	@SuppressWarnings("unused")
	@POST
	@Path("/getChannelViewText")
	@Produces(MediaType.TEXT_XML)
	public String getChannelViewText(String str) {
		ContractRootInParam rootIn = new ContractRootInParam();
		ContractRootOutParam rootOutParam = new ContractRootOutParam();
		TcpContOutParam tcpCont = new TcpContOutParam();
		ResponseOutParam response = new ResponseOutParam();

		tcpCont.setResponse(response);
		rootOutParam.setTcpCont(tcpCont);

		rootIn = (ContractRootInParam) JaxbUtil.converyToJavaBean(str,
				ContractRootInParam.class);
		// 入集团模型库
		rootOutParam = channelInfoManager.saveGroupModelStorage(rootIn,
				rootOutParam);

		// 入主数据模型库
		UomModelStorageOutParam uomModelStorageOutParam = channelInfoManager
				.saveUomModelStorage(rootIn, rootOutParam);

		return str;
	}
	
	/**
	 * 集团渠道信息入主数据库异步线程
	 * .
	 * 
	 * @版权：福富软件 版权所有 (c) 2015
	 * @author xiaof
	 * @version Revision 1.0.0
	 * @see:
	 * @创建日期：2016年10月19日
	 * @功能说明：
	 *
	 */
	private class ChannelSaveModel implements Callable<ContractRootInParam>
	{
	    private ContractRootInParam rootIn;
	    private ContractRootOutParam rootOutParam;
	    
	    public ChannelSaveModel(ContractRootInParam rootIn, ContractRootOutParam rootOutParam)
	    {
	        this.rootIn = rootIn;
	        this.rootOutParam = rootOutParam;
	    }

        @Override
        public ContractRootInParam call() throws Exception {
            // 入集团模型库
            rootOutParam = channelInfoManager.saveGroupModelStorage(rootIn,
                    rootOutParam);
            // 入主数据模型库
            rootOutParam.getTcpCont().getResponse().setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
            // 保存进入主数据
            UomModelStorageOutParam uomModelStorageOutParam = channelInfoManager
                .saveUomModelStorage(rootIn, rootOutParam);
            rootOutParam = uomModelStorageOutParam.getRootOutParam();
            rootOutParam.getTcpCont().getResponse()
                .setRspCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
            rootOutParam.getTcpCont().getResponse().setRspDesc("操作成功");
            //返回对报文的操作结果
            return uomModelStorageOutParam.getRootIn();
        }
    }
	
	@SuppressWarnings("unchecked")
    @POST
    @Path("/channelDownService")
    @Produces(MediaType.TEXT_XML)
	public String getChannelViewXml2(String xml)
	{
	    /*
         * 这里做一个时间统计器
         * */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//        Date startService = new Date();
//        System.out.println("异步4G同步请求开始：" + sdf.format(startService));
        
        String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

        GrpIntfLog grpIntfLog = new GrpIntfLog();
        
        grpIntfLog.setMsgId(msgId);
        grpIntfLog.setInterfaceType(WsConstants.INTERFACE_TYPE_1);
        grpIntfLog.setUomResult(WsConstants.TASK_INIT);
        grpIntfLog.setChannelResult(WsConstants.TASK_INIT);
        grpIntfLog.setGrpRequestContent(xml);
        grpIntfLog.setErrCode(WsConstants.CHANNEL_RSP_CODE);
        //收到集团报文时间
        grpIntfLog.setEffDate(new Date());
        
        /*
         * 获取数据库中的对应开关以及url信息
         *  // 集团渠道信息入主数据库开关
            1、 boolean channelInfoInterFaceSwitch = UomClassProvider.isOpenSwitch("channelInfoInterFaceSwitch");
            2、省渠道信息通知开关   channelInfoNoticeInterFaceSwitch
            3、oipServiceCodeChannelInfo 配置信息
            4、oipRestUrlChannelInfo 配置信息
         */
        ChannelConfigVo channelConfigVo = new ChannelConfigVo();
        List<String> classNames1 = new ArrayList<String>();
        classNames1.add("IntfSwitch"); classNames1.add("IntfConfig");
        List<String> params = new ArrayList<String>();
        params.add("channelInfoNoticeInterFaceSwitch"); params.add("channelInfoInterFaceSwitch");
        params.add("oipRestUrlChannelInfo"); params.add("oipServiceCodeChannelInfo");
        List<ChannelConfigVo> lists = channelInfoManager.queryChannelConfig(classNames1, params);
        //设置开关值
        channelConfigVo.bindValues(lists);
        //返回报文
        ContractRootOutParam rootOut = null;
        ContractRootOutParam rootOutParam = new ContractRootOutParam();
        TcpContOutParam tcpContOutParam = new TcpContOutParam();
        ResponseOutParam responseOutParam = new ResponseOutParam();
        
        //用来解析请求报文
        ContractRootInParam rootIn = null;
        
        //组装返回xml
        rootOutParam.setTcpCont(tcpContOutParam);
        tcpContOutParam.setResponse(responseOutParam);
        
        tcpContOutParam.setActionCode(WsConstants.CHANNEL_ACTION_CODE);
        tcpContOutParam.setRspTime(DateUtil.getDateTime(new Date()));
        responseOutParam.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
        responseOutParam.setRspCode(WsConstants.CHANNEL_RSP_CODE);
        
        //1、请求报文非空
        if(StrUtil.isEmpty(xml))
        {
            responseOutParam.setRspDesc("集团渠道传过来的XML报文为空");
            grpIntfLog.setErrMsg("集团渠道传过来的XML报文为空");
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        
        //----2、xml转化为bean，根节点不能为空
        rootIn = (ContractRootInParam) JaxbUtil.converyToJavaBean(xml,
            ContractRootInParam.class);
        if(rootIn == null)
        {
            responseOutParam.setRspDesc("集团渠道XML对象转换失败");
            grpIntfLog.setErrMsg("集团渠道XML对象转换失败");
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        
        //----3、getTransactionID获取到的值不能为空
        if(StrUtil.isEmpty(rootIn.getTcpCont().getTransactionID()))
        {
            responseOutParam.setRspDesc("集团渠道XML对象未传业务流水号");
            grpIntfLog.setErrMsg("集团渠道XML对象未传业务流水号");
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        
        grpIntfLog.setTransId(rootIn.getTcpCont()
            .getTransactionID());
        grpIntfLog.setOperatorNbr(rootIn.getSvcCont()
            .getChannelInfo().getOperatorNbr());
        tcpContOutParam.setTransactionID(rootIn.getTcpCont()
            .getTransactionID());
        //最后统一入库,中途没必要进行修改
        //grpIntfLog.updateOnly();
        
//        //集团渠道信息入主数据库开关 是否打开
//        boolean channelInfoInterFaceSwitch = UomClassProvider
//            .isOpenSwitch("channelInfoInterFaceSwitch");// 集团渠道信息入主数据库开关
        
        // 入主数据模型库
        //新建一个单线程池，进行异步处理
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<ContractRootInParam> future = null; //线程执行返回值
        if (channelConfigVo.isChannelInfoInterFaceSwitch()) {
            //这个异步操作，获取异步操作的rootIn结果
            rootOutParam.getTcpCont().getResponse().setRspType(WsConstants.CHANNEL_RSP_TYPE_SUCCESS);
            //启动异步线程
            future = pool.submit(new ChannelSaveModel(rootIn, rootOutParam));
        }
        
        //执行主线程相应的操作，然后获取异步结果
        grpIntfLog.setUomResult(WsConstants.TASK_SUCCESS);
        grpIntfLog.setErrCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
        grpIntfLog.setErrMsg("操作成功");
        //获取异步结果，进行赋值
        
        if(future != null)
        {// 添加省内主数据组织ID后的集团报文
            try {
                xml = JaxbUtil.convertToXml(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown(); //及时关闭线程，释放资源
        grpIntfLog.setUomRequestContent(xml);
        
//        boolean channelInfoNoticeInterFaceSwitch = UomClassProvider
//            .isOpenSwitch("channelInfoNoticeInterFaceSwitch");// 省渠道信息通知开关
        
        if (channelConfigVo.isChannelInfoNoticeInterFaceSwitch()
            && WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam.getTcpCont().getResponse()
                .getRspType())) {

//            String oipServiceCode = UomClassProvider
//                    .getIntfUrl("oipServiceCodeChannelInfo");
            String oipServiceCode = channelConfigVo.getOipServiceCode();
    
            if (StrUtil.isNullOrEmpty(oipServiceCode)) {
                responseOutParam.setRspDesc("oipServiceCodeChannelInfo 省渠道视图OIP服务编码没有配置");
                grpIntfLog.setErrMsg("oipServiceCodeChannelInfo 省渠道视图OIP服务编码没有配置");
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
    
            String oipHttpUrl = channelConfigVo.getOipHttpUrl();
//                UomClassProvider.getIntfUrl("oipRestUrlChannelInfo");
    
            if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
                responseOutParam
                        .setRspDesc("oipRestUrlChannelInfo接口地址没有配置");
                grpIntfLog.setErrMsg("oipRestUrlChannelInfo接口地址没有配置");
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
            
            //开启异步请求 到省渠道
            //记录下发到渠道的时间
            grpIntfLog.setStatusDate(new Date());
            pool = Executors.newSingleThreadExecutor();
            Future<String> futureOip = pool.submit(new ChannelOipService(oipHttpUrl, oipServiceCode, msgId, xml, 30));
            
            //获取返回值，根据返回值判断json或者其他操作
            String entity = "";
            try {
                entity = futureOip.get();
                //记录渠道返回时间
                grpIntfLog.setUpdateDate(new Date());
                grpIntfLog.setChannelResponseContent(entity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //停下线程，释放资源
            pool.shutdown();
            //设置返回报文
            grpIntfLog.setChannelResponseContent(entity);
            
            //oip返回数据不为空
            if (StrUtil.isEmpty(entity)){
                responseOutParam.setRspDesc("渠道未返回相应报文");
                grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                grpIntfLog.setErrMsg("渠道未返回相应报文");
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
            
            if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
                try {
                    
                    OipError oipError = (OipError) JacksonUtil.JSon2Object(entity, OipError.class);
                    responseOutParam.setRspCode(oipError.getErrorCode());
                    responseOutParam.setRspDesc(oipError.getErrorDesc());
                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                    grpIntfLog.setErrCode(oipError.getErrorCode());
                    grpIntfLog.setErrMsg(oipError.getErrorDesc());
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    return JaxbUtil.convertToXml(rootOutParam);

                } catch (JsonGenerationException e) {
                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                    
                    grpIntfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    
                    responseOutParam.setRspDesc(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                } catch (JsonMappingException e) {
                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                    
                    grpIntfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    responseOutParam.setRspDesc(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                } catch (IOException e) {

                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                    
                    grpIntfLog.setErrMsg(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    responseOutParam.setRspDesc(e.getMessage().length() <= 500 ? e.getMessage()
                        : e.getMessage().substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                }
            } else {
                //返回不是json直接转化，返回的数据不为空，且getRspCode应答code不为空
                rootOut = (ContractRootOutParam) JaxbUtil.converyToJavaBean(entity,
                    ContractRootOutParam.class);
                
                if (rootOut == null) {
                    responseOutParam.setRspDesc("渠道未返回会话应答代码");
                    
                    grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
                    
                    grpIntfLog.setErrMsg("渠道未返回会话应答代码");
                    
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    
                    return JaxbUtil.convertToXml(rootOutParam);
                }
                
                if (rootOut.getTcpCont().getResponse().getRspCode() == null) {
                    responseOutParam.setRspDesc("渠道未返回相应报文");
                    
                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                    
                    grpIntfLog.setErrMsg("渠道未返回相应报文");
                    
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);                    
                    return JaxbUtil.convertToXml(rootOutParam);
                }
                    
                    
                grpIntfLog.setErrCode(rootOut.getTcpCont().getResponse().getRspCode());
                grpIntfLog.setErrMsg(rootOut.getTcpCont().getResponse().getRspDesc());
                
                if (rootOut.getTcpCont().getResponse().getRspCode()
                    .equals(WsConstants.CHANNEL_RESULT_SUCCESS_CODE)) {
                    grpIntfLog.setChannelResult(WsConstants.TASK_SUCCESS);
                } else {
                    grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                }
            }
            
            //最后记录返回集团调用的时候
            grpIntfLog.setExpDate(new Date());
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
//            Date endService = new Date();
//            long ms = endService.getTime() - startService.getTime();
//            System.out.println("异步4G同步请求结束：" + sdf.format(endService) + "\n相差：" + ms + "毫秒");
            return entity;
        }
        
        grpIntfLog.setCreateDate(new Date());
        grpIntfLogDao.addObject(grpIntfLog);
        return JaxbUtil.convertToXml(rootOutParam);
	}
	
	@SuppressWarnings("unchecked")
//	@POST
//	@Path("/channelDownService")
//	@Produces(MediaType.TEXT_XML)
	public String getChannelViewXml(String xml) {
	    /*
         * 这里做一个时间统计器
         * */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//        Date startService = new Date();
//        System.out.println("非异步4G同步请求开始：" + sdf.format(startService));
        
		String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

		GrpIntfLog grpIntfLog = new GrpIntfLog();

		grpIntfLog.setMsgId(msgId);
		grpIntfLog.setInterfaceType(WsConstants.INTERFACE_TYPE_1);
		grpIntfLog.setUomResult(WsConstants.TASK_INIT);
		grpIntfLog.setChannelResult(WsConstants.TASK_INIT);
		grpIntfLog.setGrpRequestContent(xml);
		grpIntfLog.setErrCode(WsConstants.CHANNEL_RSP_CODE);
//		grpIntfLog.addOnly();

		ContractRootInParam rootIn = null;
		ContractRootOutParam rootOut = null;
		ContractRootOutParam rootOutParam = new ContractRootOutParam();
		TcpContOutParam tcpContOutParam = new TcpContOutParam();
		ResponseOutParam responseOutParam = new ResponseOutParam();

		rootOutParam.setTcpCont(tcpContOutParam);
		tcpContOutParam.setResponse(responseOutParam);

		tcpContOutParam.setActionCode(WsConstants.CHANNEL_ACTION_CODE);
		tcpContOutParam.setRspTime(DateUtil.getDateTime(new Date()));
		responseOutParam.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
		responseOutParam.setRspCode(WsConstants.CHANNEL_RSP_CODE);

		if (!StrUtil.isEmpty(xml)) {

			rootIn = (ContractRootInParam) JaxbUtil.converyToJavaBean(xml,
					ContractRootInParam.class);

			if (rootIn != null) {

				if (!StrUtil.isEmpty(rootIn.getTcpCont().getTransactionID())) {

					grpIntfLog.setTransId(rootIn.getTcpCont()
							.getTransactionID());
					grpIntfLog.setOperatorNbr(rootIn.getSvcCont()
							.getChannelInfo().getOperatorNbr());

					tcpContOutParam.setTransactionID(rootIn.getTcpCont()
							.getTransactionID());

//					grpIntfLog.updateOnly();

				} else {
					responseOutParam.setRspDesc("集团渠道XML对象未传业务流水号");
					grpIntfLog.setErrMsg("集团渠道XML对象未传业务流水号");
//					grpIntfLog.updateOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

			} else {
				responseOutParam.setRspDesc("集团渠道XML对象转换失败");
				grpIntfLog.setErrMsg("集团渠道XML对象转换失败");
//				grpIntfLog.updateOnly();
				return JaxbUtil.convertToXml(rootOutParam);
			}

			boolean channelInfoInterFaceSwitch = UomClassProvider
					.isOpenSwitch("channelInfoInterFaceSwitch");// 集团渠道信息入主数据库开关

			if (channelInfoInterFaceSwitch) {

				// 入集团模型库
				rootOutParam = channelInfoManager.saveGroupModelStorage(rootIn,
						rootOutParam);

				// 入主数据模型库
				if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
						.getTcpCont().getResponse().getRspType())) {

					rootOutParam.getTcpCont().getResponse()
							.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
					//保存进入主数据，这个后台对uomModelStorageOutParam，其实就是做了一个赋值操作，这个完全可以独立异步出来
					UomModelStorageOutParam uomModelStorageOutParam = channelInfoManager
							.saveUomModelStorage(rootIn, rootOutParam);

					rootOutParam = uomModelStorageOutParam.getRootOutParam();

					if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS
							.equals(rootOutParam.getTcpCont().getResponse()
									.getRspType())) {
						rootOutParam
								.getTcpCont()
								.getResponse()
								.setRspCode(
										WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
						rootOutParam.getTcpCont().getResponse()
								.setRspDesc("操作成功");
						grpIntfLog.setUomResult(WsConstants.TASK_SUCCESS);
						grpIntfLog
								.setErrCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
						grpIntfLog.setErrMsg("操作成功");
//						grpIntfLog.updateOnly();
						xml = JaxbUtil.convertToXml(uomModelStorageOutParam
								.getRootIn());// 添加省内主数据组织ID后的集团报文
						grpIntfLog.setUomRequestContent(xml);
//						grpIntfLog.updateOnly();
					} else {
						grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
						grpIntfLog.setUomResponseContent(JaxbUtil
								.convertToXml(rootOutParam));
						grpIntfLog.setErrMsg(rootOutParam.getTcpCont()
								.getResponse().getRspDesc());
//						grpIntfLog.updateOnly();
						return JaxbUtil.convertToXml(rootOutParam);
					}

				} else {
					grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
					grpIntfLog.setUomResponseContent(JaxbUtil
							.convertToXml(rootOutParam));
//					grpIntfLog.updateOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}
			}

			boolean channelInfoNoticeInterFaceSwitch = UomClassProvider
					.isOpenSwitch("channelInfoNoticeInterFaceSwitch");// 省渠道信息通知开关

			if (channelInfoNoticeInterFaceSwitch
					&& WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
							.getTcpCont().getResponse().getRspType())) {

				String oipServiceCode = UomClassProvider
						.getIntfUrl("oipServiceCodeChannelInfo");

				if (StrUtil.isNullOrEmpty(oipServiceCode)) {
					responseOutParam
							.setRspDesc("oipServiceCodeChannelInfo 省渠道视图OIP服务编码没有配置");
					grpIntfLog
							.setErrMsg("oipServiceCodeChannelInfo 省渠道视图OIP服务编码没有配置");
//					grpIntfLog.updateOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

				String oipHttpUrl = UomClassProvider
						.getIntfUrl("oipRestUrlChannelInfo");

				if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
					responseOutParam
							.setRspDesc("oipRestUrlChannelInfo接口地址没有配置");
					grpIntfLog.setErrMsg("oipRestUrlChannelInfo接口地址没有配置");
//					grpIntfLog.updateOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

				Client client = Client.create();

				client.setConnectTimeout(30 * 1000);

				WebResource webResource = client.resource(oipHttpUrl);

				@SuppressWarnings("rawtypes")
				MultivaluedMap queryParams = new MultivaluedMapImpl();
				queryParams.add("isRest", "true");
				queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
				queryParams.add("servCode", oipServiceCode);
				queryParams.add("msgId", msgId);
				queryParams.add("transactionId", msgId);
				
				/*
		         * 这里做一个时间统计器
		         * */
//				Date startoipService = new Date();
//		        System.out.println("非异步oip请求开始：" + sdf.format(startoipService));

				ClientResponse response = webResource.queryParams(queryParams)
						.entity(xml, MediaType.TEXT_XML)
						.post(ClientResponse.class);

				String entity = response.getEntity(String.class);
				
				/*
                 * 这里做一个时间统计器
                 * */
//				Date endoipService = new Date();
//                long ms = endoipService.getTime() - startoipService.getTime();
//                System.out.println("非异步oip请求结束：" + sdf.format(endoipService) + "\n相差：" + ms + "毫秒");

				grpIntfLog.setChannelResponseContent(entity);

				if (!StrUtil.isEmpty(entity)) {

					if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
						try {
							OipError oipError = (OipError) JacksonUtil
									.JSon2Object(entity, OipError.class);

							responseOutParam
									.setRspCode(oipError.getErrorCode());

							responseOutParam
									.setRspDesc(oipError.getErrorDesc());

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog.setErrCode(oipError.getErrorCode());

							grpIntfLog.setErrMsg(oipError.getErrorDesc());

//							grpIntfLog.updateOnly();

							return JaxbUtil.convertToXml(rootOutParam);

						} catch (JsonGenerationException e) {

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));

//							grpIntfLog.updateOnly();

							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						} catch (JsonMappingException e) {
							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
//							grpIntfLog.updateOnly();
							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						} catch (IOException e) {

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
//							grpIntfLog.updateOnly();
							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						}

					} else {

						rootOut = (ContractRootOutParam) JaxbUtil
								.converyToJavaBean(entity,
										ContractRootOutParam.class);

						if (rootOut != null) {

							if (rootOut.getTcpCont().getResponse().getRspCode() != null) {

								grpIntfLog.setErrCode(rootOut.getTcpCont()
										.getResponse().getRspCode());

								grpIntfLog.setErrMsg(rootOut.getTcpCont()
										.getResponse().getRspDesc());

								if (rootOut
										.getTcpCont()
										.getResponse()
										.getRspCode()
										.equals(WsConstants.CHANNEL_RESULT_SUCCESS_CODE)) {
									grpIntfLog
											.setChannelResult(WsConstants.TASK_SUCCESS);
								} else {
									grpIntfLog
											.setChannelResult(WsConstants.TASK_FAILED);
								}

							} else {

								responseOutParam.setRspDesc("渠道未返回会话应答代码");

								grpIntfLog
										.setUomResult(WsConstants.TASK_FAILED);

								grpIntfLog.setErrMsg("渠道未返回会话应答代码");

//								grpIntfLog.updateOnly();

								return JaxbUtil.convertToXml(rootOutParam);

							}

						} else {

							responseOutParam.setRspDesc("渠道未返回相应报文");

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog.setErrMsg("渠道未返回相应报文");

//							grpIntfLog.updateOnly();

							return JaxbUtil.convertToXml(rootOutParam);

						}

					}

				} else {

					responseOutParam.setRspDesc("渠道未返回相应报文");

					grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);

					grpIntfLog.setErrMsg("渠道未返回相应报文");

//					grpIntfLog.updateOnly();

					return JaxbUtil.convertToXml(rootOutParam);

				}

//				grpIntfLog.updateOnly();

				response.close();

				client.destroy();
				/*
                 * 这里做一个时间统计器
                 * */
//				Date endService = new Date();
//	            long ms2 = endService.getTime() - startService.getTime();
//	            System.out.println("非异步4G同步请求结束：" + sdf.format(endService) + "\n相差：" + ms2 + "毫秒");

				return entity;

			} else {
				return JaxbUtil.convertToXml(rootOutParam);
			}

		} else {

			responseOutParam.setRspDesc("集团渠道传过来的XML报文为空");

			grpIntfLog.setErrMsg("集团渠道传过来的XML报文为空");

//			grpIntfLog.updateOnly();

			return JaxbUtil.convertToXml(rootOutParam);

		}

	}
	
	/**
	 * 用来uom数据校验用异步处理类
	 * .
	 * 
	 * @版权：福富软件 版权所有 (c) 2015
	 * @author xiaof
	 * @version Revision 1.0.0
	 * @see:
	 * @创建日期：2016年10月18日
	 * @功能说明：
	 *
	 */
	private class ChannelValidUomClassProvider implements Callable<ChannelInfoVo>
	{
	    private ContractRootInParam rootIn;
        private ContractRootOutParam rootOutParam;
        private GrpIntfLog grpIntfLog;
        
        public ChannelValidUomClassProvider(ContractRootInParam rootIn, ContractRootOutParam rootOutParam, GrpIntfLog grpIntfLog)
        {
            this.rootIn = rootIn;
            this.rootOutParam = rootOutParam;
            this.grpIntfLog = grpIntfLog;
        }

        @Override
        public ChannelInfoVo call() throws Exception {
            //这个是返回处理结果
            String xml = "";
            // 入主数据模型库
            rootOutParam.getTcpCont().getResponse()
                    .setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
            UomModelStorageOutParam uomModelValidOutParam = channelInfoManager
                    .uomModelValid(rootIn, rootOutParam);

            rootOutParam = uomModelValidOutParam.getRootOutParam();

            if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
                    .getTcpCont().getResponse().getRspType())) {
                rootOutParam
                        .getTcpCont()
                        .getResponse()
                        .setRspCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
                rootOutParam.getTcpCont().getResponse().setRspDesc("操作成功");
                grpIntfLog.setUomResult(WsConstants.TASK_SUCCESS);
                grpIntfLog
                        .setErrCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
                grpIntfLog.setErrMsg("操作成功");
                xml = JaxbUtil.convertToXml(uomModelValidOutParam
                        .getRootIn());// 添加省内主数据组织ID后的集团报文
                grpIntfLog.setUomRequestContent(xml);
                
                ChannelInfoVo channelInfoVo = new ChannelInfoVo();
                channelInfoVo.setGrpIntfLog(grpIntfLog);
                channelInfoVo.setRootIn(rootIn);
                channelInfoVo.setRootOutParam(rootOutParam);
                channelInfoVo.setXml(xml);
                
                return channelInfoVo;
            } else {
                //根据outparam确定返回的
                grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
                grpIntfLog.setUomResponseContent(JaxbUtil
                        .convertToXml(rootOutParam));
                grpIntfLog.setErrMsg(rootOutParam.getTcpCont()
                        .getResponse().getRspDesc());
                ChannelInfoVo channelInfoVo = new ChannelInfoVo();
                channelInfoVo.setGrpIntfLog(grpIntfLog);
                channelInfoVo.setRootIn(rootIn);
                channelInfoVo.setRootOutParam(rootOutParam);
//                return JaxbUtil.convertToXml(rootOutParam);
                return channelInfoVo;
            }
        }
	}
	
	private class ChannelOipService implements Callable<String>
	{
        private String oipHttpUrl;
        private String oipServiceCode;
        private String msgId;
        private String xml;
        private int    waitSeconds;
        
        public ChannelOipService(String oipHttpUrl, String oipServiceCode, String msgId,
            String xml, int waitSeconds) {
            this.oipHttpUrl = oipHttpUrl;
            this.oipServiceCode = oipServiceCode;
            this.msgId = msgId;
            this.xml = xml;
            this.waitSeconds = waitSeconds;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public String call() throws Exception {
            
            /*
             * 这里做一个时间统计器
             */
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//            Date startoipService = new Date();
//            System.out.println("异步操作4G同步oip服务请求开始：" + sdf.format(startoipService));
            
            Client client = Client.create();
            
            client.setConnectTimeout(waitSeconds * 1000);
            
            WebResource webResource = client.resource(oipHttpUrl);
            
            @SuppressWarnings("rawtypes")
            MultivaluedMap queryParams = new MultivaluedMapImpl();
            queryParams.add("isRest", "true");
            queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
            queryParams.add("servCode", oipServiceCode);
            queryParams.add("msgId", msgId);
            queryParams.add("transactionId", msgId);
            
            ClientResponse response = webResource.queryParams(queryParams)
                .entity(xml, MediaType.TEXT_XML).post(ClientResponse.class);
            
            String entity = response.getEntity(String.class);
            
            /*
             * 这里做一个时间统计器
             */
//            Date endoipService = new Date();
//            long ms = endoipService.getTime() - startoipService.getTime();
//            System.out.println("异步操作4G同步oip服务请求结束：" + sdf.format(endoipService) + "\n相差：" + ms
//                + "毫秒");
            
            client.destroy();// 回收client对象
            response.close();
            return entity;
        }
    }
	
	/**
     * 集团渠道校验接口-验证数据合法性（异步线程处理）
     * 
     * @param xml
     * @return
     */
    @SuppressWarnings("unchecked")
    @POST
    @Path("/channelValidService")
    @Produces(MediaType.TEXT_XML)
	public String getChannelValidXml2(String xml)
	{
        /*
         * 这里做一个时间统计器
         * */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//        System.out.println("服务请求开始：" + sdf.format(new Date()));
        
        
        String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

        GrpIntfLog grpIntfLog = new GrpIntfLog();

        grpIntfLog.setMsgId(msgId);
        grpIntfLog.setInterfaceType(WsConstants.INTERFACE_TYPE_2);
        grpIntfLog.setUomResult(WsConstants.TASK_INIT);
        grpIntfLog.setChannelResult(WsConstants.TASK_INIT);
        grpIntfLog.setGrpRequestContent(xml);
        grpIntfLog.setErrCode(WsConstants.CHANNEL_RSP_CODE);
        grpIntfLog.setEffDate(new Date());
        
        //返回报文
        ContractRootOutParam rootOut = null;
        ContractRootOutParam rootOutParam = new ContractRootOutParam();
        TcpContOutParam tcpContOutParam = new TcpContOutParam();
        ResponseOutParam responseOutParam = new ResponseOutParam();
        
        //用来解析请求报文
        ContractRootInParam rootIn = null;
        
        //组装返回xml
        rootOutParam.setTcpCont(tcpContOutParam);
        tcpContOutParam.setResponse(responseOutParam);
        
        tcpContOutParam.setActionCode(WsConstants.CHANNEL_ACTION_CODE);
        tcpContOutParam.setRspTime(DateUtil.getDateTime(new Date()));
        responseOutParam.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
        responseOutParam.setRspCode(WsConstants.CHANNEL_RSP_CODE);
        
        //1、xml报文不能为空
        if(StrUtil.isEmpty(xml))
        {
            responseOutParam.setRspDesc("集团渠道传过来的XML报文为空");
            grpIntfLog.setErrMsg("集团渠道传过来的XML报文为空");
//            grpIntfLog.addOnly();
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        
        //2、xml转化为bean，根节点不能为空
        rootIn = (ContractRootInParam) JaxbUtil.converyToJavaBean(xml,
            ContractRootInParam.class);
        if(rootIn == null)
        {
            responseOutParam.setRspDesc("集团渠道XML对象转换失败");
            grpIntfLog.setErrMsg("集团渠道XML对象转换失败");
//            grpIntfLog.addOnly();
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        else if(StrUtil.isEmpty(rootIn.getTcpCont().getTransactionID()))
        {
            //2、1 getTransactionID获取到的值不能为空
            responseOutParam.setRspDesc("集团渠道XML对象未传业务流水号");
            grpIntfLog.setErrMsg("集团渠道XML对象未传业务流水号");
//            grpIntfLog.addOnly();
            grpIntfLog.setCreateDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            return JaxbUtil.convertToXml(rootOutParam);
        }
        
        //3、UomClassProvider.isOpenSwitch("channelInfoValidInterFaceSwitch");// 集团渠道信息校验接口开关保证打开
        boolean channelInfoValidInterFaceSwitch = UomClassProvider
            .isOpenSwitch("channelInfoValidInterFaceSwitch");// 集团渠道信息校验接口开关
        if(channelInfoValidInterFaceSwitch)
        {
            //这个异步处理
            //向异步线程传递相应的参数
            ExecutorService threadPool = Executors.newSingleThreadExecutor();
            //获取线程返回值
            Future<ChannelInfoVo> future = threadPool.submit(new ChannelValidUomClassProvider(rootIn, rootOutParam, grpIntfLog));
            //获取返回值
            try {
                ChannelInfoVo channelInfoVoTemp = future.get();
                grpIntfLog = channelInfoVoTemp.getGrpIntfLog();
                rootIn = channelInfoVoTemp.getRootIn();
                rootOutParam = channelInfoVoTemp.getRootOutParam();
                //如果验证通过xml是存在值的
                if(StrUtil.isEmpty(channelInfoVoTemp.getXml()))
                {
                    xml = channelInfoVoTemp.getXml();
                }
                //停掉线程
                threadPool.shutdown();
                //判断获取的结果进行返回
                if (!WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
                    .getTcpCont().getResponse().getRspType()))
                {
                    grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
                    grpIntfLog.setUomResponseContent(JaxbUtil
                            .convertToXml(rootOutParam));
                    grpIntfLog.setErrMsg(rootOutParam.getTcpCont()
                            .getResponse().getRspDesc());
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    return JaxbUtil.convertToXml(rootOutParam);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        boolean channelInfoValidNoticeInterFaceSwitch = UomClassProvider
            .isOpenSwitch("channelInfoValidNoticeInterFaceSwitch");// 省渠道信息校验通知开关
        //4、省渠道信息校验通知开关 打开 并且第三步验证成功，不成功直接返回return JaxbUtil.convertToXml(rootOutParam);
        if (channelInfoValidNoticeInterFaceSwitch
            && WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
                    .getTcpCont().getResponse().getRspType())) {
            //获取oip的url
            String oipServiceCode = UomClassProvider
                .getIntfUrl("oipServiceCodeChannelInfoValid");
           
            if (StrUtil.isNullOrEmpty(oipServiceCode))
            {
                responseOutParam.setRspDesc("oipServiceCodeChannelInfoValid 省渠道视图OIP服务校验编码没有配置");
                grpIntfLog.setErrMsg("oipServiceCodeChannelInfoValid 省渠道视图OIP服务校验编码没有配置");
//                grpIntfLog.addOnly();
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
            
            String oipHttpUrl = UomClassProvider
                .getIntfUrl("oipRestUrlChannelInfoValid");

            if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
                responseOutParam.setRspDesc("oipRestUrlChannelInfoValid接口地址没有配置");
                grpIntfLog.setErrMsg("oipRestUrlChannelInfoValid接口地址没有配置");
//                grpIntfLog.addOnly();
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
            //向oip发送报文，采用异步处理,获取异步结果
            //记录下发到渠道的时间
            grpIntfLog.setStatusDate(new Date());
            ExecutorService pool = Executors.newSingleThreadExecutor();
            Future<String> future = pool.submit(new ChannelOipService(oipHttpUrl, oipServiceCode, msgId, xml, 10));
            //获取返回值，根据返回值判断json或者其他操作
            String entity = "";
            try {
                entity = future.get();
                //记录渠道返回时间
                grpIntfLog.setUpdateDate(new Date());
                grpIntfLog.setChannelResponseContent(entity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            //停下线程，释放资源
            pool.shutdown();
            //json和xml分开操作,判断oip是否返回报文
            if (StrUtil.isEmpty(entity))
            {
                responseOutParam.setRspDesc("渠道未返回相应报文");
                grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);
                grpIntfLog.setErrMsg("渠道未返回相应报文");
//                grpIntfLog.addOnly();
                grpIntfLog.setCreateDate(new Date());
                grpIntfLogDao.addObject(grpIntfLog);
                return JaxbUtil.convertToXml(rootOutParam);
            }
            
            //判断返回的是json还是xml
            if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
                try {
                    OipError oipError = (OipError) JacksonUtil
                            .JSon2Object(entity, OipError.class);

                    responseOutParam
                            .setRspCode(oipError.getErrorCode());

                    responseOutParam
                            .setRspDesc(oipError.getErrorDesc());

                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);

                    grpIntfLog.setErrCode(oipError.getErrorCode());

                    grpIntfLog.setErrMsg(oipError.getErrorDesc());

//                  grpIntfLog.updateOnly();
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);

                    return JaxbUtil.convertToXml(rootOutParam);

                } catch (JsonGenerationException e) {

                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);

                    grpIntfLog
                            .setErrMsg(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));

//                  grpIntfLog.updateOnly();
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);

                    responseOutParam
                            .setRspDesc(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                } catch (JsonMappingException e) {
                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);

                    grpIntfLog
                            .setErrMsg(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));
//                  grpIntfLog.updateOnly();
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    responseOutParam
                            .setRspDesc(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                } catch (IOException e) {

                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);

                    grpIntfLog
                            .setErrMsg(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));
//                  grpIntfLog.updateOnly();
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
                    responseOutParam
                            .setRspDesc(e.getMessage().length() <= 500 ? e
                                    .getMessage() : e.getMessage()
                                    .substring(0, 500));
                    return JaxbUtil.convertToXml(rootOutParam);
                }
            }
            else
            {
                //不是json模式
                rootOut = (ContractRootOutParam) JaxbUtil
                    .converyToJavaBean(entity,
                            ContractRootOutParam.class);
                
                if(rootOut == null)
                {
                    responseOutParam.setRspDesc("渠道未返回相应报文");
                    
                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);
                    grpIntfLog.setErrMsg("渠道未返回相应报文");
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);
    
                    return JaxbUtil.convertToXml(rootOutParam);
                }
                
                if(rootOut.getTcpCont().getResponse().getRspCode() == null)
                {
                    responseOutParam.setRspDesc("渠道未返回会话应答代码");

                    grpIntfLog
                            .setUomResult(WsConstants.TASK_FAILED);

                    grpIntfLog.setErrMsg("渠道未返回会话应答代码");
//                    grpIntfLog.addOnly();
                    grpIntfLog.setCreateDate(new Date());
                    grpIntfLogDao.addObject(grpIntfLog);

                    return JaxbUtil.convertToXml(rootOutParam);
                }
                


                grpIntfLog.setErrCode(rootOut.getTcpCont()
                        .getResponse().getRspCode());
                grpIntfLog.setErrMsg(rootOut.getTcpCont()
                        .getResponse().getRspDesc());
                if (rootOut
                        .getTcpCont()
                        .getResponse()
                        .getRspCode()
                        .equals(WsConstants.CHANNEL_RESULT_SUCCESS_CODE)) {
                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_SUCCESS);
                } else {
                    grpIntfLog
                            .setChannelResult(WsConstants.TASK_FAILED);
                }
            }
            
            //最后返回异步结果
            //这个可以等到最后正确的时候进行数据添加 2016年10月18日 xiaof
//            grpIntfLog.addOnly();
            grpIntfLog.setExpDate(new Date());
            grpIntfLogDao.addObject(grpIntfLog);
            
            /*
             * 这里做一个时间统计器
             * */
//            sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//            System.out.println("服务请求结束：" + sdf.format(new Date()));
            return entity;
        }
        else
        {
            return JaxbUtil.convertToXml(rootOutParam);
        }
	}

	/**
	 * 集团渠道校验接口-验证数据合法性
	 * 
	 * @param xml
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	@POST
//	@Path("/channelValidService")
//	@Produces(MediaType.TEXT_XML)
	public String getChannelValidXml(String xml) {
	    
	    /*
         * 这里做一个时间统计器
         * */
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//        Date startService = new Date();
//        System.out.println("非异步4G同步请求开始：" + sdf.format(startService));

		String msgId = EsbHeadUtil.getOipHttpJsonMsgId(EsbHeadUtil.FTP_SENDER);

		GrpIntfLog grpIntfLog = new GrpIntfLog();

		grpIntfLog.setMsgId(msgId);
		grpIntfLog.setInterfaceType(WsConstants.INTERFACE_TYPE_2);
		grpIntfLog.setUomResult(WsConstants.TASK_INIT);
		grpIntfLog.setChannelResult(WsConstants.TASK_INIT);
		grpIntfLog.setGrpRequestContent(xml);
		grpIntfLog.setErrCode(WsConstants.CHANNEL_RSP_CODE);
		//这个可以等到最后正确的时候进行数据添加 2016年10月18日 xiaof
//		grpIntfLog.addOnly();

		ContractRootInParam rootIn = null;
		ContractRootOutParam rootOut = null;
		ContractRootOutParam rootOutParam = new ContractRootOutParam();
		TcpContOutParam tcpContOutParam = new TcpContOutParam();
		ResponseOutParam responseOutParam = new ResponseOutParam();

		rootOutParam.setTcpCont(tcpContOutParam);
		tcpContOutParam.setResponse(responseOutParam);

		tcpContOutParam.setActionCode(WsConstants.CHANNEL_ACTION_CODE);
		tcpContOutParam.setRspTime(DateUtil.getDateTime(new Date()));
		responseOutParam.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
		responseOutParam.setRspCode(WsConstants.CHANNEL_RSP_CODE);

		if (!StrUtil.isEmpty(xml)) {

			rootIn = (ContractRootInParam) JaxbUtil.converyToJavaBean(xml,
					ContractRootInParam.class);

			if (rootIn != null) {

				if (!StrUtil.isEmpty(rootIn.getTcpCont().getTransactionID())) {

					grpIntfLog.setTransId(rootIn.getTcpCont()
							.getTransactionID());
					grpIntfLog.setOperatorNbr(rootIn.getSvcCont()
							.getChannelInfo().getOperatorNbr());

					tcpContOutParam.setTransactionID(rootIn.getTcpCont()
							.getTransactionID());
					//这个可以等到最后正确的时候进行数据添加 2016年10月18日 xiaof
//					grpIntfLog.updateOnly();

				} else {
					responseOutParam.setRspDesc("集团渠道XML对象未传业务流水号");
					grpIntfLog.setErrMsg("集团渠道XML对象未传业务流水号");
//					grpIntfLog.updateOnly();
//					grpIntfLog.addOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

			} else {
				responseOutParam.setRspDesc("集团渠道XML对象转换失败");
				grpIntfLog.setErrMsg("集团渠道XML对象转换失败");
//				grpIntfLog.updateOnly();
//				grpIntfLog.addOnly();
				return JaxbUtil.convertToXml(rootOutParam);
			}
//--------------------------------------------
			boolean channelInfoValidInterFaceSwitch = UomClassProvider
					.isOpenSwitch("channelInfoValidInterFaceSwitch");// 集团渠道信息校验接口开关

			if (channelInfoValidInterFaceSwitch) {

				// 入主数据模型库
				rootOutParam.getTcpCont().getResponse()
						.setRspType(WsConstants.CHANNEL_RSP_TYPE_FAILED);
//-----------------------------------------
				UomModelStorageOutParam uomModelValidOutParam = channelInfoManager
						.uomModelValid(rootIn, rootOutParam);

				rootOutParam = uomModelValidOutParam.getRootOutParam();

				if (WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
						.getTcpCont().getResponse().getRspType())) {
					rootOutParam
							.getTcpCont()
							.getResponse()
							.setRspCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
					rootOutParam.getTcpCont().getResponse().setRspDesc("操作成功");
					grpIntfLog.setUomResult(WsConstants.TASK_SUCCESS);
					grpIntfLog
							.setErrCode(WsConstants.CHANNEL_RESULT_SUCCESS_CODE);
					grpIntfLog.setErrMsg("操作成功");
//					grpIntfLog.updateOnly();
					xml = JaxbUtil.convertToXml(uomModelValidOutParam
							.getRootIn());// 添加省内主数据组织ID后的集团报文
					grpIntfLog.setUomRequestContent(xml);
					//这个可以等到最后正确的时候进行数据添加 2016年10月18日 xiaof
//					grpIntfLog.updateOnly();
				} else {
					grpIntfLog.setUomResult(WsConstants.TASK_FAILED);
					grpIntfLog.setUomResponseContent(JaxbUtil
							.convertToXml(rootOutParam));
					grpIntfLog.setErrMsg(rootOutParam.getTcpCont()
							.getResponse().getRspDesc());
//					grpIntfLog.updateOnly();
//					grpIntfLog.addOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}
			}

			boolean channelInfoValidNoticeInterFaceSwitch = UomClassProvider
					.isOpenSwitch("channelInfoValidNoticeInterFaceSwitch");// 省渠道信息校验通知开关

			if (channelInfoValidNoticeInterFaceSwitch
					&& WsConstants.CHANNEL_RSP_TYPE_SUCCESS.equals(rootOutParam
							.getTcpCont().getResponse().getRspType())) {

				String oipServiceCode = UomClassProvider
						.getIntfUrl("oipServiceCodeChannelInfoValid");

				if (StrUtil.isNullOrEmpty(oipServiceCode)) {
					responseOutParam
							.setRspDesc("oipServiceCodeChannelInfoValid 省渠道视图OIP服务校验编码没有配置");
					grpIntfLog
							.setErrMsg("oipServiceCodeChannelInfoValid 省渠道视图OIP服务校验编码没有配置");
//					grpIntfLog.updateOnly();
//					grpIntfLog.addOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

				String oipHttpUrl = UomClassProvider
						.getIntfUrl("oipRestUrlChannelInfoValid");

				if (StrUtil.isNullOrEmpty(oipHttpUrl)) {
					responseOutParam
							.setRspDesc("oipRestUrlChannelInfoValid接口地址没有配置");
					grpIntfLog.setErrMsg("oipRestUrlChannelInfoValid接口地址没有配置");
//					grpIntfLog.updateOnly();
//					grpIntfLog.addOnly();
					return JaxbUtil.convertToXml(rootOutParam);
				}

				Client client = Client.create();

				client.setConnectTimeout(10 * 1000);

				WebResource webResource = client.resource(oipHttpUrl);

				@SuppressWarnings("rawtypes")
				MultivaluedMap queryParams = new MultivaluedMapImpl();
				queryParams.add("isRest", "true");
				queryParams.add("sender", EsbHeadUtil.FTP_SENDER);
				queryParams.add("servCode", oipServiceCode);
				queryParams.add("msgId", msgId);
				queryParams.add("transactionId", msgId);
				
//	            Date startoipService = new Date();
//	            System.out.println("oip服务请求开始：" + sdf.format(startoipService));

				ClientResponse response = webResource.queryParams(queryParams)
						.entity(xml, MediaType.TEXT_XML)
						.post(ClientResponse.class);

				String entity = response.getEntity(String.class);
				
//				Date endoipService = new Date();
//	            long ms = endoipService.getTime() - startoipService.getTime();
//	            System.out.println("oip服务请求结束：" + sdf.format(endoipService) + "\n相差：" + ms + "毫秒");

				grpIntfLog.setChannelResponseContent(entity);

				if (!StrUtil.isEmpty(entity)) {

					if (new JsonValidator().validate(entity)) {// 验证返回的字符串是不是Json格式
						try {
							OipError oipError = (OipError) JacksonUtil
									.JSon2Object(entity, OipError.class);

							responseOutParam
									.setRspCode(oipError.getErrorCode());

							responseOutParam
									.setRspDesc(oipError.getErrorDesc());

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog.setErrCode(oipError.getErrorCode());

							grpIntfLog.setErrMsg(oipError.getErrorDesc());

//							grpIntfLog.updateOnly();
//							grpIntfLog.addOnly();

							return JaxbUtil.convertToXml(rootOutParam);

						} catch (JsonGenerationException e) {

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));

//							grpIntfLog.updateOnly();
//							grpIntfLog.addOnly();

							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						} catch (JsonMappingException e) {
							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
//							grpIntfLog.updateOnly();
//							grpIntfLog.addOnly();
							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						} catch (IOException e) {

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog
									.setErrMsg(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
//							grpIntfLog.updateOnly();
//							grpIntfLog.addOnly();
							responseOutParam
									.setRspDesc(e.getMessage().length() <= 500 ? e
											.getMessage() : e.getMessage()
											.substring(0, 500));
							return JaxbUtil.convertToXml(rootOutParam);
						}

					} else {

						rootOut = (ContractRootOutParam) JaxbUtil
								.converyToJavaBean(entity,
										ContractRootOutParam.class);

						if (rootOut != null) {

							if (rootOut.getTcpCont().getResponse().getRspCode() != null) {

								grpIntfLog.setErrCode(rootOut.getTcpCont()
										.getResponse().getRspCode());

								grpIntfLog.setErrMsg(rootOut.getTcpCont()
										.getResponse().getRspDesc());

								if (rootOut
										.getTcpCont()
										.getResponse()
										.getRspCode()
										.equals(WsConstants.CHANNEL_RESULT_SUCCESS_CODE)) {
									grpIntfLog
											.setChannelResult(WsConstants.TASK_SUCCESS);
								} else {
									grpIntfLog
											.setChannelResult(WsConstants.TASK_FAILED);
								}

							} else {

								responseOutParam.setRspDesc("渠道未返回会话应答代码");

								grpIntfLog
										.setUomResult(WsConstants.TASK_FAILED);

								grpIntfLog.setErrMsg("渠道未返回会话应答代码");

//								grpIntfLog.updateOnly();
//								grpIntfLog.addOnly();

								return JaxbUtil.convertToXml(rootOutParam);

							}

						} else {

							responseOutParam.setRspDesc("渠道未返回相应报文");

							grpIntfLog
									.setChannelResult(WsConstants.TASK_FAILED);

							grpIntfLog.setErrMsg("渠道未返回相应报文");

//							grpIntfLog.updateOnly();
//							grpIntfLog.addOnly();

							return JaxbUtil.convertToXml(rootOutParam);

						}

					}

				} else {

					responseOutParam.setRspDesc("渠道未返回相应报文");

					grpIntfLog.setChannelResult(WsConstants.TASK_FAILED);

					grpIntfLog.setErrMsg("渠道未返回相应报文");

//					grpIntfLog.addOnly();

					return JaxbUtil.convertToXml(rootOutParam);

				}

				//这个可以等到最后正确的时候进行数据添加 2016年10月18日 xiaof
//				grpIntfLog.updateOnly();
//				grpIntfLog.addOnly();
				response.close();

				client.destroy();
				
				/*
		         * 这里做一个时间统计器
		         * */
//				sdf = new SimpleDateFormat("yyyyMMdd  HH:mm:ss E");
//		        System.out.println("服务请求结束：" + sdf.format(new Date()));

				return entity;

			} else {
				return JaxbUtil.convertToXml(rootOutParam);
			}

		} else {

			responseOutParam.setRspDesc("集团渠道传过来的XML报文为空");

			grpIntfLog.setErrMsg("集团渠道传过来的XML报文为空");

//			grpIntfLog.addOnly();

			return JaxbUtil.convertToXml(rootOutParam);

		}

	}

	@POST
	@Path("/getChannelViewXml")
	@Produces(MediaType.TEXT_XML)
	public ContractRootOutParam getChannelViewXml(ContractRootOutParam outParam) {
		return outParam;
	}

	@POST
	@Path("/getChannelViewJsonString")
	@Produces(MediaType.APPLICATION_JSON)
	public String getChannelViewJson(String json) {
		return json;
	}

	@GET
	@Path("/getChannelViewJson")
	@Produces(MediaType.APPLICATION_JSON)
	public User getChannelViewJson(User user) {
		return user;
	}

	public static void main(String[] args) {

		ContractRootOutParam rootOut = new ContractRootOutParam();
		TcpContOutParam tcpCont = new TcpContOutParam();
		ResponseOutParam responseOP = new ResponseOutParam();

		// tcpCont.setTransactionID("1000000037201112030000342229");
		tcpCont.setActionCode("1");
		tcpCont.setRspTime("20150729151817");

		responseOP.setRspCode("1265");
		responseOP.setRspType("1");
		responseOP.setRspDesc("字段长度溢出");

		tcpCont.setResponse(responseOP);
		rootOut.setTcpCont(tcpCont);

		// String str =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ContractRoot><TcpCont>"
		// +
		// "<TransactionID>6004040001201407020000018409</TransactionID><ActionCode>0</ActionCode><BusCode>BUS33001</BusCode>"
		// +
		// "<ServiceCode>SVC33049</ServiceCode><ServiceContractVer>SVC3304920121201</ServiceContractVer>"
		// +
		// "<ServiceLevel>1</ServiceLevel><SrcOrgID>600404</SrcOrgID><SrcSysID>6004040001</SrcSysID>"
		// +
		// "<SrcSysSign>123</SrcSysSign><DstOrgID>100000</DstOrgID><DstSysID>1000000045</DstSysID>"
		// +
		// "<ReqTime>20140702082148</ReqTime></TcpCont><SvcCont><CHANNEL_INFO><CHANNEL>"
		// +
		// "<CHANNEL_NBR>6401011046764</CHANNEL_NBR><CHANNEL_NAME>0702test</CHANNEL_NAME><CHANNEL_CLASS>10</CHANNEL_CLASS>"
		// +
		// "<CHANNEL_TYPE_CD>110201</CHANNEL_TYPE_CD><ORG_ID>1015</ORG_ID><COMMON_REGION_ID>8640101</COMMON_REGION_ID>"
		// +
		// "<STATUS_CD>1000</STATUS_CD><STATUS_DATE>20140702202148</STATUS_DATE><DESCRIPTION />"
		// +
		// "<ACTION>ADD</ACTION></CHANNEL><CHANNEL_OPERATORS_RELAS><CHANNEL_OPERATORS_RELA>"
		// +
		// "<CHANNEL_NBR>6401011046764</CHANNEL_NBR><OPERATORS_NBR>J64010048661</OPERATORS_NBR>"
		// +
		// "<RELA_TYPE>10</RELA_TYPE><DESCRIPTION /><ACTION>ADD</ACTION></CHANNEL_OPERATORS_RELA>"
		// +
		// "</CHANNEL_OPERATORS_RELAS><CHANNEL_ATTR><CHANNEL_NBR>6401011046764</CHANNEL_NBR>"
		// +
		// "<ATTR_ITEMS><ATTR_ITEM><ATTR_ID>50000051</ATTR_ID><ATTR_VALUE>20150728095402</ATTR_VALUE>"
		// +
		// "<DESCRIPTION /><ACTION>ADD</ACTION></ATTR_ITEM></ATTR_ITEMS></CHANNEL_ATTR>"
		// + "</CHANNEL_INFO></SvcCont></ContractRoot>";

		// Client client = Client.create();
		//
		// client.setConnectTimeout(30 * 1000);
		//
		// WebResource webResource = client
		// .resource("http://134.64.110.177:8005/ctg_service/data_service/channelinfo/channelDownService");
		//
		// // MultivaluedMap queryParams = new MultivaluedMapImpl();
		// // queryParams.add("param1", "val1");
		// // queryParams.add("param2", "val2");
		//
		// ClientResponse response = webResource.entity(str, MediaType.TEXT_XML)
		// .post(ClientResponse.class);
		//
		// System.out.println(response.getStatus());
		// System.out.println(response.getHeaders().get("Content-Type"));
		// String entity = response.getEntity(String.class);
		// System.out.println("==========直接调用渠道接口测试==============");
		// System.out.println(entity);
		// System.out.println("==========直接调用渠道接口测试==============");

		// WebResource webResource2 = client
		// .resource("http://localhost:8080/uom-apps/restServices/channelInfoService/getChannelViewXml");
		// ClientResponse response2 = webResource2.entity(rootOut).post(
		// ClientResponse.class);
		// System.out.println("==response2==" +
		// response2.getEntity(String.class));
		//
		// WebResource webResource1 = client
		// .resource("http://localhost:8080/uom-apps/restServices/channelInfoService/channelDownService");
		// ClientResponse response1 = webResource1.entity(str).post(
		// ClientResponse.class);
		// System.out.println("==response1==" +
		// response1.getEntity(String.class));
		//
		// System.out.println("Object to XML begin=======");
		// String str1 = JaxbUtil.convertToXml(rootOut);
		// System.out.println("Object to XML end=======");

	}
}