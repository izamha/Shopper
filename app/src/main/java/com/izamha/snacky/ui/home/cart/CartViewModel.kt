package com.izamha.snacky.ui.home.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.izamha.snacky.R
import com.izamha.snacky.model.OrderLine
import com.izamha.snacky.model.SnackRepo
import com.izamha.snacky.model.SnackbarManager
import com.izamha.snacky.model.cart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Holds the contents of the cart and allows changes to it.
 *
 * TODO: Move data to Repository so it can be displayed and changed consistently throughout the app.
 */
class CartViewModel(
    private val snackbarManager: SnackbarManager,
    snackRepository: SnackRepo,
) : ViewModel() {

    private val _orderLines: MutableStateFlow<List<OrderLine>> =
        MutableStateFlow(snackRepository.returnCart(cart))
    val orderLines: StateFlow<List<OrderLine>> get() = _orderLines

    // Logic to show errors every few requests
    private var requestCount = 0
    private fun shouldRandomlyFail(): Boolean = ++requestCount % 5 == 0

    fun increaseSnackCount(snackId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.snack.id == snackId }.count
            updateSnackCount(snackId, currentCount + 1)
        } else {
            snackbarManager.showMessage(R.string.cart_increase_error)
        }
    }

    fun decreaseSnackCount(snackId: Long) {
        if (!shouldRandomlyFail()) {
            val currentCount = _orderLines.value.first { it.snack.id == snackId }.count
            if (currentCount == 1) {
                // remove snack from cart
                removeSnack(snackId)
            } else {
                // update quantity in cart
                updateSnackCount(snackId, currentCount - 1)
            }
        } else {
            snackbarManager.showMessage(R.string.cart_decrease_error)
        }
    }

    fun removeSnack(snackId: Long) {
        _orderLines.value = _orderLines.value.filter { it.snack.id != snackId }
    }

    private fun updateSnackCount(snackId: Long, count: Int) {
        _orderLines.value = _orderLines.value.map {
            if (it.snack.id == snackId) {
                it.copy(count = count)
            } else {
                it
            }
        }
    }

    /**
     * Factory for CartViewModel that takes SnackbarManager as a dependency
     */
    companion object {
        fun provideFactory(
            snackbarManager: SnackbarManager = SnackbarManager,
            snackRepository: SnackRepo = SnackRepo
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CartViewModel(snackbarManager, snackRepository) as T
            }
        }
    }
}
