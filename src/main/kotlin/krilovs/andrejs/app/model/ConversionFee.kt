package krilovs.andrejs.app.model

import jakarta.persistence.*

@Entity
@Table(name = "conversion_fees")
data class ConversionFee(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long,
  @Column(length = 3, nullable = false)
  val fromCurrency: String,
  @Column(length = 3, nullable = false)
  val toCurrency: String,
  @Column(nullable = false)
  val fee: Double
)
