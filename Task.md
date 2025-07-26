Homework: Backend

You are developing an API for a [currency calculator](https://www.xe.com/currencyconverter/) with configurable conversion fees. There is an administrative API and a public API. The former allows an administrator to edit individual conversion fees for currency pairs. The latter is used to preview the conversions.

## Requirements

- Create API endpoints for listing, adding, editing, and removing currency conversion fees.
- Create an API endpoint for calculating a currency conversion.
- Create an API endpoint for refreshing current conversion rates. The rates should be fetched from [ECB](https://www.ecb.europa.eu/stats/policy_and_exchange_rates/euro_reference_exchange_rates/html/index.en.html).

### Administrative API

- Fees can be listed, added, edited, or removed for a given currency pair and direction.
- Fees apply exactly in the configured direction (e.g. from EUR to GBP), but not in reverse.
- Fees are specified as fractions, i.e. 0.2 for a 20% fee.

### Public API

- A conversion request consists of the amount to be converted, which currency to convert from, and which to convert to.
- If a fee is configured for the specified currency pair, it should apply to the initial amount.
- If no fee is configured, a default one is used.
- The default fee is supplied in application configuration.
- The conversion is calculated using the formula: (amount - amount * fee) * rate.

## Non-functional Requirements

- Stack: Kotlin, Spring Boot, PostgreSQL, Docker Compose.
- Conversion rates should be retrieved at application startup and cached until refreshed using the refresh API endpoint.
- Configured fees should be persisted to the database.
- There should be a Dockerfile for the application and a Compose file that runs both the application and a database instance.

## Considerations

- It should be possible to build, test, and run the application from the command-line.
- It should be possible to run the application both outside and inside a container.
- You should provide a README file that clearly explains how to do all of the above.