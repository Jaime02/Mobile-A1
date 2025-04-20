package com.example.mobile_a1.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile_a1.R
import com.example.mobile_a1.database.Converters
import com.example.mobile_a1.database.dao.OrderWithMetadata
import com.example.mobile_a1.ui.theme.Blue
import com.example.mobile_a1.ui.theme.MobileA1Theme
import com.example.mobile_a1.ui.theme.SecondaryBlue
import com.example.mobile_a1.ui.viewmodels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileA1Theme {
                val viewModel: MainActivityViewModel = hiltViewModel()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            stringResource(R.string.compras_pendientes),
                            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { /* TODO: Navigate to add supermarket/order screen */ },
                            containerColor = Blue,
                            contentColor = SecondaryBlue
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = stringResource(R.string.anadir_compra)
                            )
                        }
                    }) { innerPadding ->
                    PendingSupermarketsList(
                        Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun PendingSupermarketsList(modifier: Modifier = Modifier, viewModel: MainActivityViewModel) {
    val groupedOrders by viewModel.pendingOrders.collectAsState(initial = emptyMap())
    LazyColumn(modifier = modifier.fillMaxSize()) {
        groupedOrders.forEach { (supermarketName, orders) ->
            item {
                SupermarketItem(supermarketName = supermarketName, orders = orders)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun SupermarketItem(supermarketName: String, orders: List<OrderWithMetadata>) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(4.dp)
    ) {
        Text(
            text = supermarketName,
            style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        if (isExpanded) {
            if (orders.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_hay_pedidos),
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            } else {
                // Sort the orders by date, from most recent to oldest
                val sortedOrders = orders.sortedBy { it.order.orderDate }
                sortedOrders.forEach { orderWithMetadata ->
                    OrderItemRow(orderWithMetadata = orderWithMetadata)
                }
            }
        }
    }
}

@Composable
fun OrderItemRow(orderWithMetadata: OrderWithMetadata) {
    val context = LocalContext.current
    val productText = context.resources.getQuantityString(
        R.plurals.product_count, orderWithMetadata.orderLineCount, orderWithMetadata.orderLineCount
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, OrderActivity::class.java)
                intent.putExtra("orderId", orderWithMetadata.order.id)
                context.startActivity(intent)
            }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = Converters.dateToUserString(orderWithMetadata.order.orderDate),
            modifier = Modifier.width(100.dp),
        )
        Text(
            text = productText,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                val intent = Intent(context, OrderActivity::class.java)
                intent.putExtra("orderId", orderWithMetadata.order.id)
                context.startActivity(intent)
            },
            modifier = Modifier
        ) {
            Text(text = stringResource(R.string.ver))
        }
    }
}