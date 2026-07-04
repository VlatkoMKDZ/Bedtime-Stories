package com.dreamykids.bedtimestories.repository

import com.dreamykids.bedtimestories.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoryRepository {
    val categories = listOf(
        StoryCategory("dragons","🐉","Dragons",4,"Gentle dragon adventures for brave dreamers"),
        StoryCategory("princesses","👸","Princesses",5,"Kind princesses, castles, and moonlit wishes"),
        StoryCategory("fairy","🏰","Fairy Tales",6,"Classic-feeling tales with cozy endings"),
        StoryCategory("unicorns","🦄","Unicorns",4,"Sparkly forest friends and pastel dreams"),
        StoryCategory("dinosaurs","🦖","Dinosaurs",4,"Soft prehistoric journeys for sleepy explorers"),
        StoryCategory("space","🚀","Space Adventures",5,"Starlit rockets, planets, and bedtime wonder"),
        StoryCategory("animals","🐻","Animal Adventures",6,"Forest friends learning kindness and courage"),
        StoryCategory("magic","🧙","Wizards & Magic",4,"Warm spells, wise wizards, and glowing lanterns"),
        StoryCategory("pirates","🏴","Pirates",4,"Cozy treasure hunts across calm seas"),
        StoryCategory("knights","⚔","Brave Knights",4,"Gentle quests with courage and friendship"),
        StoryCategory("ocean","🌊","Ocean Adventures",5,"Whales, mermaids, and calming waves")
    )
    val stories = categories.flatMap { c -> listOf(
        Story("${c.id}-1",c.id,c.emoji,"The Brave Little ${c.title.dropLast(if(c.title.endsWith("s"))1 else 0)}","Luna Willow",8,listOf(c.title,"calm","sleep")),
        Story("${c.id}-2",c.id,c.emoji,"${c.title} Secret Cave","Oliver Moon",12,listOf("adventure",c.title)),
        Story("${c.id}-3",c.id,c.emoji,"The Golden Bedtime Wish","Mia Starling",9,listOf("magic","cozy",c.title)),
        Story("${c.id}-4",c.id,c.emoji,"The Sleepy Moonlight Journey","Luna Willow",15,listOf("moon","stars",c.title))
    ) }
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites
    fun toggleFavorite(id:String){ _favorites.value = if(id in _favorites.value) _favorites.value-id else _favorites.value+id }
    fun search(query:String)= if(query.isBlank()) stories else stories.filter { s -> (listOf(s.title,s.narrator,s.categoryId)+s.keywords).any{ it.contains(query,true)} }
}
