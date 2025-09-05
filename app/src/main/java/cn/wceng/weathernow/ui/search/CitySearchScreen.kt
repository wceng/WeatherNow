package cn.wceng.weathernow.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.wceng.weathernow.domain.model.SavableCity
import cn.wceng.weathernow.ui.component.ErrorContent
import cn.wceng.weathernow.ui.component.LoadingContent
import cn.wceng.weathernow.ui.previewSavableCity
import cn.wceng.weathernow.ui.theme.WeatherNowTheme

@Composable
fun CitySearchScreen(
    onBackClick: () -> Unit = {},
    onNavigateToHomeScreen: (String) -> Unit = {},
    viewModel: CitySearchViewModel = hiltViewModel(),
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CitySearchContent(
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        uiState = uiState,
        onBackClick = onBackClick,
        onAddCity = viewModel::savedCity,
        onSelectCity = {
            viewModel.savedCity(it, true)
            onNavigateToHomeScreen(it)
        }
    )
}

@Composable
private fun CitySearchContent(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    uiState: CitySearchUiState,
    onBackClick: () -> Unit = {},
    onAddCity: (String, Boolean) -> Unit = { _, _ -> },
    onSelectCity: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            SearchTopBar(
                searchQuery = searchQuery,
                onSearchQueryChanged = onSearchQueryChanged,
                onBackClick = onBackClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (uiState) {
                CitySearchUiState.Idle -> SearchPlaceHolder()
                CitySearchUiState.Loading -> LoadingContent()
                is CitySearchUiState.Success ->
                    CityResultList(
                        cities = uiState.savedCities,
                        onSavedCity = onAddCity,
                        onSelectCity = onSelectCity,
                    )

                CitySearchUiState.EmptyResult ->
                    EmptyResultContent()

                is CitySearchUiState.Error ->
                    ErrorContent(
                        message = uiState.message,
                    )
            }
        }
    }
}

@Composable
private fun SearchTopBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                placeholder = { Text("输入省份、城市、...") },
                singleLine = true,
                colors =
                    TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                leadingIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            null
                        )
                    }
                }
            )
        }
    )
}

@Composable
private fun CityResultList(
    cities: List<SavableCity>,
    onSavedCity: (String, Boolean) -> Unit,
    onSelectCity: (String) -> Unit
) {
    LazyColumn {
        items(cities, key = { it.city.id }) { savedCity ->
            CityResultItem(
                savedCity,
                onSaveCity = { onSavedCity(savedCity.city.id, it) },
                onSelect = { onSelectCity(savedCity.city.id) }
            )
        }
    }
}

@Composable
private fun CityResultItem(
    savableCity: SavableCity,
    modifier: Modifier = Modifier,
    onSaveCity: (Boolean) -> Unit,
    onSelect: () -> Unit
) {
    ListItem(
        modifier = modifier
            .clickable {
                onSelect()
            },
        headlineContent = {
            Text(
                text =
                    "${savableCity.city.name} - ${savableCity.city.district}" +
                            " - ${savableCity.city.province} - ${savableCity.city.country}"
            )
        },
        trailingContent = {
            if (savableCity.saved.not()) {
                IconButton(onClick = { onSaveCity(true) }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            } else {
                FilledTonalIconButton(onClick = { onSaveCity(false) }) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            }

        }
    )
}

@Composable
private fun SearchPlaceHolder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "输入城市名或坐标开始搜索",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EmptyResultContent(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "没有找到匹配的城市",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Preview
@Composable
private fun SearchTopBarPreview() {
    Surface {
        SearchTopBar(
            searchQuery = "Su Zhou",
            onSearchQueryChanged = {}
        ) { }
    }
}

@Preview
@Composable
private fun SearchContentSuccess() {
    Surface {
        CitySearchContent(
            searchQuery = "Beijing",
            onSearchQueryChanged = { },
            uiState = CitySearchUiState.Success(listOf(previewSavableCity)),
            onBackClick = { }
        ) {}
    }
}

@Preview
@Composable
private fun SearchContentEmptyResult() {
    Surface {
        CitySearchContent(
            searchQuery = "Beijing",
            onSearchQueryChanged = { },
            uiState = CitySearchUiState.EmptyResult,
            onBackClick = { }
        ) {}
    }
}

@Preview
@Composable
private fun SearchContentIdea() {
    WeatherNowTheme {
        Surface {
            CitySearchContent(
                searchQuery = "Beijing",
                onSearchQueryChanged = { },
                uiState = CitySearchUiState.Idle,
                onBackClick = { }
            ) {}
        }
    }
}