package com.example.mobile_a1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobile_a1.R
import com.example.mobile_a1.database.entities.Product

// Composable function that represents a single line item in an order.
@Composable
fun OrderLineItem(
    product: Product,
    quantityState: MutableState<Long>,
    onRemoveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(product.name, modifier = Modifier.weight(1f))
        QuantityInput(quantityState = quantityState)
        IconButton(onClick = onRemoveClick) {
            Icon(Icons.Default.Delete, stringResource(R.string.delete))
        }
    }
}