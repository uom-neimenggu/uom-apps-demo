package cn.ffcs.uom.restservices.restdemo;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import cn.ffcs.uom.common.util.JaxbUtil;
import cn.ffcs.uom.restservices.model.ContractRootInParam;

@Path("/restService")
public class RestService {
	@POST
	@Path("/getUserText")
	@Produces(MediaType.TEXT_XML)
	public String getUserText(String xml) {
		return xml;
	}

	@POST
	@Path("/getUserXMLString")
	@Produces(MediaType.APPLICATION_XML)
	public String getUserXMLString(String xml) {
		return xml;
	}

	@POST
	@Path("/getUserXml")
	@Produces(MediaType.APPLICATION_XML)
	public User getUserXml(@QueryParam("param1") String param1,
			@QueryParam("param2") String param2, User oldUser) {
		User user = new User();
		user.setName("snail");
		user.setAge(22L);
		user.setSex("male");
		return user;
	}

	@GET
	@Path("/getUserJson")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserJson() {
		User user = new User();
		user.setName("snail");
		user.setAge(22L);
		user.setSex("male");
		return user;
	}

	public static void main(String[] args) {
		// User user1 = new User();
		// user1.setName("zhulintao1");
		// user1.setAge(12L);
		// user1.setSex("male1");
		// user1.setNameDesc("姓名描述");
		// user1.setNameDesc1("姓名描述1");
		// user1.setUserId(1000l);
		// user1.setStatusCd("1000");
		// user1.setTestDate(new Date());
		// user1.setEffDate(new Date());
		//
		// List<AttrItemInParam> items = new ArrayList<AttrItemInParam>();
		//
		// AttrItemInParam item1 = new AttrItemInParam();
		// item1.setAttrId(1L);
		// item1.setAction("Add");
		// item1.setAttrValue("zlt");
		// item1.setDescription("测试一");
		//
		// AttrItemInParam item2 = new AttrItemInParam();
		// item2.setAttrId(2L);
		// item2.setAction("Add");
		// item2.setAttrValue("林宝玉");
		// item2.setDescription("测试二");
		//
		// AttrItemInParam item3 = new AttrItemInParam();
		// item3.setAttrId(3L);
		// item3.setAction("Del");
		// item3.setAttrValue("zlt");
		// item3.setDescription("测试三");
		//
		// items.add(item1);
		// items.add(item3);
		// items.add(item2);
		// user1.setAttrItems(items);

		// System.out.println("Object to XML begin=======");
		// String str1 = JaxbUtil.convertToXml(user1);
		// System.out.println(str1);
		// System.out.println("Object to XML end=======");
		//
		// String xml3 =
		// "<USER><effDate>20150810144018</effDate><TEST_DATE>20150810151918</TEST_DATE><id>1000</id><statusCd>1000</statusCd><nameDesc1>姓名描述1</nameDesc1><name>zhulintao1</name>"
		// +
		// "<Age>2</Age><SEX_>male1</SEX_><ATTR_ITEMS><ATTR_ITEM><ATTR_ID>1</ATTR_ID><ATTR_VALUE>zlt</ATTR_VALUE>"
		// +
		// "<DESCRIPTION>测试一</DESCRIPTION><ACTION>Add</ACTION></ATTR_ITEM><ATTR_ITEM><ATTR_ID>1</ATTR_ID>"
		// +
		// "<ATTR_VALUE>zlt</ATTR_VALUE><DESCRIPTION>测试三</DESCRIPTION><ACTION>Del</ACTION></ATTR_ITEM>"
		// +
		// "<ATTR_ITEM><ATTR_ID>2</ATTR_ID><ATTR_VALUE>林宝玉</ATTR_VALUE><DESCRIPTION>测试二</DESCRIPTION>"
		// + "<ACTION>Add</ACTION></ATTR_ITEM></ATTR_ITEMS></USER>";
		// System.out.println("Object to JavaBean begin=======");
		// User user = JaxbUtil.converyToJavaBean(xml3, User.class);
		// System.out.println("getEffDate: " + user.getEffDate());
		// System.out.println("getTestDate: " + user.getTestDate());
		// System.out.println(user.getNameDesc1());
		// System.out.println("Object to JavaBean end=======");

		// User user2 = new User();
		// user2.setName("zhulintao2");
		// user2.setAge("2");
		// user2.setSex("male2");
		//
		// String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
		// +
		// "<ContractRoot><TcpCont><TransactionID>1000000037201112030000342229</TransactionID>"
		// +
		// "<ActionCode>1</ActionCode><RspTime></RspTime><Response><RspType></RspType><RspCode>"
		// +
		// "</RspCode><RspDesc></RspDesc></Response></TcpCont></ContractRoot>";
		// String xml1 =
		// "<ContractRoot><TcpCont><TransactionID>1000000037201112030000342229</TransactionID>"
		// +
		// "<ActionCode>1</ActionCode><RspTime></RspTime><Response><RspType></RspType><RspCode>"
		// +
		// "</RspCode><RspDesc></RspDesc></Response></TcpCont></ContractRoot>";

		String xmlIn = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<ContractRoot><TcpCont><TransactionID>1000000045201508142000174211</TransactionID><ActionCode>0</ActionCode><BusCode>BUS33001</BusCode>"
				+ "<ServiceCode>SVC33049</ServiceCode><ServiceContractVer>SVC3304920141225</ServiceContractVer><ServiceLevel>1</ServiceLevel><SrcOrgID>100000</SrcOrgID><SrcSysID>1000000045</SrcSysID>"
				+ "<SrcSysSign>******</SrcSysSign><DstOrgID>600301</DstOrgID><DstSysID>6003010001</DstSysID><ReqTime>20150814100421</ReqTime></TcpCont><SvcCont><CHANNEL_INFO>"
				+ "<OPERATORS><OPERATORS_NBR>J34000005224</OPERATORS_NBR><OPERATORS_NAME>经营主体新增测试814</OPERATORS_NAME><CERT_TYPE>1</CERT_TYPE><CERT_NBR>341182198712121221</CERT_NBR>"
				+ "<OPERATORS_SNAME/><LEGAL_REPR/><ADDRESS/><TELEPHONE/><CONTACT/><EMAIL/><ORG_ID/><OPERATORS_AREA_GRADE>1100</OPERATORS_AREA_GRADE><PARENT_OPER_NBR/>"
				+ "<COMMON_REGION_ID>8340000</COMMON_REGION_ID><STATUS_CD>1000</STATUS_CD><STATUS_DATE>20150814100701</STATUS_DATE><DESCRIPTION/><ACTION>ADD</ACTION></OPERATORS><OPERATORS_ATTR>"
				+ "<OPERATORS_NBR>J34000005224</OPERATORS_NBR><ATTR_ITEMS><ATTR_ITEM><ATTR_ID>50000038</ATTR_ID><ATTR_VALUE>20</ATTR_VALUE><DESCRIPTION/><ACTION>ADD</ACTION>"
				+ "</ATTR_ITEM><ATTR_ITEM><ATTR_ID>50000041</ATTR_ID><ATTR_VALUE>经营主体新增测试814</ATTR_VALUE><DESCRIPTION/><ACTION>ADD</ACTION></ATTR_ITEM></ATTR_ITEMS>"
				+ "</OPERATORS_ATTR><OPERATOR_NBR>Y34000010691</OPERATOR_NBR></CHANNEL_INFO></SvcCont></ContractRoot>";

		String xmlIn1 = "<ContractRoot><TcpCont><TransactionID>1000000045201508142000174211</TransactionID><ActionCode>0</ActionCode><BusCode>BUS33001</BusCode>"
				+ "<ServiceCode>SVC33049</ServiceCode><ServiceContractVer>SVC3304920141225</ServiceContractVer><ServiceLevel>1</ServiceLevel><SrcOrgID>100000</SrcOrgID><SrcSysID>1000000045</SrcSysID>"
				+ "<SrcSysSign>******</SrcSysSign><DstOrgID>600301</DstOrgID><DstSysID>6003010001</DstSysID><ReqTime>20150814100421</ReqTime></TcpCont><SvcCont><CHANNEL_INFO>"
				+ "<OPERATORS><OPERATORS_NBR>J34000005224</OPERATORS_NBR><OPERATORS_NAME>经营主体新增测试814</OPERATORS_NAME><CERT_TYPE>1</CERT_TYPE><CERT_NBR>341182198712121221</CERT_NBR>"
				+ "<OPERATORS_SNAME/><LEGAL_REPR/><ADDRESS/><TELEPHONE/><CONTACT/><EMAIL/><ORG_ID/><OPERATORS_AREA_GRADE>1100</OPERATORS_AREA_GRADE><PARENT_OPER_NBR/>"
				+ "<COMMON_REGION_ID>8340000</COMMON_REGION_ID><STATUS_CD>1000</STATUS_CD><STATUS_DATE>20150814100701</STATUS_DATE><DESCRIPTION/><ACTION>ADD</ACTION></OPERATORS><OPERATORS_ATTR>"
				+ "<OPERATORS_NBR>J34000005224</OPERATORS_NBR><ATTR_ITEMS><ATTR_ITEM><ATTR_ID>50000038</ATTR_ID><ATTR_VALUE>20</ATTR_VALUE><DESCRIPTION/><ACTION>ADD</ACTION>"
				+ "</ATTR_ITEM><ATTR_ITEM><ATTR_ID>50000041</ATTR_ID><ATTR_VALUE>经营主体新增测试814</ATTR_VALUE><DESCRIPTION/><ACTION>ADD</ACTION></ATTR_ITEM></ATTR_ITEMS>"
				+ "</OPERATORS_ATTR><OPERATOR_NBR>Y34000010691</OPERATOR_NBR></CHANNEL_INFO></SvcCont></ContractRoot>";

		ContractRootInParam rootIn1 = (ContractRootInParam) JaxbUtil
				.converyToJavaBean(xmlIn1, ContractRootInParam.class);

		ContractRootInParam rootIn = (ContractRootInParam) JaxbUtil
				.converyToJavaBean(xmlIn, ContractRootInParam.class);

		// ContractRootOutParam rootOut = (ContractRootOutParam) JaxbUtil
		// .converyToJavaBean(xml, ContractRootOutParam.class);
		//
		// System.out.println(rootOut);
		//
		// Client client = Client.create();
		// WebResource webResource = client
		// .resource("http://localhost:8080/uom-apps/rest/restService/getUserXml");
		//
		// MultivaluedMap queryParams = new MultivaluedMapImpl();
		// queryParams.add("param1", "val1");
		// queryParams.add("param2", "val2");
		//
		// ClientResponse response = webResource.queryParams(queryParams)
		// .entity(user1, MediaType.APPLICATION_XML)
		// .post(ClientResponse.class);
		//
		// System.out.println(response.getStatus());
		// System.out.println(response.getHeaders().get("Content-Type"));
		// String entity = response.getEntity(String.class);
		// System.out.println(entity);
		//
		// ClientResponse response1 = webResource.queryParams(queryParams).post(
		// ClientResponse.class, user2);
		//
		// System.out.println("=response1==" +
		// response1.getEntity(String.class));
		//
		// WebResource webResource2 = client
		// .resource("http://localhost:8080/uom-apps/rest/restService/getUserText");
		// ClientResponse response2 = webResource2.post(ClientResponse.class,
		// xml);
		// System.out.println("=response2==" +
		// response2.getEntity(String.class));
	}
}