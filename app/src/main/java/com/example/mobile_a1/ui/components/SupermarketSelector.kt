package com.example.mobile_a1.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.mobile_a1.R
import com.example.mobile_a1.database.entities.Supermarket

// Composable function that displays a dropdown menu for selecting a supermarket.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupermarketSelector(
    supermarkets: List<Supermarket>,
    selectedSupermarketId: Long?,
    onSupermarketSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedSupermarketName = supermarkets.find { it.id == selectedSupermarketId }?.name
        ?: stringResource(R.string.select_supermarket)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = selectedSupermarketName,
            onValueChange = {},
            label = { Text(stringResource(R.string.supermarket)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            supermarkets.forEach { supermarket ->
                DropdownMenuItem(
                    text = { Text(supermarket.name) },
                    onClick = {
                        onSupermarketSelected(supermarket.id)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}