package com.example.a2_1p

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a2_1p.ui.theme._21PTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _21PTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConversionCategoriesScreen(
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

fun convertFuel(from: String, to: String, value: Double): Double {
    return when {
        from == "mpg" && to == "km/L" -> value * 0.425
        from == "km/L" && to == "mpg" -> value / 0.425
        from == "gallon" && to == "liter" -> value * 3.785
        from == "liter" && to == "gallon" -> value / 3.785
        else -> value
    }
}

fun convertTemperature(from: String, to: String, value: Double): Double {
    val celsius = when (from) {
        "Celsius" -> value
        "Fahrenheit" -> (value - 32) / 1.8
        "Kelvin" -> value - 273.15
        else -> value
    }

    return when (to) {
        "Celsius" -> celsius
        "Fahrenheit" -> (celsius * 1.8) + 32
        "Kelvin" -> celsius + 273.15
        else -> celsius
    }
}

@Composable
fun ConversionCategoriesScreen(modifier: Modifier = Modifier) {
    val categories = listOf("Currency", "Fuel", "Temperature")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            categories.forEachIndexed { index, category ->
                SegmentedButton(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = categories.size
                    )
                ) {
                    Text(category)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedCategory) {
            "Currency" -> CurrencySelectorScreen()
            "Fuel" -> FuelSelectorScreen()
            "Temperature" -> TemperatureSelectorScreen()
        }
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
        modifier = modifier.fillMaxSize()) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelSelectorScreen(modifier: Modifier = Modifier) {
    val fuelUnits = listOf("mpg", "km/L", "gallon", "liter")
    var sourceExpanded by remember { mutableStateOf(false) }
    var targetExpanded by remember { mutableStateOf(false) }
    var sourceUnit by remember { mutableStateOf(fuelUnits[0]) }
    var targetUnit by remember { mutableStateOf(fuelUnits[1]) }
    var inputAmount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    SelectorContent(
        modifier = modifier,
        sourceLabel = "Source Unit",
        targetLabel = "Target Unit",
        sourceValue = sourceUnit,
        targetValue = targetUnit,
        sourceExpanded = sourceExpanded,
        targetExpanded = targetExpanded,
        onSourceExpandedChange = { sourceExpanded = !sourceExpanded },
        onTargetExpandedChange = { targetExpanded = !targetExpanded },
        onSourceDismiss = { sourceExpanded = false },
        onTargetDismiss = { targetExpanded = false },
        inputAmount = inputAmount,
        result = result,
        amountLabel = "Fuel Value",
        units = fuelUnits,
        onSourceSelect = {
            sourceUnit = it
            sourceExpanded = false
        },
        onTargetSelect = {
            targetUnit = it
            targetExpanded = false
        },
        onAmountChange = { inputAmount = it },
        onConvert = {
            val amount = inputAmount.toDoubleOrNull()
            if (amount == null) {
                result = "Invalid number"
            } else if (amount < 0) {
                result = "Fuel cannot be negative"
            } else if (sourceUnit == targetUnit) {
                result = amount.toString()
            } else if (
                (sourceUnit == "mpg" && targetUnit == "km/L") ||
                (sourceUnit == "km/L" && targetUnit == "mpg") ||
                (sourceUnit == "gallon" && targetUnit == "liter") ||
                (sourceUnit == "liter" && targetUnit == "gallon")
            ) {
                result = convertFuel(sourceUnit, targetUnit, amount).toString()
            } else {
                result = "Invalid fuel conversion"
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemperatureSelectorScreen(modifier: Modifier = Modifier) {
    val temperatureUnits = listOf("Celsius", "Fahrenheit", "Kelvin")
    var sourceExpanded by remember { mutableStateOf(false) }
    var targetExpanded by remember { mutableStateOf(false) }
    var sourceUnit by remember { mutableStateOf(temperatureUnits[0]) }
    var targetUnit by remember { mutableStateOf(temperatureUnits[1]) }
    var inputAmount by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    SelectorContent(
        modifier = modifier,
        sourceLabel = "Source Unit",
        targetLabel = "Target Unit",
        sourceValue = sourceUnit,
        targetValue = targetUnit,
        sourceExpanded = sourceExpanded,
        targetExpanded = targetExpanded,
        onSourceExpandedChange = { sourceExpanded = !sourceExpanded },
        onTargetExpandedChange = { targetExpanded = !targetExpanded },
        onSourceDismiss = { sourceExpanded = false },
        onTargetDismiss = { targetExpanded = false },
        inputAmount = inputAmount,
        result = result,
        amountLabel = "Temperature",
        units = temperatureUnits,
        onSourceSelect = {
            sourceUnit = it
            sourceExpanded = false
        },
        onTargetSelect = {
            targetUnit = it
            targetExpanded = false
        },
        onAmountChange = { inputAmount = it },
        onConvert = {
            val amount = inputAmount.toDoubleOrNull()
            result = if (amount != null) {
                convertTemperature(sourceUnit, targetUnit, amount).toString()
            } else {
                "Please enter a valid number"
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorContent(
    modifier: Modifier = Modifier,
    sourceLabel: String,
    targetLabel: String,
    sourceValue: String,
    targetValue: String,
    sourceExpanded: Boolean,
    targetExpanded: Boolean,
    onSourceExpandedChange: () -> Unit,
    onTargetExpandedChange: () -> Unit,
    onSourceDismiss: () -> Unit,
    onTargetDismiss: () -> Unit,
    inputAmount: String,
    result: String,
    amountLabel: String,
    units: List<String>,
    onSourceSelect: (String) -> Unit,
    onTargetSelect: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onConvert: () -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = "Select source")

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = sourceExpanded,
            onExpandedChange = { onSourceExpandedChange() }
        ) {
            TextField(
                value = sourceValue,
                onValueChange = {},
                readOnly = true,
                label = { Text(sourceLabel) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sourceExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = sourceExpanded,
                onDismissRequest = onSourceDismiss
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = { onSourceSelect(unit) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Select target")

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = targetExpanded,
            onExpandedChange = { onTargetExpandedChange() }
        ) {
            TextField(
                value = targetValue,
                onValueChange = {},
                readOnly = true,
                label = { Text(targetLabel) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = targetExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = targetExpanded,
                onDismissRequest = onTargetDismiss
            ) {
                units.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit) },
                        onClick = { onTargetSelect(unit) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "From: $sourceValue  To: $targetValue")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputAmount,
            onValueChange = onAmountChange,
            label = { Text(amountLabel) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onConvert,
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
        ConversionCategoriesScreen()
    }
}