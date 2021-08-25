package com.example.newsbreeze.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsbreeze.data.model.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend  fun Insert(articleDao: ArticleDao):Long
    @Delete
    suspend fun deleteArticle(article: Article)
    @Query("SELECT *FROM articles")
    fun getAllArticles():LiveData<List<Article>>
}