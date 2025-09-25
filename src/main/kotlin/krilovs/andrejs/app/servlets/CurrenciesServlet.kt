package krilovs.andrejs.app.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import krilovs.andrejs.app.currency.CurrencyDto

class CurrenciesServlet : AbstractServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val path = req.pathInfo?.removePrefix("/")?.takeIf { it.isNotBlank() }

    handleRequest(resp) {
      when (path) {
        null -> currencyService.getAvailableCurrencies()
        else -> currencyService.findCurrencyByCode(path.uppercase())
      }
    }
  }

  override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp, HttpServletResponse.SC_CREATED) {
      val dto = req.reader.use { mapper.readValue(it, CurrencyDto::class.java) }
      currencyService.addCurrency(dto)
    }
  }
}
