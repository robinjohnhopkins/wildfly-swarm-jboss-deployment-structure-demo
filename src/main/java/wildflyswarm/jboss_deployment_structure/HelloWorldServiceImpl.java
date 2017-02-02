package wildflyswarm.jboss_deployment_structure;

import javax.jws.WebService;

@WebService(
  serviceName = "HelloWorldService",
  portName = "HelloWorld",
  name = "HelloWorld",
  endpointInterface = "wildflyswarm.jboss_deployment_structure.HelloWorldService",
  targetNamespace = "http://wildfly-swarm.io/HelloWorld")
public class HelloWorldServiceImpl implements HelloWorldService {

  @Override
  public String sayHello() {
    return "Hello World!";
  }

}