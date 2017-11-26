package net.bogus.githubsearch

import io.reactivex.Maybe
import io.reactivex.Single
import net.bogus.githubsearch.model.Repository
import net.bogus.githubsearch.model.RepositoryRequest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by burak on 11/19/17.
 */
interface APIClient {

    companion object {

        fun createAPIClient() : APIClient {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.github.com/")
                    .build()

            return retrofit.create(APIClient::class.java)
        }

    }

    @GET("search/repositories")
    fun search(@Query("q") query:String, @Query("page") page:Int) : Maybe<RepositoryRequest>



}