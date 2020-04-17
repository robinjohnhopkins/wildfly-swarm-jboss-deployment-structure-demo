## How to specify a dependency on a JBoss Module in JBoss EAP 6 / 7

https://access.redhat.com/solutions/341703


### Environment
Red Hat JBoss Enterprise Application Platform (EAP)
6.x
7.x

### Issue
How to specify a dependency on a JBoss Module in JBoss EAP 6
Dependencies in MANIFEST.MF in JBoss EAP 6
Referring to jar file from modules. We have a jar file which has to be referenced from modules ($JBOSS_HOME/modules/test/api/main/xx.jar) and not from WEB-INF/lib of EAR application. Therefore added Dependencies: test.api in MANIFEST.MF and below code in jboss-deployment-structure.xml. However ClassNotFoundException is thrown at runtime. How this can be achieved. Jar file has to be in modules as this is required for other dependencies.

```
<deployment>
        <dependencies>
            <module name="test.api" />
        </dependencies>
    </deployment>
```

How to declare a dependency on a JBoss module in JBoss EAP 6?
I created com/mysql/main under modules and placed the mysql-connector-java-5.1.35-bin.jar in there. I created a module.xml file with these contents and put that in the same directory, still When I run my application on the server it says that it can not find the driver.:

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="com.mysql"> 
    <resources>  
        <resource-root path="mysql-connector-java-5.1.35-bin.jar" />  
    </resources>
    <dependencies>  
        <module name="javax.api"/>  
        <module name="javax.transaction.api"/>  
    </dependencies>  
</module>
```

### Resolution
JBoss Module dependencies can be specified either in the jboss-deployment-structure.xml or in the MANIFEST.MF of the jar containing classes that need access to the module.

Via jboss-deployment-structure.xml

The jboss-deployment-structure.xml goes in the top level deployment only and the file is used to configure the deployment and any sub-deployments.

The jboss-deployment-structure.xml goes in the WEB-INF if the top-level deployment is a war and goes in META-INF for all other top-level deployment types. A top-level deployment is the deployment in the deployments directory, so the jboss-deployment-structure.xml is only valid in the top-level, if you put it in a sub-deployment of an ear for example, it would have no effect.

Take an ear containing an ejb for example.

Example structure:
```
example.ear
 - example-ejb.jar
  - META-INF/
   - MANIFEST.MF
```

To add the dependency org.apache.cxf to the ejb only it would look like:

```
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
  <deployment>
  </deployment>
  <sub-deployment name="example-ejb.jar">
    <dependencies>
      <module name="org.apache.cxf" />
    </dependencies>
  </sub-deployment>
</jboss-deployment-structure>
```

To add the org.apache.cxf dependency to all sub-deployments of the ear, it can be specified on the ear which is configured by the <deploment> section in this example, since the ear is the top-level deployment. Setting the option export to true on the dependency will export the dependency to all of the sub-deployments in the ear.

```
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
  <deployment>
    <dependencies>
      <module name="org.apache.cxf" export="true"/>
    </dependencies>
  </deployment>
  <sub-deployment name="example-ejb.jar">
    <dependencies>
    </dependencies>
  </sub-deployment>
</jboss-deployment-structure>
```

One more example, if the top-level deployment is a war named example.war with a structure like this:

```
example.war
 - WEB-INF
  - jboss-deployment-structure.xml
The jboss-deployment-structure.xml would look like this:
```

```
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
  <deployment>
    <dependencies>
      <module name="org.apache.cxf"/>
    </dependencies>
  </deployment>
</jboss-deployment-structure>
```

### Via MANIFEST.MF Dependencies

Example of depending on the org.apache.cxf module from an EJB jar sub deployment of an ear.

Example structure:

```
example.ear
 - example-ejb.jar
  - META-INF/
   - MANIFEST.MF
```

You add the module to the Dependencies line.

```
Dependencies: org.apache.cxf
```
You can specify export after the module if you want to export this module to other modules that depend on your module.

```
Dependencies: org.apache.cxf export
```

If you have more than one module they would be separated by a ,.

```
Dependencies: org.apache.cxf export, org.springframework.spring
```

For dependencies involving a deployed resource adapter an explicit dependency to the resource adapter archive is needed. Note deployment. preceeds the resource adapter.

```
Dependencies: deployment.myrar.rar
```
However, MANIFEST.MF does not have a way to exclude specific JBoss libraries that you wish to override. For this functionality you would need to use the jboss-deployment-structure.xml method.

Depending on a JBoss Module and viewing files from META-INF and META-INF/services

If a custom JBoss Module is created and an application depends on it and needs to have access to files in the META-INF or META-INF/services paths, the meta-inf / services options need to be specified in the jboss-deployment-structure.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="my.apache.vfs">
  <resources>
    <resource-root path="commons-vfs2-2.1.jar" />
  </resources>
  <dependencies>
    <module name="javaee.api"/>
    <module name="javax.api"/>
  </dependencies>
</module>
```

To make the META-INF path available (applies to WEB-INF as well) to the deployment, the jboss-deployment-structure.xml would look like this:

```
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
  <deployment>
    <dependencies>
      <module name="my.apache.vfs" meta-inf="import"/>
    </dependencies>
  </deployment>
</jboss-deployment-structure>
```

Where import allows the deployment to see the META-INF resources and export allows the deployment to see the resources as well as it is exported so that any other deployment depending on this deployment's module can also see the resources. The default is none which does not import and the resources are not visible.

Similarly for services, this makes the META-INF/services resources available, for example:

```
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.2">
  <deployment>
    <dependencies>
      <module name="my.apache.vfs" services="export"/>
    </dependencies>
  </deployment>
</jboss-deployment-structure>
```

This is a bit of overlap with meta-inf.
Due to WFLY-3971 this will not work in EAP6, as workaround meta-inf can be used.

Making annotations on classes in a custom module visible to a deployment depending on that module

See How to create a jandex index for a jar so that JBoss EAP 6 or EAP 7 modules will scan a modules annotations

### Diagnostic Steps
If there is a jboss-deployment-structure.xml packaged in a sub deployment of an application, a warning is logged indicating that it is not valid in this location and it is ignored since it is not in the top level deployment:

```
WARN  [org.jboss.as.server.deployment] (MSC service thread 1-7) JBAS015850: /home/jboss-eap-6.4/standalone/deployments/example.ear/example-ejb.jar/META-INF/jboss-deployment-structure.xml in subdeployment ignored. jboss-deployment-structure.xml is only parsed for top level deployments.
```
