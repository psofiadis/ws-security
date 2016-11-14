/*
 *  Copyright 2016 ADVA Optical Networking SE. All rights reserved.
 *
 *  Owner: ext_psofiadis
 *
 *  $Id: GetActiveAlarmsMediator.java 96000 2016-02-08 10:56:27Z ext_psofiadis $
 */
package com.adva.mtosi.server;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.tmforum.mtop.fmw.xsd.hdr.v1.CommunicationPatternType;
import org.tmforum.mtop.fmw.xsd.hdr.v1.CommunicationStyleType;
import org.tmforum.mtop.fmw.xsd.hdr.v1.Header;
import org.tmforum.mtop.fmw.xsd.hdr.v1.MessageTypeType;
import org.tmforum.mtop.fmw.xsd.msg.v1.GetAllDataIteratorRequestType;
import org.tmforum.mtop.fmw.xsd.nam.v1.NamingAttributeListType;
import org.tmforum.mtop.fmw.xsd.nam.v1.NamingAttributeType;
import org.tmforum.mtop.fmw.xsd.nam.v1.RelativeDistinguishNameType;
import org.tmforum.mtop.nra.xsd.alm.v1.AlarmListType;
import org.tmforum.mtop.rtm.wsdl.ar.v1_0.AlarmRetrieval;
import org.tmforum.mtop.rtm.wsdl.ar.v1_0.GetActiveAlarmsCountException;
import org.tmforum.mtop.rtm.wsdl.ar.v1_0.GetActiveAlarmsException;
import org.tmforum.mtop.rtm.wsdl.ar.v1_0.GetActiveAlarmsIteratorException;
import org.tmforum.mtop.rtm.xsd.ar.v1.ActiveAlarmFilterType;
import org.tmforum.mtop.rtm.xsd.ar.v1.GetActiveAlarmsCountRequest;
import org.tmforum.mtop.rtm.xsd.ar.v1.GetActiveAlarmsCountResponse;
import org.tmforum.mtop.rtm.xsd.ar.v1.GetActiveAlarmsRequest;

import javax.jws.WebParam;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import java.util.HashMap;
import java.util.Map;

@javax.jws.WebService(
    serviceName = "AlarmRetrievalHttp",
    portName = "AlarmRetrievalSoapHttp",
    targetNamespace = "http://www.tmforum.org/mtop/rtm/wsdl/ar/v1-0",
    endpointInterface = "org.tmforum.mtop.rtm.wsdl.ar.v1_0.AlarmRetrieval")
public class GetActiveAlarmsMediator implements AlarmRetrieval {
  private static final QName SERVICE_NAME_V2 = new QName("http://www.tmforum.org/mtop/rtm/wsdl/ar/v1-0", "AlarmRetrievalHttp");
  private static final Logger log = Logger.getLogger(GetActiveAlarmsMediator.class);

  public static void prepareAndSendNotification(){
    GetActiveAlarmsRequest activeAlarmsRequest = new GetActiveAlarmsRequest();
    ActiveAlarmFilterType activeAlarmFilterType = new ActiveAlarmFilterType();
    NamingAttributeListType nvms = new NamingAttributeListType();
    nvms.getName().add(createNamingAttributesMe());
    activeAlarmFilterType.setScope(nvms);
    activeAlarmsRequest.setFilter(activeAlarmFilterType);
    Holder<Header> header = createMtosiv2Header();

    sendNotifications(header, activeAlarmsRequest);
  }

  @Override
  public GetActiveAlarmsCountResponse getActiveAlarmsCount(@WebParam(partName = "mtopHeader", mode = WebParam.Mode.INOUT, name = "header", targetNamespace = "http://www.tmforum.org/mtop/fmw/xsd/hdr/v1", header = true) Holder<Header> mtopHeader, @WebParam(partName = "mtopBody", name = "getActiveAlarmsCountRequest", targetNamespace = "http://www.tmforum.org/mtop/rtm/xsd/ar/v1") GetActiveAlarmsCountRequest mtopBody) throws GetActiveAlarmsCountException {
    return null;
  }

  @Override
  public AlarmListType getActiveAlarms(@WebParam(partName = "mtopHeader", mode = WebParam.Mode.INOUT, name = "header", targetNamespace = "http://www.tmforum.org/mtop/fmw/xsd/hdr/v1", header = true) Holder<Header> mtopHeader, @WebParam(partName = "mtopBody", name = "getActiveAlarmsRequest", targetNamespace = "http://www.tmforum.org/mtop/rtm/xsd/ar/v1") GetActiveAlarmsRequest mtopBody) throws GetActiveAlarmsException {
    prepareAndSendNotification();
    return null;
  }

  @Override
  public AlarmListType getActiveAlarmsIterator(@WebParam(partName = "mtopHeader", mode = WebParam.Mode.INOUT, name = "header", targetNamespace = "http://www.tmforum.org/mtop/fmw/xsd/hdr/v1", header = true) Holder<Header> mtopHeader, @WebParam(partName = "mtopBody", name = "getActiveAlarmsIteratorRequest", targetNamespace = "http://www.tmforum.org/mtop/rtm/xsd/ar/v1") GetAllDataIteratorRequestType mtopBody) throws GetActiveAlarmsIteratorException {
    return null;
  }

  private static void sendNotifications(Holder<Header> header, GetActiveAlarmsRequest activeAlarmsRequest) {
    Service srv=   Service.create(SERVICE_NAME_V2);
    AlarmRetrieval port = srv.getPort(AlarmRetrieval.class);
    org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
    org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

    WSS4JInInterceptor wssIn = new WSS4JInInterceptor(getInProps());
    WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(getOutProps());
    cxfEndpoint.getInInterceptors().add(wssIn);
    cxfEndpoint.getOutInterceptors().add(wssOut);

    BindingProvider bp = (BindingProvider) port;
    Map<String, Object> context = bp.getRequestContext();
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://advaoptical.com:8443/mtosi/v2/ResourceTroubleManagement/AlarmRetrieval");
//    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/mtosi/v2/ResourceTroubleManagement/AlarmRetrieval");
    try {
      AlarmListType response = port.getActiveAlarms(header, activeAlarmsRequest);
      log.info(response);
    } catch (GetActiveAlarmsException e) {
      e.printStackTrace();
    }
  }

  private static Map<String, Object> getOutProps(){
    Map<String, Object> outProps = new HashMap<>();
//    outProps.put("action", "UsernameToken Timestamp Signature Encrypt");
    outProps.put("action", "UsernameToken Timestamp");
    outProps.put("passwordType", "PasswordDigest");
    outProps.put("user", "nms-client-key");
    outProps.put("signaturePropFile", "client-crypto.properties");
    outProps.put("encryptionPropFile", "client-crypto.properties");
    outProps.put("signatureKeyIdentifier", "DirectReference");
    outProps.put("encryptionUser", "nms-server-key");
    outProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
    outProps.put("passwordCallbackClass", "com.adva.mtosi.server.config.ClientPasswordCallback");
    outProps.put("signatureParts", "{Element}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body");
    outProps.put("encryptionParts", "{Element}{http://www.w3.org/2000/09/xmldsig#}Signature;{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body");
    outProps.put("encryptionSymAlgorithm", "http://www.w3.org/2001/04/xmlenc#tripledes-cbc");
    return outProps;
  }

  private static Map<String, Object> getInProps(){
    Map<String, Object> inProps = new HashMap<>();
    inProps.put("action", "UsernameToken Timestamp");
    inProps.put("passwordType", "PasswordDigest");
    inProps.put("signaturePropFile", "client-crypto.properties");
    //USE allowRSA15KeyTransportAlgorithm for backward compatibility with cxf 2.5.2
//    inProps.put("allowRSA15KeyTransportAlgorithm", "true");

//    inProps.put("decryptionPropFile", "client-crypto.properties");
//    inProps.put("signatureAlgorithm", "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256");
    inProps.put("passwordCallbackClass", "com.adva.mtosi.server.config.ClientPasswordCallback");

    return inProps;
  }

  private static NamingAttributeType createNamingAttributesMe() {
    NamingAttributeType namingAttributes = new NamingAttributeType();
    RelativeDistinguishNameType rdn1 = new RelativeDistinguishNameType();
    RelativeDistinguishNameType rdn2 = new RelativeDistinguishNameType();
    rdn1.setType("MD");
    rdn1.setValue("ADVA/Network");
    rdn2.setType("ME");
    rdn2.setValue("f7_105_42");
    namingAttributes.getRdn().add(rdn1);
    namingAttributes.getRdn().add(rdn2);
    return namingAttributes;
  }

  private static Holder<Header> createMtosiv2Header() {
    org.tmforum.mtop.fmw.xsd.hdr.v1.ObjectFactory headerObjectFactory = new org.tmforum.mtop.fmw.xsd.hdr.v1.ObjectFactory();
    Holder<Header> headerHolder=new Holder<>();
    Header header = headerObjectFactory.createHeader();
    header.setActivityName("getActiveAlarms");
    header.setMsgName("getActiveAlarms");
    header.setMsgType(MessageTypeType.REQUEST);
    header.setCommunicationPattern(CommunicationPatternType.MULTIPLE_BATCH_RESPONSE);
    header.setCommunicationStyle(CommunicationStyleType.RPC);
    header.setRequestedBatchSize(0L);
    headerHolder.value = header;
    return headerHolder;
  }
}
