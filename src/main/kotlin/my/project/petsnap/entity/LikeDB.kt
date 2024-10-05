package my.project.petsnap.entity

import jakarta.persistence.*

@Entity
data class LikeDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserDB,

    @ManyToOne
    @JoinColumn(name = "post_id")
    var post: PostDB,
)
