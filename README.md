# wildfly-swarm-jboss-deployment-structure-demo

WildFly Swarm Demo uses jboss-deployment-structure.xml .

## Build & Run

``` sh
./mvnw clean package &&\
java -jar target/wildfly-swarm-jboss-deployment-structure-demo-swarm.jar
```

## Access API

### With JAX-WS Service

``` sh
curl 'localhost:8080/demo/api/hello'
```

### With org.apache.cxf.jaxws.JaxWsProxyFactoryBean

``` sh
curl 'localhost:8080/demo/api/hello?cxf-proxy=true'
```
