/**
 * Copyright 2013 Ingo Renner <ingo@typo3.org>
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

package org.typo3.nutch.indexer.endtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * This Indexer adds the endtime field to the document,
 * defaulting to unix time 0 (January 1, 1970, 00:00:00 GMT).
 *
 */
public class EndtimeIndexingFilter implements IndexingFilter {

	protected static final Logger LOG = LoggerFactory
			.getLogger(EndtimeIndexingFilter.class);

	protected static final String INDEXING_FIELD = "endtime";
	protected static final String CONF_ENDTIME_PROPERTY = "typo3.endtime";

	protected Configuration conf;

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

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		// fetch the setting, default to 1970-01-01
		String isoDateOrSecondsToAdd = conf.get(CONF_ENDTIME_PROPERTY, "1970-01-01T00:00:00Z");
		long epoch = 0;

		// Check if configured value is a long value
		long milliSecondsToAdd = 0;
		try {
			milliSecondsToAdd = Long.parseLong(isoDateOrSecondsToAdd);
		} catch (NumberFormatException e) {
			milliSecondsToAdd = 0;
		}

		// If milliSecondsToAdd could be parsed add them to the current timestamp.
		if (milliSecondsToAdd > 0) {
			epoch = new Date().getTime() + milliSecondsToAdd;
		} else {
			// Try to convert configured ISO date to time stamp
			try {
				epoch = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(isoDateOrSecondsToAdd).getTime();
			} catch (ParseException e) {
				LOG.error("ERROR! Cannot parse date, must fit pattern yyyy-MM-dd'T'HH:mm:ssZ : " + isoDateOrSecondsToAdd);
			}
		}

		// Index the endtime
		doc.add(INDEXING_FIELD, new Date(epoch));

		return doc;
	}

}
