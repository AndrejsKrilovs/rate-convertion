package krilovs.andrejs.app

import jakarta.servlet.DispatcherType
import jakarta.servlet.ServletContextEvent
import jakarta.servlet.ServletContextListener
import krilovs.andrejs.app.currency.CurrencyService
import krilovs.andrejs.app.exchange.ExchangeService
import krilovs.andrejs.app.rate.RateService
import krilovs.andrejs.app.servlets.CurrenciesServlet
import krilovs.andrejs.app.servlets.ExchangeServlet
import krilovs.andrejs.app.servlets.FilterServlet
import krilovs.andrejs.app.servlets.RatePairServlet
import krilovs.andrejs.app.servlets.RateServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.util.EnumSet

class AppInitializer : ServletContextListener {
  override fun contextInitialized(sce: ServletContextEvent) {
    val currencyService = CurrencyService()
    val rateService = RateService(currencyService)
    val exchangeService = ExchangeService(rateService)

    sce.servletContext.setAttribute("currencyService", currencyService)
    sce.servletContext.setAttribute("rateService", rateService)
    sce.servletContext.setAttribute("exchangeService", exchangeService)
  }
}

fun main() {
  val server = Server(8080)
  val context = ServletContextHandler(ServletContextHandler.SESSIONS)
  context.contextPath = "/"
  server.handler = context

  context.addEventListener(AppInitializer())
  context.addServlet(ExchangeServlet::class.java, "/currency-converter/exchange")
  context.addServlet(RateServlet::class.java, "/currency-converter/exchangeRates")
  context.addServlet(RatePairServlet::class.java, "/currency-converter/exchangeRates/*")
  context.addServlet(CurrenciesServlet::class.java, "/currency-converter/currencies/*")
  context.addFilter(FilterServlet::class.java, "/*", EnumSet.of(DispatcherType.REQUEST))

  server.start()
  println("Server started")
  server.join()
}
