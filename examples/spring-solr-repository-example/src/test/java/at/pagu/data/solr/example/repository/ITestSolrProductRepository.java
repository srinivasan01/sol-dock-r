/*
 * Copyright (C) 2012 j73x73r@gmail.com.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.pagu.data.solr.example.repository;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.pagu.data.solr.example.model.Product;
import at.pagu.soldockr.core.query.result.FacetEntry;
import at.pagu.soldockr.core.query.result.FacetPage;

/**
 * @author Christoph Strobl
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:at/pagu/data/solr/example/applicationContext.xml")
public class ITestSolrProductRepository extends AbstractSolrIntegrationTest {

  @Autowired
  SolrProductRepository repo;

  @After
  public void tearDown() {
    repo.deleteAll();
  }

  @Test
  public void testCRUD() {
    Assert.assertEquals(0, repo.count());

    Product initial = createProduct(1);
    repo.save(initial);
    Assert.assertEquals(1, repo.count());

    Product loaded = repo.findOne(initial.getId());
    Assert.assertEquals(initial.getName(), loaded.getName());
    
    loaded.setName("changed named");
    repo.save(loaded);
    Assert.assertEquals(1, repo.count());

    loaded = repo.findOne(initial.getId());
    Assert.assertEquals("changed named", loaded.getName());

    repo.delete(loaded);
    Assert.assertEquals(0, repo.count());
  }

  @Test
  public void testQuery() {
    Assert.assertEquals(0, repo.count());

    List<Product> baseList = createProductList(10);
    repo.save(baseList);

    Assert.assertEquals(baseList.size(), repo.count());

    Page<Product> popularProducts = repo.findByPopularity(20);
    Assert.assertEquals(1, popularProducts.getTotalElements());

    Assert.assertEquals("2", popularProducts.getContent().get(0).getId());
  }

  @Test
  public void testFacetQuery() {
    List<Product> baseList = createProductList(10);
    repo.save(baseList);

    FacetPage<Product> facetPage = repo.findByNameStartingWithAndFacetOnAvailable("pro");
    Assert.assertEquals(10, facetPage.getNumberOfElements());

    Page<FacetEntry> page = facetPage.getFacetResultPage(SolrSearchableFields.AVAILABLE);
    Assert.assertEquals(2, page.getNumberOfElements());

    for (FacetEntry entry : page) {
      Assert.assertEquals(SolrSearchableFields.AVAILABLE.getName(), entry.getField().getName());
      Assert.assertEquals(5, entry.getValueCount());
    }

  }

  @Test
  public void testFilterQuery() {
    List<Product> baseList = createProductList(10);
    repo.save(baseList);

    Page<Product> availableProducts = repo.findByAvailableTrue();
    Assert.assertEquals(5, availableProducts.getTotalElements());
    for (Product product : availableProducts) {
      Assert.assertTrue(product.isAvailable());
    }
  }

}
