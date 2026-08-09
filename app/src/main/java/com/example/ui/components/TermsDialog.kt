package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue

@Composable
fun TermsDialog(
    onDismissRequest: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Termos de Uso e Política de Privacidade") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "Última atualização: 24 de Outubro de 2023",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Bem-vindo ao SoundWave. Ao utilizar nosso aplicativo, você concorda com estes termos. Este documento foi elaborado para ser transparente e direto, em total conformidade com a Lei Geral de Proteção de Dados (LGPD - Lei nº 13.709/2018).")
                Spacer(modifier = Modifier.height(16.dp))
                Text("Coleta de Dados", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Coletamos dados pessoais fornecidos diretamente por você... e informações de arquivos locais, caso sincronizados, apenas para a função de reprodução local.")
            }
        },
        confirmButton = {
            Button(
                onClick = onAccept,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Aceitar e Continuar")
            }
        }
    )
}
