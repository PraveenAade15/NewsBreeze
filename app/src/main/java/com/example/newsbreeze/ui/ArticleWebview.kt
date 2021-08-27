package com.example.newsbreeze.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsbreeze.NewsActivity
import com.example.newsbreeze.R
import com.example.newsbreeze.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article_webview.*
import kotlinx.android.synthetic.main.item_layout.*


class ArticleWebview : Fragment(R.layout.fragment_article_webview) {
    lateinit var viewModel: NewsViewModel
 val args:ArticleWebviewArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
  val article=args.article
       webView.apply {
           webViewClient=WebViewClient()
           loadUrl(article.url)
       }
        fab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT).show()
        }
        }

    }

