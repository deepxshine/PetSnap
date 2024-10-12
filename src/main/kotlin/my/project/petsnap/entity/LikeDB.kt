package my.project.petsnap.entity

import jakarta.persistence.*

@Entity
data class LikeDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserDB,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: PostDB,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "comment_id")
//    var comment: CommentDB?
)
