package my.project.petsnap.entity


import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
data class UserDB(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var username: String,
    var password: String,
    var birthday: LocalDate?=null,
    var avatar: String?=null, // path to pic
    var bio: String?=null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: MutableList<PostDB> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var likes: MutableList<LikeDB> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<CommentDB> = mutableListOf(),

    )
