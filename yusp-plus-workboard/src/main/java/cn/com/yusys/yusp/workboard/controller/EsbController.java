package cn.com.yusys.yusp.workboard.controller;

import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientReqEntity;
import cn.com.yusys.yusp.commons.module.adapter.web.rest.JClientRspEntity;
import cn.com.yusys.yusp.workboard.domain.dto.Msgm00001Req;
import cn.com.yusys.yusp.workboard.domain.dto.Msgm00001Resp;
import cn.cscb.uadp.tc.esb.model.request.EsbRequest;
import cn.cscb.uadp.tc.esb.model.response.EsbResponse;
import cn.cscb.uadp.tc.esb.service.EsbHttpService;
import cn.cscb.uadp.tc.esb.util.EsbUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 公共组件示例：ESB 公共组件
 *
 * @author 19814
 * @since 2025-11-21 10:06
 */

@Slf4j
@RestController
@RequestMapping("/api/tc/esb")
public class EsbController {

    @Resource
    private EsbHttpService esbHttpService;

    /**
     * 当前系统作为服务调用方，请求 ESB，其中 Msgm00001Req 表示请求报文
     *
     * @param req 发送给 ESB 的业务数据
     * @return ESB 响应结果
     */
    @PostMapping("/send")
    public JClientRspEntity<String> send(@RequestBody JClientReqEntity<Msgm00001Req> req) throws IOException {
        Msgm00001Req msgm00001Req = req.getBody();
        // 构建业务请求内容
        msgm00001Req.setTemplateId("123");
        msgm00001Req.setMsgType("微信");
        // 发送请求，获取响应结果
        String resultXml = esbHttpService.postEsbWs(msgm00001Req, "MSGM00001");
        // 将响应结果转换为对象
        EsbResponse<Msgm00001Resp> respObject = EsbUtil.buildRespObject(resultXml, Msgm00001Resp.class);
        // 获取 ESB 标准报文中的响应内容：响应码和响应结果
        String respReturnCode = respObject.getRespReturnCode();
        String respReturnMessage = respObject.getRespReturnMessage();
        // 获取 实际业务响应内容
        Msgm00001Resp respBody = respObject.getRespBody();
        log.info("响应码为：{}，响应message为：{}，实际业务响应内容为：{}", respReturnCode, respReturnMessage, respBody);
        return JClientRspEntity.buildSuccess("调用成功");
    }

    /**
     * 作为服务提供方，收到来自于 ESB 的请求，进行业务处理后，将结果返回给 ESB
     *
     * @param req ESB 送过来的请求数据，通常为 XML 报文数据
     * @return 响应给 ESB 的结果数据，格式通常为 XML
     */
    @PostMapping("/provider")
    public String provider(String req) throws IOException {
        // 将请求报文转换成对象
        EsbRequest<Msgm00001Req> result = EsbUtil.buildReqObject(req, Msgm00001Req.class);
        // 获取实际业务对象
        Msgm00001Req reqContent = result.getReqContent();
        // 业务逻辑执行，，，
        Msgm00001Resp resp = doRealService(reqContent);
        // 构建 响应报文
        return EsbUtil.buildSimpleRespXml("00000000", "请求成功", resp);
    }

    private Msgm00001Resp doRealService(Msgm00001Req reqContent) {
        return new Msgm00001Resp();
    }
}
