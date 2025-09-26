package krilovs.andrejs.app.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class ExchangeServlet : AbstractServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp) {
      val from = req.getParameter("from") ?: throw IllegalArgumentException("Отсутствует параметр 'from'")
      val to = req.getParameter("to") ?: throw IllegalArgumentException("Отсутствует параметр 'to'")
      val amount = req.getParameter("amount")?.toDoubleOrNull() ?: throw IllegalArgumentException("Некорректная сумма")

      exchangeService.exchangeCurrency(from.uppercase(), to.uppercase(), amount)
    }
  }
}