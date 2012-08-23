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

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Callback interface for data access code that works with SolrServer. 
 * To be used with SolrTemplate execute methods
 * 
 * @param <T>
 * 
 * @author Christoph Strobl
 */
public interface SolrCallback<T> {

  /**
   * @param solrServer execute
   * @return
   * @throws SolrServerException
   * @throws IOException
   */
  T doInSolr(SolrServer solrServer) throws SolrServerException, IOException;

}
