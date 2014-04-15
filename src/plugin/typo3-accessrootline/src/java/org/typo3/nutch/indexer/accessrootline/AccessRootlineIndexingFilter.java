/**
 * Copyright 2012 Ingo Renner <ingo.renner@dkd.de>
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

package org.typo3.nutch.indexer.accessrootline;

//import javax.xml.soap.Text;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.parse.Parse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Indexer adds the access information to the document
 */
public class AccessRootlineIndexingFilter implements IndexingFilter {

	protected static final Logger LOG = LoggerFactory
			.getLogger(AccessRootlineIndexingFilter.class);

	protected static final String INDEXING_FIELD = "access";
	protected static final String CONF_ACCESS_PROPERTY = "typo3.access.rootline";

	protected Configuration conf;

	/**
	 * Adds the Solr TYPO3 Access information to the document based on the
	 * typo3.access.rootline parameter
	 */
	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		// Index the access rootline
		doc.add(INDEXING_FIELD, conf.get(CONF_ACCESS_PROPERTY, "c:0"));

		return doc;
	}

	@Override
	public Configuration getConf() {
		return conf;
	}

	/**
	 * handles conf assignment
	 */
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}
}
