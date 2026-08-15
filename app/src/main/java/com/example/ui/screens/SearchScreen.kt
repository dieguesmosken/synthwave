package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonPink

val categories = listOf("Synthwave", "Pop", "Eletrônica", "Rock", "Hip Hop", "K-Pop", "Jazz", "Funk")
val colorList = listOf(Color(0xFF0052CC), Color(0xFFFF007F), Color(0xFF9D00FF), Color(0xFFD32F2F), Color(0xFF4DD0E1), Color(0xFFFF9800), Color(0xFF8BC34A), Color(0xFFE91E63))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Artistas, músicas ou podcasts", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Pesquisar", tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Color.White.copy(alpha = 0.1f)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = NeonPink
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("Gêneros e Mood", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories.zip(colorList)) { (cat, col) ->
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(col)
                            .clickable(role = Role.Button) { }
                            .semantics(mergeDescendants = true) {}
                            .padding(16.dp)
                    ) {
                        Text(
                            text = cat,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

