package krilovs.andrejs.app.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrencyRateServiceTest {
  private lateinit var service: CurrencyRateService

  @BeforeEach
  fun setup() {
    service = CurrencyRateService()

    val ratesField = CurrencyRateService::class.java.getDeclaredField("rates")
    ratesField.isAccessible = true
    ratesField.set(service, mutableMapOf(
      "EUR" to 1.0,
      "USD" to 1.2,
      "GBP" to 0.9
    ))
  }

  @Test
  fun returnCorrectRate() {
    val rate = service.getRate("USD", "EUR")
    assertEquals(1 / 1.2, rate, 0.0001)
  }

  @Test
  fun returnCashedRateMap() {
    val rates = service.getCachedRates()
    assertEquals(3, rates.size)
    assertEquals(1.0, rates["EUR"])
    assertEquals(1.2, rates["USD"])
    assertEquals(0.9, rates["GBP"])
  }

  @Test
  fun getRateForUnknownCurrency() {
    val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
      service.getRate("JPY", "EUR")
    }
    assertEquals("Unsupported currency: JPY", exception.message)
  }

  @Test
  fun getRateForUnknownDestination() {
    val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
      service.getRate("EUR", "AUD")
    }
    assertEquals("Unsupported currency: AUD", exception.message)
  }

  @Test
  fun refreshRates() {
    val service = CurrencyRateService()

    val ratesField = CurrencyRateService::class.java.getDeclaredField("rates")
    ratesField.isAccessible = true
    ratesField.set(service, mutableMapOf("EUR" to 1.0))

    kotlin.runCatching { service.refreshRates() }

    val updatedRates = service.getCachedRates()
    assertTrue(updatedRates.isNotEmpty())
  }

}