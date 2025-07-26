package krilovs.andrejs.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CurrencyCalculatorApplication

fun main(args: Array<String>) {
  runApplication<CurrencyCalculatorApplication>(*args)
}
