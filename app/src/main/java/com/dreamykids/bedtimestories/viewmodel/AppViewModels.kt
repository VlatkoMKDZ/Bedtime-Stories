package com.dreamykids.bedtimestories.viewmodel

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.content.pm.PackageManager
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dreamykids.bedtimestories.model.PlayerState
import com.dreamykids.bedtimestories.model.SettingsState
import com.dreamykids.bedtimestories.model.Story
import com.dreamykids.bedtimestories.repository.StoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private val Application.settingsStore by preferencesDataStore("dreamy_settings")

class DreamyViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: StoryRepository = StoryRepository()
    val categories = repo.categories
    val favorites = repo.favorites

    private val darkModeKey = booleanPreferencesKey("dark_mode")
    private val rememberKey = booleanPreferencesKey("remember_last_story")
    private val notificationsKey = booleanPreferencesKey("notifications")
    private val speedKey = floatPreferencesKey("playback_speed")

    private val _player = MutableStateFlow(PlayerState())
    val player: StateFlow<PlayerState> = _player

    val settings: StateFlow<SettingsState> = application.settingsStore.data
        .map { prefs ->
            SettingsState(
                darkMode = prefs[darkModeKey] ?: false,
                playbackSpeed = prefs[speedKey] ?: 1f,
                rememberLastStory = prefs[rememberKey] ?: true,
                notifications = prefs[notificationsKey] ?: false
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SettingsState())

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val searchResults: StateFlow<List<Story>> = _query
        .map { query -> repo.search(query) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), repo.stories)

    init {
        viewModelScope.launch {
            settings.collect { state ->
                _player.value = _player.value.copy(speed = state.playbackSpeed)
            }
        }
    }

    fun storiesFor(categoryId: String): List<Story> = repo.stories.filter { story -> story.categoryId == categoryId }

    fun play(story: Story) {
        _player.value = _player.value.copy(current = story, isPlaying = true, progress = 0.22f, speed = settings.value.playbackSpeed)
        updatePlaybackNotification()
    }

    fun togglePlay() {
        _player.value = _player.value.copy(isPlaying = !_player.value.isPlaying)
        updatePlaybackNotification()
    }
    fun toggleFavorite(id: String) { repo.toggleFavorite(id) }
    fun setTimer(minutes: Int?) { _player.value = _player.value.copy(sleepTimerMinutes = minutes) }
    fun setSpeed(speed: Float) { updateSettings { it.copy(playbackSpeed = speed) } }
    fun updateQuery(value: String) { _query.value = value }

    private fun updatePlaybackNotification() {
        val app = getApplication<Application>()
        val manager = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (!settings.value.notifications || !_player.value.isPlaying) {
            manager.cancel(101)
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel("dreamy_playback", "Story playback", NotificationManager.IMPORTANCE_LOW)
            )
        }
        if (Build.VERSION.SDK_INT >= 33 && app.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(app, "dreamy_playback")
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(app)
        }
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentTitle(_player.value.current?.title ?: "Dreamy Bedtime Stories")
            .setContentText("Playback controls: pause, previous, next")
            .setOngoing(true)
            .addAction(android.R.drawable.ic_media_pause, "Pause", null)
            .addAction(android.R.drawable.ic_media_previous, "Previous", null)
            .addAction(android.R.drawable.ic_media_next, "Next", null)
            .build()
        manager.notify(101, notification)
    }

    fun updateSettings(transform: (SettingsState) -> SettingsState) {
        val next = transform(settings.value)
        viewModelScope.launch {
            getApplication<Application>().settingsStore.edit { prefs ->
                prefs[darkModeKey] = next.darkMode
                prefs[speedKey] = next.playbackSpeed
                prefs[rememberKey] = next.rememberLastStory
                prefs[notificationsKey] = next.notifications
            }
        }
    }
}
