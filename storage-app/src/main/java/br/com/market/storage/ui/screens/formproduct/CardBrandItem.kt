package br.com.market.storage.ui.screens.formproduct

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import br.com.market.core.theme.MarketTheme
import br.com.market.core.theme.colorCardActive
import br.com.market.core.theme.colorPrimary
import br.com.market.core.ui.components.buttons.MenuIconButton
import br.com.market.domain.ProductBrandDomain
import br.com.market.storage.R
import java.util.*

/**
 * Card do item da lista de marcas.
 *
 * @param productBrandDomain Objeto com os dados a serem exibidos no card.
 * @param onItemClick Listener executado ao clicar no item.
 * @param onMenuItemDeleteBrandClick Listener executado ao clicar na opção Delete do menu.
 *
 * @author Nikolas Luiz Schmitt
 */
@Composable
fun CardBrandItem(
    productBrandDomain: ProductBrandDomain,
    onItemClick: (ProductBrandDomain) -> Unit = { },
    onMenuItemDeleteBrandClick: (UUID) -> Unit = { }
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onItemClick(productBrandDomain) },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = colorCardActive)
    ) {
        ConstraintLayout(
            Modifier.fillMaxSize()
        ) {
            val (brandLabelRef, brandNameRef, countRef, countLabelRef, menuButtonRef) = createRefs()

            createHorizontalChain(brandLabelRef, countLabelRef, menuButtonRef)
            createHorizontalChain(brandNameRef, countRef, menuButtonRef)

            Text(
                modifier = Modifier
                    .constrainAs(brandLabelRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)

                        horizontalChainWeight = 0.6F

                        width = Dimension.fillToConstraints
                    }
                    .padding(top = 8.dp, start = 8.dp, end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = colorPrimary,
                text = stringResource(R.string.card_brand_item_label_product)
            )

            Text(
                modifier = Modifier
                    .constrainAs(brandNameRef) {
                        top.linkTo(brandLabelRef.bottom)
                        start.linkTo(brandLabelRef.start)

                        horizontalChainWeight = 0.6F

                        width = Dimension.fillToConstraints
                    }
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimary,
                text = "${productBrandDomain.productName} ${productBrandDomain.brandName}"
            )

            Text(
                modifier = Modifier
                    .constrainAs(countLabelRef) {
                        top.linkTo(brandLabelRef.top)
                        start.linkTo(brandLabelRef.end)
                        end.linkTo(menuButtonRef.start)

                        horizontalChainWeight = 0.25F

                        width = Dimension.fillToConstraints
                    }
                    .padding(top = 8.dp, end = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = colorPrimary,
                text = stringResource(R.string.card_brand_item_label_qtd)
            )

            Text(
                modifier = Modifier
                    .constrainAs(countRef) {
                        top.linkTo(countLabelRef.bottom)
                        end.linkTo(countLabelRef.end)

                        horizontalChainWeight = 0.25F

                        width = Dimension.fillToConstraints
                    }
                    .padding(end = 8.dp, bottom = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                color = colorPrimary,
                text = productBrandDomain.count.toString()
            )

            Box(
                modifier = Modifier
                    .constrainAs(menuButtonRef) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)

                        horizontalChainWeight = 0.1F

                        width = Dimension.fillToConstraints

                    }) {

                MenuIconButton {
                    DropdownMenuItem(text = { Text("Deletar") }, onClick = {
                        onMenuItemDeleteBrandClick(productBrandDomain.brandId!!)
                    })
                }
            }
        }
    }
}

@Preview
@Composable
fun CardBrandItemPreview() {
    MarketTheme {
        Surface {
            CardBrandItem(ProductBrandDomain(productName = "Arroz", brandName = "Dalfovo", count = 4))
        }
    }
}