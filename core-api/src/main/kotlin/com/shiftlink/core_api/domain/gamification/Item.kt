package com.shiftlink.core_api.domain.gamification

import com.shiftlink.core_api.domain.file.FileMaster
import jakarta.persistence.*
import java.time.LocalDateTime

enum class ItemCategory { HEAD, BODY, BACKGROUND }

@Entity
@Table(name = "items", schema = "gamification")
class Item(
    @Column(name = "name", nullable = false, length = 100)
    var name: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    var category: ItemCategory,

    @Column(name = "price", nullable = false)
    var price: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_image_id", nullable = false)
    var assetImage: FileMaster,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
