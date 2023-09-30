package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.enums.EnumDateTimePatterns
import br.com.market.core.extensions.format
import br.com.market.core.theme.BLUE_500
import br.com.market.core.theme.GREEN_500
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.ORANGE_500
import br.com.market.core.theme.RED_500
import br.com.market.market.compose.components.InfoChip
import br.com.market.market.compose.components.LabeledText
import br.com.market.enums.EnumOperationType
import br.com.market.enums.EnumUnit
import br.com.market.storage.R
import br.com.market.storage.extensions.formatQuantityIn
import java.time.LocalDateTime

@Composable
fun MovementListItem(
    productName: String,
    operationType: EnumOperationType,
    quantity: Int,
    onItemClick: () -> Unit = { },
    datePrevision: LocalDateTime? = null,
    dateRealization: LocalDateTime? = null,
    responsibleName: String? = null,
    description: String? = null
) {
    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClick()
            }
    ) {
        val (
            nameRef, chipOperationTypeRef, previsionRef, realizationRef,
            quantityRef, responsibleRef, descriptionRef
        ) = createRefs()
        createHorizontalChain(nameRef, chipOperationTypeRef)

        br.com.market.market.compose.components.LabeledText(
            modifier = Modifier
                .constrainAs(nameRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.6F
                }
                .padding(end = 8.dp),
            label = stringResource(R.string.movement_list_item_label_name),
            value = productName
        )

        when (operationType) {
            EnumOperationType.Input -> {
                br.com.market.market.compose.components.InfoChip(
                    label = stringResource(R.string.movement_list_item_label_chip_input),
                    icon = {
                        Icon(
                            painter = painterResource(id = br.com.market.core.R.drawable.ic_add_16dp),
                            contentDescription = null
                        )
                    },
                    backgroundColor = BLUE_500,
                    borderColor = BLUE_500,
                    labelColor = Color.White,
                    iconColor = Color.White,
                    modifier = Modifier
                        .constrainAs(chipOperationTypeRef) {
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.top)

                            horizontalChainWeight = 0.4F

                        }
                        .padding(end = 8.dp)
                )
            }

            EnumOperationType.Sell -> {
                br.com.market.market.compose.components.InfoChip(
                    label = stringResource(R.string.movement_list_item_label_chip_sell),
                    icon = {
                        Icon(
                            painter = painterResource(id = br.com.market.core.R.drawable.ic_money_16dp),
                            contentDescription = null
                        )
                    },
                    backgroundColor = GREEN_500,
                    borderColor = GREEN_500,
                    labelColor = Color.White,
                    iconColor = Color.White,
                    modifier = Modifier
                        .constrainAs(chipOperationTypeRef) {
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.top)

                            horizontalChainWeight = 0.4F

                        }
                        .padding(end = 8.dp)
                )
            }

            EnumOperationType.Output -> {
                br.com.market.market.compose.components.InfoChip(
                    label = stringResource(R.string.movement_list_item_label_chip_output),
                    icon = {
                        Icon(
                            painter = painterResource(id = br.com.market.core.R.drawable.ic_warning_16dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    backgroundColor = RED_500,
                    borderColor = RED_500,
                    labelColor = Color.White,
                    iconColor = Color.White,
                    modifier = Modifier
                        .constrainAs(chipOperationTypeRef) {
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.top)

                            horizontalChainWeight = 0.4F

                        }
                        .padding(end = 8.dp)
                )
            }

            EnumOperationType.ScheduledInput -> {
                br.com.market.market.compose.components.InfoChip(
                    label = stringResource(R.string.movement_list_item_label_chip_scheduled_input),
                    icon = {
                        Icon(
                            painter = painterResource(id = br.com.market.core.R.drawable.ic_calendar_16dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    backgroundColor = ORANGE_500,
                    borderColor = ORANGE_500,
                    labelColor = Color.White,
                    iconColor = Color.White,
                    modifier = Modifier
                        .constrainAs(chipOperationTypeRef) {
                            end.linkTo(parent.end)
                            top.linkTo(nameRef.top)

                            horizontalChainWeight = 0.4F

                        }
                        .padding(end = 8.dp)
                )
            }
        }

        when {
            datePrevision != null && dateRealization != null -> {
                createHorizontalChain(previsionRef, realizationRef, quantityRef)

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(previsionRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_prevision),
                    value = datePrevision.format(EnumDateTimePatterns.DATE_TIME)
                )

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(realizationRef) {
                            start.linkTo(previsionRef.end)
                            top.linkTo(previsionRef.top)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_realization),
                    value = dateRealization.format(EnumDateTimePatterns.DATE_TIME)
                )

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            linkTo(start = realizationRef.end, end = chipOperationTypeRef.end, bias = 0f)
                            top.linkTo(realizationRef.top)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.25F
                        }.padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_quantity),
                    value = quantity.formatQuantityIn(EnumUnit.UNIT),
                    textAlign = TextAlign.End
                )

            }

            datePrevision != null -> {
                createHorizontalChain(previsionRef, quantityRef)

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(previsionRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_prevision),
                    value = datePrevision.format(EnumDateTimePatterns.DATE_TIME)
                )

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            linkTo(start = previsionRef.end, end = chipOperationTypeRef.end, bias = 0f)
                            top.linkTo(previsionRef.top)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }.padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_quantity),
                    value = quantity.formatQuantityIn(EnumUnit.UNIT),
                    textAlign = TextAlign.End
                )

            }

            dateRealization != null -> {
                createHorizontalChain(realizationRef, quantityRef)

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(realizationRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_realization),
                    value = dateRealization.format(EnumDateTimePatterns.DATE_TIME)
                )

                br.com.market.market.compose.components.LabeledText(
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            linkTo(start = realizationRef.end, end = chipOperationTypeRef.end, bias = 0f)
                            top.linkTo(realizationRef.top)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }.padding(top = 8.dp, end = 8.dp),
                    label = stringResource(R.string.movement_list_item_label_quantity),
                    value = quantity.formatQuantityIn(EnumUnit.UNIT),
                    textAlign = TextAlign.End
                )
            }
        }

        if (operationType == EnumOperationType.Output) {
            br.com.market.market.compose.components.LabeledText(
                modifier = Modifier
                    .constrainAs(responsibleRef) {
                        linkTo(start = parent.start, end = parent.end)

                        if (datePrevision != null)
                            top.linkTo(previsionRef.bottom, margin = 8.dp)
                        else
                            top.linkTo(realizationRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                    }.padding(top = 8.dp),
                label = stringResource(R.string.movement_list_item_label_responsible),
                value = responsibleName!!
            )

            br.com.market.market.compose.components.LabeledText(
                modifier = Modifier
                    .constrainAs(descriptionRef) {
                        linkTo(start = responsibleRef.start, end = responsibleRef.end)
                        top.linkTo(responsibleRef.bottom, margin = 8.dp)

                        width = Dimension.fillToConstraints
                    }.padding(top = 8.dp),
                label = stringResource(R.string.movement_list_item_label_description),
                value = description!!,
                maxLinesValue = 4
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardInputPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã",
                operationType = EnumOperationType.Input,
                datePrevision = null,
                dateRealization = LocalDateTime.now(),
                quantity = 500
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardInputWithPrevisionPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.Input,
                datePrevision = LocalDateTime.now(),
                dateRealization = LocalDateTime.now().plusDays(2),
                quantity = 500
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardInputWithPrevisionAndRealizationIqualsPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.Input,
                datePrevision = LocalDateTime.now(),
                dateRealization = LocalDateTime.now(),
                quantity = 500
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardSellPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.Sell,
                dateRealization = LocalDateTime.now(),
                quantity = 5
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardOutputPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.Output,
                dateRealization = LocalDateTime.now(),
                quantity = 50,
                responsibleName = "Nikolas Luiz Schmitt",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."
            )
        }
    }
}

@Preview
@Composable
fun StorageListCardScheduledInputPreview() {
    MarketTheme {
        Surface {
            MovementListItem(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.ScheduledInput,
                datePrevision = LocalDateTime.now(),
                dateRealization = LocalDateTime.now(),
                quantity = 50
            )
        }
    }
}