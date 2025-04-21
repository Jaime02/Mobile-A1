package com.example.mobile_a1.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.mobile_a1.R
import com.example.mobile_a1.database.entities.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductDialog(
    availableProducts: List<Product>,
    onDismissRequest: () -> Unit,
    onProductSelectedToAdd: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedProductDropdown by remember { mutableStateOf(false) }
    var selectedProductCandidate by remember { mutableStateOf<Product?>(null) }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.anadir_producto)) },
        text = {
            if (availableProducts.isEmpty()) {
                Text(stringResource(R.string.no_available_products))
            } else {
                ExposedDropdownMenuBox(
                    expanded = expandedProductDropdown,
                    onExpandedChange = { expandedProductDropdown = !expandedProductDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedProductCandidate?.name ?: stringResource(R.string.select_product),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.product)) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProductDropdown)
                        },
                        modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedProductDropdown,
                        onDismissRequest = { expandedProductDropdown = false }
                    ) {
                        availableProducts.forEach { product ->
                            DropdownMenuItem(
                                text = { Text(product.name) },
                                onClick = {
                                    selectedProductCandidate = product
                                    expandedProductDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedProductCandidate?.let { productToAdd ->
                        onProductSelectedToAdd(productToAdd)
                        selectedProductCandidate = null
                    }
                },
                enabled = selectedProductCandidate != null && availableProducts.isNotEmpty()
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}