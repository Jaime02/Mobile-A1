package com.example.mobile_a1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile_a1.ui.theme.MobileA1Theme
import com.example.mobile_a1.ui.viewmodels.OrderListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileA1Theme {
                val viewModel: OrderListViewModel = hiltViewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { Text("Lista de la compra") },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { /* Navegar a la pantalla de añadir */ }) {
                            Icon(Icons.Default.Add, contentDescription = "Añadir")
                        }
                    }) { innerPadding -> ShoppingOrderList(Modifier.padding(innerPadding), viewModel) }
            }
        }
    }
}

@Composable
fun ShoppingOrderList(modifier: Modifier = Modifier, viewModel: OrderListViewModel) {
    val items by viewModel.items.collectAsState()

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(items) { item ->
            Row(modifier = Modifier.padding(8.dp)) {
                Text(item.productName, modifier = Modifier.weight(1f))
                Text("x${item.quantity}")
            }
        }
    }
}
