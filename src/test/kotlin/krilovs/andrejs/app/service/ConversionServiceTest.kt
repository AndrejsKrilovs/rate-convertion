package krilovs.andrejs.app.service

import io.mockk.every
import io.mockk.mockk
import krilovs.andrejs.app.model.ConversionFee
import krilovs.andrejs.app.model.ConversionRequest
import krilovs.andrejs.app.repository.ConversionFeeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ConversionServiceTest {

 private val feeRepository = mockk<ConversionFeeRepository>()
 private val rateService = mockk<CurrencyRateService>()
 private val service = ConversionService(feeRepository, rateService, defaultFee = 0.01)

 @Test
 fun applyCustomFeeWhenAvailable() {
  val request = ConversionRequest("EUR", "USD", 100.0)
  every { feeRepository.findByFromCurrencyAndToCurrency("EUR", "USD") } returns ConversionFee(
   id = 1,
   fromCurrency = "EUR",
   toCurrency = "USD",
   fee = 0.2
  )
  every { rateService.getRate("EUR", "USD") } returns 1.1

  val response = service.convert(request)

  assertEquals(100.0, response.originalAmount)
  assertEquals(0.2, response.feeApplied)
  assertEquals(88.0, response.convertedAmount) // (100 - 20) * 1.1
  assertEquals(1.1, response.exchangeRate)
 }

 @Test
 fun applyDefaultFee() {
  val request = ConversionRequest("EUR", "GBP", 200.0)
  every { feeRepository.findByFromCurrencyAndToCurrency("EUR", "GBP") } returns null
  every { rateService.getRate("EUR", "GBP") } returns 0.8

  val response = service.convert(request)

  assertEquals(200.0, response.originalAmount)
  assertEquals(0.01, response.feeApplied)
  assertEquals(158.4, response.convertedAmount)
  assertEquals(0.8, response.exchangeRate)
 }
}
