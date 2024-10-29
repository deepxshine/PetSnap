package my.project.petsnap.dto

data class UserFollowersAndFollowings(
    val followersList: List<FollowDTO>,
    val followingsList: List<FollowDTO>
)
