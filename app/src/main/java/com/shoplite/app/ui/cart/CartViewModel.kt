package com.shoplite.app.ui.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.shoplite.app.data.repository.CartRepository
import com.shoplite.app.utils.calc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
) : ViewModel() {

    var items by mutableStateOf(cartRepo.get())
    var total by mutableStateOf(calc(cartRepo.get()))

    // Removes an item from the cart
    fun delete(id: Int) {
        cartRepo.remove(id)
        items = cartRepo.get()
        total = calc(items)
    }
}
