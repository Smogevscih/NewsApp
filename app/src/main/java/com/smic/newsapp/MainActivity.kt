package com.smic.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smic.newsapp.ui.theme.GreyNews

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}

@Composable
fun SearchView(viewModel: NewsViewModel) {
    val state = viewModel.state
    var isCorrectQuest by rememberSaveable {
        mutableStateOf(true)
    }
    Column() {
        TextField(
            value = state.value,
            onValueChange = { value ->
                state.value = value
                isCorrectQuest = true
            },
            placeholder = { Text(text = "Введите тему для просмотра новостей") },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = if (isCorrectQuest) Color.Black else Color.Red,
                fontSize = 18.sp
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(15.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (state.value != TextFieldValue("")) {
                    IconButton(
                        onClick = {
                            state.value =
                                TextFieldValue("")
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(15.dp)
                                .size(24.dp)
                        )
                    }
                }
            },
            singleLine = true,
            maxLines = 1,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                leadingIconColor = Color.Black,
                trailingIconColor = Color.Black,
                backgroundColor = GreyNews,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        Button(
            onClick = {
                isCorrectQuest =
                    state.value.annotatedString.text.length in (3..50)
                if (isCorrectQuest) viewModel.loadNews(state.value.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 4.dp),
            border = BorderStroke(1.dp, Color.Gray),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text(text = "Запрос новостей".uppercase(), color = Color.DarkGray)

        }
    }
}


@Preview(showBackground = true)
@Composable
fun Ui() {
    val vm: NewsViewModel = viewModel()
    SearchView(vm)

}