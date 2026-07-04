package com.dreamykids.bedtimestories.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.dreamykids.bedtimestories.model.*
import com.dreamykids.bedtimestories.ui.theme.*

@Composable fun NightSky(content:@Composable BoxScope.()->Unit){
 val infinite= rememberInfiniteTransition(label="stars"); val alpha by infinite.animateFloat(.35f,1f,infiniteRepeatable(tween(1800),RepeatMode.Reverse),label="twinkle")
 Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Night, Color(0xFF2B185A), Color(0xFF130A28))))) { Text("✦  ·   ✧      ✦    ·   ✧\n   ·  ✦    ☾       ✦   ·", color=Color.White.copy(alpha=alpha), fontSize=26.sp, modifier=Modifier.padding(24.dp).fillMaxWidth()); content() }
}
@Composable fun HeroHeader(onSettings:()->Unit){ Row(Modifier.fillMaxWidth().padding(20.dp),verticalAlignment=Alignment.CenterVertically){ Column(Modifier.weight(1f)){Text("Dreamy Bedtime Stories",fontSize=30.sp,fontWeight=FontWeight.ExtraBold);Text("Choose a magical adventure for tonight.",color=Color.White.copy(.75f))}; IconButton(onSettings){Icon(Icons.Rounded.Settings,"Settings")}} }
@Composable fun MoonHero(){ val t=rememberInfiniteTransition(label="float"); val y by t.animateFloat(0f,10f,infiniteRepeatable(tween(2400),RepeatMode.Reverse),label="y"); Card(Modifier.padding(horizontal=20.dp).fillMaxWidth().height(150.dp).offset(y=y.dp),shape=RoundedCornerShape(32.dp),colors=CardDefaults.cardColors(Color(0x33211B56))){ Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){ Text("☾ 👶📖 ✨",fontSize=70.sp); Text("A cozy storybook moment under a glowing moon",Modifier.align(Alignment.BottomCenter).padding(14.dp),color=Color.White.copy(.78f))}}}
@Composable fun CategoryCard(category:StoryCategory,onClick:()->Unit){ Card(Modifier.padding(8.dp).fillMaxWidth().height(210.dp).clickable(onClick=onClick),shape=RoundedCornerShape(28.dp),colors=CardDefaults.cardColors(Card), elevation=CardDefaults.cardElevation(8.dp)){ Column(Modifier.padding(18.dp).fillMaxSize(),verticalArrangement=Arrangement.SpaceBetween){ Text(category.emoji,fontSize=48.sp); Column{Text(category.title,fontWeight=FontWeight.Bold,fontSize=21.sp);Text("${category.count} stories",color=Glow,fontSize=13.sp);Text(category.description,color=Color.White.copy(.72f),fontSize=12.sp,maxLines=2)}; Icon(Icons.AutoMirrored.Rounded.ArrowForward,null,Modifier.align(Alignment.End)) } } }
@Composable fun StoryCard(story:Story,isFavorite:Boolean,onFavorite:()->Unit,onPlay:()->Unit){ Card(Modifier.padding(horizontal=16.dp,vertical=7.dp).fillMaxWidth().clickable(onClick=onPlay),shape=RoundedCornerShape(24.dp),colors=CardDefaults.cardColors(Card)){ Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){ Text(story.emoji,fontSize=42.sp,modifier=Modifier.size(58.dp)); Column(Modifier.weight(1f)){Text(story.title,fontWeight=FontWeight.Bold);Text("${story.durationMinutes} minutes • ${story.narrator}",color=Color.White.copy(.7f),fontSize=13.sp)}; IconButton(onFavorite){Icon(if(isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,"Favorite",tint=Glow)}; IconButton({}){Icon(Icons.Rounded.Download,"Download")}}}}
@Composable fun TimerChips(selected:Int?,onSelect:(Int?)->Unit){ val options=listOf(15,30,60,120); Row(Modifier.horizontalScroll(rememberScrollState()).padding(16.dp)){ options.forEach{ AssistChip(onClick={onSelect(it)}, label={Text("$it min")}, modifier=Modifier.padding(end=8.dp))}; AssistChip(onClick={onSelect(null)}, label={Text("End of story")}) } }
