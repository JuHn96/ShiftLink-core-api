package com.shiftlink.core_api.domain.cs

import com.shiftlink.core_api.domain.file.FileMaster
import com.shiftlink.core_api.domain.user.User
import jakarta.persistence.*
import java.time.LocalDateTime

enum class InquiryCategory { BILLING, BUG, ACCOUNT }
enum class InquiryStatus { WAITING, ANSWERED }

@Entity
@Table(name = "inquiries", schema = "core")
class Inquiry(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    var category: InquiryCategory,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evidence_image_id")
    var evidenceImage: FileMaster? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: InquiryStatus = InquiryStatus.WAITING

    @Column(name = "answer_content", columnDefinition = "TEXT")
    var answerContent: String? = null

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "answered_at")
    var answeredAt: LocalDateTime? = null
}
