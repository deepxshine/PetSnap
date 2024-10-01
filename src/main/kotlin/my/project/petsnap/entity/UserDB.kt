package my.project.petsnap.entity


import jakarta.persistence.*
import java.time.LocalDate

@Entity
data class UserDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var username: String,
    var password: String,
    var gender: Int,
    var birthday: LocalDate,
    var avatar: String, // path to pic

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: MutableList<PostDB> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableList<LikeDB> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "friendship",
        joinColumns = [JoinColumn(name = "follower_id")],
        inverseJoinColumns = [JoinColumn(name = "following_id")]
    )
    var following: MutableSet<UserDB> = mutableSetOf(),

    @ManyToMany(mappedBy = "following")
    var followers: MutableSet<UserDB> = mutableSetOf(),

    )
