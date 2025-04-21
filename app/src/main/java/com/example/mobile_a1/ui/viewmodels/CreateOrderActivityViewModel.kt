package com.example.mobile_a1.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.ProductDao
import com.example.mobile_a1.database.dao.SupermarketDao
import com.example.mobile_a1.database.entities.Order
import com.example.mobile_a1.database.entities.OrderLine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CreateOrderActivityViewModel @Inject constructor(
    supermarketDao: SupermarketDao,
    productDao: ProductDao,
    private val orderDao: OrderDao,
    private val orderLineDao: OrderLineDao
) : ViewModel() {

    val supermarkets = supermarketDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val products: StateFlow<List<com.example.mobile_a1.database.entities.Product>> = productDao.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun createOrder(supermarketId: Long, orderLinesWithQuantities: List<Pair<Long, Long>>, onOrderCreated: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val newOrder = Order(supermarketId = supermarketId, orderDate = Date(), completed = false)
            val orderId = orderDao.insert(newOrder)
            orderLinesWithQuantities.forEach { (productId, quantity) ->
                val orderLine = OrderLine(orderId = orderId, productId = productId, quantity = quantity, completed = false)
                orderLineDao.insert(orderLine)
            }
            onOrderCreated()
        }
    }
}