package br.com.market.storage.ui.screens.movement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import br.com.market.core.theme.colorCardActive
import br.com.market.core.ui.components.InfoChip
import br.com.market.enums.EnumOperationType
import br.com.market.enums.EnumUnit
import br.com.market.storage.extensions.formatQuantityIn
import java.time.LocalDateTime

@Composable
fun StorageListCard(
    productName: String,
    operationType: EnumOperationType,
    quantity: Int,
    onItemClick: () -> Unit = { },
    datePrevision: LocalDateTime? = null,
    dateRealization: LocalDateTime? = null,
    responsibleName: String? = null,
    description: String? = null
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable {
                onItemClick()
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorCardActive)
    ) {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            val (
                labelNameRef, nameRef,
                chipOperationTypeRef,
                labelPrevisionRef, previsionRef,
                labelRealizationRef, realizationRef,
                labelQuantityRef, quantityRef,
                labelResponsibleRef, responsibleRef,
                labelDescriptionRef, descriptionRef
            ) = createRefs()
            createHorizontalChain(labelNameRef, chipOperationTypeRef)
            createHorizontalChain(nameRef, chipOperationTypeRef)

            Text(
                text = "Nome",
                modifier = Modifier
                    .constrainAs(labelNameRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.6F
                    }
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = productName,
                modifier = Modifier
                    .constrainAs(nameRef) {
                        start.linkTo(labelNameRef.start)
                        top.linkTo(labelNameRef.bottom)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.6F
                    }
                    .padding(start = 8.dp, end = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )

            when (operationType) {
                EnumOperationType.Input -> {
                    InfoChip(
                        label = "Entrada",
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
                                top.linkTo(labelNameRef.top)

                                horizontalChainWeight = 0.4F

                            }
                            .padding(end = 8.dp)
                    )
                }

                EnumOperationType.Sell -> {
                    InfoChip(
                        label = "Venda",
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
                                top.linkTo(labelNameRef.top)

                                horizontalChainWeight = 0.4F

                            }
                            .padding(end = 8.dp)
                    )
                }

                EnumOperationType.Output -> {
                    InfoChip(
                        label = "Baixa",
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
                                top.linkTo(labelNameRef.top)

                                horizontalChainWeight = 0.4F

                            }
                            .padding(end = 8.dp)
                    )
                }

                EnumOperationType.ScheduledInput -> {
                    InfoChip(
                        label = "Entrada Prevista",
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
                                top.linkTo(labelNameRef.top)

                                horizontalChainWeight = 0.4F

                            }
                            .padding(end = 8.dp)
                    )
                }
            }

            if (datePrevision != null && dateRealization != null) {
                createHorizontalChain(labelPrevisionRef, labelRealizationRef, labelQuantityRef)
                createHorizontalChain(previsionRef, realizationRef, quantityRef)

                Text(
                    text = "Previsão",
                    modifier = Modifier
                        .constrainAs(labelPrevisionRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = datePrevision.format(EnumDateTimePatterns.DATE_TIME),
                    modifier = Modifier
                        .constrainAs(previsionRef) {
                            start.linkTo(labelPrevisionRef.start)
                            top.linkTo(labelPrevisionRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(start = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Realização",
                    modifier = Modifier
                        .constrainAs(labelRealizationRef) {
                            start.linkTo(labelPrevisionRef.end)
                            top.linkTo(labelPrevisionRef.top)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = dateRealization.format(EnumDateTimePatterns.DATE_TIME),
                    modifier = Modifier
                        .constrainAs(realizationRef) {
                            start.linkTo(labelRealizationRef.start)
                            top.linkTo(labelRealizationRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.32F
                        }
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Quantidade",
                    modifier = Modifier
                        .constrainAs(labelQuantityRef) {
                            start.linkTo(labelRealizationRef.end)
                            top.linkTo(labelRealizationRef.top)
                            end.linkTo(chipOperationTypeRef.end)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.25F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )

                Text(
                    text = quantity.formatQuantityIn(EnumUnit.UNIT),
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            start.linkTo(labelQuantityRef.start)
                            top.linkTo(labelQuantityRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.25F
                        }
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            } else if (datePrevision != null) {
                createHorizontalChain(labelPrevisionRef, labelQuantityRef)
                createHorizontalChain(previsionRef, quantityRef)

                Text(
                    text = "Previsão",
                    modifier = Modifier
                        .constrainAs(labelPrevisionRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = datePrevision.format(EnumDateTimePatterns.DATE_TIME),
                    modifier = Modifier
                        .constrainAs(previsionRef) {
                            start.linkTo(labelPrevisionRef.start)
                            top.linkTo(labelPrevisionRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(start = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Quantidade",
                    modifier = Modifier
                        .constrainAs(labelQuantityRef) {
                            start.linkTo(labelPrevisionRef.end)
                            top.linkTo(labelPrevisionRef.top)
                            end.linkTo(chipOperationTypeRef.end)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )

                Text(
                    text = quantity.formatQuantityIn(EnumUnit.UNIT),
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            start.linkTo(labelQuantityRef.start)
                            top.linkTo(labelQuantityRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            } else if (dateRealization != null) {
                createHorizontalChain(labelRealizationRef, labelQuantityRef)
                createHorizontalChain(realizationRef, quantityRef)

                Text(
                    text = "Realização",
                    modifier = Modifier
                        .constrainAs(labelRealizationRef) {
                            start.linkTo(nameRef.start)
                            top.linkTo(nameRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = dateRealization.format(EnumDateTimePatterns.DATE_TIME),
                    modifier = Modifier
                        .constrainAs(realizationRef) {
                            start.linkTo(labelRealizationRef.start)
                            top.linkTo(labelRealizationRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(start = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Quantidade",
                    modifier = Modifier
                        .constrainAs(labelQuantityRef) {
                            start.linkTo(labelRealizationRef.end)
                            top.linkTo(labelRealizationRef.top)
                            end.linkTo(chipOperationTypeRef.end)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(top = 8.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End
                )

                Text(
                    text = quantity.formatQuantityIn(EnumUnit.UNIT),
                    modifier = Modifier
                        .constrainAs(quantityRef) {
                            start.linkTo(labelQuantityRef.start)
                            top.linkTo(labelQuantityRef.bottom)

                            width = Dimension.fillToConstraints
                            horizontalChainWeight = 0.5F
                        }
                        .padding(end = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End
                )
            }

            if (operationType == EnumOperationType.Output) {
                Text(
                    text = "Responsável",
                    modifier = Modifier
                        .constrainAs(labelResponsibleRef) {
                            linkTo(start = parent.start, end = parent.end)

                            if (datePrevision != null)
                                top.linkTo(previsionRef.bottom, margin = 8.dp)
                            else
                                top.linkTo(realizationRef.bottom, margin = 8.dp)

                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = responsibleName!!,
                    modifier = Modifier
                        .constrainAs(responsibleRef) {
                            linkTo(start = labelResponsibleRef.start, end = labelResponsibleRef.end)
                            top.linkTo(labelResponsibleRef.bottom)

                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Descrição",
                    modifier = Modifier
                        .constrainAs(labelDescriptionRef) {
                            linkTo(start = responsibleRef.start, end = responsibleRef.end)
                            top.linkTo(responsibleRef.bottom, margin = 8.dp)

                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = description!!,
                    modifier = Modifier
                        .constrainAs(descriptionRef) {
                            linkTo(start = labelDescriptionRef.start, end = labelDescriptionRef.end)
                            linkTo(top = labelDescriptionRef.bottom, bottom = parent.bottom, bias = 0F)

                            width = Dimension.fillToConstraints
                        }
                        .padding(start = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun StorageListCardInputPreview() {
    MarketTheme {
        Surface {
            StorageListCard(
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
            StorageListCard(
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
            StorageListCard(
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
            StorageListCard(
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
            StorageListCard(
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
            StorageListCard(
                productName = "Wafer de Chocolate com Avelã dos Montes da Índia Nevada do Alaska",
                operationType = EnumOperationType.ScheduledInput,
                datePrevision = LocalDateTime.now(),
                dateRealization = LocalDateTime.now(),
                quantity = 50
            )
        }
    }
}