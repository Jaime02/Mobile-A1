package com.example.mobile_a1.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile_a1.R
import com.example.mobile_a1.database.entities.Product
import com.example.mobile_a1.ui.components.AddProductDialog
import com.example.mobile_a1.ui.components.OrderLineList
import com.example.mobile_a1.ui.components.SupermarketSelector
import com.example.mobile_a1.ui.theme.MobileA1Theme
import com.example.mobile_a1.ui.viewmodels.CreateOrderActivityViewModel
import dagger.hilt.android.AndroidEntryPoint


// Activity that allows the user to create a new order by selecting a supermarket and adding
// products to the order.
@AndroidEntryPoint
class CreateOrderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileA1Theme {
                val viewModel: CreateOrderActivityViewModel = hiltViewModel()
                CreateOrderScreen(
                    viewModel = viewModel,
                    onOrderCreated = {
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen(
    viewModel: CreateOrderActivityViewModel,
    onOrderCreated: () -> Unit
) {
    val supermarkets by viewModel.supermarkets.collectAsState(initial = emptyList())
    val allProducts by viewModel.products.collectAsState(initial = emptyList())
    var selectedSupermarketId by remember { mutableStateOf<Long?>(null) }
    val orderLines = remember { mutableStateListOf<Pair<Product, MutableState<Long>>>() }
    var showAddProductDialog by remember { mutableStateOf(false) }

    // Products available to be added (not already in orderLines)
    val productsAvailableToAdd = remember(allProducts, orderLines) {
        val addedProductIds = orderLines.map { it.first.id }.toSet()
        allProducts.filterNot { it.id in addedProductIds }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.new_order)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddProductDialog = true }) {
                Icon(Icons.Default.Add, stringResource(R.string.anadir_producto))
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    selectedSupermarketId?.let { supermarketId ->
                        val orderLineEntities = orderLines.map { (product, quantityState) ->
                            product.id to quantityState.value.coerceAtLeast(1L)
                        }
                        viewModel.createOrder(supermarketId, orderLineEntities) {
                            onOrderCreated()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = selectedSupermarketId != null && orderLines.isNotEmpty()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SupermarketSelector(
                supermarkets = supermarkets,
                selectedSupermarketId = selectedSupermarketId,
                onSupermarketSelected = { id -> selectedSupermarketId = id }
            )
            OrderLineList(
                orderLines = orderLines,
                onRemoveProduct = { productToRemovePair ->
                    orderLines.remove(productToRemovePair)
                },
                modifier = Modifier.weight(1f)
            )

            if (showAddProductDialog) {
                AddProductDialog(
                    availableProducts = productsAvailableToAdd,
                    onDismissRequest = { showAddProductDialog = false },
                    onProductSelectedToAdd = { product ->
                        orderLines.add(product to mutableLongStateOf(1L))
                        showAddProductDialog = false
                    }
                )
            }
        }
    }
}
