package krilovs.andrejs.app.currency

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers
import java.util.Currency

@Mapper
interface CurrencyMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "currencyCode")
    @Mapping(target = "name", source = "displayName")
    @Mapping(target = "sign", source = "symbol")
    fun toDto(entity: Currency): CurrencyDto

    companion object {
        val INSTANCE: CurrencyMapper = Mappers.getMapper(CurrencyMapper::class.java)
    }
}