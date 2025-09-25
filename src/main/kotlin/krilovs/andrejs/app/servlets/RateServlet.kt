package krilovs.andrejs.app.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import krilovs.andrejs.app.rate.RateRequest

class RateServlet : AbstractServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp) {
      rateService.getRates()
    }
  }

  override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    handleRequest(resp) {
      val dto = req.reader.use { mapper.readValue(it, RateRequest::class.java) }
      rateService.addRate(dto).also { resp.status = HttpServletResponse.SC_CREATED }
    }
  }
}