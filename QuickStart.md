### Quick Start ###
**NOTE:** SolrRepositoryFactory will be available from v.0.2.0 on<br>
<b>NOTE:</b> Fields have to be annotated with <code>org.apache.solr.client.solrj.beans.Field</code>

<h4>Repository Factory</h4>
For many cases the usage SolrRepositoryFactory might be sufficient.<br>
<br>
Create the Interface Definition according to what is possible in spring-data-commons. Additionally extend SolrCrudRepository.<br>
In case @Query was added to the method, place holders (?0, ?1, ?...) within query will be replaced by method call parameters.<br>
Otherwise SolrRepositoryFactory will try to derive the query from the provided method name.<br>
<br>
<pre><code>public interface SolrProductRepository extends SolrCrudRepository&lt;Product, String&gt; {<br>
<br>
  //Derived Query will be "q=popularity:&lt;popularity&gt;&amp;start=&lt;page.number&gt;&amp;rows=&lt;page.size&gt;"<br>
  Page&lt;Product&gt; findByPopularity(Integer popularity, Pageable page);<br>
<br>
  //Will execute count before to determine total number of elements<br>
  //Derived Query will be "q=name:&lt;name&gt;*&amp;start=0&amp;rows=&lt;result of count query for q=name:&lt;value&gt;&gt;"<br>
  List&lt;Product&gt; findByNameStartingWith(String name);<br>
<br>
  //Derived Query will be "q=inStock:true&amp;start=&lt;page.number&gt;&amp;rows=&lt;page.size&gt;"<br>
  Page&lt;Product&gt; findByAvailableTrue(Pageable page);<br>
  <br>
  @Query(SearchableProduct.AVAILABLE_FIELD+":false")<br>
  Page&lt;Product&gt; findByAvailableFalseUsingAnnotatedQuery(Pageable page);<br>
  <br>
}<br>
<br>
public class MyProductSearchRepositoryFactory {<br>
  <br>
  @Autwired<br>
  private SolrOperations solrOperations;<br>
  <br>
  public SolrProductRepository create() {<br>
  	return new SolrRepositoryFactory(this.solrOperations).getRepository(SolrProductRepository.class);<br>
  }<br>
  <br>
}<br>
</code></pre>

<h4>Manual implementation</h4>
Instead of using the Factory to create your Repository without the need of manually implementing the query, one might create queries by hand.<br>
<br>
<pre><code>public class SolrProductRepositoryImpl extends SimpleSolrRepository&lt;Product&gt; implements SolrProductRepository {<br>
<br>
  @Override<br>
  public Page&lt;Product&gt; findByPopularity(Integer popularity, Pageable pageable) {<br>
    Query query = new SimpleQuery(new Criteria(SolrSearchableFields.POPULARITY).is(popularity));<br>
    query.setPageRequest(pageable); <br>
    return getSolrOperations().executeListQuery(query, Product.class);<br>
  }<br>
<br>
  @Override<br>
  public List&lt;Product&gt; findByNameStartingWith(String namePrefix) {<br>
    Query query = new SimpleQuery(new Criteria(SolrSearchableFields.NAME).startsWith(namePrefix));<br>
    long totalElements = getSolrOperations.executeCount(query);<br>
    query.setPageRequest(new PageRequest(0, totalElements);<br>
    return getSolrOperations().executeQuery(query, Product.class).getContent();<br>
  }<br>
<br>
  //... and so on....<br>
}<br>
</code></pre>

<h4>Repository with Custom Implementation</h4>

Some cases might require the usage of custom queries along with derived ones. In this case one might pass in the custom repository implementation.<br>
<br>
<pre><code>public interface CustomSolrRepository {<br>
<br>
  Page&lt;Product&gt; findProductsByCustomImplementation(String value, Pageable page);<br>
	<br>
}<br>
<br>
public class CustomSolrRepositoryImpl implements CustomSolrRepository {<br>
	<br>
  private SolrOperations solrTemplate;<br>
	<br>
  public CustomSolrRepositoryImpl(SolrOperations solrTemplate) {<br>
    super();<br>
    this.solrTemplate = solrTemplate;<br>
  }<br>
<br>
  @Override<br>
  public Page&lt;Product&gt; findProductsByCustomImplementation(String value, Pageable page) {<br>
    return solrTemplate.executeListQuery(new SimpleQuery(new SimpleStringCriteria("name:"+value))<br>
      .setPageRequest(page), Product.class);<br>
  }<br>
<br>
}<br>
</code></pre>

The full repository might look like this, combining default SolrCrudRepository and CustomSolrRepository.<br>
<br>
<pre><code>public interface DerivedSolrProductRepository extends CustomSolrRepository, SolrCrudRepository&lt;Product, String&gt; {<br>
<br>
  Page&lt;Product&gt; findByPopularity(Integer popularity, Pageable page);<br>
<br>
  List&lt;Product&gt; findByNameStartingWith(String name);<br>
<br>
  Page&lt;Product&gt; findByAvailableTrue(Pageable page);<br>
  <br>
  @Query(SearchableProduct.AVAILABLE_FIELD+":false")<br>
  Page&lt;Product&gt; findByAvailableFalseUsingAnnotatedQuery(Pageable page);<br>
  <br>
}<br>
</code></pre>

When creating the Repository via SolrRepositoryFactory provide the custom implementation as shown below.<br>
<br>
<pre><code>new SolrRepositoryFactory(this.solrOperations)<br>
  .getRepository(DerivedSolrProductRepository.class, new CustomSolrRepositoryImpl(this.solrOperations));<br>
</code></pre>