package krilovs.andrejs.app.repository

import krilovs.andrejs.app.model.ConversionFee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversionFeeRepository : JpaRepository<ConversionFee, Long> {
  fun findByFromCurrencyAndToCurrency(fromCurrency: String, toCurrency: String): ConversionFee?
  fun deleteByFromCurrencyAndToCurrency(fromCurrency: String, toCurrency: String): Long
}