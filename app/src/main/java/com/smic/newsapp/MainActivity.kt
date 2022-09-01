package com.smic.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.smic.newsapp.model.entities.Articles
import com.smic.newsapp.ui.theme.GreyNews
import com.smic.newsapp.ui.theme.Shapes
import com.smic.newsapp.ui.theme.Typography

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController: NavHostController = rememberNavController()
            Scaffold {
                NavigationComponent(navController)
            }
        }
    }
}

@Composable
fun NavigationComponent(navController: NavHostController) {
    val viewModel: NewsViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.HOME
    ) {

        composable(NavigationRoute.HOME) {
            MainScreen(navController = navController, viewModel = viewModel)
        }
        composable(NavigationRoute.DETAILS) {
            SelectedNewsItem(viewModel = viewModel)
        }
    }
}

object NavigationRoute {
    const val HOME = "home"
    const val DETAILS = "details"
}

@Composable
fun MainScreen(viewModel: NewsViewModel, navController: NavHostController) {
    Column {
        SearchView(viewModel)
        when (val state = viewModel.newsUiState.collectAsState().value) {
            NewsUiState.Empty -> EmptyScreen()
            is NewsUiState.Error -> ErrorScreen()
            is NewsUiState.Loaded -> NewsScreen(state.data.articles, navController, viewModel)
            NewsUiState.Loading -> LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SearchView(viewModel: NewsViewModel) {
    val state = viewModel.state
    var isCorrectQuest by rememberSaveable {
        mutableStateOf(true)
    }
    Column {
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
                color = Color.Black,
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
                            isCorrectQuest = true
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
                backgroundColor = if (isCorrectQuest) GreyNews else Color.Red,
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

@Composable
fun NewsScreen(list: List<Articles>, navController: NavHostController, viewModel: NewsViewModel) {
    LazyColumn {
        items(items = list) { articles ->
            NewsItem(articles = articles, navController, viewModel = viewModel)
        }
    }
}


@Composable
fun EmptyScreen() {
}

@Composable
fun ErrorScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error")
    }
}

@Composable
fun SelectedNewsItem(viewModel: NewsViewModel) {
    val articles = viewModel.articles

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = articles.title ?: "",
            style = Typography.body1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            NewsImage(url = articles.urlToImage, description = articles.title ?: "", 200)
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = articles.publishedAt?.getDateOfPublished ?: "",
                    modifier = Modifier
                        .padding(start = 8.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = articles.author ?: "",
                    color = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .padding(end = 8.dp),
                    maxLines = 2
                )
            }

            Text(
                text = articles.content ?: "",
                style = Typography.body1,
                modifier = Modifier
                    .padding(all = 4.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun NewsImage(url: String?, description: String, size: Int) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(RectangleShape)
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }
            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(id = R.drawable.image_not_supported),
                    contentDescription = "none"
                )
            }
            else -> {
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsItem(
    articles: Articles,
    navController: NavHostController,
    viewModel: NewsViewModel
) {

    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth(),
        elevation = 2.dp,
        backgroundColor = Color.White,
        shape = Shapes.small,
        onClick = {
            viewModel.articles = articles
            navController.navigate(NavigationRoute.DETAILS)
        }
    ) {
        Row {
            NewsImage(url = articles.urlToImage, description = articles.title ?: "", 100)

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Row {
                    Text(text = articles.publishedAt?.getDateOfPublished ?: "")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = articles.author ?: "",
                        color = MaterialTheme.colors.secondaryVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = articles.title ?: "",
                    style = Typography.body1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = articles.description ?: "",
                    style = Typography.body1
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Ui() {
    val vm: NewsViewModel = viewModel()
    SearchView(vm)

}