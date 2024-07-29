package com.dhrutikambar.agecalculator

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhrutikambar.agecalculator.ui.theme.AgeCalculatorTheme
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgeCalculatorTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text(text = "Age Calculator") }) },
                    modifier = Modifier.fillMaxSize(), content = {
                        MainScreen(modifier = Modifier.padding(it))
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val startDate = remember {
        mutableStateOf(TextFieldValue("Date of Birth"))
    }
    val endDate = remember {
        mutableStateOf(TextFieldValue("Current or Another Date"))
    }

    val datePickerIndex = remember {
        mutableIntStateOf(-1)
    }

    val yearsCalculated = remember {
        mutableStateOf("")
    }

    val monthsCalculated = remember {
        mutableStateOf("")
    }

    val daysCalculated = remember {
        mutableStateOf("")
    }

    val showCalculatedAge = remember {
        mutableStateOf(false)
    }


    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, selectedDayOfMonth ->

            showCalculatedAge.value = false
            val formattedDay = String.format("%02d", selectedDayOfMonth)
            val formattedMonth = String.format("%02d", selectedMonth.plus(1))
            if (datePickerIndex.intValue == 0) {
                startDate.value = TextFieldValue("$formattedDay/$formattedMonth/$selectedYear")
            }

            if (datePickerIndex.intValue == 1) {
                endDate.value = TextFieldValue("$formattedDay/$formattedMonth/$selectedYear")
            }

        },
        year,
        month,
        day
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .absolutePadding(top = 100.dp, left = 11.dp, right = 11.dp)
        ) {


            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )


            /**   Show Calculated Age   **/
            if (showCalculatedAge.value) {
                if (yearsCalculated.value.isNotEmpty() && yearsCalculated.value.toInt() >= 0 && monthsCalculated.value.isNotEmpty() && monthsCalculated.value.toInt() >= 0 && daysCalculated.value.isNotEmpty() && daysCalculated.value.toInt() >= 0) {
                    Text(
                        text = "${yearsCalculated.value} years, ${monthsCalculated.value} months, ${daysCalculated.value} days",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = "Kindly specify dates properly!",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center, color = Color.Red
                    )
                }
            }



            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )



            TextField(
                value = startDate.value,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    if (startDate.value.text.contains("/")) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                datePickerIndex.intValue = 0
                                startDate.value = TextFieldValue("Date of Birth")
                                showCalculatedAge.value = false
                            })
                    } else {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                datePickerIndex.intValue = 0
                                datePickerDialog.show()
                            })
                    }

                },

                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            )


            TextField(
                value = endDate.value,
                onValueChange = {

                },
                readOnly = true,
                trailingIcon = {
                    if (endDate.value.text.contains("/")) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                datePickerIndex.intValue = 1
                                endDate.value = TextFieldValue("Current or Another Date")
                                showCalculatedAge.value = false
                            })
                    } else {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                datePickerIndex.intValue = 1
                                datePickerDialog.show()
                            })
                    }

                },

                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            Button(
                onClick = {

                    if (!startDate.value.text.contains("/")) {
                        Toast.makeText(context, "Kindly Select Date of Birth", Toast.LENGTH_LONG).show()
                    } else if (!endDate.value.text.contains("/")) {
                        Toast.makeText(context, "Kindly Select Current or Another Date", Toast.LENGTH_LONG).show()
                    } else {
                        calculateAge(
                            startDate,
                            endDate,
                            context,
                            yearsCalculated,
                            monthsCalculated,
                            daysCalculated
                        )

                        showCalculatedAge.value = true
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(text = "Calculate ", fontSize = 18.sp)
                }

            }


        }

    }


}

@RequiresApi(Build.VERSION_CODES.O)
fun calculateAge(
    startDate: MutableState<TextFieldValue>,
    endDate: MutableState<TextFieldValue>,
    context: Context,
    yearsCalculated: MutableState<String>,
    monthsCalculated: MutableState<String>,
    daysCalculated: MutableState<String>
) {
    try {

        Log.d("startDate", startDate.value.text)
        Log.d("endDate", endDate.value.text)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val birth = LocalDate.parse(startDate.value.text, formatter)
        val current = LocalDate.parse(endDate.value.text, formatter)

        val age = Period.between(birth, current)
        yearsCalculated.value = age.years.toString()
        monthsCalculated.value = age.months.toString()
        daysCalculated.value = age.days.toString()
        //  Toast.makeText(context, age.years.toString(), Toast.LENGTH_LONG).show()
    } catch (ex: Exception) {
        yearsCalculated.value = "0"
        monthsCalculated.value = "0"
        daysCalculated.value = "0"
        Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_LONG).show()

    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AgeCalculatorTheme {
        MainScreen(modifier = Modifier)
    }
}