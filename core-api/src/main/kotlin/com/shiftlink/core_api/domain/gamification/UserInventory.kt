package com.shiftlink.core_api.domain.gamification

import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_inventories", schema = "gamification")
class UserInventory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    var item: Item,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "acquired_at", nullable = false, updatable = false)
    val acquiredAt: LocalDateTime = LocalDateTime.now()
}
