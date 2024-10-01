package my.project.petsnap.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class PostDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var userId: Long,
    var image: String,
    var post: String,
    var postTime: LocalDateTime = LocalDateTime.now(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserDB,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<CommentDB> = mutableListOf(),

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableList<LikeDB> = mutableListOf()
)
