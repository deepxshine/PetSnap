package my.project.petsnap.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class CommentDB(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var comment: String,
    var commentTime: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: PostDB,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserDB,

//    @OneToMany(mappedBy = "comment", cascade = [CascadeType.ALL], orphanRemoval = true)
//    var likes: MutableList<LikeDB> = mutableListOf(),
)
