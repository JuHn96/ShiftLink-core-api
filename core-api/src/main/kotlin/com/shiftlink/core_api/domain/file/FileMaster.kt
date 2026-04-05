package com.shiftlink.core_api.domain.file

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "files", schema = "core")
class FileMaster(
    @Column(name = "user_id")
    var userId: Long? = null,

    @Column(name = "s3_url", nullable = false, length = 500)
    var s3Url: String,

    @Column(name = "file_type", nullable = false, length = 50)
    var fileType: String,

    @Column(name = "usage_context", nullable = false, length = 50)
    var usageContext: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
