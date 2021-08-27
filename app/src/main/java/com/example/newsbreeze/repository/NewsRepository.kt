package com.example.newsbreeze.repository

import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.api.RetrofitInstance
import com.example.newsbreeze.data.local.ArticleDatabase
import com.example.newsbreeze.data.model.Article

class NewsRepository(val db:ArticleDatabase) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)
    suspend fun searchNews(searchQuery:String,pageNumber:Int)=RetrofitInstance.api.searchForNews(searchQuery,pageNumber)

    suspend fun insert(article: Article)=db.getArticleDao().Insert(article)
    fun getSvedNews()=db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article)=db.getArticleDao().deleteArticle(article)
}