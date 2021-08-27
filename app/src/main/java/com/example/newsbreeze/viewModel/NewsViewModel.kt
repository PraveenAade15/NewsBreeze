package com.example.newsbreeze.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.data.model.Article
import com.example.newsbreeze.data.model.NewsResponse
import com.example.newsbreeze.repository.NewsRepository
import com.example.newsbreeze.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository):ViewModel() {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> =MutableLiveData()
    var breakingNewsPage=1
    var breakingNewsResponse:NewsResponse?=null
    val searchgNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage=1
    var searchNewsResponse:NewsResponse?=null
    init {
        getBreakingNews("us")
    }
    //only call suspend or coroutine

    fun getBreakingNews(countryCode:String)=viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }
    fun searchNews(searchQuery:String)=viewModelScope.launch {
        searchgNews.postValue((Resource.Loading()))
        val response=newsRepository.searchNews(searchQuery,searchNewsPage)
        searchgNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {resultResponse->

                breakingNewsPage++
                if (breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticle=breakingNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let {resultResponse->

                searchNewsPage++
                if (searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }else{
                    val oldArticle=searchNewsResponse?.articles
                    val newArticles=resultResponse.articles
                    oldArticle?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)

            }
        }
        return Resource.Error(response.message())
    }
    fun saveArticle(article: Article)=viewModelScope.launch {
        newsRepository.insert(article)
    }
    fun getSavedNews()=newsRepository.getSvedNews()
    fun deleteArticle(article: Article)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }


}