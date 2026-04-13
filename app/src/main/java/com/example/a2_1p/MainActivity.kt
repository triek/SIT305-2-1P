package com.example.a2_1p

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.a2_1p.ui.theme._21PTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _21PTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CurrencySelectorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun convertCurrency(from: String, to: String, amount: Double): Double {
    val usdAmount = when (from) {
        "USD" -> amount
        "AUD" -> amount / 1.55
        "EUR" -> amount / 0.92
        "JPY" -> amount / 148.50
        else -> amount
    }

    return when (to) {
        "USD" -> usdAmount
        "AUD" -> usdAmount * 1.55
        "EUR" -> usdAmount * 0.92
        "JPY" -> usdAmount * 148.50
        else -> usdAmount
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectorScreen(modifier: Modifier = Modifier) {
    val currencies = listOf("AUD", "USD", "EUR", "JPY")
    var sourceExpanded by remember { mutableStateOf(false) }
    var targetExpanded by remember { mutableStateOf(false) }
    var selectedCurrency by remember { mutableStateOf(currencies[0]) }
    var targetCurrency by remember { mutableStateOf(currencies[1]) }
    var inputAmount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Select source currency")

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = sourceExpanded,
            onExpandedChange = { sourceExpanded = !sourceExpanded }
        ) {
            TextField(
                value = selectedCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text("Source Currency") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sourceExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = sourceExpanded,
                onDismissRequest = { sourceExpanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            selectedCurrency = currency
                            sourceExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select target currency")

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = targetExpanded,
            onExpandedChange = { targetExpanded = !targetExpanded }
        ) {
            TextField(
                value = targetCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text("Target Currency") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = targetExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = targetExpanded,
                onDismissRequest = { targetExpanded = false }
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            targetCurrency = currency
                            targetExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "From: $selectedCurrency  To: $targetCurrency")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputAmount,
            onValueChange = { inputAmount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = inputAmount.toDoubleOrNull()
                if (amount != null) {
                    val converted = convertCurrency(selectedCurrency, targetCurrency, amount)
                    result = converted.toString()
                } else {
                    result = "Please enter a valid number"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convert")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Result: $result")
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectorPreview() {
    _21PTheme {
        CurrencySelectorScreen()
    }
}