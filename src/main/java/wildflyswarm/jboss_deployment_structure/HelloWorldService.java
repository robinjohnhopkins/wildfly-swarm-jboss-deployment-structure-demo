package wildflyswarm.jboss_deployment_structure;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://wildfly-swarm.io/HelloWorld")
public interface HelloWorldService {

  @WebMethod
  public String sayHello();

}
