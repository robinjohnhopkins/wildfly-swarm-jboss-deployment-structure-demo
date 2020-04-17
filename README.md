## Wildfly swarm 

WildFly Swarm is a framework based on the popular WildFly Java application server to enable the creation of small, standalone microservice-based applications. WildFly Swarm is capable of producing so-called just enough app-server to support each component of your system.

https://docs.thorntail.io/2018.5.0/#creating-an-uberjar


## FIX ADDED
Specified below (see MANIFEST.MF to declare module dependencies)

I have ALSO added an article which gave me the solution:

HowToSpecifyADependency.md


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


Hello World!
```

### With org.apache.cxf.jaxws.JaxWsProxyFactoryBean

``` sh
curl 'localhost:8080/demo/api/hello?cxf-proxy=true'
```

### MANIFEST.MF to declare module dependencies
```
      This demo uses Apache CXF 3.1.6

      When calling
      curl 'localhost:8080/demo/api/hello'
      Hello World!
      was returned no problem

      HOWEVER when calling curl 'localhost:8080/demo/api/hello?cxf-proxy=true'

      java.lang.NoClassDefFoundError: org.apache.cxf.jaxws.JaxWsProxyFactoryBean

      The class org.apache.cxf.jaxws.JaxWsProxyFactoryBean
      is found in
      .m2/repository/org/apache/cxf/cxf-rt-frontend-jaxws/3.1.6/cxf-rt-frontend-jaxws-3.1.6.jar


      A similar named jar found in
      https://github.com/suvo/jboss-eap/blob/master/modules/system/layers/base/org/apache/cxf/impl/main/module.xml
      <module xmlns="urn:jboss:module:1.1" name="org.apache.cxf.impl">
      <resource-root path="cxf-rt-frontend-jaxws-2.7.14.redhat-1.jar"/>

      is mentioned on the web as being what is needed as a fix

      i.e. add
      <module name="org.apache.cxf" export="true" />
      <module name="org.apache.cxf.impl" export="true" />

      In my case the did not work

      In the end I created
      src/main/resources/META-INF/MANIFEST.MF
        Dependencies: org.apache.cxf
        Dependencies: org.apache.cxf.impl

      This enabled running of the swarm jar and calling the end point that previously errored
```

## test api

```
curl 'localhost:8080/demo/api/hello/test1'
{"val":"null"}

curl 'localhost:8080/demo/api/hello/test1?p1'
{"val":"empty"}

curl 'localhost:8080/demo/api/hello/test1?p1=red'
{"val":"red"}


curl 'localhost:8080/demo/api/hello/test2'
{"val":"Mr. hello"}

in logs:

2020-04-17 13:59:45,911 INFO  [org.wildfly.swarm] (main) THORN99999: Thorntail is Ready
2020-04-17 13:59:53,651 INFO  [stdout] (default task-1) aaa.bbb.Test class Loaded  [VERSION-1.0]
2020-04-17 13:59:53,652 INFO  [stdout] (default task-1) aaa.bbb.Test sayHello() called v1



2020-04-17 14:07:38,343 INFO  [stdout] (default task-1) aaa.bbb.Test class Loaded  [VERSION-1.1]
2020-04-17 14:07:38,345 INFO  [stdout] (default task-1) aaa.bbb.Test sayHello() called v1.1

```
