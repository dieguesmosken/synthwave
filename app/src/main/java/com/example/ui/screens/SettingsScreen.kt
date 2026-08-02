package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(darkTheme: Boolean, onThemeChanged: (Boolean) -> Unit, onBack: () -> Unit) {
    var showLGPD by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Manage your account and app preferences.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            SettingsSectionHeader("ACCOUNT")
            SettingsCard {
                SettingsListItem(icon = Icons.Outlined.Person, title = "Profile settings")
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(icon = Icons.Outlined.Email, title = "Email", subtitle = "user@example.com")
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(icon = Icons.Outlined.Lock, title = "Password")
            }

            SettingsSectionHeader("PLAYBACK")
            SettingsCard {
                SettingsListItem(icon = Icons.Outlined.HighQuality, title = "Audio quality", subtitle = "High", showArrow = false)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchItem(icon = Icons.Outlined.GraphicEq, title = "Crossfade", subtitle = "3s", checked = true, onCheckedChange = {})
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchItem(icon = Icons.Outlined.VolumeUp, title = "Normalize volume", subtitle = "Set the same volume level for all tracks", checked = false, onCheckedChange = {})
            }

            SettingsSectionHeader("PRIVACY & LGPD")
            SettingsCard {
                SettingsListItem(icon = Icons.Outlined.Dataset, title = "Gerenciar Dados")
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(icon = Icons.Outlined.AdUnits, title = "Preferências de Anúncios")
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(icon = Icons.Outlined.Visibility, title = "Privacidade da Atividade")
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(icon = Icons.Outlined.Gavel, title = "Termos de Uso e EULA", onClick = { showLGPD = true })
            }

            SettingsSectionHeader("NOTIFICATIONS")
            SettingsCard {
                SettingsSwitchItem(icon = Icons.Outlined.Notifications, title = "Push notifications", checked = true, onCheckedChange = {})
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchItem(icon = Icons.Outlined.Mail, title = "Email alerts", checked = false, onCheckedChange = {})
            }

            SettingsSectionHeader("ABOUT")
            SettingsCard {
                SettingsListItem(title = "Version", subtitle = "4.12.0 (Build 982)", showArrow = false, icon = null)
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
                SettingsListItem(title = "Legal Info", icon = null)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonPink)
            ) {
                Text("Log out", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        if (showLGPD) {
            AlertDialog(
                onDismissRequest = { showLGPD = false },
                title = { Text("Termos de Uso e Política de Privacidade") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text("Última atualização: 24 de Outubro de 2023", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Bem-vindo ao SoundWave. Ao utilizar nosso aplicativo, você concorda com estes termos. Este documento foi elaborado para ser transparente e direto, em total conformidade com a Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018).")
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Coleta de Dados", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Coletamos dados pessoais fornecidos diretamente por você... e informações de arquivos locais, caso sincronizados, apenas para a função de reprodução local.")
                    }
                },
                confirmButton = {
                    Button(onClick = { showLGPD = false }, colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)) {
                        Text("Aceitar e Continuar")
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ElectricBlue,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun SettingsListItem(
    icon: ImageVector?,
    title: String,
    subtitle: String? = null,
    showArrow: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.width(16.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        if (subtitle != null) {
            Text(subtitle, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(modifier = Modifier.width(8.dp))
        }
        if (showArrow) {
            Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            if (subtitle != null) {
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = null,
            colors = SwitchDefaults.colors(checkedTrackColor = ElectricBlue)
        )
    }
}

