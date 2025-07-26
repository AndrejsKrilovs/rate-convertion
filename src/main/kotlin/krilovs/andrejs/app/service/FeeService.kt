package krilovs.andrejs.app.service

import krilovs.andrejs.app.model.ConversionFee
import krilovs.andrejs.app.repository.ConversionFeeRepository
import org.springframework.stereotype.Service

@Service
class FeeService(
  private val feeRepository: ConversionFeeRepository
) {
  fun getAllFees(): List<ConversionFee> = feeRepository.findAll()

  fun addFee(fee: ConversionFee): ConversionFee = feeRepository.save(fee)

  fun updateFee(from: String, to: String, newFee: Double): ConversionFee {
    val existing = feeRepository.findByFromCurrencyAndToCurrency(from.uppercase(), to.uppercase())
      ?: throw IllegalArgumentException("Fee not found for $from -> $to")
    val updated = existing.copy(fee = newFee)
    return feeRepository.save(updated)
  }

  fun deleteFee(from: String, to: String): Boolean {
    return feeRepository.deleteByFromCurrencyAndToCurrency(from.uppercase(), to.uppercase()) > 0
  }
}