package krilovs.andrejs.app.servlets

import com.fasterxml.jackson.module.kotlin.KotlinInvalidNullException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.jdi.request.DuplicateRequestException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletResponse
import krilovs.andrejs.app.currency.CurrencyService
import krilovs.andrejs.app.rate.RateService

abstract class AbstractServlet : HttpServlet() {
  protected val messageConstant = "message"
  protected val mapper = jacksonObjectMapper()
  protected lateinit var rateService: RateService
  protected lateinit var currencyService: CurrencyService

  override fun init() {
    rateService = servletContext.getAttribute("rateService") as RateService
    currencyService = servletContext.getAttribute("currencyService") as CurrencyService
  }

  protected fun handleRequest(
    resp: HttpServletResponse,
    defaultStatus: Int = HttpServletResponse.SC_OK,
    block: () -> Any
  ) {
    try {
      val result = block()
      resp.status = defaultStatus
      mapper.writeValue(resp.writer, result)
    }
    catch (kine: KotlinInvalidNullException) {
      resp.status = HttpServletResponse.SC_BAD_REQUEST
      mapper.writeValue(resp.writer, mapOf(messageConstant to "Отсутствует нужное поле формы"))
    }
    catch (nsee: NoSuchElementException) {
      resp.status = HttpServletResponse.SC_NOT_FOUND
      mapper.writeValue(resp.writer, mapOf(messageConstant to nsee.message))
    }
    catch (dre: DuplicateRequestException) {
      resp.status = HttpServletResponse.SC_CONFLICT
      mapper.writeValue(resp.writer, mapOf(messageConstant to dre.message))
    }
    catch (iae: IllegalArgumentException) {
      resp.status = HttpServletResponse.SC_BAD_REQUEST
      mapper.writeValue(resp.writer, mapOf(messageConstant to iae.message))
    }
  }
}