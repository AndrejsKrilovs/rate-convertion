package krilovs.andrejs.app.service

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

@Service
class CurrencyRateService {
  @Value("\${ecb.rates.url:https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml}")
  private lateinit var ecbUrl: String

  private val rates: MutableMap<String, Double> = mutableMapOf("EUR" to 1.0)

  fun getRate(from: String, to: String): Double {
    val fromRate = rates[from] ?: throw IllegalArgumentException("Unsupported currency: $from")
    val toRate = rates[to] ?: throw IllegalArgumentException("Unsupported currency: $to")
    return toRate / fromRate
  }

  fun getCachedRates(): Map<String, Double> = rates.toMap()

  @PostConstruct
  fun loadRatesOnStartup() = refreshRates()

  fun refreshRates() {
    val response = RestTemplate().getForObject(ecbUrl, String::class.java) ?: return
    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(response.byteInputStream())
    doc.documentElement.normalize()

    val cubeList = doc.getElementsByTagName("Cube")
    val newRates = mutableMapOf("EUR" to 1.0)

    for (i in 0 until cubeList.length) {
      val node = cubeList.item(i)
      if (node is Element && node.hasAttribute("currency") && node.hasAttribute("rate")) {
        val currency = node.getAttribute("currency")
        val rate = node.getAttribute("rate").toDoubleOrNull() ?: continue
        newRates[currency] = rate
      }
    }

    rates.clear()
    rates.putAll(newRates)
  }
}