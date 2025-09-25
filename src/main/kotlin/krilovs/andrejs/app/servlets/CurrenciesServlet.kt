package krilovs.andrejs.app.servlets

import com.fasterxml.jackson.module.kotlin.KotlinInvalidNullException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sun.jdi.request.DuplicateRequestException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import krilovs.andrejs.app.currency.CurrencyDto
import krilovs.andrejs.app.currency.CurrencyService

class CurrenciesServlet: HttpServlet() {
    private val messageConstant = "message"
    private val mapper = jacksonObjectMapper()
    private val currencyService = CurrencyService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val path = req.pathInfo?.removePrefix("/")?.takeIf { it.isNotBlank() }

        when (path) {
            null -> {
                resp.status = HttpServletResponse.SC_OK
                mapper.writeValue(resp.writer, currencyService.getAvailableCurrencies())
            }
            else -> {
                try {
                    var currencyFromDatabase = currencyService.findCurrencyByCode(path.uppercase())
                    resp.status = HttpServletResponse.SC_OK
                    mapper.writeValue(resp.writer, currencyFromDatabase)
                }
                catch (nsee: NoSuchElementException) {
                    resp.status = HttpServletResponse.SC_NOT_FOUND
                    var reasonMessage = mapOf(messageConstant to nsee.message)
                    mapper.writeValue(resp.writer, reasonMessage)
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val dto = req.reader.use { mapper.readValue(it, CurrencyDto::class.java) }
            val saved = currencyService.addCurrency(dto)
            resp.status = HttpServletResponse.SC_CREATED
            mapper.writeValue(resp.writer, saved)
        }
        catch (kine: KotlinInvalidNullException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            var reasonMessage = mapOf(messageConstant to "Отсутствует нужное поле формы")
            mapper.writeValue(resp.writer, reasonMessage)
        }
        catch (iae: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            var reasonMessage = mapOf(messageConstant to "Неверно задан валютный код")
            mapper.writeValue(resp.writer, reasonMessage)
        }
        catch (dre: DuplicateRequestException) {
            resp.status = HttpServletResponse.SC_CONFLICT
            var reasonMessage = mapOf(messageConstant to dre.message)
            mapper.writeValue(resp.writer, reasonMessage)
        }
    }
}