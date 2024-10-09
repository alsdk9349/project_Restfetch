package com.suisei.restfetch.di

import android.content.Context
import com.suisei.restfetch.data.remote.SSEClient
import com.suisei.restfetch.data.repository.AccountRepository
import com.suisei.restfetch.data.repository.MyDataRepository
import com.suisei.restfetch.data.repository.NotifyRepository
import com.suisei.restfetch.data.repository.QRScannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNotifyRepository(): NotifyRepository {
        return NotifyRepository()
    }

    @Provides
    @Singleton
    fun provideAccountRepository(): AccountRepository {
        return AccountRepository()
    }

    @Provides
    @Singleton
    fun provideMyDataRepository(): MyDataRepository {
        return MyDataRepository()
    }

    @Provides
    @Singleton
    fun provideQRScannerRepository(): QRScannerRepository {
        return QRScannerRepository()
    }

    @Provides
    @Singleton
    fun provideSSEClient(): SSEClient {
        return SSEClient()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}