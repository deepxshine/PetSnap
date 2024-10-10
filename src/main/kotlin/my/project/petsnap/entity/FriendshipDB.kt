package my.project.petsnap.entity

import jakarta.persistence.*

@Entity
data class FriendshipDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "follower_id")
    var follower: UserDB,

    @ManyToOne
    @JoinColumn(name = "following_id")
    var following: UserDB
)