package krilovs.andrejs.app.controller

import krilovs.andrejs.app.model.ConversionFee
import krilovs.andrejs.app.service.FeeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin/fees")
class AdminFeeController(
  private val feeService: FeeService
) {
  @GetMapping
  fun getAll(): List<ConversionFee> = feeService.getAllFees()

  @PostMapping
  fun addFee(@RequestBody fee: ConversionFee): ConversionFee = feeService.addFee(fee)

  @PutMapping
  fun updateFee(
    @RequestParam from: String,
    @RequestParam to: String,
    @RequestParam newFee: Double
  ): ConversionFee = feeService.updateFee(from, to, newFee)

  @DeleteMapping
  fun deleteFee(
    @RequestParam from: String,
    @RequestParam to: String
  ): ResponseEntity<Void> {
    return if (feeService.deleteFee(from, to)) {
      ResponseEntity.noContent().build()
    }
    else {
      ResponseEntity.notFound().build()
    }
  }
}