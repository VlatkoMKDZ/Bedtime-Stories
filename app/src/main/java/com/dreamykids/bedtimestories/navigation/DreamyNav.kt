package com.dreamykids.bedtimestories.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.dreamykids.bedtimestories.ui.screens.*
import com.dreamykids.bedtimestories.viewmodel.DreamyViewModel

data class Tab(val route:String,val label:String,val icon:ImageVector)
@Composable fun DreamyNav(vm:DreamyViewModel= viewModel()){
 val nav= rememberNavController(); val tabs=listOf(Tab("home","Categories",Icons.Rounded.MenuBook),Tab("favorites","Favorites",Icons.Rounded.Favorite),Tab("timer","Timer",Icons.Rounded.Timer),Tab("more","More",Icons.Rounded.MoreHoriz))
 Scaffold(bottomBar={ NavigationBar{ val back by nav.currentBackStackEntryAsState(); tabs.forEach{ NavigationBarItem(selected=back?.destination?.route==it.route,onClick={nav.navigate(it.route){launchSingleTop=true}}, icon={Icon(it.icon,it.label)}, label={Text(it.label)}) } } }){ _ -> NavHost(nav,"home"){ composable("home"){HomeScreen(vm,{nav.navigate("category/$it")},{nav.navigate("more")})}; composable("category/{id}"){CategoryScreen(vm,it.arguments?.getString("id")!!){nav.navigate("player")}}; composable("player"){PlayerScreen(vm)}; composable("favorites"){FavoritesScreen(vm){nav.navigate("player")}}; composable("timer"){TimerScreen(vm)}; composable("more"){MoreScreen(vm)} } }
}
