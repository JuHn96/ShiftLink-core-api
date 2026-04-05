package com.shiftlink.core_api.domain.user

import com.shiftlink.core_api.domain.system.OsType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user_devices", schema = "core")
class UserDevice(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Column(name = "device_token", nullable = false, unique = true)
    var deviceToken: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "os_type", nullable = false, length = 20)
    var osType: OsType,

    @Column(name = "last_login_at", nullable = false)
    var lastLoginAt: LocalDateTime = LocalDateTime.now(),
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}
