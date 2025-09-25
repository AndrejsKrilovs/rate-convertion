package krilovs.andrejs.app

import jakarta.servlet.DispatcherType
import krilovs.andrejs.app.servlets.CurrenciesServlet
import krilovs.andrejs.app.servlets.FilterServlet
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.ServletContextHandler
import java.util.EnumSet

fun main() {
    val server = Server(8080)
    val context = ServletContextHandler(ServletContextHandler.SESSIONS)
    context.contextPath = "/"
    server.handler = context

    context.addServlet(CurrenciesServlet::class.java, "/currency-converter/currencies/*")
    context.addFilter(FilterServlet::class.java, "/*", EnumSet.of(DispatcherType.REQUEST))

    server.start()
    println("Server started at http://localhost:8080/currency-converter")
    server.join()
}

