package com.horie1024.apollosample

import ShowLoginUserQuery
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    companion object {
        private const val BASE_URL = "https://api.github.com/graphql"
        private const val GITHUB_ACCESS_TOKENS = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val okHttpClient = OkHttpClient.Builder()
            .authenticator { _, response ->
                response.request().newBuilder()
                    .addHeader("Authorization", "Bearer $GITHUB_ACCESS_TOKENS")
                    .build()
            }
            .build()

        val apolloClient = ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()

        val query = ShowLoginUserQuery.builder().build()

        apolloClient.query(query).enqueue(object : ApolloCall.Callback<ShowLoginUserQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                e.printStackTrace()
            }

            override fun onResponse(response: Response<ShowLoginUserQuery.Data>) {
                val viewer = response.data()?.viewer()
                Log.i("test login user", viewer?.login())
            }
        })
    }
}
