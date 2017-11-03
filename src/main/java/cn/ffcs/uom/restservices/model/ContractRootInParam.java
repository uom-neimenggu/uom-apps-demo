package cn.ffcs.uom.restservices.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ContractRoot")
public class ContractRootInParam {

	@Setter
	@Getter
	@XmlElement(name = "TcpCont")
	private TcpContInParam tcpCont;

	@Setter
	@Getter
	@XmlElement(name = "SvcCont")
	private SvcContInParam svcCont;

}
