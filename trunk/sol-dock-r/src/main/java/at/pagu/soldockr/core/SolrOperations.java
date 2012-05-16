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

import at.pagu.soldockr.core.query.Query;

public interface SolrOperations {
  
  /**
   * Get the underlying SolrServer instance
   * @return
   */
  SolrServer getSolrServer();
  
  /**
   * Execute ping against solrServer and return duration in msec
   * @return 
   */
  SolrPingResponse executePing();
  
  UpdateResponse executeAddBean(Object obj);
  
  UpdateResponse executeAddBeans(Collection<?> beans);
  
  UpdateResponse executeAddDocument(SolrInputDocument document);
  
  UpdateResponse executeAddDocuments(Collection<SolrInputDocument> documents);
  
  UpdateResponse executeDelete(Query query);
  
  UpdateResponse executeDeleteById(String id);
  
  QueryResponse executeQuery(Query query);
  
  <T> T executeObjectQuery(Query query, Class<T> clazz);
  
  <T> Page<T> executeListQuery(Query query, Class<T> clazz);

  void executeCommit();
  
}
