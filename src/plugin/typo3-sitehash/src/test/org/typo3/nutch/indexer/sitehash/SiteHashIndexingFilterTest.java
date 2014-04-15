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

package org.typo3.nutch.indexer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.nutch.indexer.IndexingException;

public class SiteHashIndexingFilterTest extends SiteHashIndexingFilter {

	@Test
	public void testConstructApiUrl() {
		String expectedResult = "http://www.dkd.de/index.php?" + TYPO3_API_CALL
				+ "&" + API_SPECIFICATION + "&" + API_PARAMETER + "="
				+ "www.typo3-solr.com" + "&" + API_PARAMETER_KEY + "="
				+ "cd2d87b593a3testkey7507024398";

		String testUrl1 = constructApiUrl("http://www.dkd.de",
				"www.typo3-solr.com", "cd2d87b593a3testkey7507024398");
		String testUrl2 = constructApiUrl("http://www.dkd.de/",
				"www.typo3-solr.com", "cd2d87b593a3testkey7507024398");

		assertEquals("Constructed Api Url is incorrect", expectedResult,
				testUrl1);
		assertEquals("Constructed Api Url is incorrect", expectedResult,
				testUrl2);
	}

	@Test
	public void testGetSiteHashFromJsonStream() {
		InputStream json1 = new ByteArrayInputStream(
				"{\"sitehash\":\"05f48cd4e54df68f61dd17a72f81f3d43d100c70\"}"
						.getBytes());
		InputStream json2 = new ByteArrayInputStream(
				"{\"wrongJson\":\"wrongwrongwrongwrongwrongwrong\"}".getBytes());
		InputStream json3 = new ByteArrayInputStream(
				"not even Json at all".getBytes());

		try {
			assertEquals("Incorrect sitehash returned",
					"05f48cd4e54df68f61dd17a72f81f3d43d100c70",
					getSiteHashFromJsonStream(json1));
		} catch (IndexingException e) {
			fail("Was not able to interpret correct Json");
		}

		try {
			getSiteHashFromJsonStream(json2);
			fail("Interpreted incorrect Json");
		} catch (IndexingException e) {

		}

		try {
			getSiteHashFromJsonStream(json3);
			fail("Interpreted incorrect Json");
		} catch (IndexingException e) {

		}
	}
}
