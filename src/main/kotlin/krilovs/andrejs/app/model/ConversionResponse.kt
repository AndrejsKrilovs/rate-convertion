package krilovs.andrejs.app.model

data class ConversionResponse(
  val originalAmount: Double,
  val feeApplied: Double,
  val convertedAmount: Double,
  val exchangeRate: Double
)