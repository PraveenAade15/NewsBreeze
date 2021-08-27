package com.example.newsbreeze

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsbreeze.data.local.ArticleDatabase
import com.example.newsbreeze.repository.NewsRepository
import com.example.newsbreeze.viewModel.NewsViewModel
import com.example.newsbreeze.viewModel.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val newsRepository= NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory= NewsViewModelProviderFactory(newsRepository)
        viewModel= ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    }
}