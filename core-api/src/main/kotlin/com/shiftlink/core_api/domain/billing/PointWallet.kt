package com.shiftlink.core_api.domain.billing

import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

enum class PointType { FREE, PAID }

@Entity
@Table(name = "point_wallets", schema = "billing")
class PointWallet(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "point_type", nullable = false, length = 20)
    var pointType: PointType,

    @Column(name = "original_amount", nullable = false)
    var originalAmount: Int,

    @Column(name = "remaining_amount", nullable = false)
    var remainingAmount: Int,

    @Column(name = "expires_at")
    var expiresAt: LocalDateTime? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
