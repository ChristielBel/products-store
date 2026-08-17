package com.example.productsStore.presentation.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.productsStore.domain.model.Product
import com.example.productsStore.presentation.common.ErrorBox
import com.example.productsStore.presentation.common.LoadingBox
import com.example.productsStore.utils.prettyPrint

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel,
    onNavigateToProduct: (Int) -> Unit,
    onNavigateToCart: () -> Unit,
) {
    val store = viewModel.store
    val state by store.state.collectAsState()
    val products =
        viewModel.products.collectAsLazyPagingItems()

    LaunchedEffect(store) {
        store.news.collect { news ->
            when (news) {
                is ProductsNews.OpenProductDetails -> {
                    onNavigateToProduct(news.id)
                }

                ProductsNews.OpenCart -> {
                    onNavigateToCart()
                }
            }
        }
    }

    when (val refresh = products.loadState.refresh) {
        is LoadState.Loading -> {
            LoadingBox()
            return
        }

        is LoadState.Error -> {
            ErrorBox(
                refresh.error.prettyPrint(),
                products::retry
            )
            return
        }

        else -> Unit
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        ProductsHeader(
            cartItemCount = state.cartItemCount,
            onCartClick = {
                store.dispatch(
                    ProductsEvent.Ui.OnCartClicked
                )
            }
        )

        Divider(
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.07f)
        )

        ProductsList(
            products = products,
            onClick = { id ->
                store.dispatch(
                    ProductsEvent.Ui.OnProductClicked(id)
                )
            }
        )
    }
}

@Composable
private fun ProductsHeader(
    cartItemCount: Int,
    onCartClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Products",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground
        )
        CartIconButton(
            itemCount = cartItemCount,
            onClick = onCartClick
        )
    }
}

@Composable
private fun CartIconButton(
    itemCount: Int,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        if (itemCount > 0) {
            BadgedBox(
                badge = {
                    Badge {
                        Text(
                            text = if (itemCount > 99) "99+" else itemCount.toString(),
                            style = MaterialTheme.typography.overline
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = "Cart ($itemCount items)",
                    tint = MaterialTheme.colors.onBackground
                )
            }
        } else {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Cart",
                tint = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ProductsList(
    products: LazyPagingItems<Product>,
    onClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        productItems(products = products, onClick = onClick)

        when (products.loadState.append) {
            is LoadState.Loading -> item {
                LoadingBox(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            is LoadState.Error -> item { AppendErrorItem(onRetry = products::retry) }
            else -> Unit
        }
    }
}

private fun LazyListScope.productItems(
    products: LazyPagingItems<Product>,
    onClick: (Int) -> Unit,
) {
    items(
        count = products.itemCount,
        key = { index -> products[index]?.id ?: index }
    ) { index ->
        products[index]?.let { product ->
            ProductCard(
                product = product,
                onClick = { onClick(product.id) }
            )
        }
    }
}

@Composable
private fun AppendErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Failed to load more products",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
        TextButton(onClick = onRetry) {
            Text("Retry", color = MaterialTheme.colors.primary)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        onClick = onClick,
        elevation = 0.dp,
        color = MaterialTheme.colors.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductCardInfo(
                modifier = Modifier.weight(1f),
                title = product.title,
                brand = product.brand,
                id = product.id
            )
            ProductCardPrice(price = product.price)
        }
    }
}

@Composable
private fun ProductCardInfo(
    modifier: Modifier = Modifier,
    title: String,
    brand: String?,
    id: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = brand ?: "Unknown brand",
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
            letterSpacing = 0.3.sp
        )
        ProductIdBadge(id = id)
    }
}

@Composable
private fun ProductIdBadge(id: Int) {
    Box(
        modifier = Modifier
            .padding(top = 2.dp)
            .background(
                color = MaterialTheme.colors.primary.copy(alpha = 0.08f),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "#$id",
            style = MaterialTheme.typography.overline,
            color = MaterialTheme.colors.primary,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun ProductCardPrice(price: Double) {
    Text(
        text = "$$price",
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface
    )
}