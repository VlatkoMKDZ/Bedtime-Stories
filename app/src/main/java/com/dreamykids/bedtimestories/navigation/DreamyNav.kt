package com.dreamykids.bedtimestories.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dreamykids.bedtimestories.ui.screens.AboutScreen
import com.dreamykids.bedtimestories.ui.screens.CategoryScreen
import com.dreamykids.bedtimestories.ui.screens.FavoritesScreen
import com.dreamykids.bedtimestories.ui.screens.HomeScreen
import com.dreamykids.bedtimestories.ui.screens.MoreScreen
import com.dreamykids.bedtimestories.ui.screens.PlayerScreen
import com.dreamykids.bedtimestories.ui.screens.PrivacyScreen
import com.dreamykids.bedtimestories.ui.theme.DreamyTheme
import com.dreamykids.bedtimestories.viewmodel.DreamyViewModel

data class Tab(val route:String,val label:String,val icon:ImageVector)

@Composable
fun DreamyNav(vm: DreamyViewModel = viewModel()) {
    val nav = rememberNavController()
    val settings by vm.settings.collectAsState()
    val tabs = listOf(
        Tab("home", "Categories", Icons.Rounded.MenuBook),
        Tab("favorites", "Favorites", Icons.Rounded.Favorite),
        Tab("more", "More", Icons.Rounded.MoreHoriz)
    )
    DreamyTheme(darkMode = settings.darkMode) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val back by nav.currentBackStackEntryAsState()
                    tabs.forEach { tab ->
                        NavigationBarItem(
                            selected = back?.destination?.route == tab.route,
                            onClick = { nav.navigate(tab.route) { launchSingleTop = true } },
                            icon = { Icon(tab.icon, tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(nav, "home", Modifier.padding(padding)) {
                composable("home") { HomeScreen(vm, { nav.navigate("category/$it") }, { nav.navigate("more") }) }
                composable("category/{id}") { CategoryScreen(vm, it.arguments?.getString("id")!!) { nav.navigate("player") } }
                composable("player") { PlayerScreen(vm) }
                composable("favorites") { FavoritesScreen(vm) { nav.navigate("player") } }
                composable("more") { MoreScreen(vm, onAbout = { nav.navigate("about") }, onPrivacy = { nav.navigate("privacy") }) }
                composable("about") { AboutScreen() }
                composable("privacy") { PrivacyScreen() }
            }
        }
    }
}
