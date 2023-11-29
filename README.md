Apache Nutch plugins for TYPO3 CMS
===============

# Introduction

## What is TYPO3 CMS

TYPO3 CMS is an Open Source Content Management System and Framework, well suited for internet, intranet, and extranet applications and websites.
Due to its flexible plugin architecture TYPO3 CMS offers a maximum of possibilities.
This makes it one of the most popular Open Source CMS worldwide.

TYPO3 CMS is backed by the [TYPO3 Association](https://typo3.org/).

## What is Apache Nutch

Apache Nutch is an open source web-search software project. Stemming from Apache Lucene, it now builds on Apache Solr adding web-specifics, such as a crawler, a link-graph database and parsing support handled by Apache Tika for HTML and and array other document formats.

Apache Nutch can run on a single machine, but gains a lot of its strength from running in a Hadoop cluster

The system can be enhanced (eg other document formats can be parsed) using a highly flexible, easily extensible and thoroughly maintained plugin infrastructure.

[Apache Nutch](https://nutch.apache.org/) is a project of the Apache Software Foundation.
Learn more by visiting the [official wiki of Apache Nutch](https://cwiki.apache.org/confluence/display/NUTCH/Home).

## What is Apache Nutch for TYPO3 CMS

Apache Nutch for TYPO3 CMS is a set of Nutch Plug-Ins to enable easy integration of the Nutch crawler with TYPO3 and specifically the Apache Solr for TYPO3 extension.

The plug-ins provide the following features for use with Apache Solr for TYPO3:

- Indexing of the access field – Access Rootline
- Indexing of the endtime field
- Indexing of keywords from meta tags
- Indexing of the SiteHash field
- Indexing of the uid field

Learn more on the [official website of Apache Nutch for TYPO3 CMS](https://www.typo3-solr.com/solr-for-typo3/add-ons/public-extensions/apache-nutch-for-typo3/).

# Setup

## Systems Requirements

- TYPO3 CMS: 10.4 - 12.4
- EXT:solr Version 11.2 - 12.0+
- Apache Solr: 8.11, 9.3
- Ant 1.10+
- Apache Ivy 2.5+

## Installation

For creating your own binaries you can build this plugin with ant.
Please visit the [official Apache Ant website](http://ant.apache.org/) for further information.

- build with `ant dist`
- copy and unpack `build/dist/apache-nutch-for-typo3-2.2.0.tar.gz`

## Configuration

Most of the settings are pre-configured already and should be ok for most scenarios. However, you still need to configure a few things.

Set the **URL** and the **API key** of your TYPO3 site with the Apache Solr for TYPO3 extension.
You can find them in the navigation pane on the left side under `Apache Solr > Info`.

Inside `conf/nutch-site.xml` you need to put these values for the properties `typo3.baseUrl` and `typo3.api.key`.

The sites to index must be added in `urls/seed.txt`, one per line.

# Usage

## Nutch 1.19+

First you have to configure your Solr core in: conf/index-writers.xml

To start the crawling use the following command to run Nutch 1.19+:

`bin/crawl -i -s <seedDir> <crawlID> <numberOfRounds>`

Example: `bin/crawl -i -s urls testcrawl 2`

The place holders must be replaced by values fitting your environment of course.

# Development

The Nutch version used for developing the plugins is Apache Nutch 1.5.1. Latest tested version is 1.19, 2.0 has not been tested.

## Development Environment Setup

- Check out Apache Nutch (AN) from GitHub
- Check out Apache Nutch for TYPO3 (ANfT) from GitHub
- In your IDE link AN into ANfT to get code completion

## Packaging

The Apache Nutch for TYPO3 CMS project checkout provides an Ant build script. On the command line change to the project directory and simply run ant To build a distributable package. The distributable will be build in AnfT/build/dist/.

The Ant build script
- checks out Apache Nutch
- patches Nutch with the changes listed below
- copies the TYPO3 plug-ins into the checked-out Nutch source directory
- compiles Nutch
- compiles the TYPO3 plug-ins
- packages a distributable tar.gz file

## Changes

The distribution includes the following changes:
NUTCH-585: Allows to exclude certain parts of a HTML page from being indexed.
nutch-585-excludeNodes.patch – 2011-09-18

## Authors

- Ingo Renner
- Phuong Doan

## License

Apache License Version 2.0, January 2004

## Support

Please use the issues in Github for community support.
Or contact [dkd Internet Service GmbH](https://www.dkd.de/) for SLA based help.
