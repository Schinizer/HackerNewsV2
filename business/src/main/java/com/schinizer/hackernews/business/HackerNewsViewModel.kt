package com.schinizer.hackernews.business

import androidx.collection.LruCache
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.schinizer.hackernews.data.HackerNewsRepository
import com.schinizer.hackernews.data.dagger.DispatcherModule
import com.schinizer.hackernews.data.remote.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HackerNewsViewModel @Inject constructor(
    private val repo: HackerNewsRepository,
    @DispatcherModule.IO private val io: CoroutineDispatcher
): ViewModel() {
    private val data = LruCache<Int, Item>(500)
    private val jobPool = mutableMapOf<Int, Job>()
    private val itemOrder = mutableListOf<Int>()

    // State flow to represent UI State
    private val _dataFlow = MutableStateFlow<List<ItemState>>(emptyList())
    val dataFlow = _dataFlow.asStateFlow()

    // Shared Flow to signal android specific actions (Launch intent etc)
    private val _action = MutableSharedFlow<Action>()
    val actionFlow = _action.asSharedFlow()

    // State flow to signal SwipeRefreshLayout
    private val _isLoading = MutableStateFlow(false)
    val isLoadingFlow = _isLoading.asSharedFlow()

    init {
        refreshData()
    }

    // Refreshes data ceremony:
    // 1: Clear job pool and existing jobs
    // 2: Get fresh top500Stories ids
    // 3: Update dataFlow
    fun refreshData() {
        viewModelScope.launch(io) {
            with(jobPool) {
                forEach { (_, job) -> job.cancel() }
                clear()
            }

            with(itemOrder) {
                clear()
                _isLoading.emit(true)
                addAll(repo.top500Stories())
                _isLoading.emit(false)
                _dataFlow.value = itemOrder.map { ItemState(it, data[it]) }
            }
        }
    }

    fun loadItem(id: Int) {
        if(data[id] != null) return
        jobPool.getOrPut(id) {
            viewModelScope.launch(io) {
                repo.fetchItem(id)?.let { data.put(id, it) }
                jobPool.remove(id)
                _dataFlow.value = itemOrder.map { ItemState(it, data[it]) }
            }
        }
    }

    fun cancelLoadItem(id: Int) {
        jobPool[id]?.let {
            it.cancel()
            jobPool.remove(id)
        }
    }

    fun openItem(id: Int) {
        data[id]?.let { item ->
            when(item) {
                is Item.Story -> {
                    val openBrowser = item.url?.let { OpenBrowser(it) }
                    viewModelScope.launch {
                        _action.emit(openBrowser ?: ShowUnsupportedSnackBar)
                    }
                    return

                }
                else -> viewModelScope.launch {
                    _action.emit(ShowUnsupportedSnackBar)
                }
            }
        }
    }

    data class ItemState(
        val id: Int,
        val item: Item?
    )

    sealed interface Action
    data class OpenBrowser(val url: String) : Action
    object ShowUnsupportedSnackBar : Action
}

