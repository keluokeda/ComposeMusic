package com.ke.music.repository

import com.ke.music.common.mediator.HotArtistRemoteMediator
import com.ke.music.common.mediator.NewAlbumsRemoteMediator
import com.ke.music.repository.mediator.AllMvRemoteMediator
import com.ke.music.repository.mediator.HotArtistListRemoteMediator
import com.ke.music.repository.mediator.NewAlbumListRemoteMediator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
@Suppress("unused")
abstract class BindRemoteMediatorModule {


    @Binds
    internal abstract fun bindAllMvRemoteMediator(mediator: AllMvRemoteMediator): com.ke.music.common.mediator.AllMvRemoteMediator

    @Binds
    internal abstract fun bindHotArtistRemoteMediator(hotArtistListRemoteMediator: HotArtistListRemoteMediator): HotArtistRemoteMediator

    @Binds
    internal abstract fun bindNewAlbumsRemoteMediator(newAlbumListRemoteMediator: NewAlbumListRemoteMediator): NewAlbumsRemoteMediator
}