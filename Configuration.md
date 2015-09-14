### Context Configuration ###

Configure your application Context according to your needs.

#### Single Core HTTP Solr Server ####
Connect to a single core solr server instance via HTTP.

```
<bean id="solrTemplate" class="at.pagu.soldockr.core.SolrTemplate" scope="singleton">
  <constructor-arg>
    <bean class="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer">
      <constructor-arg value="${solr.host}" />
      <property name="connectionTimeout" value="100" />
      <property name="defaultMaxConnectionsPerHost" value="100" />
      <property name="maxTotalConnections" value="1000" />
    </bean>
  </constructor-arg>
</bean>
```

#### Multi Core HTTP Solr Server ####
Connect to a multicore solre server instance via HTTP and reuse SolrServer configuration for each instance.
**NOTE:** One SolrTemplate can only server one SolrCore

```
<bean id="solrServer" abstract="true">
    <constructor-arg value="${solr.host}" />
    <property name="connectionTimeout" value="100" />
    <property name="defaultMaxConnectionsPerHost" value="100" />
    <property name="maxTotalConnections" value="1000" /> 
</bean>

<bean id="solrTemplateCore_1" class="at.pagu.soldockr.core.SolrTemplate" scope="singleton">
    <constructor-arg>
      <bean class="at.pagu.soldockr.core.HttpSolrServerFactory">
        <constructor-arg>
          <bean class="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer" parent="solrServer" />
        </constructor-arg>
        <constructor-arg>
          <value type="java.lang.String">core1</value>
        </constructor-arg>
      </bean>
    </constructor-arg>  
</bean>

<bean id="solrTemplateCore_2" class="at.pagu.soldockr.core.SolrTemplate" scope="singleton">
    <constructor-arg>
      <bean class="at.pagu.soldockr.core.HttpSolrServerFactory">
        <constructor-arg>
          <bean class="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer" parent="solrServer" />
        </constructor-arg>
        <constructor-arg>
          <value type="java.lang.String">core2</value>
        </constructor-arg>
      </bean>
    </constructor-arg>   
</bean>
```

#### Embedded Solr Server ####
In order to run an embedded solr server the optional dependency to 'solr-core'.
This option might be useful for integration testing, but is not recommended for production use.

```
<dependency>
  <groupId>org.apache.solr</groupId>
  <artifactId>solr-core</artifactId>
  <version>${solr.version}</version>
</dependency>
```

The constructor argument points to the directory holding the 'solr.xml' configuration file.
Note: see [ResourceUtils](http://static.springsource.org/spring/docs/3.1.0.M1/javadoc-api/org/springframework/util/ResourceUtils.html) for possible formats


```
<bean id="solrTemplate" class="at.pagu.soldockr.core.SolrTemplate"> 
  <constructor-arg>
    <bean class="at.pagu.soldockr.core.EmbeddedSolrServerFactory">
      <constructor-arg>
        <value type="java.lang.String">classpath:your/namespace/here/core/config</value>
      </constructor-arg>
    </bean>
  </constructor-arg> 
</bean>
```

#### Authentication Support ####
Configure solr client to work with servers configured for authentication by using the underlying HttpClient.

```
<bean id="solrTemplate" class="at.pagu.soldockr.core.SolrTemplate"> 
  <constructor-arg> 
    <bean class="at.pagu.soldockr.core.HttpSolrServerFactory"> 
      <constructor-arg> 
        <bean class="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer" parent="solrServer" /> 
      </constructor-arg> 
      <constructor-arg> 
        <value type="java.lang.String">${solr.core}</value> 
      </constructor-arg> 
      <constructor-arg> 
        <bean class="org.apache.http.auth.UsernamePasswordCredentials">
          <constructor-arg> 
            <value type="java.lang.String">${solr.auth.username}</value> 
          <constructor-arg> 
          <constructor-arg> 
        	<value type="java.lang.String">${solr.auth.password}</value> 
          <constructor-arg> 
        </bean>
      <constructor-arg> 
        <!-- Use one of the following: BASIC, DIGEST, NTLM -->
        <value type="java.lang.String">${solr.auth.method}</value> 
      </constructor-arg> 
    </bean> 
  </constructor-arg> 
</bean>
```