package com.shiftlink.core_api.domain.gamification

import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_equipped_avatars", schema = "gamification")
class UserEquippedAvatar(
    @Id
    @Column(name = "user_id")
    val userId: Long,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_item_id")
    var headItem: Item? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_item_id")
    var bodyItem: Item? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bg_item_id")
    var bgItem: Item? = null,
) {
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
