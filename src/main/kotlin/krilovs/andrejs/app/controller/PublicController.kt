package krilovs.andrejs.app.controller

import jakarta.validation.Valid
import krilovs.andrejs.app.model.ConversionRequest
import krilovs.andrejs.app.model.ConversionResponse
import krilovs.andrejs.app.service.ConversionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/convert")
class PublicController(
  private val conversionService: ConversionService
) {
  @PostMapping
  fun convert(@Valid @RequestBody request: ConversionRequest): ConversionResponse {
    return conversionService.convert(request)
  }
}