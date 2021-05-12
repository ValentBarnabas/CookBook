package hu.bme.aut.android.cookbook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.CardRecipeBinding
import hu.bme.aut.android.cookbook.databinding.GridLayoutListItemBinding

class RecipeAdapter(private val context: Context) :
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(itemCallback) {

    private val recipeList: MutableList<Recipe> = mutableListOf()
    private var lastPosition = -1

    class RecipeViewHolder(binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAuthor: TextView = binding.tvAuthor
        val tvTitle: TextView = binding.tvTitle
        val tvBody: TextView = binding.tvBody
        val imgPost: ImageView = binding.imgPost
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecipeViewHolder(CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val tmpPost = recipeList[position]
        holder.tvAuthor.text = tmpPost.author
        holder.tvTitle.text = tmpPost.title
        holder.tvBody.text = tmpPost.rating.toString()

        if (tmpPost.imageUrl.isNullOrBlank()) {
            holder.imgPost.visibility = View.GONE
        } else {
            Glide.with(context).load(tmpPost.imageUrl).into(holder.imgPost)
            holder.imgPost.visibility = View.VISIBLE
        }

        setAnimation(holder.itemView, position)
    }

    fun addRecipe(recipe: Recipe?) {
        recipe ?: return

        recipeList += (recipe)
        submitList((recipeList))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    companion object {
        object itemCallback : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }
}