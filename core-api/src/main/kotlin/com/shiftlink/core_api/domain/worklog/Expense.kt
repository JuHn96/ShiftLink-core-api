package com.shiftlink.core_api.domain.worklog

import com.shiftlink.core_api.domain.file.FileMaster
import com.shiftlink.core_api.domain.user.ClientType
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

enum class ExpenseCategory { TRANSPORT, MEAL, ETC }

@Entity
@Table(name = "expenses", schema = "core")
class Expense(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_log_id", nullable = false)
    var workLog: WorkLog,

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    var clientType: ClientType,

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    var category: ExpenseCategory,

    @Column(name = "description", length = 255)
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_receipt_id")
    var expenseReceipt: FileMaster? = null,

    @Column(name = "expensed_at", nullable = false)
    var expensedAt: LocalDateTime,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false
}
