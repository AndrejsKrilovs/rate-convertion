package krilovs.andrejs.app.rate

import krilovs.andrejs.app.currency.CurrencyDto

data class RateResponse(
  var id: Long,
  var baseCurrency: CurrencyDto,
  var targetCurrency: CurrencyDto,
  var rate: Double
)