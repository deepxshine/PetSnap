import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petsnap.databinding.RvFragmentProfileBinding
import com.example.petsnap.domain.model.PostOnProfile

class PostsAdapter(private var posts: List<PostOnProfile>) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: RvFragmentProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RvFragmentProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val imageView = holder.binding.postImage

        // Используем Glide для загрузки изображения
        Glide.with(holder.itemView.context)
            .load(post.image)
            .centerCrop()
            .into(imageView)
    }

    override fun getItemCount(): Int = posts.size

    // обновление списка постов
    fun updatePosts(newPosts: List<PostOnProfile>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}
