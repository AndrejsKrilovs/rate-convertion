package krilovs.andrejs.app.controller

import krilovs.andrejs.app.service.CurrencyRateService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rates")
class RateController(
  private val rateService: CurrencyRateService
) {
  @PostMapping("/refresh")
  fun refreshRates(): ResponseEntity<String> {
    rateService.refreshRates()
    return ResponseEntity.ok("Rates refreshed successfully")
  }

  @GetMapping
  fun getRates(): Map<String, Double> = rateService.getCachedRates()
}