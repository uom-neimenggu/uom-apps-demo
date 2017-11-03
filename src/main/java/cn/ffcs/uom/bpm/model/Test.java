package cn.ffcs.uom.bpm.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import cn.ffcs.raptornuke.plugin.common.json.jackson.JsonUtil;
import cn.ffcs.uom.common.util.JacksonUtil;
import cn.ffcs.uom.webservices.constants.WsConstants;
import cn.ffcs.uom.webservices.util.EsbHeadUtil;
import cn.ffcs.uom.webservices.util.WsClientUtil;

public class Test {
    
    public static void main(String[] args) {
        ProcessInformReq req = new ProcessInformReq();
        req.setSerial("aaaaaa");
        req.setStatus("1");
        req.setDesc("aaa");
        req.setExpa1("aaaaa");
        String s;
        try {
            s = JacksonUtil.Object2JSon(req);
            System.out.println(s);
            String msgId = EsbHeadUtil.getEAMMsgId(WsConstants.OIP_SENDER);
            
/*            String esbHead = EsbHeadUtil.getEsbHead(WsConstants.OIP_SENDER,
                "1207.uomService.SynReq", msgId, "");
//            String callUrl = "http://134.64.110.182:9999/service/mboss/route";
            String callUrl = "http://134.64.110.100:9005/uom-apps/services/HelloWorldWSDD";
            String outJson = WsClientUtil.wsCallOnOip(esbHead,
                s, callUrl, "dbpmMethod", "");
            System.out.println(outJson);
            String out = WsClientUtil.wsCallUtil("aaa", callUrl, "sayHello");
            System.out.println(out);*/
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
