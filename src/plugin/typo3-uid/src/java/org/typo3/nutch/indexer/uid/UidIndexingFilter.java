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

package org.typo3.nutch.indexer.uid;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.parse.Parse;

/**
 * This Indexer adds uids to the external sites, so they can be deleted in the
 * TYPO3 Backend
 */
public class UidIndexingFilter implements IndexingFilter {

	protected static final String INDEXING_FIELD_UID = "uid";
	protected static final String CONFIGURATION_UID_OFFSET = "typo3.indexing.uid.offset";

	protected Configuration configuration;
	protected int currentUid = 1;

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		int uidOffset = Integer.parseInt(configuration.get(CONFIGURATION_UID_OFFSET, "0"));

		doc.add(INDEXING_FIELD_UID, uidOffset + currentUid);
		currentUid++;

		return doc;
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}

	/**
	 * Sets configuration
	 */
	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
	}
}
