package com.shiftlink.core_api.domain.user

import jakarta.persistence.*
import java.time.LocalDateTime

enum class ClientType { WEB_PC, WEB_MO, APP_AOS, APP_IOS }

@Entity
@Table(name = "login_histories", schema = "core")
class LoginHistory(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false, length = 20)
    var clientType: ClientType,

    @Column(name = "user_agent", nullable = false)
    var userAgent: String,

    @Column(name = "login_ip", nullable = false, length = 50)
    var loginIp: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
