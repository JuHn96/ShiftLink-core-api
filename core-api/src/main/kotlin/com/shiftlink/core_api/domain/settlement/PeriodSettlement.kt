package com.shiftlink.core_api.domain.settlement

import com.shiftlink.core_api.domain.user.User
import com.shiftlink.core_api.domain.workplace.Workplace
import jakarta.persistence.*
import java.math.BigDecimal

enum class PeriodType { WEEKLY, MONTHLY }
enum class SettlementStatus { WAITING, COMPLETED }

@Entity
@Table(name = "period_settlements", schema = "core")
class PeriodSettlement(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", nullable = false)
    var workplace: Workplace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "period_type", nullable = false, length = 20)
    var periodType: PeriodType,

    @Column(name = "period_value", nullable = false, length = 20)
    var periodValue: String,

    @Column(name = "total_work_hours", nullable = false, precision = 10, scale = 2)
    var totalWorkHours: BigDecimal,

    @Column(name = "expected_wage", nullable = false, precision = 10, scale = 2)
    var expectedWage: BigDecimal,

    @Column(name = "actual_deposit", precision = 10, scale = 2)
    var actualDeposit: BigDecimal? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "is_holiday_pay_met", nullable = false)
    var isHolidayPayMet: Boolean = false

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: SettlementStatus = SettlementStatus.WAITING
}
