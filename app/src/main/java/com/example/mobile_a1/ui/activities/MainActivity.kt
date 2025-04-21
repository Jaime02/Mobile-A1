package com.example.mobile_a1.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
                val context = LocalContext.current
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            stringResource(R.string.compras_pendientes),
                            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(context, CreateOrderActivity::class.java)
                                context.startActivity(intent)
                            },
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

    if (groupedOrders.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_pending_orders),
                style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.tap_add_button_to_create_order),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            groupedOrders.forEach { (supermarketName, orders) ->
                item {
                    SupermarketOrdersItem(supermarketName = supermarketName, orders = orders)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun SupermarketOrdersItem(supermarketName: String, orders: List<OrderWithMetadata>) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "arrowRotation")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = supermarketName,
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                modifier = Modifier
                    .rotate(rotation)
                    .padding(start = 8.dp)
            )
        }

        if (isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 8.dp)) {
                if (orders.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_hay_pedidos),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else {
                    val sortedOrders = orders.sortedBy { it.order.orderDate }
                    sortedOrders.forEach { orderWithMetadata ->
                        OrderItemRow(orderWithMetadata = orderWithMetadata)
                    }
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
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = Converters.dateToUserString(orderWithMetadata.order.orderDate),
            modifier = Modifier.width(100.dp),
        )
        Text(
            text = productText,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
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
