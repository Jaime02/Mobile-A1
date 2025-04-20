package com.example.mobile_a1.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile_a1.R
import com.example.mobile_a1.database.dao.OrderLineWithProduct
import com.example.mobile_a1.database.entities.OrderLine
import com.example.mobile_a1.database.entities.Product
import com.example.mobile_a1.ui.theme.MobileA1Theme
import com.example.mobile_a1.ui.viewmodels.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderId = intent.getLongExtra("orderId", -1L)
        if (orderId == -1L) {
            // Handle error: No order ID provided
            finish()
            return
        }

        setContent {
            MobileA1Theme {
                val viewModel: OrderViewModel = hiltViewModel()
                viewModel.loadOrderData(orderId)
                OrderScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun OrderScreen(viewModel: OrderViewModel) {
    val orderLinesWithProducts by viewModel.orderLinesWithProducts.collectAsState(initial = emptyList())
    val supermarket by viewModel.supermarket.collectAsState(initial = null)

    Scaffold(
        topBar = {
            Text(
                stringResource(R.string.pedido_en_supermercado, supermarket?.name ?: ""),
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(
                    orderLinesWithProducts,
                    key = { item: OrderLineWithProduct -> item.orderLine.id }) { orderLineWithProduct ->
                    OrderLineItem(
                        orderLine = orderLineWithProduct.orderLine,
                        product = orderLineWithProduct.product,
                        onCheckedChange = { isChecked ->
                            viewModel.updateOrderLineCompletion(
                                orderLineWithProduct.orderLine.id,
                                isChecked
                            )
                        })
                }
            }
        }
    )
}

@Composable
fun OrderLineItem(orderLine: OrderLine, product: Product, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = orderLine.completed,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = stringResource(R.string.quantity_product, orderLine.quantity, product.name),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}