package com.example.mobile_a1.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mobile_a1.R
import com.example.mobile_a1.database.entities.Product

@Composable
fun OrderLineList(
    orderLines: SnapshotStateList<Pair<Product, MutableState<Long>>>,
    onRemoveProduct: (Pair<Product, MutableState<Long>>) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(stringResource(R.string.products), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (orderLines.isEmpty()) {
            Text(stringResource(R.string.no_products))
        } else {
            LazyColumn {
                items(orderLines, key = { it.first.id }) { orderLinePair ->
                    OrderLineItem(
                        product = orderLinePair.first,
                        quantityState = orderLinePair.second,
                        onRemoveClick = { onRemoveProduct(orderLinePair) }
                    )
                }
            }
        }
    }
}