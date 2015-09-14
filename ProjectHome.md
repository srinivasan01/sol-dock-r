sol-dock-r offers a [spring-data](http://www.springsource.org/spring-data) centric approach to [Apache Solr](http://lucene.apache.org/solr/). It's built upon solrj and [springframework](http://www.springsource.org/) providing a thin layer abstracting and enriching search API experience.

The Project targets both, those who quickly want to get started using solr, as well as the ones creating enterprise solutions.


---

**! This project has moved !**

<font color='#006600'>
**You may follow us on github: <a href='https://github.com/SpringSource/spring-data-solr/'>spring-data-solr</a>
</font>

---**

### Goals ###
  * simple to use API
  * SolrTemplate
  * spring-data integration
  * CDI Support (target for v.0.3.0)

...have a look at the [Roadmap](Roadmap.md) for more details!

### Current Release ###
**[v.0.2.0](http://code.google.com/p/sol-dock-r/downloads/list)** is now available as [jar-without-dependencies](http://sol-dock-r.googlecode.com/files/sol-dock-r-0.2.0.jar).

_**Mavenized?**_

_feel free to build on your own http://code.google.com/p/sol-dock-r/source/checkout_

### Get Started ###
  * Check out the [QuickStart](QuickStart.md) and [Example](http://sol-dock-r.googlecode.com/svn/trunk/examples/spring-solr-repository-example/) to get going within minutes.
  * Learn how to [configure](Configuration.md) SolrServerTemplate
  * [API Reference](QuerySupport.md)

### Usage ###

```java

@Autowired SolrOperations solrTemplate;

//init repository
SolrRepositoryFactory factory = new SolrRepositoryFactory(solrTemplate);
SimpleSolrRepository solrRepository = factory.getRepository(SimpleSolrRepository.class);

//add Bean
solrRepository.save(new MySearchBean("id-1"));

//check if exists
boolean beanExists = solrRepository.exists("id-1");

//count ellements in collection
long beanCount = solrRepository.count();

//find single bean by id
MySearchBean bean = solrRepository.findOne("id-1");

//find all Beans
Iterable<MySearchBean> allBeans = solrRepository.findAll();

//Page through elements in collection
Page<MySearchBean> page = solrRepository.findAll(new PageRequest(0, 10));

//Delete bean
solrRepository.delete("id-1");
```


---


&lt;wiki:gadget url="http://www.ohloh.net/p/601358/widgets/project\_partner\_badge.xml" height="53" border="0"/&gt;