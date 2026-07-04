package com.dreamykids.bedtimestories.viewmodel

import androidx.lifecycle.ViewModel
import com.dreamykids.bedtimestories.model.*
import com.dreamykids.bedtimestories.repository.StoryRepository
import kotlinx.coroutines.flow.*

class DreamyViewModel(private val repo: StoryRepository = StoryRepository()) : ViewModel() {
    val categories = repo.categories
    val favorites = repo.favorites
    private val _player = MutableStateFlow(PlayerState())
    val player: StateFlow<PlayerState> = _player
    private val _settings = MutableStateFlow(SettingsState())
    val settings: StateFlow<SettingsState> = _settings
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query
    val searchResults = _query.map { repo.search(it) }.stateIn(kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main.immediate), SharingStarted.Eagerly, repo.stories)
    fun storiesFor(categoryId:String)=repo.stories.filter{it.categoryId==categoryId}
    fun play(story: Story){ _player.value = _player.value.copy(current=story,isPlaying=true,progress=.22f) }
    fun togglePlay(){ _player.value = _player.value.copy(isPlaying=!_player.value.isPlaying) }
    fun toggleFavorite(id:String)=repo.toggleFavorite(id)
    fun setTimer(minutes:Int?){ _player.value = _player.value.copy(sleepTimerMinutes=minutes) }
    fun setSpeed(speed:Float){ _player.value = _player.value.copy(speed=speed); _settings.value=_settings.value.copy(playbackSpeed=speed) }
    fun updateQuery(value:String){ _query.value=value }
    fun updateSettings(transform:(SettingsState)->SettingsState){ _settings.value=transform(_settings.value) }
}
