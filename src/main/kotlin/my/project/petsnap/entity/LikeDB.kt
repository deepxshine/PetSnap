package my.project.petsnap.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class LikeDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var userId: Long,
    var postId: Long,
    var likeTime: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserDB,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: PostDB,
)
