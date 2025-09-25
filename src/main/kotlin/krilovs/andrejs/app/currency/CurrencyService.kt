package krilovs.andrejs.app.currency

import com.sun.jdi.request.DuplicateRequestException
import java.util.Currency
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class CurrencyService {
    private val idGenerator = AtomicLong(0)
    private val mapper = CurrencyMapper.INSTANCE
    private val currencies = ConcurrentHashMap<String, CurrencyDto>()

    fun getAvailableCurrencies() = currencies.map { it.value }
    fun findCurrencyByCode(code: String) =
        currencies[code.uppercase()] ?:
        throw NoSuchElementException("Валюта '$code' не найдена")

    fun addCurrency(item: CurrencyDto): CurrencyDto {
        val codeKey = item.code.uppercase()
        val newCurrency = mapper
            .toDto(Currency.getInstance(codeKey))
            .copy(id = idGenerator.getAndIncrement(), name = item.name, sign = item.sign)

        if (Objects.nonNull(currencies.putIfAbsent(codeKey, newCurrency))) {
            throw DuplicateRequestException("Валюта с кодом '$codeKey' уже существует")
        }

        return newCurrency
    }
}