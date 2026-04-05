package com.shiftlink.core_api.domain.user

import com.shiftlink.core_api.domain.file.FileMaster
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

enum class AuthProvider { LOCAL, KAKAO, NAVER, GOOGLE }
enum class UserRole { ROLE_USER, ROLE_ADMIN }
enum class UserStatus { ACTIVE, SUSPENDED, WITHDRAWN }

@Entity
@Table(name = "users", schema = "core")
class User(
    @Column(name = "login_id", unique = true, length = 50)
    var loginId: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false, length = 20)
    var authProvider: AuthProvider,

    @Column(name = "provider_id", length = 100)
    var providerId: String? = null,

    @Column(name = "password_hash")
    var passwordHash: String? = null,

    @Column(name = "phone_number", unique = true, length = 20)
    var phoneNumber: String? = null,

    @Column(name = "email", length = 100)
    var email: String? = null,

    @Column(name = "name", nullable = false, length = 50)
    var name: String,

    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,

    @Column(name = "nickname", nullable = false, length = 50)
    var nickname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    var role: UserRole = UserRole.ROLE_USER,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: UserStatus = UserStatus.ACTIVE,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    var profileImage: FileMaster? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(name = "is_payday_warning_on", nullable = false)
    var isPaydayWarningOn: Boolean = true

    @Column(name = "is_clockout_reminder_on", nullable = false)
    var isClockoutReminderOn: Boolean = true

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean = false

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
}
