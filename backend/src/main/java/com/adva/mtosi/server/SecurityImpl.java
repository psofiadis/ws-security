package com.adva.mtosi.server;

import com.adva.mtosi.Utils.LayeredParameterUtils;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.tmforum.mtop.fmw.xsd.gen.v1.AnyListType;
import org.tmforum.mtop.fmw.xsd.hdr.v1.CommunicationPatternType;
import org.tmforum.mtop.fmw.xsd.hdr.v1.CommunicationStyleType;
import org.tmforum.mtop.fmw.xsd.hdr.v1.Header;
import org.tmforum.mtop.fmw.xsd.hdr.v1.MessageTypeType;
import org.tmforum.mtop.fmw.xsd.nam.v1.MECreateData;
import org.tmforum.mtop.fmw.xsd.nam.v1.NamingAttributeType;
import org.tmforum.mtop.fmw.xsd.nam.v1.RelativeDistinguishNameType;
import org.tmforum.mtop.mri.wsdl.mec.v1_0.CreateManagedElementException;
import org.tmforum.mtop.mri.wsdl.mec.v1_0.ManagedElementControlRPC;
import org.tmforum.mtop.mri.wsdl.mer.v1_0.GetAllManagedElementsException;
import org.tmforum.mtop.mri.wsdl.mer.v1_0.ManagedElementRetrievalRPC;
import org.tmforum.mtop.mri.xsd.mec.v1.CreateManagedElementRequest;
import org.tmforum.mtop.mri.xsd.mec.v1.CreateManagedElementResponse;
import org.tmforum.mtop.mri.xsd.mer.v1.GetAllManagedElementsRequest;
import org.tmforum.mtop.mri.xsd.mer.v1.MultipleMeObjectsResponseType;
import org.tmforum.mtop.nrb.xsd.lp.v1.LayeredParametersListType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class SecurityImpl {

  private static final QName MANAGED_ELEMENT_RETRIEVAL_SERVICE_NAME = new QName("http://www.tmforum.org/mtop/mri/wsdl/mer/v1-0", "ManagedElementRetrievalHttp");
  private static final QName MANAGED_ELEMENT_CONTROL_SERVICE_NAME = new QName("http://www.tmforum.org/mtop/mri/wsdl/mec/v1-0", "ManagedElementControlHttp");
  private static final org.tmforum.mtop.fmw.xsd.nam.v1.ObjectFactory meCreateDataObjectFactory= new org.tmforum.mtop.fmw.xsd.nam.v1.ObjectFactory();
  private static final org.tmforum.mtop.fmw.xsd.coi.v1.ObjectFactory objFactoryCOI = new org.tmforum.mtop.fmw.xsd.coi.v1.ObjectFactory();
  private static final org.tmforum.mtop.nrb.xsd.lp.v1.ObjectFactory objFactoryLP = new org.tmforum.mtop.nrb.xsd.lp.v1.ObjectFactory();
  private static final Logger log = Logger.getLogger(SecurityImpl.class);


  private final String hostName, username, passwordType;
  private final int portNumber;

  public SecurityImpl(String hostName, String username, String passwordType, int portNumber) {
    this.hostName = hostName;
    this.portNumber = portNumber;
    this.username = username;
    this.passwordType = passwordType;
  }

  public String getAllManagedElements(){
    Service srv =   Service.create(MANAGED_ELEMENT_RETRIEVAL_SERVICE_NAME);
    ManagedElementRetrievalRPC port = srv.getPort(ManagedElementRetrievalRPC.class);

    org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
    org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

    WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(getOutProps());
    cxfEndpoint.getOutInterceptors().add(wssOut);
    BindingProvider bp = (BindingProvider) port;
    Map<String, Object> context = bp.getRequestContext();
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://"+hostName+":"+portNumber+"/mtosi/v2/ManageResourceInventory/ManagedElementRetrieval");

    GetAllManagedElementsRequest getAllManagedElementsRequest = new GetAllManagedElementsRequest();
    getAllManagedElementsRequest.setMdOrMlsnRef(createNamingAttributesMd());

    try {
      MultipleMeObjectsResponseType response = port.getAllManagedElements(createMtosiv2Header("getAllManagedElements",true), getAllManagedElementsRequest);
//      JAXBContext jaxbContext = JAXBContext.newInstance(MultipleMeObjectsResponseType.class);
//      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//      StringWriter sw = new StringWriter();
//      return sw.toString();
      log.info(response.toString());
    } catch (GetAllManagedElementsException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String createManagedElement(){
    Service srv =   Service.create(MANAGED_ELEMENT_CONTROL_SERVICE_NAME);
    ManagedElementControlRPC port = srv.getPort(ManagedElementControlRPC.class);

    org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port);
    org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();

    WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(getOutProps());
    cxfEndpoint.getOutInterceptors().add(wssOut);
    BindingProvider bp = (BindingProvider) port;
    Map<String, Object> context = bp.getRequestContext();
    context.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "https://"+hostName+":"+portNumber+"/mtosi/v2/ManageResourceInventory/ManagedElementControl");

    CreateManagedElementRequest createManagedElementRequest = new CreateManagedElementRequest();
    MECreateData createData = new MECreateData();
    createData.setName(createNamingAttributesMe("FSP3000_MTOSI2"));
    createData.setProductName(meCreateDataObjectFactory.createMECreateDataProductName("FSP 3000R7"));
    AnyListType vendorExtensionsList =  new AnyListType();
    LayeredParametersListType layeredParametersListType = objFactoryLP.createLayeredParametersListType();

    // layers
    LayeredParameterUtils.addLayer(layeredParametersListType, "PROP_ADVA_Topology");
    LayeredParameterUtils.addLayeredParameter(layeredParametersListType, "PROP_ADVA_Topology","SubnetPath", "/r7");

    LayeredParameterUtils.addLayer(layeredParametersListType, "PROP_ADVA");
    LayeredParameterUtils.addLayeredParameter(layeredParametersListType, "PROP_ADVA","IpAddress", "10.12.105.44");

    LayeredParameterUtils.addLayer(layeredParametersListType, "PROP_ADVA_SNMP");
    LayeredParameterUtils.addLayeredParameter(layeredParametersListType, "PROP_ADVA_SNMP","UseGlobalSNMPSettings", "True");

    JAXBElement<LayeredParametersListType> managementParams = new JAXBElement<>(new QName("adva.tmf864ext.v1", "managementParametersList"), LayeredParametersListType.class, layeredParametersListType);

    JAXBElement<AnyListType> vendorExtenstion = objFactoryCOI.createCommonObjectInfoTypeVendorExtensions(vendorExtensionsList);
    vendorExtensionsList.getAny().add(managementParams);
    createData.setVendorExtensions(vendorExtenstion);
    createManagedElementRequest.setCreateData(createData);

    try {
      CreateManagedElementResponse response = port.createManagedElement(createMtosiv2Header("createManagedElement", false), createManagedElementRequest);
//      JAXBContext jaxbContext = JAXBContext.newInstance(MultipleMeObjectsResponseType.class);
//      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//      StringWriter sw = new StringWriter();
//      jaxbMarshaller.marshal(response, sw);
//      return sw.toString();
      log.info(response.toString());
    } catch (CreateManagedElementException e) {
      e.printStackTrace();
    }
    return "";
  }

  private static NamingAttributeType createNamingAttributesMd() {
    NamingAttributeType namingAttributes = new NamingAttributeType();
    RelativeDistinguishNameType rdn1 = new RelativeDistinguishNameType();
    rdn1.setType("MD");
    rdn1.setValue("ADVA/Network");
    namingAttributes.getRdn().add(rdn1);
    return namingAttributes;
  }

  private static NamingAttributeType createNamingAttributesMe(String meName) {
    NamingAttributeType namingAttributes = new NamingAttributeType();
    RelativeDistinguishNameType rdn1 = new RelativeDistinguishNameType();
    RelativeDistinguishNameType rdn2 = new RelativeDistinguishNameType();
    rdn1.setType("MD");
    rdn1.setValue("ADVA/Network");
    rdn2.setType("ME");
    rdn2.setValue(meName);
    namingAttributes.getRdn().add(rdn1);
    namingAttributes.getRdn().add(rdn2);
    return namingAttributes;
  }

  private Map<String, Object> getOutProps(){
    Map<String, Object> outProps = new HashMap<>();
//    outProps.put("action", "UsernameToken Timestamp Signature Encrypt");
    outProps.put("action", "UsernameToken Timestamp");
    outProps.put("passwordType", passwordType);
    outProps.put("user", username);
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

  private static Holder<Header> createMtosiv2Header(String activity, boolean isMultibatch) {
    org.tmforum.mtop.fmw.xsd.hdr.v1.ObjectFactory headerObjectFactory = new org.tmforum.mtop.fmw.xsd.hdr.v1.ObjectFactory();
    Holder<Header> headerHolder=new Holder<>();
    Header header = headerObjectFactory.createHeader();
    header.setActivityName(activity);
    header.setMsgName(activity);
    header.setMsgType(MessageTypeType.REQUEST);
    if(isMultibatch)
      header.setCommunicationPattern(CommunicationPatternType.MULTIPLE_BATCH_RESPONSE);
    else
      header.setCommunicationPattern(CommunicationPatternType.SIMPLE_RESPONSE);
    header.setCommunicationStyle(CommunicationStyleType.RPC);
    header.setRequestedBatchSize(0L);
    headerHolder.value = header;
    return headerHolder;
  }

}
