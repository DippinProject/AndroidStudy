package com.project.clonecodding.tinder.viewmodel

import androidx.lifecycle.ViewModel
import com.project.clonecodding.tinder.TinderEvent
import com.project.clonecodding.tinder.TinderState
import com.project.clonecodding.tinder.model.TinderModel
import com.project.clonecodding.youtube.model.DummyData.tinderModels
import com.project.clonecodding.youtube.model.HomeContentType
import com.project.clonecodding.youtube.model.YoutubeVideoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TinderViewModel : ViewModel() {
    private val _state: MutableStateFlow<TinderState> = MutableStateFlow(TinderState())
    val state get() = _state.asStateFlow()

    init {
        onEvent(TinderEvent.FetchTinderModels)
    }

    fun onEvent(event: TinderEvent) {
        when (event) {
            TinderEvent.FetchTinderModels -> {
                fetchTinderModels()
            }

            is TinderEvent.OnDislike -> {

            }

            is TinderEvent.OnFavorite -> {

            }

            is TinderEvent.OnLike -> {

            }
        }
    }


    private fun fetchTinderModels() {
        _state.value = _state.value.copy(
            modelList = tinderModels
        )
    }

}