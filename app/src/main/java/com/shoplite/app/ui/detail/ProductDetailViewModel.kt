package com.shoplite.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplite.app.data.repository.CartRepository
import com.shoplite.app.data.repository.ProductRepository
import com.shoplite.app.network.ApiService
import com.shoplite.app.network.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailState(
    val product: Product? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
)

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val api: ApiService,
    private val repo: ProductRepository,
    private val cartRepo: CartRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state

    // Loads the product for the screen
    fun getIt(id: Int) {
        if (_state.value.product?.id == id) return
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            try {
                val p = api.getProduct(id)
                _state.value = _state.value.copy(product = p, loading = false, error = null)
            } catch (e: Exception) {
                val cached = repo.getOne(id)
                _state.value = _state.value.copy(
                    product = cached,
                    loading = false,
                    error = if (cached == null) e.message else null
                )
            }
        }
    }

    // Adds the current product to the cart
    fun add() {
        val p = _state.value.product ?: return
        viewModelScope.launch {
            try {
                val ok = cartRepo.process(p)
                _state.value = _state.value.copy(
                    message = if (ok) "Added to cart" else "Could not add to cart"
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(message = "Could not add to cart")
            }
        }
    }

    fun messageShown() {
        _state.value = _state.value.copy(message = null)
    }
}
