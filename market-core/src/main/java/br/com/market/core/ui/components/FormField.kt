package br.com.market.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.R
import br.com.market.core.theme.GREY_800
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.RED_600
import br.com.market.core.ui.states.Field

@Composable
fun FormField(
    modifier: Modifier = Modifier,
    labelResId: Int,
    field: Field,
    onNavigateClick: () -> Unit,
    additionalAction: (@Composable () -> Unit)? = null,
    hideValue: Boolean = false
) {
    ConstraintLayout(
        modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onNavigateClick() }
    ) {
        val (labeledTextRef, buttonAction, errorMessageRef, dividerRef, aditionalActionRef) = createRefs()
        createHorizontalChain(labeledTextRef, aditionalActionRef, buttonAction)

        if (field.valueIsEmpty()) {
            Text(
                modifier = Modifier.constrainAs(labeledTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)

                    if (field.errorMessage.isEmpty())
                        bottom.linkTo(dividerRef.top)
                    else
                        bottom.linkTo(errorMessageRef.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.8f
                },
                text = stringResource(id = labelResId),
                style = MaterialTheme.typography.titleSmall,
                color = GREY_800
            )

            if (additionalAction != null) {
                Row(
                    Modifier.constrainAs(aditionalActionRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(dividerRef.top)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.1f
                    }
                ) {
                    additionalAction()
                }
            }

            NavigateButton(
                modifier = Modifier.constrainAs(buttonAction) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(dividerRef.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.1f
                }
            )
        } else {
            LabeledText(
                modifier = Modifier.constrainAs(labeledTextRef) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)

                    if (field.errorMessage.isEmpty())
                        bottom.linkTo(parent.bottom)
                    else
                        bottom.linkTo(errorMessageRef.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.9f
                },
                label = stringResource(id = labelResId),
                value = if (hideValue) '\u2022'.toString().repeat(field.value.length) else field.value
            )

            if (additionalAction != null) {
                Row(
                    Modifier.constrainAs(aditionalActionRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(dividerRef.top)

                        width = Dimension.fillToConstraints
                        horizontalChainWeight = 0.1f
                    }
                ) {
                    additionalAction()
                }
            }

            ClearButton(
                modifier = Modifier.constrainAs(buttonAction) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(dividerRef.top)

                    width = Dimension.fillToConstraints
                    horizontalChainWeight = 0.1f
                },
                onClearClick = { field.onChange("") }
            )
        }

        if (field.errorMessage.isNotEmpty()) {
            Text(
                modifier = Modifier.constrainAs(errorMessageRef) {
                    start.linkTo(parent.start)
                    top.linkTo(
                        labeledTextRef.bottom,
                        margin = if (!field.valueIsEmpty()) 4.dp else 0.dp
                    )
                    bottom.linkTo(dividerRef.top)

                    width = Dimension.fillToConstraints
                },
                text = field.errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = RED_600
            )
        }

        Divider(
            Modifier
                .fillMaxWidth()
                .constrainAs(dividerRef) {
                    linkTo(start = parent.start, end = parent.end, bias = 0F)
                    bottom.linkTo(parent.bottom)
                }
                .padding(top = 8.dp)
        )

    }
}

@Composable
private fun ClearButton(
    modifier: Modifier,
    onClearClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClearClick
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = stringResource(R.string.label_clear)
        )
    }
}

@Composable
private fun NavigateButton(modifier: Modifier) {
    IconButton(
        modifier = modifier,
        onClick = { }
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.label_navigate)
        )
    }
}

@Preview
@Composable
fun FormFieldWithValuePreview() {
    MarketTheme {
        Surface {
            FormField(
                labelResId = R.string.label_adicionar,
                field = Field(value = "Teste"),
                onNavigateClick = { },
            )
        }
    }
}

@Preview
@Composable
fun FormFieldWithErrorPreview() {
    MarketTheme {
        Surface {
            FormField(
                labelResId = R.string.label_adicionar,
                field = Field(errorMessage = "O campo é obrigatório"),
                onNavigateClick = { },
            )
        }
    }
}

@Preview
@Composable
fun FormFieldWithErrorAndValuePreview() {
    MarketTheme {
        Surface {
            FormField(
                labelResId = R.string.label_adicionar,
                field = Field(value = "Lero lero lero", errorMessage = "O valor é inválido"),
                onNavigateClick = { },
            )
        }
    }
}

@Preview
@Composable
fun FormFieldPreview() {
    MarketTheme {
        Surface {
            FormField(
                labelResId = R.string.label_adicionar,
                field = Field(),
                onNavigateClick = { },
            )
        }
    }
}