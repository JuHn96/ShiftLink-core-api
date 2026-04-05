package com.shiftlink.core_api.domain.system

import jakarta.persistence.*
import java.time.LocalDateTime

enum class OsType { AOS, IOS }

@Entity
@Table(name = "app_versions", schema = "core")
class AppVersion(
    @Enumerated(EnumType.STRING)
    @Column(name = "os_type", nullable = false, length = 20)
    var osType: OsType,

    @Column(name = "latest_version", nullable = false, length = 20)
    var latestVersion: String,

    @Column(name = "min_required_version", nullable = false, length = 20)
    var minRequiredVersion: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
