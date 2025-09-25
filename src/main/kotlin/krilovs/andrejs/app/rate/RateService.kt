package krilovs.andrejs.app.rate

import com.sun.jdi.request.DuplicateRequestException
import krilovs.andrejs.app.currency.CurrencyService
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class RateService(private val currencyService: CurrencyService) {
  private val idGenerator = AtomicLong(0)
  private val rates = ConcurrentHashMap<Long, RateResponse>()

  fun getRates() = rates.map { it.value }

  fun findExchangeRatePair(pair: String): RateResponse {
    if (pair.length != 6) {
      throw IllegalArgumentException("Некорректный код валютной пары в адресе")
    }

    var baseCurrency = currencyService.findCurrencyByCode(pair.substring(0, 3))
    var targetCurrency = currencyService.findCurrencyByCode(pair.substring(3, 6))

    return rates.values
      .find { it.baseCurrency == baseCurrency && it.targetCurrency == targetCurrency }
      ?: throw NoSuchElementException("Обменный курс для пары '$pair' не найден")
  }

  fun addRate(item: RateRequest): RateResponse {
    var baseCurrency = currencyService.findCurrencyByCode(item.baseCurrencyCode)
    var targetCurrency = currencyService.findCurrencyByCode(item.targetCurrencyCode)
    var duplicateRate = rates.values.find { it.baseCurrency == baseCurrency && it.targetCurrency == targetCurrency }
    if (Objects.nonNull(duplicateRate)) {
      throw DuplicateRequestException(
        "Валютная пара с кодом '${item.baseCurrencyCode + item.targetCurrencyCode}' уже существует"
      )
    }

    val id = idGenerator.getAndIncrement()
    val newRate = RateResponse(
      id = id,
      baseCurrency = baseCurrency,
      targetCurrency = targetCurrency,
      rate = item.rate
    )

    rates.putIfAbsent(id, newRate)
    return newRate
  }

  fun changeRateForPair(pair: String, rate: Double): RateResponse {
    return findExchangeRatePair(pair).copy(rate = rate)
  }
}