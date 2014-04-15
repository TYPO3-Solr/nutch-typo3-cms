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

package org.typo3.nutch.indexer.sitehash;

//import javax.xml.soap.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.parse.Parse;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Indexer adds siteHash information to the document
 */
public class SiteHashIndexingFilter implements IndexingFilter {

	protected static final Logger LOG = LoggerFactory
			.getLogger(SiteHashIndexingFilter.class);

	protected static final String CONF_BASEURL_PROPERTY = "typo3.baseUrl";
	protected static final String CONF_APIKEY_PROPERTY = "typo3.api.key";
	protected static final String TYPO3_API_CALL = "eID=tx_solr_api";
	protected static final String API_SPECIFICATION = "api=siteHash";
	protected static final String API_PARAMETER = "domain";
	protected static final String API_PARAMETER_KEY = "apiKey";
	protected static final String INDEXING_FIELD_SITEHASH = "siteHash";
	protected static final String INDEXING_FIELD_ID = "id";

	protected Configuration conf;
	protected final ObjectMapper jsonMapper = new ObjectMapper();
	protected boolean siteHashApiConnectionError = false;
	protected final Map<String, String> hashCache = new HashMap<String, String>();

	/**
	 * Gets the sitehash for the domain from the Solr Typo3 Api and index it
	 */
	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text url,
			CrawlDatum datum, Inlinks inlinks) throws IndexingException {

		// if wasn't able to connect to the Solr TYPO3 API, don't retry it for
		// every document
		if (siteHashApiConnectionError) {
			return null;
		}

		// get the base domain for the document url
		String requestedDomain = null;
		try {
			requestedDomain = new URL(url.toString()).getHost();
		} catch (MalformedURLException e) {
			LOG.error("ERROR! Could not get Domain Name for requested url: "
					+ url.toString());
			throw (new IndexingException(e));
		}

		// lookup in the hashCache if there is already a siteHash for the
		// requested Domain
		String siteHash = hashCache.get(requestedDomain);

		if (siteHash == null) {

			LOG.info("Getting siteHash for domain: " + requestedDomain);

			InputStream stream = getApiInputStream(
					conf.get(CONF_BASEURL_PROPERTY, ""), requestedDomain,
					conf.get(CONF_APIKEY_PROPERTY, ""));
			siteHash = getSiteHashFromJsonStream(stream);

			// Cache the siteHash
			hashCache.put(requestedDomain, siteHash);
		}

		// Index the siteHash
		doc.add(INDEXING_FIELD_SITEHASH, siteHash.toString());

		// Index the id (siteHash + nutch_external + url)
		doc.add(INDEXING_FIELD_ID,
				siteHash + "/tx_nutch_external/" + url.toString());

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

	/**
	 * Gets the InputStream of the Solr TYPO3 Api located at apiBaseUrl with the
	 * requestedDomain parameter
	 *
	 * @param apiBaseUrl
	 * @param requestedDomain
	 * @return
	 * @throws IndexingException
	 */
	protected InputStream getApiInputStream(String apiBaseUrl,
			String requestedDomain, String apiKey) throws IndexingException {
		URL apiUrl = null;
		InputStream stream = null;

		try {
			apiUrl = new URL(constructApiUrl(apiBaseUrl, requestedDomain,
					apiKey));
		} catch (MalformedURLException e) {
			LOG.error("ERROR! Property typo3.baseUrl in nutch-site.xml is either missing or false");
			throw (new IndexingException(e));
		}

		// connect to the api Url and receive output as a stream
		try {
			stream = apiUrl.openStream();

			LOG.info("TYPO3 Solr API Request sent.");
		} catch (IOException e) {
			siteHashApiConnectionError = true;
			LOG.error("ERROR! could not connect to " + apiUrl);
			throw (new IndexingException(e));
		}

		return stream;
	}

	/**
	 * gets the siteHash from the received Json data in form of InputStream
	 *
	 * @param stream
	 *            with Json data
	 * @return siteHash
	 * @throws IndexingException
	 */
	protected String getSiteHashFromJsonStream(InputStream stream)
			throws IndexingException {
		try {
			JsonNode rootNode = jsonMapper.readValue(stream, JsonNode.class);
			String siteHash = rootNode.get("sitehash").getTextValue();

			LOG.info("TYPO3 Solr siteHash retrieved: " + siteHash);

			return siteHash;
		} catch (Exception e) {
			LOG.error("ERROR! could not receive correct siteHash data from the Solr TYPO3 Api");

			throw (new IndexingException(e));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException streamException) {
					LOG.error(streamException.getMessage());
				}
			}
		}
	}

	protected String constructApiUrl(String apiBaseUrl, String requestedDomain,
			String apiKey) {

		if (apiBaseUrl.lastIndexOf('/') != apiBaseUrl.length() - 1) {
			apiBaseUrl += '/';
		}

		String apiUrl = apiBaseUrl + "index.php?" + TYPO3_API_CALL + "&"
				+ API_SPECIFICATION + "&" + API_PARAMETER + "="
				+ requestedDomain + "&" + API_PARAMETER_KEY + "=" + apiKey;

		LOG.info("Constructed TYPO3 API URL: " + apiUrl);

		return apiUrl;
	}
}
