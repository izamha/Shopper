package com.izamha.snacky.model

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.izamha.snacky.repository.callToRoom
import com.izamha.snacky.viewmodel.SnackViewModel

data class SnackCollection(
    val id: Long,
    val name: String,
    val snacks: List<Snack>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }

object SnackRepo {

    @Composable
    fun getSnacks(
        snackViewModel: SnackViewModel
    ): List<SnackCollection> {
        val snacksState = snackViewModel.readAllData.observeAsState()
        val snacks2: List<Snack>? = snacksState.value

        var notNull: List<Snack> = listOf()

       if (snacks2 != null) {
           // Log.d("SNACK_COLLECTION", "getSnacks: ${snacks2.subList(0, 4)}")
           notNull = snacks2
       }

        val tastyTreats = SnackCollection(
            id = 1L,
            name = "My Personal Picks",
            type = CollectionType.Highlight,
            snacks = notNull
        )

        val wfhFavs = tastyTreats.copy(
            id = 3L,
            name = "Healthy. Choose yours."
        )

        val popular = SnackCollection(
            id = 2L,
            name = "Popular on Snacky",
            // snacks = snacks.subList(14, 19)
            snacks = notNull
        )

        val newlyAdded = popular.copy(
            id = 4L,
            name = "Newly Added"
        )

        val exclusive = tastyTreats.copy(
            id = 5L,
            name = "Only on Snacky"
        )

        return listOf(
            tastyTreats,
            newlyAdded,
            wfhFavs,
            exclusive
        )
    }

    @Composable
    fun getSnacksStatic(): List<SnackCollection> {
        val tastyTreats = SnackCollection(
            id = 1L,
            name = "My Personal Picks",
            type = CollectionType.Highlight,
            snacks = snacks.subList(0, 6)
        )

        val wfhFavs = tastyTreats.copy(
            id = 3L,
            name = "Healthy. Choose yours."
        )

        val popular = SnackCollection(
            id = 2L,
            name = "Popular on Snacky",
            // snacks = snacks.subList(14, 19)
            snacks = snacks.subList(7, 12)
        )

        val newlyAdded = popular.copy(
            id = 4L,
            name = "Newly Added"
        )

        val exclusive = tastyTreats.copy(
            id = 5L,
            name = "Only on Snacky"
        )

        return listOf(
            tastyTreats,
            newlyAdded,
            wfhFavs,
            exclusive
        )
    }

    @Composable
    fun getSnack(
        snackId: Long,
        snackViewModel: SnackViewModel
    ): Snack {
        val snackState = snackViewModel.readAllData.observeAsState()
        val snacks: List<Snack>? = snackState.value
        return snacks!!.find { it.id == snackId }!!
    }

    @Composable
    fun getSnackStatic(
        snackId: Long
    ): Snack {
        return snacks.find { it.id == snackId }!!
    }


    fun getRelated(@Suppress("UNUSED_PARAMETER") snackId: Long) = related
    fun getInspiredByCart() = inspiredByCart
    fun getFilters() = filters
    fun getPriceFilters() = priceFilters
    fun getCart() = cart
    fun getSortFilters() = sortFilters
    fun getDestinations() = destinations
    fun getCategoryFilters() = categoryFilters
    fun getAddDestination() = addDestination
    fun getSortDefault() = sortDefault
    fun getLifeStyleFilters() = lifeStyleFilters

    fun returnCart(cart: List<OrderLine>): List<OrderLine> {
        return cart
    }
}

/**
 * Static data
 */

private val tastyTreats = SnackCollection(
    id = 1L,
    name = "My Personal Picks",
    type = CollectionType.Highlight,
    snacks = data
)

private val popular = SnackCollection(
    id = 2L,
    name = "Popular on Snacky",
    snacks = snacks.subList(14, 19)
)

private val wfhFavs = tastyTreats.copy(
    id = 3L,
    name = "Healthy. Choose yours."
)

private val newlyAdded = popular.copy(
    id = 4L,
    name = "Newly Added"
)

private val exclusive = tastyTreats.copy(
    id = 5L,
    name = "Only on Snacky"
)

private val also = tastyTreats.copy(
    id = 6L,
    name = "Customers also bought"
)

private val inspiredByCart = tastyTreats.copy(
    id = 7L,
    name = "Inspired by your cart"
)

private val snackCollections = listOf(
    tastyTreats,
    popular,
    wfhFavs,
    newlyAdded,
    exclusive
)

private val related = listOf(
    also,
    popular
)

val cart = mutableListOf(
    OrderLine(snacks[1], 3),
)

@Composable
fun CreateCart() {
    val newCart = callToRoom()
    OrderLine(newCart[1], 3)
}

data class OrderLine(
    val snack: Snack,
    val count: Int
)
