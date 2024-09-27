package com.project.clonecoding.presentation.tinder.viewmodel

import androidx.lifecycle.ViewModel
import com.project.clonecoding.presentation.tinder.TinderEvent
import com.project.clonecoding.presentation.tinder.TinderState
import com.project.clonecoding.presentation.youtube.model.DummyData.tinderModels
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