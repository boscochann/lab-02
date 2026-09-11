package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

// was having error of unresolved reference 'mutableStateListOf'
import androidx.compose.runtime.mutableStateListOf

// another error unresolved reference 'remember'
import androidx.compose.runtime.remember

// same issue with mutableStateOf
import androidx.compose.runtime.mutableStateOf

// same issue with by
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// same issue with Column and Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

// dp unresolved reference and outlinedtextfield
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField

// this is for spacer and width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width

// for button, lazycolumn, item and fillmaxwidth
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.clickable

// https://developer.android.com/develop/ui/compose/modifiers
// https://developer.android.com/develop/ui/compose/touch-input/pointer-input/tap-and-press
// Google, "unresolved reference mutableStateListOf"

class MainActivity : ComponentActivity() { // MainActivity inherits ComponentActivity
    override fun onCreate(savedInstanceState: Bundle?) { // override function onCreate
        super.onCreate(savedInstanceState) // take the ComponentActivity version and add on
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent { // kind of thought of setContent like a main function
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities, // list of cities
                        onAddCity = { cityRepository.addCity(it) }, // used to pass in new city
                        onDeleteCity = { cityRepository.deleteCity(it)},
                        modifier = Modifier.padding(paddingValues = innerPadding)

                    )
                }
            }
        }
    }
}

class CityRepository {
    // _cities is the storage
    // private, only this class can edit it
    // outside code can access it but cannot make changes to the list
    // Note: mutableStateListOf vs mutableListOf, notifies us of changes
    private val _cities = mutableStateListOf(
        "Edmonton", "Vancouver", "Moscow",
        "Sydney", "Berlin", "Vienna",
        "Tokyo", "Beijing", "Osaka",
        "New Delhi"
    )

    val cities: List<String> // declares cities with type List String
        get() = _cities // go get what is in _cities

    fun addCity(city: String)
    {
        _cities.add(city)
    }

    fun deleteCity(city: String) // similar to above function, just for removing a city
    {
        _cities.remove(city)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
// multiple possible implementations to delete city
// 1. Create rectangle/box around each city name, click the box and delete city
// 2. Type in city name, click delete city
// 3. Create a delete button beside the name of each city
// this function takes a list of cities, the function onAddCity, and a modifier
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // this variable is the in-progress typed text of the new city
    var newCityName by remember {mutableStateOf(value = "") }
    var selectedCity by remember {mutableStateOf(value = "")}

    // we have a column where we first have a row at the top
    // this row is where we type in the new city
    // below it is a LazyColumn with the list of cities
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }

            ) {
                Text("Add City")
            }
            Button(
                onClick = {
                    if (selectedCity.isNotBlank()) {
                        onDeleteCity(selectedCity)
                    }
                }
            ) {
                Text("Remove City")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(city = city, ClickedCity = { selectedCity = city })

            }
        }
    }
}

@Composable
// these are the specifics of how each city is displayed
fun CityRow(city: String, ClickedCity: () -> Unit) {
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .clickable // each row has its own clickable
            // so when we click on that row, it runs ClickedCity
            // in the lazycolumn this actually sets the selectedCity to the city we clicked on
            // each clickable is unique to a city in the list
            {
                ClickedCity()
            }

    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ListyCityTheme {
        Greeting("Android")
    }
}