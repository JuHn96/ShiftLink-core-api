package com.shiftlink.core_api.domain.system

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "system_constants", schema = "core")
class SystemConstant(
    @Column(name = "key_name", nullable = false, unique = true, length = 50)
    var keyName: String,

    @Column(name = "value", nullable = false)
    var value: String,

    @Column(name = "description")
    var description: String? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
