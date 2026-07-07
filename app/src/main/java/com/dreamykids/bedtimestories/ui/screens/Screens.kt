package com.dreamykids.bedtimestories.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Forward10
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Replay10
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamykids.bedtimestories.ui.components.HeroHeader
import com.dreamykids.bedtimestories.ui.components.MoonHero
import com.dreamykids.bedtimestories.ui.components.NightSky
import com.dreamykids.bedtimestories.ui.components.StoryCard
import com.dreamykids.bedtimestories.ui.components.TimerChips
import com.dreamykids.bedtimestories.ui.theme.CreamSurface
import com.dreamykids.bedtimestories.ui.theme.DarkerOrange
import com.dreamykids.bedtimestories.ui.theme.DarkBrown
import com.dreamykids.bedtimestories.ui.theme.MediumBrown
import com.dreamykids.bedtimestories.ui.theme.WarmBorder
import com.dreamykids.bedtimestories.viewmodel.DreamyViewModel

@Composable
fun HomeScreen(
    vm: DreamyViewModel,
    onCategory: (String) -> Unit,
    onSettings: () -> Unit
) {
    val query by vm.query.collectAsState()
    val results by vm.searchResults.collectAsState()

    NightSky {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(170.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                HeroHeader(onSettings = onSettings)
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                MoonHero()
            }
            item(span = { GridItemSpan(maxLineSpan) }) {
                OutlinedTextField(
                    value = query,
                    onValueChange = vm::updateQuery,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Search") },
                    label = { Text("Search stories, categories, keywords, narrators") },
                    singleLine = true
                )
            }

            if (query.isBlank()) {
                items(vm.categories) { category ->
                    com.dreamykids.bedtimestories.ui.components.CategoryCard(category) {
                        onCategory(category.id)
                    }
                }
            } else {
                items(
                    items = results,
                    span = { GridItemSpan(maxLineSpan) }
                ) { story ->
                    StoryCard(
                        story = story,
                        isFavorite = false,
                        onFavorite = { vm.toggleFavorite(story.id) },
                        onPlay = { vm.play(story) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryScreen(
    vm: DreamyViewModel,
    id: String,
    onStory: (String) -> Unit
) {
    val favorites by vm.favorites.collectAsState()
    val category = vm.categories.first { item -> item.id == id }

    NightSky {
        LazyColumn(contentPadding = PaddingValues(vertical = 20.dp)) {
            item {
                Text(
                    text = "${category.emoji} ${category.title} Stories",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(20.dp)
                )
                Text(
                    text = category.description,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )
            }
            items(vm.storiesFor(id)) { story ->
                StoryCard(
                    story = story,
                    isFavorite = story.id in favorites,
                    onFavorite = { vm.toggleFavorite(story.id) },
                    onPlay = {
                        vm.play(story)
                        onStory(story.id)
                    }
                )
            }
        }
    }
}

@Composable
fun PlayerScreen(vm: DreamyViewModel) {
    val player by vm.player.collectAsState()

    NightSky {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = player.current?.emoji ?: "📚", fontSize = 130.sp)
            Text(
                text = player.current?.title ?: "Choose a story",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Narrated by ${player.current?.narrator ?: "Dreamy Kids"} • " +
                    "${player.current?.durationMinutes ?: 0} minutes"
            )
            LinearProgressIndicator(
                progress = { player.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {}) { Icon(Icons.Rounded.SkipPrevious, "Previous") }
                IconButton(onClick = {}) { Icon(Icons.Rounded.Replay10, "10 sec rewind") }
                FilledIconButton(
                    onClick = { vm.togglePlay() },
                    modifier = Modifier.size(76.dp)
                ) {
                    Icon(
                        imageVector = if (player.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier.size(42.dp)
                    )
                }
                IconButton(onClick = {}) { Icon(Icons.Rounded.Forward10, "10 sec forward") }
                IconButton(onClick = {}) { Icon(Icons.Rounded.SkipNext, "Next") }
            }
            Row {
                AssistChip(onClick = {}, label = { Text("❤ Favorite") })
                Spacer(Modifier.width(8.dp))
                AssistChip(onClick = { vm.setSpeed(nextSpeed(player.speed)) }, label = { Text("${player.speed}x Speed") })
                Spacer(Modifier.width(8.dp))
                AssistChip(onClick = {}, label = { Text("Sleep Timer") })
            }
            TimerChips(selected = player.sleepTimerMinutes, onSelect = vm::setTimer)
        }
    }
}

@Composable
fun FavoritesScreen(
    vm: DreamyViewModel,
    onStory: (String) -> Unit
) {
    val favorites by vm.favorites.collectAsState()
    val favoriteStories = vm.categories
        .flatMap { category -> vm.storiesFor(category.id) }
        .filter { story -> story.id in favorites }

    NightSky {
        LazyColumn {
            item {
                Text(
                    text = "❤️ Favorites",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(20.dp)
                )
                Text(
                    text = "Sort by: Recently Played • Favorites • Downloaded",
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            items(favoriteStories) { story ->
                StoryCard(
                    story = story,
                    isFavorite = true,
                    onFavorite = { vm.toggleFavorite(story.id) },
                    onPlay = {
                        vm.play(story)
                        onStory(story.id)
                    }
                )
            }
        }
    }
}

@Composable
fun TimerScreen(vm: DreamyViewModel) {
    val player by vm.player.collectAsState()

    NightSky {
        Column(Modifier.padding(24.dp)) {
            Text("🕒 Sleep Timer", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text("Fade volume before stopping playback for a peaceful ending.")
            TimerChips(selected = player.sleepTimerMinutes, onSelect = vm::setTimer)
        }
    }
}

@Composable
fun MoreScreen(vm: DreamyViewModel, onAbout: () -> Unit, onPrivacy: () -> Unit) {
    val settings by vm.settings.collectAsState()
    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)

    NightSky {
        LazyColumn(contentPadding = PaddingValues(24.dp)) {
            item {
                Text("More", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = DarkBrown)
                SettingRow("Dark Mode", settings.darkMode) { vm.updateSettings { it.copy(darkMode = !settings.darkMode) } }
                SettingRow("Remember Last Story", settings.rememberLastStory) { vm.updateSettings { it.copy(rememberLastStory = !settings.rememberLastStory) } }
                SettingRow("Notifications", settings.notifications) { vm.updateSettings { it.copy(notifications = !settings.notifications) } }
                Text("Playback Speed", modifier = Modifier.padding(top = 18.dp), fontWeight = FontWeight.Bold, color = DarkBrown)
                Row(Modifier.horizontalScroll(androidx.compose.foundation.rememberScrollState())) {
                    speeds.forEach { speed ->
                        FilterChip(
                            selected = settings.playbackSpeed == speed,
                            onClick = { vm.setSpeed(speed) },
                            label = { Text("${speed}x") },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
                Button(onClick = onAbout, modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) { Text("About Dreamy Bedtime Stories") }
                Button(onClick = onPrivacy, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) { Text("Privacy Policy") }
            }
        }
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), color = DarkBrown)
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}

@Composable
fun AboutScreen() {
    NightSky {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Card(colors = CardDefaults.cardColors(CreamSurface), border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)) {
                Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Dreamy Bedtime Stories", fontWeight = FontWeight.Bold, color = DarkBrown)
                    Text("Version 1.0.0", color = DarkBrown)
                    Spacer(Modifier.height(18.dp))
                    Text("Dreamy Bedtime Stories is a magical app for kids, created with love by Parent101.net to make bedtime peaceful, fun, and memorable.", color = DarkBrown)
                    Spacer(Modifier.height(18.dp))
                    Text("A product from Parent101.net ❤", color = DarkerOrange, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PrivacyScreen() {
    NightSky {
        Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Card(colors = CardDefaults.cardColors(CreamSurface), border = androidx.compose.foundation.BorderStroke(1.dp, WarmBorder)) {
                Column(Modifier.padding(24.dp)) {
                    Text("Privacy Policy", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DarkBrown)
                    Text("Your privacy is important to us.\nWe do not collect any personal information from children.\nThis app does not require registration and does not collect or share your data.\nWe may use anonymous usage data to improve the app experience.\nThis app is safe, COPPA compliant, and designed for children and families.", color = MediumBrown, modifier = Modifier.padding(top = 16.dp))
                    Text("Learn more at Parent101.net/privacy", color = DarkerOrange, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 18.dp))
                }
            }
        }
    }
}

private fun nextSpeed(current: Float): Float {
    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
    return speeds[(speeds.indexOf(current).takeIf { it >= 0 } ?: 1).plus(1) % speeds.size]
}
