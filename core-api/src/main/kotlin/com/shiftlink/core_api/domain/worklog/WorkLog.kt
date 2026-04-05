package com.shiftlink.core_api.domain.worklog

import com.shiftlink.core_api.domain.file.FileMaster
import com.shiftlink.core_api.domain.user.ClientType
import com.shiftlink.core_api.domain.user.User
import com.shiftlink.core_api.domain.workplace.Workplace
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

enum class WorkStatus { NORMAL, LATE, EARLY_LEAVE, SICK, SUBSTITUTE }

@Entity
@Table(name = "work_logs", schema = "core")
class WorkLog(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id", nullable = false)
    var workplace: Workplace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    var clientType: ClientType,

    @Column(name = "start_time", nullable = false)
    var startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    var endTime: LocalDateTime,

    @Column(name = "rest_minutes", nullable = false)
    var restMinutes: Int = 30,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: WorkStatus,

    @Column(name = "total_calculated_wage", nullable = false, precision = 10, scale = 2)
    var totalCalculatedWage: BigDecimal,

    @Column(name = "memo", columnDefinition = "TEXT")
    var memo: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_image_id")
    var receiptImage: FileMaster? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
