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
package at.pagu.soldockr;


/**
 * QueryException indicates a fault while executing a Query against solr.
 * SolrExeption is wrapped within the QueryException.
 * 
 * @author Christoph Strobl
 */
public class QueryException extends SolDockRException {

  private static final long serialVersionUID = -1358167909638012314L;

  public QueryException() {
    super();
  }

  public QueryException(String message, Throwable cause) {
    super(message, cause);
  }

  public QueryException(String message) {
    super(message);
  }

  public QueryException(Throwable cause) {
    super(cause);
  }

}
