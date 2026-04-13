Travel Companion App – SIT305 Task 2.1

Overview
This Android app helps international travellers convert essential values quickly.
It includes three main features: currency, fuel, and temperature conversion.
Built using Kotlin and Jetpack Compose.

---

Features

Currency Conversion

* Supports AUD, USD, EUR, JPY
* Uses fixed rates from the task
* Handles invalid input and same currency

Fuel Conversion

* mpg ↔ km/L
* gallon ↔ liter
* Blocks invalid combinations
* Prevents negative values

Temperature Conversion

* Celsius ↔ Fahrenheit
* Celsius ↔ Kelvin
* Handles invalid input

---

Technical Details

* Language: Kotlin
* UI: Jetpack Compose (Material 3)
* Structure:

    * Separate conversion functions
    * Reusable UI component (SelectorContent)
    * Category switching with segmented buttons

---

Validation

* Handles non-numeric input
* Returns same value for same unit
* Prevents invalid fuel conversions
* Displays error messages


