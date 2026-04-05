package com.shiftlink.core_api.domain.workplace

import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

enum class TaxRateType { TAX_3_3, INSURANCE_4, NONE, CUSTOM }

@Converter(autoApply = true)
class TaxRateTypeConverter : AttributeConverter<TaxRateType, String> {
    override fun convertToDatabaseColumn(attribute: TaxRateType?): String? {
        return when (attribute) {
            TaxRateType.TAX_3_3 -> "3.3"
            TaxRateType.INSURANCE_4 -> "4INSURANCE"
            TaxRateType.NONE -> "NONE"
            TaxRateType.CUSTOM -> "CUSTOM"
            null -> null
        }
    }

    override fun convertToEntityAttribute(dbData: String?): TaxRateType? {
        return when (dbData) {
            "3.3" -> TaxRateType.TAX_3_3
            "4INSURANCE" -> TaxRateType.INSURANCE_4
            "NONE" -> TaxRateType.NONE
            "CUSTOM" -> TaxRateType.CUSTOM
            else -> null
        }
    }
}

@Entity
@Table(name = "workplaces", schema = "core")
class Workplace(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Column(name = "theme_color", nullable = false, length = 10)
    var themeColor: String,

    @Column(name = "base_hourly_wage", nullable = false, precision = 10, scale = 2)
    var baseHourlyWage: BigDecimal,

    @Column(name = "payday", nullable = false)
    var payday: Int,

    @Column(name = "tax_rate_type", nullable = false, length = 20)
    var taxRateType: TaxRateType,

    @Column(name = "custom_tax_amount", precision = 10, scale = 2)
    var customTaxAmount: BigDecimal? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "has_holiday_pay", nullable = false)
    var hasHolidayPay: Boolean = false

    @Column(name = "has_overtime_pay", nullable = false)
    var hasOvertimePay: Boolean = false

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
