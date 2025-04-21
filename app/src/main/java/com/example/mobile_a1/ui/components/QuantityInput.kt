package com.example.mobile_a1.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mobile_a1.R

@Composable
fun QuantityInput(quantityState: MutableState<Long>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentWidth()
    ) {
        // Minus Button
        IconButton(
            onClick = {
                if (quantityState.value > 1) {
                    quantityState.value--
                }
            },
            modifier = Modifier.border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                CircleShape
            ),
            colors = IconButtonDefaults.outlinedIconButtonColors()
        ) {
            Icon(Icons.Filled.Remove, contentDescription = stringResource(R.string.decrease_quantity))
        }

        OutlinedTextField(
            value = quantityState.value.toString(),
            onValueChange = { newValue ->
                val filteredValue = newValue.filter { it.isDigit() }
                quantityState.value = filteredValue.toLongOrNull() ?: 0L
            },
            label = { Text(stringResource(R.string.quantity)) },
            modifier = Modifier
                .width(100.dp)
                .padding(horizontal = 4.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        IconButton(
            onClick = {
                quantityState.value++
            },
            modifier = Modifier.border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            CircleShape
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors()
        ) {
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.increase_quantity))
        }
    }
}
