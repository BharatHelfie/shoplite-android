package com.shoplite.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoplite.app.data.repository.AuthRepository
import com.shoplite.app.data.repository.CartRepository
import com.shoplite.app.data.repository.ProductRepository
import com.shoplite.app.network.ApiService
import com.shoplite.app.network.model.Product
import com.shoplite.app.utils.fix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val products: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val query: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val userName: String? = null,
    val userImage: String? = null,
    val cartCount: Int = 0,
    val page: Int = 0,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: ProductRepository,
    private val api: ApiService,
    private val cartRepo: CartRepository,
    private val authRepo: AuthRepository,
) : ViewModel() {

    val state = MutableStateFlow(HomeState())

    init {
        doIt()
        getData2()
        callApi()
        state.value = state.value.copy(cartCount = cartRepo.size())
    }

    // Loads the first page of products
    fun doIt() {
        viewModelScope.launch {
            state.value = state.value.copy(loading = true, error = null)
            val list = repo.doIt(0)
            state.value = state.value.copy(products = fix(list), loading = false, page = 0)
        }
    }

    // Loads the next page of products
    fun more() {
        viewModelScope.launch {
            val next = state.value.page + 1
            val list = repo.doIt(next)
            state.value = state.value.copy(
                products = state.value.products + fix(list),
                page = next
            )
        }
    }

    // Searches products by the text the user typed
    fun handle(q: String) {
        state.value = state.value.copy(query = q)
        viewModelScope.launch {
            try {
                if (q.isEmpty()) {
                    doIt()
                    return@launch
                }
                val res = api.search(q)
                state.value = state.value.copy(products = res.products, error = null)
            } catch (e: Exception) {
                state.value = state.value.copy(error = e.message)
            }
        }
    }

    // Gets the list of categories
    fun getData2() {
        viewModelScope.launch {
            try {
                state.value = state.value.copy(categories = api.getCategories())
            } catch (e: Exception) {
            }
        }
    }

    // Shows only products from the selected category
    fun select(c: String?) {
        state.value = state.value.copy(selectedCategory = c)
        viewModelScope.launch {
            if (c == null) {
                doIt()
                return@launch
            }
            val res = api.getByCategory(c)
            state.value = state.value.copy(products = res.body()!!.products)
        }
    }

    // Fetches the logged in user's profile
    fun callApi() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val me = api.me()
                state.value = state.value.copy(userName = me.firstName, userImage = me.image)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "callApi failed", e)
            }
        }
    }

    // Logs the user out
    fun out() {
        authRepo.clear()
    }
}
