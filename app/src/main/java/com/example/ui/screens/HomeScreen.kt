package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPink

val recentMocks = listOf("Retro Drive", "Midnight City", "Nightcall")
val forYouMocks = listOf("Mix Diário 1", "Descobertas", "Flashback", "Energia Alta")
val topsMocks = listOf(
    Pair("Neon Nights", "Synthetic Dreams"),
    Pair("Analog Heart", "Vapor Theory"),
    Pair("Cyber City", "Data Stream"),
    Pair("Neon Odyssey", "Aurora Blade")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onNavigateToPlayer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.GraphicEq, contentDescription = "Logotipo", tint = NeonPink, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SoundWave", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notificações", tint = MaterialTheme.colorScheme.onBackground)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Configurações", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text("Boa noite, Lucas", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                    Text("Pronto para a sua Neon Odyssey?", fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                }
            }

            item { RecentSection(onNavigateToPlayer) }
            item { ForYouSection(onNavigateToPlayer) }
            item { TopBrasilSection(onNavigateToPlayer) }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun RecentSection(onNavigateToPlayer: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Tocadas recentemente", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Ver tudo", fontSize = 14.sp, color = NeonPink, modifier = Modifier.clickable(role = Role.Button) {})
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recentMocks) { item ->
                Column(
                    modifier = Modifier.width(140.dp).clickable(role = Role.Button) { onNavigateToPlayer() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.DarkGray)
                    ) {
                        AsyncImage(
                            model = "https://picsum.photos/seed/$item/200",
                            contentDescription = item,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = item, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                    Text(text = "Synthwave", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                }
            }
        }
    }
}

@Composable
fun ForYouSection(onNavigateToPlayer: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text("Feito para você", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        
        // Emulating a 2x2 Grid via 2 Rows of 2 elements each
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ForYouCard(forYouMocks[0], Color(0xFF0052CC), Modifier.weight(1f), onNavigateToPlayer)
                ForYouCard(forYouMocks[1], Color(0xFFFF007F), Modifier.weight(1f), onNavigateToPlayer)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ForYouCard(forYouMocks[2], Color(0xFF9D00FF), Modifier.weight(1f), onNavigateToPlayer)
                ForYouCard(forYouMocks[3], Color(0xFFFF5722), Modifier.weight(1f), onNavigateToPlayer)
            }
        }
    }
}

@Composable
fun ForYouCard(title: String, color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .clickable(role = Role.Button) { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun TopBrasilSection(onNavigateToPlayer: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text("Top Brasil", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
        
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            topsMocks.forEachIndexed { index, track ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button) { onNavigateToPlayer() }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), modifier = Modifier.width(30.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray)
                    ) {
                        AsyncImage(
                            model = "https://picsum.photos/seed/${track.first}/100",
                            contentDescription = track.first,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(track.first, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Text(track.second, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Mais opções", tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                    }
                }
            }
        }
    }
}

