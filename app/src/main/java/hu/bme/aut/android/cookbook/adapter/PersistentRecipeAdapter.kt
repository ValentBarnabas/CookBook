package hu.bme.aut.android.cookbook.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.CardRecipeBinding

class PersistentRecipeAdapter(private val context: Context) :
    ListAdapter<Recipe, PersistentRecipeAdapter.RecipeViewHolder>(itemCallback) {
    private var lastPosition = -1

    var itemClickListener: OnItemClickListener? = null

    inner class RecipeViewHolder(binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding = binding
        var recipe: Recipe? = null
        init {
            itemView.setOnClickListener{
                recipe?.let { recipe -> itemClickListener?.onItemClick(recipe) }                      //TODO: make this into working recipe opening thing
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecipeViewHolder(CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent,false))

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val tmpRecipe = this.getItem(position)

        holder.recipe = tmpRecipe
        holder.binding.tvTitle.text = tmpRecipe.title
        holder.binding.tvRating.text = tmpRecipe.rating.toString()

        if(tmpRecipe.imageUrl.isNullOrBlank()) {
            holder.binding.imgRecipe.visibility = View.GONE
        } else {
            var requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)
            Glide.with(context).load(tmpRecipe.imageUrl).apply(requestOptions).into(holder.binding.imgRecipe)
            holder.binding.imgRecipe.visibility = View.VISIBLE
        }
        setAnimation(holder.itemView, position)

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
                return oldItem.uID == newItem.uID
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}
