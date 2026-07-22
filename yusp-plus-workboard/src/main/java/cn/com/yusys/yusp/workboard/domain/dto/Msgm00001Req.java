package cn.com.yusys.yusp.workboard.domain.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author 19814
 * @version 1.0
 * @since 2025/11/21 10:48
 */
@Data
@ToString
public class Msgm00001Req {

    /**
     *  模板ID
     */
    @JacksonXmlProperty(localName = "TemplateId")
    private String templateId;

    /**
     * 短信类型
     */
    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;
}
