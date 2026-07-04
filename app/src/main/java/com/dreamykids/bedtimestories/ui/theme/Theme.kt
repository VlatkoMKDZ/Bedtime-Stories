package com.dreamykids.bedtimestories.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val DreamPurple=Color(0xFF8E6CFF); val Night=Color(0xFF120B2D); val Card=Color(0xFF21164A); val Glow=Color(0xFFFFC6E7)
@Composable fun DreamyTheme(content:@Composable()->Unit){ MaterialTheme(colorScheme=darkColorScheme(primary=DreamPurple,secondary=Glow,background=Night,surface=Card,onSurface=Color.White), typography=Typography(), content=content) }
