package krilovs.andrejs.app.exchange

import krilovs.andrejs.app.rate.RateService
import java.math.BigDecimal
import java.math.RoundingMode

class ExchangeService(private val rateService: RateService) {
  fun exchangeCurrency(currencyFrom: String, currencyTo: String, amount: Double): ExchangeResponse {
    val rateData = rateService.findExchangeRatePair(currencyFrom + currencyTo)
    val convertedAmount = BigDecimal
      .valueOf(amount * rateData.rate)
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble()

    return ExchangeResponse(
      rateData.baseCurrency,
      rateData.targetCurrency,
      rateData.rate,
      amount,
      convertedAmount
    )
  }
}