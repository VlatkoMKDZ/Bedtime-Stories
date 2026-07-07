package com.dreamykids.bedtimestories.model

data class StoryCategory(val id:String,val emoji:String,val title:String,val count:Int,val description:String,val premium:Boolean=false)
data class Story(val id:String,val categoryId:String,val emoji:String,val title:String,val narrator:String,val durationMinutes:Int,val keywords:List<String>,val premium:Boolean=false)
data class PlayerState(val current:Story?=null,val isPlaying:Boolean=false,val progress:Float=0f,val speed:Float=1f,val sleepTimerMinutes:Int?=null)
data class SettingsState(val darkMode:Boolean=false,val playbackSpeed:Float=1f,val rememberLastStory:Boolean=true,val notifications:Boolean=false)
