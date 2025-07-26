package krilovs.andrejs.app.service

import krilovs.andrejs.app.model.ConversionRequest
import krilovs.andrejs.app.model.ConversionResponse
import krilovs.andrejs.app.repository.ConversionFeeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class ConversionService(
  private val feeRepository: ConversionFeeRepository,
  private val rateService: CurrencyRateService,
  @Value("\${default.conversion.fee:0.01}")
  private val defaultFee: Double
) {
  fun convert(request: ConversionRequest): ConversionResponse {
    val fee = feeRepository.findByFromCurrencyAndToCurrency(
      request.fromCurrency.uppercase(), request.toCurrency.uppercase()
    )?.fee ?: defaultFee

    val rate = rateService.getRate(request.fromCurrency.uppercase(), request.toCurrency.uppercase())
    val feeAmount = request.amount * fee
    val result = (request.amount - feeAmount) * rate

    return ConversionResponse(
      originalAmount = request.amount,
      feeApplied = fee,
      convertedAmount = BigDecimal(result).setScale(2, RoundingMode.HALF_UP).toDouble(),
      exchangeRate = rate
    )
  }
}