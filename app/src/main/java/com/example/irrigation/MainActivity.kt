package com.example.irrigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

data class Zone(
    val id: Int,
    val name: String,
    var moisture: Int,
    var enabled: Boolean,
    var durationMinutes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                IrrigationApp()
            }
        }
    }
}

@Composable
fun IrrigationApp() {
    val zones = remember {
        mutableStateListOf(
            Zone(1, "Front Lawn", moisture = 45, enabled = true, durationMinutes = 12),
            Zone(2, "Vegetable Patch", moisture = 31, enabled = true, durationMinutes = 18),
            Zone(3, "Greenhouse", moisture = 66, enabled = false, durationMinutes = 8)
        )
    }
    var pumpRunning by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Irrigation Controller") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (pumpRunning) "Pump: Running" else "Pump: Stopped",
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = { pumpRunning = !pumpRunning }) {
                    Text(if (pumpRunning) "Stop" else "Start")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Zones", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(zones, key = { it.id }) { zone ->
                    ZoneCard(
                        zone = zone,
                        onEnabledChanged = { zone.enabled = it },
                        onDurationChanged = { zone.durationMinutes = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoneCard(
    zone: Zone,
    onEnabledChanged: (Boolean) -> Unit,
    onDurationChanged: (Int) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(zone.name, style = MaterialTheme.typography.titleMedium)
                    Text("Soil moisture: ${zone.moisture}%")
                }
                Switch(checked = zone.enabled, onCheckedChange = onEnabledChanged)
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("Watering duration: ${zone.durationMinutes} min")
            Slider(
                value = zone.durationMinutes.toFloat(),
                onValueChange = { onDurationChanged(it.roundToInt()) },
                valueRange = 1f..60f
            )
        }
    }
}
