# Security Policy

## Supported Versions

| Version | Supported          |
|---------|--------------------|
| 2.3.x   | :white_check_mark: |
| < 2.3   | :x:                |

## Reporting a Vulnerability

If you discover a security vulnerability in this project, please report it responsibly.

**Do not open a public GitHub issue for security vulnerabilities.**

Instead, please report them via one of the following channels:

- **GitHub Security Advisories**: Use the [private vulnerability reporting](https://github.com/TYPO3-Solr/nutch-typo3-cms/security/advisories/new) feature on this repository.
- **Email**: Contact the team at [opensource@dkd.de](mailto:opensource@dkd.de)

### What to include

- A description of the vulnerability
- Steps to reproduce
- Affected versions
- Any potential impact

### Response timeline

- **Acknowledgment**: Within 5 business days
- **Assessment**: Within 10 business days
- **Fix release**: Depending on severity, typically within 30 days

## Security Best Practices

When deploying Apache Nutch for TYPO3:

- Keep your Nutch installation and TYPO3 plugins up to date
- Protect the TYPO3 API key configured in `conf/nutch-site.xml`
- Restrict network access to your Solr instance
- Run Nutch with minimal required permissions
