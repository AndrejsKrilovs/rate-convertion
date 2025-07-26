package krilovs.andrejs.app.model

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class ConversionRequest(
  @field:NotBlank(message = "fromCurrency is required")
  val fromCurrency: String,

  @field:NotBlank(message = "toCurrency is required")
  val toCurrency: String,

  @field:Min(value = 0, message = "Amount must be non-negative")
  val amount: Double
)
