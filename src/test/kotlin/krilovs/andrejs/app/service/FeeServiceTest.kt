package krilovs.andrejs.app.service

import io.mockk.every
import io.mockk.mockk
import krilovs.andrejs.app.model.ConversionFee
import krilovs.andrejs.app.repository.ConversionFeeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FeeServiceTest {
  private val repo = mockk<ConversionFeeRepository>(relaxed = true)
  private val service = FeeService(repo)

  @Test
  fun getAllFees() {
    val fees = listOf(
      ConversionFee(1, "EUR", "USD", 0.1),
      ConversionFee(2, "USD", "GBP", 0.2)
    )
    every { repo.findAll() } returns fees

    val result = service.getAllFees()
    assertFalse(result.isEmpty())
    assertEquals(2, result.size)
  }

  @Test
  fun updateExistingFee() {
    val existing = ConversionFee(1, "EUR", "USD", 0.1)
    val updated = existing.copy(fee = 0.25)

    every { repo.findByFromCurrencyAndToCurrency("EUR", "USD") } returns existing
    every { repo.save(any()) } returns updated

    val result = service.updateFee("eur", "usd", 0.25)
    assertEquals(0.25, result.fee)
  }

  @Test
  fun updateNonExistentFee() {
    every { repo.findByFromCurrencyAndToCurrency("EUR", "CHF") } returns null

    val exception = assertThrows(IllegalArgumentException::class.java) {
      service.updateFee("EUR", "CHF", 0.2)
    }

    assertEquals("Fee not found for EUR -> CHF", exception.message)
  }

  @Test
  fun deleteFeeSuccess() {
    every { repo.deleteByFromCurrencyAndToCurrency("EUR", "USD") } returns 1
    val result = service.deleteFee("EUR", "USD")
    assertTrue(result)
  }

  @Test
  fun deleteFeeFailed() {
    every { repo.deleteByFromCurrencyAndToCurrency("EUR", "GBP") } returns 0
    val result = service.deleteFee("EUR", "GBP")
    assertFalse(result)
  }

  @Test
  fun addFee() {
    val fee = ConversionFee(0, "EUR", "JPY", 0.3)
    every { repo.save(fee) } returns fee

    val result = service.addFee(fee)
    assertEquals("EUR", result.fromCurrency)
    assertEquals("JPY", result.toCurrency)
    assertEquals(0.3, result.fee)
  }

}
