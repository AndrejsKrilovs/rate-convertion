package krilovs.andrejs.app.exchange

import krilovs.andrejs.app.currency.CurrencyDto

data class ExchangeResponse(
  val baseCurrency: CurrencyDto,
  val targetCurrency: CurrencyDto,
  val rate: Double,
  val amount: Double,
  val convertedAmount: Double
)