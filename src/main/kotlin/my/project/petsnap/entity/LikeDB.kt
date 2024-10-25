package my.project.petsnap.entity

import jakarta.persistence.*

@Entity
data class LikeDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    var user: UserDB,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "post_id")
    var post: PostDB,
)
{
    override fun toString(): String {
        return "LikeDB(id=$id, userId=${user.id}, postId=${post.id}, likesOnPost=${post.likes.size})"
    }
}
