package com.example.newsbreeze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsbreeze.R
import com.example.newsbreeze.data.model.Article
import kotlinx.android.synthetic.main.item_layout.view.*

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    //create a list of item and notify data set change it will create two method in background
    private val differCallback=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
         return oldItem.url==newItem.url
            //comparing url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
            //the content changes
        }

    }
    //asynch list differ
    val differ=AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        R.layout.item_layout,parent,false
                )

        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
         val article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivPoster)
            tvTitle.text=article.title
            tvDescription.text=article.description
            setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener:((Article)->Unit)?=null
    fun setOnItemClickListener(listener:(Article) ->Unit){
        onItemClickListener=listener
    }
}