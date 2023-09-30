package br.com.market.market.compose.components.screens

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.market.core.ui.states.PDFViewerUIState
import br.com.market.market.compose.components.screens.viewmodel.PDFViewerViewModel
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import coil.memory.MemoryCache
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.math.sqrt

@Composable
fun PDFViewer(
    viewModel: PDFViewerViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    PDFViewer(
        state = state,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PDFViewer(
    state: PDFViewerUIState = PDFViewerUIState(),
    onBackClick: () -> Unit = { }
) {
    val coroutineScope = rememberCoroutineScope()
    val mutex = remember { Mutex() }

    val renderer by produceState<PdfRenderer?>(null, state.uri!!) {
        coroutineScope.launch(IO) {
            val input = ParcelFileDescriptor.open(File(state.path!!), ParcelFileDescriptor.MODE_READ_ONLY)
            value = PdfRenderer(input)
        }

        awaitDispose {
            val currentRenderer = value
            coroutineScope.launch(IO) {
                mutex.withLock {
                    currentRenderer?.close()
                }
            }
        }
    }

    val context = LocalContext.current
    val imageLoader = context.imageLoader

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = "Visualizador de PDF",
                onBackClick = onBackClick,
                showMenuWithLogout = false
            )
        }
    ) { padding ->
        ConstraintLayout(modifier = Modifier.fillMaxSize().padding(padding)) {
            val scrollState = rememberLazyListState()
            var scale by remember { mutableFloatStateOf(1f) }
            var rotation by remember { mutableFloatStateOf(0f) }
            var offset by remember { mutableStateOf(Offset.Zero) }
            val transformableState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
                scale *= zoomChange
                rotation += rotationChange
                offset += offsetChange
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y,
                    )
                    .transformable(state = transformableState)
            ) {
                val width = with(LocalDensity.current) { maxWidth.toPx() }.toInt()
                val height = (width * sqrt(2f)).toInt()
                val pageCount by remember(renderer) { derivedStateOf { renderer?.pageCount ?: 0 } }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = scrollState
                ) {
                    items(
                        count = pageCount,
                        key = { index -> "${state.uri}-$index" }
                    ) { index ->
                        val cacheKey = MemoryCache.Key("$state.uri-$index")
                        var bitmap by remember { mutableStateOf(imageLoader.memoryCache?.get(cacheKey) as? Bitmap?) }

                        if (bitmap == null) {
                            DisposableEffect(state.uri, index) {
                                val job = coroutineScope.launch(IO) {
                                    val destinationBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                                    mutex.withLock {
                                        try {
                                            renderer?.let {
                                                it.openPage(index).use { page ->
                                                    page.render(
                                                        destinationBitmap,
                                                        null,
                                                        null,
                                                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                                    )
                                                }
                                            }
                                        } catch (e: Exception) {
                                            return@launch
                                        }
                                    }
                                    bitmap = destinationBitmap
                                }
                                onDispose {
                                    job.cancel()
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .background(Color.White)
                                    .aspectRatio(1f / sqrt(2f))
                                    .fillMaxWidth()
                            )
                        } else {
                            val request = ImageRequest.Builder(context)
                                .size(width, height)
                                .memoryCacheKey(cacheKey)
                                .data(bitmap)
                                .build()

                            Image(
                                modifier = Modifier
                                    .background(Color.White)
                                    .aspectRatio(1f / sqrt(2f))
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit,
                                painter = rememberAsyncImagePainter(request),
                                contentDescription = "Page ${index + 1} of $pageCount"
                            )
                        }
                    }
                }
            }
        }
    }
}