package com.shiftlink.core_api.domain.billing

import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

enum class TransactionType { EARN, USE, EXPIRE, REFUND }

@Entity
@Table(name = "point_transactions", schema = "billing")
class PointTransaction(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_wallet_id", nullable = false)
    var pointWallet: PointWallet,

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    var transactionType: TransactionType,

    @Column(name = "amount", nullable = false)
    var amount: Int,

    @Column(name = "description", nullable = false, length = 255)
    var description: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
