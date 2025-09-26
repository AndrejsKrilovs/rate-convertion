package krilovs.andrejs.app.rate

import krilovs.andrejs.app.currency.CurrencyDto

data class RateResponse(
  val id: Long,
  val baseCurrency: CurrencyDto,
  val targetCurrency: CurrencyDto,
  val rate: Double
)