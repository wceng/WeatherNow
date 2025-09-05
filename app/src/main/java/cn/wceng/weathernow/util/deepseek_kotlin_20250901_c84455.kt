package cn.wceng.weathernow.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TranslateHomeScreen2() {
    // 状态管理
    var sourceText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var sourceLanguage by remember { mutableStateOf("中文") }
    var targetLanguage by remember { mutableStateOf("英语") }
    var isTranslating by remember { mutableStateOf(false) }
    var isFavorited by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    
    // 模拟翻译过程
    if (sourceText.isNotEmpty() && translatedText.isEmpty()) {
        LaunchedEffect(sourceText) {
            isTranslating = true
            delay(1000) // 模拟网络请求延迟
            translatedText = when {
                sourceText.contains("你好") -> "Hello"
                sourceText.contains("谢谢") -> "Thank you"
                else -> "This is a translated text: ${sourceText.reversed()}"
            }
            isTranslating = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "翻译助手",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { /* 打开设置 */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "设置")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                NavigationItem(
                    icon = Icons.Default.Translate,
                    label = "翻译",
                    isSelected = true,
                    modifier = Modifier.weight(1f)
                )
                NavigationItem(
                    icon = Icons.Default.History,
                    label = "历史",
                    isSelected = false,
                    modifier = Modifier.weight(1f)
                )
                NavigationItem(
                    icon = Icons.Default.FavoriteBorder,
                    label = "收藏",
                    isSelected = false,
                    modifier = Modifier.weight(1f)
                )
                NavigationItem(
                    icon = Icons.Default.Person,
                    label = "我的",
                    isSelected = false,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            item {
                // 语言选择区域
                LanguageSelectionRow(
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    onSourceLanguageClick = { /* 打开源语言选择 */ },
                    onTargetLanguageClick = { /* 打开目标语言选择 */ },
                    onSwapLanguages = {
                        // 交换语言
                        val temp = sourceLanguage
                        sourceLanguage = targetLanguage
                        targetLanguage = temp
                        
                        // 交换文本
                        val tempText = sourceText
                        sourceText = translatedText
                        translatedText = tempText
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 输入区域
                TranslationCard(
                    title = sourceLanguage,
                    text = sourceText,
                    onTextChange = { sourceText = it },
                    hint = "请输入要翻译的文本...",
                    isResult = false,
                    showClearButton = sourceText.isNotEmpty(),
                    onClearClick = { sourceText = ""; translatedText = "" },
                    modifier = Modifier.fillMaxWidth(),
                    focusRequester = focusRequester
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 输入辅助按钮
                InputAssistantRow(
                    onVoiceInputClick = { /* 语音输入 */ },
                    onCameraInputClick = { /* 拍照翻译 */ },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 翻译结果区域
                TranslationCard(
                    title = targetLanguage,
                    text = translatedText,
                    onTextChange = { /* 通常不允许直接编辑结果 */ },
                    hint = if (isTranslating) "翻译中..." else "翻译结果将显示在这里",
                    isResult = true,
                    showClearButton = false,
                    onClearClick = { },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 翻译中的加载指示器
                if (isTranslating) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 结果操作栏
                if (translatedText.isNotEmpty()) {
                    ResultActionsRow(
                        isFavorited = isFavorited,
                        onFavoriteClick = { isFavorited = !isFavorited },
                        onCopyClick = {
                            // 复制到剪贴板
                            // 显示成功消息
                        },
                        onSpeakClick = { /* 文本朗读 */ },
                        onShareClick = { /* 分享结果 */ },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionRow(
    sourceLanguage: String,
    targetLanguage: String,
    onSourceLanguageClick: () -> Unit,
    onTargetLanguageClick: () -> Unit,
    onSwapLanguages: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 源语言选择
        LanguageButton(
            language = sourceLanguage,
            onClick = onSourceLanguageClick,
            modifier = Modifier.weight(1f)
        )
        
        // 交换按钮
        IconButton(
            onClick = onSwapLanguages,
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "交换语言",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(90f)
            )
        }
        
        // 目标语言选择
        LanguageButton(
            language = targetLanguage,
            onClick = onTargetLanguageClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun LanguageButton(
    language: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = language,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "选择语言",
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun TranslationCard(
    title: String,
    text: String,
    onTextChange: (String) -> Unit,
    hint: String,
    isResult: Boolean,
    showClearButton: Boolean,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Box {
                // 文本输入/显示区域
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 24.sp
                    ),
                    enabled = !isResult,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        // 隐藏键盘
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier)
                        .height(120.dp),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                )
                
                // 清除按钮
                if (showClearButton && !isResult) {
                    IconButton(
                        onClick = onClearClick,
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "清除文本",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputAssistantRow(
    onVoiceInputClick: () -> Unit,
    onCameraInputClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        // 语音输入按钮
        IconButton(onClick = onVoiceInputClick) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "语音输入",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // 相机输入按钮
        IconButton(onClick = onCameraInputClick) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "拍照翻译",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ResultActionsRow(
    isFavorited: Boolean,
    onFavoriteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSpeakClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // 收藏按钮
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorited) "取消收藏" else "收藏",
                tint = if (isFavorited) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        }
        
        // 复制按钮
        IconButton(onClick = onCopyClick) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "复制文本",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // 朗读按钮
        IconButton(onClick = onSpeakClick) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "朗读",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        // 分享按钮
        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "分享",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            fontSize = 12.sp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TranslateHomeScreen2Preview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TranslateHomeScreen2()
        }
    }
}