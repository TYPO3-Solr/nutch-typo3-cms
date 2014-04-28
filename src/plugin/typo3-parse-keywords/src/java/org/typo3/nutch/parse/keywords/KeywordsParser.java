/**
 * Copyright 2012-2014 
 *     Ingo Renner <ingo.renner@dkd.de>, 
 *     Phuong Doan <phuong.doan@dkd.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typo3.nutch.parse.keywords;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DocumentFragment;

/**
 * Gets the keywords metatag information from the website and stores it in the metadata
 */
public class KeywordsParser implements HtmlParseFilter {

  private static final Logger LOG = LoggerFactory.getLogger(KeywordsParser.class);

  protected static final String HTML_METATAG_KEYWORDS = "keywords";
  protected static final String STORE_METADATA_KEYWORDS = "metatag.keywords";

  private Configuration conf;

  /**
   * check if the keywords metatag is set and store it in the metadata
   */
  @Override
  public ParseResult filter(Content content, ParseResult parseResult,
      HTMLMetaTags metaTags, DocumentFragment doc) {
    Parse parse = parseResult.get(content.getUrl());
    Metadata metadata = parse.getData().getParseMeta ();

    // check in the metadata first : the tika-parser
    // might have stored the values there already
    for (String mdName : metadata.names()) {
      String value = metadata.get(mdName);

      checkMetatag(metadata, mdName, value);
    }
    
    Metadata generalMetaTags = metaTags.getGeneralTags();
    for (String name: generalMetaTags.names()){
    	String value = generalMetaTags.get(name);
    	checkMetatag(metadata, name, value);
    }
    
    Properties httpequiv = metaTags.getHttpEquivTags();
    for (Enumeration<?> tagNames = httpequiv.propertyNames(); tagNames.hasMoreElements ();) {
      String name = (String)tagNames.nextElement();
      String value = httpequiv.getProperty(name);

      checkMetatag(metadata, name, value);
    }

    return parseResult;
  }

  @Override
  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  @Override
  public Configuration getConf() {
    return this.conf;
  }

  /**
   * checks if this is the keywords metatag and sotres it at the metadata
   * @param metadata
   * @param name
   * @param value
   */
  protected void checkMetatag(Metadata metadata, String name, String value) {
    if (name.equals(HTML_METATAG_KEYWORDS))
    {
      metadata.add(STORE_METADATA_KEYWORDS, value);
    }
  }
}
