package krilovs.andrejs.app.rate

data class RateRequest(
  val baseCurrencyCode: String,
  val targetCurrencyCode: String,
  val rate: Double
)