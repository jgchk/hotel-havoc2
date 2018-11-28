package com.jgchk.hotelhavoc.core.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jgchk.hotelhavoc.AndroidApplication
import com.jgchk.hotelhavoc.BuildConfig
import com.jgchk.hotelhavoc.core.gson.RuntimeTypeAdapterFactory
import com.jgchk.hotelhavoc.model.ActionController
import com.jgchk.hotelhavoc.model.actions.OnCompleteListener
import com.jgchk.hotelhavoc.model.ingredients.Ingredient
import com.jgchk.hotelhavoc.model.orders.Fries
import com.jgchk.hotelhavoc.model.orders.Hamburger
import com.jgchk.hotelhavoc.model.orders.Milkshake
import com.jgchk.hotelhavoc.model.orders.Order
import com.jgchk.hotelhavoc.network.SensorsRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: AndroidApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val ingredientTypeFactory = RuntimeTypeAdapterFactory
            .of(Ingredient::class.java, "type")
        Ingredient.ingredientMap.forEach { ingredientTypeFactory.registerSubtype(it.value, it.key) }

        val orderTypeFactory = RuntimeTypeAdapterFactory
            .of(Order::class.java, "type")
            .registerSubtype(Hamburger::class.java, "hamburger")
            .registerSubtype(Fries::class.java, "fries")
            .registerSubtype(Milkshake::class.java, "milkshake")

        return GsonBuilder()
            .registerTypeAdapterFactory(ingredientTypeFactory)
            .registerTypeAdapterFactory(orderTypeFactory)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://graph.api.smartthings.com/api/smartapps/installations/b8361a51-d8b1-45a2-8912-dc3f1e396de5/")
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor {
            val original = it.request()
            val request = original.newBuilder()
                .header("Authorization", "Bearer 2f954bb9-ada0-49ae-822d-5630b8ea69d2")
                .method(original.method(), original.body())
                .build()
            it.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideSensorsRepository(dataSource: SensorsRepository.Network): SensorsRepository = dataSource

    @Provides
    @Singleton
    fun provideOnCompleteListener(listener: ActionController): OnCompleteListener = listener

}