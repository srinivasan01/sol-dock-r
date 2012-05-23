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
package at.pagu.soldockr.core.query;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import at.pagu.soldockr.ApiUsageException;
import at.pagu.soldockr.core.query.Criteria;
import at.pagu.soldockr.core.query.Field;
import at.pagu.soldockr.core.query.SimpleField;

public class CriteriaTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCriteriaForNullString() {
    new Criteria((String) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCriteriaForNullField() {
    new Criteria((Field) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCriteriaForNullFieldName() {
    new Criteria(new SimpleField(StringUtils.EMPTY));
  }

  @Test
  public void testIs() {
    Criteria criteria = new Criteria("field_1").is("is");
    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:is", criteria.createQueryString());
  }

  @Test
  public void testMultipleIs() {
    Criteria criteria = new Criteria("field_1").is("is").is("another is");
    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:(is \"another is\")", criteria.createQueryString());
  }

  @Test(expected = ApiUsageException.class)
  public void testContainsWithBlank() {
    new Criteria("field_1").contains("no blank");
  }

  @Test(expected = ApiUsageException.class)
  public void testStartsWithBlank() {
    new Criteria("field_1").startsWith("no blank");
  }

  @Test(expected = ApiUsageException.class)
  public void testEndsWithBlank() {
    new Criteria("field_1").endsWith("no blank");
  }

  @Test
  public void testEndsWith() {
    Criteria criteria = new Criteria("field_1").endsWith("end");

    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:*end", criteria.createQueryString());
  }

  @Test
  public void testStartsWith() {
    Criteria criteria = new Criteria("field_1").startsWith("start");

    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:start*", criteria.createQueryString());
  }

  @Test
  public void testContains() {
    Criteria criteria = new Criteria("field_1").contains("contains");

    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:*contains*", criteria.createQueryString());
  }

  @Test
  public void testExpression() {
    Criteria criteria = new Criteria("field_1").expression("(have fun using +solr && expressions*)");
    Assert.assertEquals("field_1:(have fun using +solr && expressions*)", criteria.createQueryString());
  }

  @Test
  public void testCriteriaChain() {
    Criteria criteria = new Criteria("field_1").startsWith("start").endsWith("end").contains("contains").is("is");
    Assert.assertEquals("field_1", criteria.getField().getName());
    Assert.assertEquals("field_1:(start* *end *contains* is)", criteria.createQueryString());
  }

  @Test
  public void testAnd() {
    Criteria criteria = new Criteria("field_1").startsWith("start").endsWith("end").and("field_2").startsWith("2start").endsWith("2end");
    Assert.assertEquals("field_2", criteria.getField().getName());
    Assert.assertEquals("field_1:(start* *end) AND field_2:(2start* *2end)", criteria.createQueryString());
  }

  @Test
  public void testOr() {
    Criteria criteria = new Criteria("field_1").startsWith("start").or("field_2").endsWith("end").startsWith("start2");
    Assert.assertEquals("field_1:start* OR field_2:(*end start2*)", criteria.createQueryString());
  }

  @Test
  public void testCriteriaWithWhiteSpace() {
    Criteria criteria = new Criteria("field_1").is("white space");
    Assert.assertEquals("field_1:\"white space\"", criteria.createQueryString());
  }

  @Test
  public void testCriteriaWithDoubleQuotes() {
    Criteria criteria = new Criteria("field_1").is("with \"quote");
    Assert.assertEquals("field_1:\"with \\\"quote\"", criteria.createQueryString());
  }
  
  @Test
  public void testIsNot() {
    Criteria criteria = new Criteria("field_1").isNot("value_1");
    Assert.assertEquals("field_1:-value_1", criteria.createQueryString());
  }
  
  @Test
  public void testFuzzy() {
    Criteria criteria = new Criteria("field_1").fuzzy("value_1");
    Assert.assertEquals("field_1:value_1~", criteria.createQueryString());
  }
  
  @Test
  public void testFuzzyWithDistance() {
    Criteria criteria = new Criteria("field_1").fuzzy("value_1", 0.5f);
    Assert.assertEquals("field_1:value_1~0.5", criteria.createQueryString());
  }
  
  @Test(expected=ApiUsageException.class)
  public void testFuzzyWithNegativeDistance() {
    new Criteria("field_1").fuzzy("value_1", -0.5f);
  }
  
  @Test(expected=ApiUsageException.class)
  public void testFuzzyWithTooHighDistance() {
    new Criteria("field_1").fuzzy("value_1", 1.5f);
  }
  
  @Test
  public void testBoost() {
    Criteria criteria = new Criteria("field_1").is("value_1").boost(2f);
    Assert.assertEquals("field_1:value_1^2.0", criteria.createQueryString());
  }
  
  @Test
  public void testBoostMultipleValues() {
    Criteria criteria = new Criteria("field_1").is("value_1").is("value_2").boost(2f);
    Assert.assertEquals("field_1:(value_1 value_2)^2.0", criteria.createQueryString());
  }
  
  @Test
  public void testBoostMultipleCriteriasValues() {
    Criteria criteria = new Criteria("field_1").is("value_1").is("value_2").boost(2f).and("field_3").is("value_3");
    Assert.assertEquals("field_1:(value_1 value_2)^2.0 AND field_3:value_3", criteria.createQueryString());
  }
}
