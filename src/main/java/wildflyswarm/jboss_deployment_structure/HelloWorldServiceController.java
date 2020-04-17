package wildflyswarm.jboss_deployment_structure;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

@Path("/hello")
public class HelloWorldServiceController {

  private static final String ENDPOINT = "http://localhost:8080/demo/HelloWorldService";

  @GET
  public String get(@QueryParam("cxf-proxy") boolean withCxfProxy) {
    HelloWorldService client;
    if (withCxfProxy) {
      client = getClientWithCxfProxy();
    } else {
      client = getClient();
    }

    return client.sayHello();
  }

  @Path("/test1")
  @GET
  public String get(@QueryParam("p1") String p1) {
    if (p1 == null) {
      return "{\"val\":\"null\"}";
    } else {
      if (p1.length() == 0) {
        return "{\"val\":\"empty\"}";
      } else {
        return "{\"val\":\"" + p1 + "\"}";
      }
    }
  }

  @Path("/test2")
  @GET
  public String get2(@QueryParam("p1") String p1) {

    aaa.bbb.Test test = new aaa.bbb.Test();

    return "{\"val\":\"" + test.sayHello("hello") + "\"}";
  }

  private HelloWorldService getClient() {
    QName serviceName = new QName("http://wildfly-swarm.io/HelloWorld", "HelloWorldService");

    try {
      Service service = Service.create(new URL(ENDPOINT +"?wsdl"), serviceName);
      return service.getPort(HelloWorldService.class);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }

  private HelloWorldService getClientWithCxfProxy() {
    JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
    factory.setServiceClass(HelloWorldService.class);
    factory.setAddress(ENDPOINT);
    return (HelloWorldService) factory.create();
  }

}
