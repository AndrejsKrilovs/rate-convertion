package krilovs.andrejs.app.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import krilovs.andrejs.app.rate.ChangeRateRequest
import kotlin.text.removePrefix

class RatePairServlet: AbstractServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp) {
      val currencyPair = req.pathInfo.removePrefix("/").uppercase()
      rateService.findExchangeRatePair(currencyPair)
    }
  }

  override fun doPatch(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp) {
      val currencyPair = req.pathInfo.removePrefix("/").uppercase()
      val dto = req.reader.use { mapper.readValue(it, ChangeRateRequest::class.java) }
      rateService.changeRateForPair(currencyPair, dto.rate).also { resp.status = HttpServletResponse.SC_ACCEPTED }
    }
  }
}