package com.movtery.zalithlauncher.ui.screens.content

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun EnderPersonalizationScreen(
    backgroundUri: Uri?,
    backgroundDim: Float,
    onChooseBackground: () -> Unit,
    onResetBackground: () -> Unit,
    onDimChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(22.dp),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(
            modifier = Modifier.weight(0.58f).fillMaxHeight()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xEE171020))
                .border(BorderStroke(1.dp, Color(0xFF62417A)), RoundedCornerShape(18.dp))
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("PERSONALIZAÇÃO", color = Color(0xFFE5C7FF), fontSize = 18.sp, fontWeight = FontWeight.Black, letterSpacing = 1.5.sp)
            Text("Deixe o Ender Launcher com a sua identidade.", color = Color(0xFFB9A7C8), fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text("FUNDO DO LAUNCHER", color = Color(0xFFCDA7EC), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text(
                if (backgroundUri == null) "Tema original: dimensão do End" else "Imagem personalizada selecionada",
                color = Color.White, fontSize = 12.sp
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PersonalizationAction("ESCOLHER DA GALERIA", true, onChooseBackground, Modifier.weight(1f))
                PersonalizationAction("RESTAURAR", false, onResetBackground, Modifier.weight(0.7f))
            }
            Spacer(Modifier.height(6.dp))
            Text("ESCURO DO FUNDO", color = Color(0xFFCDA7EC), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Text("Ajuste para deixar os textos mais fáceis de ler.", color = Color(0xFFB9A7C8), fontSize = 11.sp)
            Slider(value = backgroundDim, onValueChange = onDimChanged, valueRange = 0.25f..0.85f)
            Text((backgroundDim * 100).toInt().toString() + "% de escurecimento", color = Color(0xFFD7C5E5), fontSize = 10.sp)
            Spacer(Modifier.weight(1f))
            Text("As alterações são salvas automaticamente neste aparelho.", color = Color(0xFF9583A5), fontSize = 10.sp)
        }

        Column(
            modifier = Modifier.weight(0.42f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("PRÉ-VISUALIZAÇÃO", color = Color(0xFFCDA7EC), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF21152E))
                    .border(BorderStroke(1.dp, Color(0xFF62417A)), RoundedCornerShape(18.dp))
            ) {
                if (backgroundUri != null) {
                    AsyncImage(
                        model = backgroundUri,
                        contentDescription = "Prévia do fundo escolhido",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    EnderDimensionBackdrop(modifier = Modifier.fillMaxSize())
                }
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = backgroundDim)))
                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("ENDER", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                    Text("JAVA MOBILE • THE END", color = Color(0xFFE5C7FF), fontSize = 9.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun PersonalizationAction(
    label: String,
    primary: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(42.dp).clip(RoundedCornerShape(10.dp))
            .background(if (primary) Color(0xFF713A98) else Color(0xFF2A1B36))
            .border(BorderStroke(1.dp, if (primary) Color(0xFFC58AFF) else Color(0xFF62417A)), RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
