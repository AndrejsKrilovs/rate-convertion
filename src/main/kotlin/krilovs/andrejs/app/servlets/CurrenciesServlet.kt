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
    private val currencyService = CurrencyService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val mapper = jacksonObjectMapper()
        val path = req.pathInfo?.removePrefix("/")?.takeIf { it.isNotBlank() }

        when (path) {
            null -> {
                val currencyList = mapper.writeValue(resp.writer, currencyService.getAvailableCurrencies())
                resp.status = HttpServletResponse.SC_OK
                resp.writer.print(currencyList)
            }
            else -> {
                try {
                    var currencyFromDatabase = currencyService.findCurrencyByCode(path.uppercase())
                    var mappedCurrency = mapper.writeValue(resp.writer, currencyFromDatabase)
                    resp.status = HttpServletResponse.SC_OK
                    resp.writer.print(mappedCurrency)
                }
                catch (nsee: NoSuchElementException) {
                    resp.status = HttpServletResponse.SC_NOT_FOUND
                    var reasonMessage = mapOf("reason" to nsee.message)
                    jacksonObjectMapper().writeValue(resp.writer, reasonMessage)
                }
            }
        }
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        try {
            val dto = req.reader.use { jacksonObjectMapper().readValue(it, CurrencyDto::class.java) }
            val saved = currencyService.addCurrency(dto)
            resp.status = HttpServletResponse.SC_CREATED
            jacksonObjectMapper().writeValue(resp.writer, saved)
        }
        catch (kine: KotlinInvalidNullException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            var reasonMessage = mapOf("reason" to "Отсутствует нужное поле формы")
            jacksonObjectMapper().writeValue(resp.writer, reasonMessage)
        }
        catch (iae: IllegalArgumentException) {
            resp.status = HttpServletResponse.SC_BAD_REQUEST
            var reasonMessage = mapOf("reason" to "Неверно задан валютный код")
            jacksonObjectMapper().writeValue(resp.writer, reasonMessage)
        }
        catch (dre: DuplicateRequestException) {
            resp.status = HttpServletResponse.SC_CONFLICT
            var reasonMessage = mapOf("reason" to dre.message)
            jacksonObjectMapper().writeValue(resp.writer, reasonMessage)
        }
    }
}