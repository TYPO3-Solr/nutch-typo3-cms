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

package org.typo3.nutch.indexer.keywords;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Indexer adds the keywords metatag information to the document
 */
public class KeywordsIndexingFilter implements IndexingFilter {

	protected static final Logger LOG = LoggerFactory
			.getLogger(KeywordsIndexingFilter.class);

	protected static final String HTML_METATAG_KEYWORDS = "keywords";
	protected static final String STORE_METADATA_KEYWORDS = "metatag.keywords";

	protected Configuration conf;

	/**
	 * Adds the keywords metatag information to the document
	 */
	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		Metadata metadata = parse.getData().getParseMeta();

		String keywordsString = metadata.get(STORE_METADATA_KEYWORDS);

		if (keywordsString != null) {
			String[] keywords = keywordsString.split(",");

			for (String keyword : keywords) {
				if (keyword.length() > 0 && !keyword.equals(" ")) {
					// remove preceding spaces
					while (keyword.length() > 0 && keyword.charAt(0) == ' ') {
						keyword = keyword.substring(1);
					}

					if(keyword.length() > 0) {
						doc.add(HTML_METATAG_KEYWORDS, keyword);
					}
				}
			}
		}

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
