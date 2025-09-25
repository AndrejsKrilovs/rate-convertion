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
    catch (ex: Exception) {
      val (status, body) = exceptionHandlers[ex.javaClass]?.invoke(ex) ?:
        (HttpServletResponse.SC_INTERNAL_SERVER_ERROR to mapOf(messageConstant to ex.message))

      resp.status = status
      mapper.writeValue(resp.writer, body)
    }
  }

  private val exceptionHandlers: Map<Class<out Exception>, (Exception) -> Pair<Int, Any>> =
    mapOf(
      KotlinInvalidNullException::class.java to { _ ->
        HttpServletResponse.SC_BAD_REQUEST to mapOf(messageConstant to "Отсутствует нужное поле формы")
      },
      IllegalArgumentException::class.java to { ex ->
        HttpServletResponse.SC_BAD_REQUEST to mapOf(messageConstant to ex.message)
      },
      NoSuchElementException::class.java to { ex ->
        HttpServletResponse.SC_NOT_FOUND to mapOf(messageConstant to ex.message)
      },
      DuplicateRequestException::class.java to { ex ->
        HttpServletResponse.SC_CONFLICT to mapOf(messageConstant to ex.message)
      }
    )
}