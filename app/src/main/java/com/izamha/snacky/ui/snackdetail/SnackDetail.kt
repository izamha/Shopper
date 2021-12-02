package com.izamha.snacky.ui.snackdetail

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.gowtham.ratingbar.*
import com.izamha.snacky.R
import com.izamha.snacky.model.*
import com.izamha.snacky.repository.callToRoom
import com.izamha.snacky.ui.components.*
import com.izamha.snacky.ui.theme.Neutral8
import com.izamha.snacky.ui.theme.SnackyTheme
import com.izamha.snacky.ui.utils.mirroringBackIcon
import com.izamha.snacky.viewmodel.SnackViewModel
import kotlin.math.max
import kotlin.math.min

private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 150.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)


@ExperimentalCoilApi
@Composable
fun SnackDetail(
    snackId: Long,
    upPress: () -> Unit,
    snackViewModel: SnackViewModel
) {
    val context = LocalContext.current
    val snacksCollValue = SnackRepo.getSnacks(snackViewModel = snackViewModel)
    val snackCollections = remember { snacksCollValue }

    if (snackCollections[0].snacks.isNotEmpty()) {
        val snackValue = SnackRepo.getSnack(
            snackId = snackId,
            snackViewModel = snackViewModel
        )

        val snack = remember(snackId) { snackValue }
        val related = remember(snackId) { SnackRepo.getRelated(snackId) }
        val newCart = if (callToRoom().isNotEmpty()) callToRoom() else snacks


        Box(Modifier.fillMaxSize()) {
            val scroll = rememberScrollState(0)
            Header()
            Body(related = related, scroll = scroll, snack = snack)
            Title(snack = snack, scroll = scroll.value)
            Image(imageUrl = snack.imageUrl!!, scroll = scroll.value)
            Up(upPress)
            CartBottomBar(
                snack = snack,
                modifier = Modifier.align(Alignment.BottomCenter),
                addToCart = { snack, count ->
                    addStuff(context, snack = newCart[snack.id!!.toInt()], count = count)
                }
            )
        }
    } else {
        val snackValue = SnackRepo.getSnackStatic(
            snackId = snackId,
        )
        val snack = remember(snackId) { snackValue }
        val related = remember(snackId) { SnackRepo.getRelated(snackId) }
        val newCart = if (callToRoom().isNotEmpty()) callToRoom() else snacks

        Box(Modifier.fillMaxSize()) {
            val scroll = rememberScrollState(0)
            Header()
            Body(related = related, scroll = scroll, snack = snack)
            Title(snack = snack, scroll = scroll.value)
            Image(imageUrl = snack.imageUrl!!, scroll = scroll.value)
            Up(upPress)
            CartBottomBar(
                snack = snack,
                modifier = Modifier.align(Alignment.BottomCenter),
                addToCart = { snack, count ->
                    addStuff(context = context, snack = newCart[snack.id!!.toInt()], count = count)
                })
        }
    }

}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(SnackyTheme.colors.tornado1))
    )
}

@Composable
private fun Up(upPress: () -> Unit) {
    IconButton(
        onClick = upPress,
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Neutral8.copy(alpha = 0.32f),
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = mirroringBackIcon(),
            tint = SnackyTheme.colors.iconInteractive,
            contentDescription = stringResource(R.string.label_back)
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun Body(
    related: List<SnackCollection>,
    scroll: ScrollState,
    snack: Snack
) {

    var rating: Float by remember { mutableStateOf(3.5f) }

    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            SnackySurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.detail_header),
                        style = MaterialTheme.typography.overline,
                        color = SnackyTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(16.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = stringResource(R.string.detail_placeholder),
                        style = MaterialTheme.typography.body1,
                        color = SnackyTheme.colors.textHelp,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                        modifier = HzPadding
                    )
                    val textButton = if (seeMore) {
                        stringResource(id = R.string.see_more)
                    } else {
                        stringResource(id = R.string.see_less)
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = SnackyTheme.colors.textLink,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = stringResource(R.string.ingredients),
                        style = MaterialTheme.typography.overline,
                        color = SnackyTheme.colors.textHelp,
                        modifier = HzPadding
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.ingredients_list),
                        style = MaterialTheme.typography.body1,
                        color = SnackyTheme.colors.textHelp,
                        modifier = HzPadding
                    )

                    Spacer(Modifier.height(16.dp))

                    // Rating bar
                    Row(modifier = HzPadding) {
                        RatingBar(
                            value = rating,
                            ratingBarStyle = RatingBarStyle.HighLighted,
                            size = 24.dp,
                            inactiveColor = Color.LightGray,
                            onValueChange = {
                                rating = it
                            }) {
                            Log.d("RATE", "onRatingChanged: $it, ${snack.name}")
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    SnackyDivider()

                    related.forEach { snackCollection ->
                        key(snackCollection.id) {
                            SnackCollection(
                                snackCollection = snackCollection,
                                onSnackClick = { },
                                highlight = false
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding(start = false, end = false)
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Title(snack: Snack, scroll: Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }
    val offset = (maxOffset - scroll).coerceAtLeast(minOffset)

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .graphicsLayer { translationY = offset }
            .background(color = SnackyTheme.colors.uiBackground)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = snack.name!!,
            style = MaterialTheme.typography.h4,
            color = SnackyTheme.colors.textSecondary,
            modifier = HzPadding
        )
        // TODO: Do we really need a tag line?!
//        Text(
//            text = snack.tagline,
//            style = MaterialTheme.typography.subtitle2,
//            fontSize = 20.sp,
//            color = SnackyTheme.colors.textHelp,
//            modifier = HzPadding
//        )
        Spacer(Modifier.height(4.dp))
        Text(
            // text = formatPrice(snack.price),
            text = "${snack.price}Rwf",
            style = MaterialTheme.typography.h6,
            color = SnackyTheme.colors.textPrimary,
            modifier = HzPadding
        )
        Spacer(Modifier.height(8.dp))
        SnackyDivider()
    }
}

@ExperimentalCoilApi
@Composable
private fun Image(
    imageUrl: String,
    scroll: Int
) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFraction = (scroll / collapseRange).coerceIn(0f, 1f)

    CollapsingImageLayout(
        collapseFraction = collapseFraction,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        SnackImage(
            imageUrl = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = lerp(imageMaxSize, imageMinSize, collapseFraction)
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        val imageX = lerp(
            (constraints.maxWidth - imageWidth) / 2, // centered when expanded
            constraints.maxWidth - imageWidth, // right aligned when collapsed
            collapseFraction
        )
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun CartBottomBar(
    snack: Snack,
    modifier: Modifier = Modifier,
    addToCart: (Snack, count: Int) -> Unit,
) {
    val context = LocalContext.current
    val (count, updateCount) = remember { mutableStateOf(1) }
    SnackySurface(modifier) {
        Column {
            SnackyDivider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .navigationBarsPadding(start = false, end = false)
                    .then(HzPadding)
                    .heightIn(min = BottomBarHeight)
            ) {
                QuantitySelector(
                    count = count,
                    decreaseItemCount = { if (count > 0) updateCount(count - 1) },
                    increaseItemCount = { updateCount(count + 1) }
                )
                Spacer(Modifier.width(16.dp))
                SnackyButton(
                    onClick = { addToCart(snack, count) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.add_to_cart),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun addStuff(
    context: Context,
    snack: Snack,
    count: Int
) {
    val currentCartItems = mutableListOf<String>()

    cart.forEach {
        currentCartItems.add(it.snack.name!!)
    }

    if (currentCartItems.contains(snack.name)) {
        Toast.makeText(
            context,
            "Already in cart. Updating count by ($count)",
            Toast.LENGTH_LONG
        ).show()
    } else {
        cart.add(OrderLine(snack, count))
        Toast.makeText(
            context, "Just added ${snack.name} ($count)",
            Toast.LENGTH_LONG
        ).show()
    }

    Log.d("SnackDetail", "addStuff: $currentCartItems")

}


@Preview("default")
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview("large font", fontScale = 2f)
@Composable
private fun SnackDetailPreview() {
    SnackyTheme {
//        SnackDetail(
//            snackId = 1L,
//            upPress = { },
//        )
    }
}
