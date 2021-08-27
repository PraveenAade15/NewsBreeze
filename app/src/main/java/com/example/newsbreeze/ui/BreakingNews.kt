package com.example.newsbreeze.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsbreeze.NewsActivity
import com.example.newsbreeze.R
import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsbreeze.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsbreeze.util.Resource
import com.example.newsbreeze.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.item_layout.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class BreakingNews : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG="BreakingNews Fragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()
       newsAdapter.setOnItemClickListener {
          val bundle=Bundle().apply {
              putSerializable("article",it)
          }
           findNavController().navigate(
               R.id.action_breakingNews_to_articleWebview,
               bundle
           )
       }
        var job: Job?=null
        etSearchNews.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages=newsResponse.totalResults/ QUERY_PAGE_SIZE+2
                        islastPage=viewModel.breakingNewsPage==totalPages
                        if (islastPage){
                            rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")

                    }
                }
                is Resource.Loading ->{
                    showProgressBar()
                }


            }
        })
    }




    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false

    }
    private fun showProgressBar() {
        paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }


    var isLoading=false
    var islastPage=false
    var isScrolling=false
    val scrollListener=object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager=recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition =layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount=layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNpotLastPage=!isLoading && !islastPage
            val isAtLastitem=firstVisibleItemPosition+visibleItemCount>=totalItemCount
            val isNotAtBegining=firstVisibleItemPosition>=0
            val isTotalMoreThenVisible=totalItemCount>=QUERY_PAGE_SIZE
            val shouldPaginate=isNotLoadingAndNpotLastPage && isAtLastitem && isNotAtBegining &&
                    isTotalMoreThenVisible && isScrolling
            if (shouldPaginate){
                viewModel.getBreakingNews("india")
                isScrolling=false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true
            }
        }



    }


    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)

        }

    }


}