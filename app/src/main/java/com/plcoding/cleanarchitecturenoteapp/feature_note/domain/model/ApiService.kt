package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.model

import retrofit2.http.GET

interface ApiService {

    // 反回的 .gif动图 该如何保存呢？data bean: 保存哪些信息？
    @GET("http://pinyin.cm/")
    suspend fun requestSearchByCoroutines(@retrofit2.http.Query("s") keywords: String):

}