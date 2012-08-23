/*
 * Copyright (C) 2012 sol-dock-r authors.
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
package at.pagu.soldockr.core;

import java.util.Collection;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.data.domain.Page;

import at.pagu.soldockr.core.convert.SolrConverter;
import at.pagu.soldockr.core.query.FacetQuery;
import at.pagu.soldockr.core.query.Query;
import at.pagu.soldockr.core.query.SolDockRQuery;
import at.pagu.soldockr.core.query.result.FacetPage;

/**
 * Interface that specifies a basic set of Solr operations.
 * 
 * @author Christoph Strobl
 */
public interface SolrOperations {

  /**
   * Get the underlying SolrServer instance
   * 
   * @return
   */
  SolrServer getSolrServer();

  /**
   * Execute ping against solrServer and return duration in msec
   * 
   * @return
   */
  SolrPingResponse executePing();

  /**
   * return number of elements found by for given query
   * 
   * @param query
   * @return
   */
  long executeCount(SolDockRQuery query);

  /**
   * Execute add operation against solr
   * 
   * @param obj
   * @return
   */
  UpdateResponse executeAddBean(Object obj);

  /**
   * Add a collection of beans to solr
   * 
   * @param beans
   * @return
   */
  UpdateResponse executeAddBeans(Collection<?> beans);

  /**
   * Add a solrj input document to solr
   * 
   * @param document
   * @return
   */
  UpdateResponse executeAddDocument(SolrInputDocument document);

  /**
   * Add multiple solrj input documents to solr
   * 
   * @param documents
   * @return
   */
  UpdateResponse executeAddDocuments(Collection<SolrInputDocument> documents);

  /**
   * Find and delete all objects matching the provided Query
   * 
   * @param query
   * @return
   */
  UpdateResponse executeDelete(SolDockRQuery query);

  /**
   * Detele the one object with provided id
   * 
   * @param id
   * @return
   */
  UpdateResponse executeDeleteById(String id);

  /**
   * Delete objects with given ids
   * 
   * @param id
   * @return
   */
  UpdateResponse executeDeleteById(Collection<String> id);

  /**
   * Execute query against Solr
   * 
   * @param query
   * @return
   */
  QueryResponse executeQuery(SolDockRQuery query);

  /**
   * Execute the query against solr and return the first returned object
   * 
   * @param query
   * @param clazz
   * @return the first matching object
   */
  <T> T executeObjectQuery(Query query, Class<T> clazz);

  /**
   * Execute the query against solr and retrun result as {@link Page}
   * 
   * @param query
   * @param clazz
   * @return
   */
  <T> Page<T> executeListQuery(Query query, Class<T> clazz);

  /**
   * Execute a facet query against solr
   * facet result will be returned along with query result within the FacetPage
   * 
   * @param query
   * @param clazz
   * @return
   */
  <T> FacetPage<T> executeFacetQuery(FacetQuery query, Class<T> clazz);

  /**
   * Send commit command
   */
  void executeCommit();

  /**
   * send rollback command
   */
  void executeRollback();

  /**
   * Convert given bean into a solrj InputDocument
   * 
   * @param bean
   * @return
   */
  SolrInputDocument convertBeanToSolrInputDocument(Object bean);

  /**
   * @return Converter in use
   */
  SolrConverter getConverter();

}
