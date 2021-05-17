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
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.android.cookbook.R
import hu.bme.aut.android.cookbook.data.Recipe
import hu.bme.aut.android.cookbook.databinding.CardRecipeBinding

class RecipeAdapter(private val context: Context) :
    ListAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(itemCallback) {

    private val recipeList: MutableList<Recipe> = mutableListOf()
    private var lastPosition = -1

    var itemClickListener: OnItemClickListener? = null

    inner class RecipeViewHolder(binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvTitle: TextView = binding.tvTitle
        val tvRating: TextView = binding.tvRating
        val imgRecipe: ImageView = binding.imgRecipe

        var recipe: Recipe? = null

        init {
            itemView.setOnClickListener{
                var position= adapterPosition
                if(position != RecyclerView.NO_POSITION) itemClickListener?.onItemClick(recipeList.get(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecipeViewHolder(CardRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val tmpPost = recipeList[position]
        holder.tvTitle.text = tmpPost.title
        holder.tvRating.text = tmpPost.rating.toString()

        if (tmpPost.imageUrl.isNullOrBlank()) {
            holder.imgRecipe.visibility = View.GONE
        } else {
            var requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background)    //Fixes images looking bad
            Glide.with(context).load(tmpPost.imageUrl).apply(requestOptions).into(holder.imgRecipe)
            holder.imgRecipe.visibility = View.VISIBLE
        }

        setAnimation(holder.itemView, position)
    }

    fun addRecipe(recipe: Recipe?) {
        recipe ?: return

        recipeList += (recipe)
        submitList((recipeList))
    }

    fun removeAllItems() {
        for (item in recipeList) recipeList.removeAt(0)
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

    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}