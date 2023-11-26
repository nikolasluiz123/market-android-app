package br.com.market.storage.ui.screens.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.enums.EnumDialogType
import br.com.market.core.enums.EnumFileExtension
import br.com.market.core.extensions.format
import br.com.market.core.theme.MarketTheme
import br.com.market.core.ui.components.bottomsheet.report.EnumReportBottomSheetOptions
import br.com.market.core.utils.FileUtils
import br.com.market.market.compose.components.LabeledText
import br.com.market.market.compose.components.bottomsheet.BottomSheetReportOperations
import br.com.market.market.compose.components.dialog.MarketDialog
import br.com.market.market.compose.components.filter.SimpleFilter
import br.com.market.market.compose.components.list.LazyVerticalListWithEmptyState
import br.com.market.market.compose.components.topappbar.SimpleMarketTopAppBar
import br.com.market.market.pdf.generator.common.ReportFile
import br.com.market.storage.R
import br.com.market.storage.ui.states.report.ReportSearchUIState
import br.com.market.storage.ui.viewmodels.report.ReportSearchScreenViewModel
import java.time.LocalDateTime

@Composable
fun ReportSearchScreen(
    viewModel: ReportSearchScreenViewModel,
    onBackClick: () -> Unit,
    onNavigateToPDFViewer: (uri: String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    ReportSearchScreen(
        state = state,
        onBackClick = onBackClick,
        onSimpleFilterChange = viewModel::simpleFilter,
        onReportClick = viewModel::saveReportClicked,
        onNavigateToPDFViewer = onNavigateToPDFViewer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportSearchScreen(
    state: ReportSearchUIState = ReportSearchUIState(),
    onBackClick: () -> Unit = { },
    onSimpleFilterChange: (filter: String) -> Unit = { },
    onReportClick: (report: ReportFile) -> Unit = { },
    onNavigateToPDFViewer: (uri: String) -> Unit = { }
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleMarketTopAppBar(
                title = stringResource(R.string.report_search_screen_label_title),
                onBackClick = onBackClick,
                showMenuWithLogout = false
            )
        }
    ) { padding ->
        ConstraintLayout(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val (listRef, searchBarRef, searchDividerRef) = createRefs()
            var searchActive by remember { mutableStateOf(false) }
            var openBottomSheet by rememberSaveable { mutableStateOf(false) }

            MarketDialog(
                type = EnumDialogType.CONFIRMATION,
                show = state.showDialog,
                onDismissRequest = { state.onHideDialog() },
                message = state.dialogMessage,
                onConfirm = state.onConfirm,
                onCancel = state.onCancel
            )

            SimpleFilter(
                modifier = Modifier
                    .constrainAs(searchBarRef) {
                        linkTo(start = parent.start, end = parent.end, bias = 0F)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth(),
                onSimpleFilterChange = onSimpleFilterChange,
                active = searchActive,
                onActiveChange = { searchActive = it },
                placeholderResId = R.string.reports_screen_search_for
            ) {
                LazyVerticalListWithEmptyState(items = state.reports) {
                    ReportListItem(item = it) { report ->
                        onReportClick(report)
                        openBottomSheet = true
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }

            if (!searchActive) {
                Divider(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(searchDividerRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            top.linkTo(searchBarRef.bottom)
                        }
                )

                LazyVerticalListWithEmptyState(
                    modifier = Modifier
                        .constrainAs(listRef) {
                            linkTo(start = parent.start, end = parent.end, bias = 0F)
                            linkTo(top = searchBarRef.bottom, bottom = parent.bottom, bias = 0F)
                        }
                        .fillMaxSize(),
                    items = state.reports
                ) {
                    ReportListItem(item = it) { report ->
                        onReportClick(report)
                        openBottomSheet = true
                    }
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }

            if (openBottomSheet) {
                BottomSheetReportOperations(
                    onDismissRequest = {
                        openBottomSheet = false
                    },
                    onItemClickListener = {
                        when (it) {
                            EnumReportBottomSheetOptions.SHARE -> {
                                FileUtils.shareFile(
                                    context,
                                    state.reportClicked?.path!!,
                                    EnumFileExtension.PDF_FILE,
                                    context.getString(R.string.report_search_screen_share_report_title)
                                )
                            }

                            EnumReportBottomSheetOptions.VIEW -> {
                                onNavigateToPDFViewer(state.reportClicked?.path!!)
                            }

                            EnumReportBottomSheetOptions.DELETE -> {
                                state.onShowDialog?.onShow(
                                    type = EnumDialogType.CONFIRMATION,
                                    message = context.getString(R.string.report_search_screen_delete_report_message),
                                    onConfirm = {
                                        FileUtils.deleteFile(state.reportClicked?.path!!)
                                        onSimpleFilterChange("")
                                        state.onHideDialog()
                                    },
                                    onCancel = { state.onHideDialog() }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun ReportListItem(item: ReportFile, onItemClick: (item: ReportFile) -> Unit) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item) }) {
        val (nameRef, dateRef) = createRefs()
        createHorizontalChain(nameRef, dateRef)


        LabeledText(
            modifier = Modifier.constrainAs(nameRef) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.6f
            },
            label = stringResource(id = R.string.dialog_reports_label_name),
            value = item.name
        )

        LabeledText(
            modifier = Modifier.constrainAs(dateRef) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)

                width = Dimension.fillToConstraints
                horizontalChainWeight = 0.4f
            },
            label = stringResource(id = R.string.dialog_reports_label_date),
            value = item.date
        )
    }
}

@Preview
@Composable
fun ReportListItemPreview() {
    MarketTheme {
        Surface {
            ReportListItem(
                item = ReportFile(
                    name = "relatorio_operacoes",
                    date = LocalDateTime.now().format(EnumDateTimePatterns.DATE_TIME),
                    path = ""
                )
            ) { }
        }
    }
}

@Preview
@Composable
fun ReportSearchScreenPreview() {
    MarketTheme {
        Surface {
            ReportSearchScreen()
        }
    }
}