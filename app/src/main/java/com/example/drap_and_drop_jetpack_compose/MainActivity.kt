package com.example.drap_and_drop_jetpack_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.drap_and_drop_jetpack_compose.model.ShoesArticle
import com.example.drap_and_drop_jetpack_compose.model.SlideState
import com.example.drap_and_drop_jetpack_compose.ui.ShoesCard
import com.example.drap_and_drop_jetpack_compose.ui.theme.Blue
import com.example.drap_and_drop_jetpack_compose.ui.theme.DragAndDropInComposeTheme
import com.example.drap_and_drop_jetpack_compose.ui.theme.Pink
import com.example.drap_and_drop_jetpack_compose.ui.theme.Yellow

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragAndDropInComposeTheme(true) {
                Home()
            }
        }
    }
}

val allShoesArticles = arrayOf(
    ShoesArticle(
        title = "La Sportiva Katana",
        price = 149f,
        width = "38.5",
        drawable = R.mipmap.ic_climbig_blue,
        color = Blue
    ),
    ShoesArticle(
        title = "La Sportiva Solution Men",
        price = 119.99f,
        width = "39.5",
        drawable = R.mipmap.ic_climbig_yellow,
        color = Yellow
    ),
    ShoesArticle(
        title = "La Sportiva Solution Women",
        price = 99f,
        width = "36.0",
        drawable = R.mipmap.ic_climbig_pink,
        color = Pink
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun Home() {
    val shoesArticles = remember { mutableStateListOf(*allShoesArticles) }
    val slideStates = remember {
        mutableStateMapOf<ShoesArticle, SlideState>()
            .apply {
                shoesArticles.map { shoesArticle ->
                    shoesArticle to SlideState.NONE
                }.toMap().also {
                    putAll(it)
                }
            }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Drag and Drop In Compose")
                },
                actions = {
                    IconButton(onClick = {
                        val newShoesArticles = mutableListOf<ShoesArticle>()
                        ShoesArticle.ID += 1
                        allShoesArticles.forEach {
                            newShoesArticles.add(it.copy(id = ShoesArticle.ID))
                        }

                        shoesArticles.addAll(newShoesArticles)
                        Log.i("MainActivity", shoesArticles.toList().toString())
                    }) {
                        Icon(Icons.Filled.AddCircle, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        ShoesList(
            modifier = Modifier.padding(innerPadding),
            shoesArticles = shoesArticles,
            slideStates = slideStates,
            updateSlideState = { shoesArticle, slideState -> slideStates[shoesArticle] = slideState },
            updateItemPosition = { currentIndex, destinationIndex ->
                val shoesArticle = shoesArticles[currentIndex]
                shoesArticles.removeAt(currentIndex)
                shoesArticles.add(destinationIndex, shoesArticle)
                slideStates.apply {
                    shoesArticles.map { shoesArticle ->
                        shoesArticle to SlideState.NONE
                    }.toMap().also {
                        putAll(it)
                    }
                }
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun ShoesList(
    modifier: Modifier,
    shoesArticles: MutableList<ShoesArticle>,
    slideStates: Map<ShoesArticle, SlideState>,
    updateSlideState: (shoesArticle: ShoesArticle, slideState: SlideState) -> Unit,
    updateItemPosition: (currentIndex: Int, destinationIndex: Int) -> Unit
) {
    val lazyListState = rememberLazyListState()
    LazyColumn(
        state = lazyListState,
        modifier = modifier.padding(top = dimensionResource(id = R.dimen.list_top_padding))
    ) {
        items(shoesArticles.size) { index ->
            val shoesArticle = shoesArticles.getOrNull(index)
            if (shoesArticle != null) {
                key(shoesArticle) {
                    val slideState = slideStates[shoesArticle] ?: SlideState.NONE
                    ShoesCard(
                        shoesArticle = shoesArticle,
                        slideState = slideState,
                        shoesArticles = shoesArticles,
                        updateSlideState = updateSlideState,
                        updateItemPosition = updateItemPosition
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DragAndDropInComposeTheme(true) {
        Home()
    }
}

@Composable
fun Main(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    DragAndDropInComposeTheme(isDarkTheme) {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colorScheme.background) {
            content()
        }
    }
}