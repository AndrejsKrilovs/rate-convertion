package krilovs.andrejs.app.servlets

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse

class FilterServlet: Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        request?.characterEncoding = "UTF-8"

        chain?.doFilter(request, response)
    }
}