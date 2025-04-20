package com.example.mobile_a1.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_a1.database.dao.OrderDao
import com.example.mobile_a1.database.dao.OrderLineDao
import com.example.mobile_a1.database.dao.OrderLineWithProduct
import com.example.mobile_a1.database.dao.SupermarketDao
import com.example.mobile_a1.database.entities.Order
import com.example.mobile_a1.database.entities.Supermarket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderDao: OrderDao,
    private val orderLineDao: OrderLineDao,
    private val supermarketDao: SupermarketDao,
) : ViewModel() {

    private val _order = MutableStateFlow<Order?>(null)
    val order: StateFlow<Order?> = _order

    private val _orderLinesWithProducts = MutableStateFlow<List<OrderLineWithProduct>>(emptyList())
    val orderLinesWithProducts: StateFlow<List<OrderLineWithProduct>> = _orderLinesWithProducts

    private val _supermarket = MutableStateFlow<Supermarket?>(null)
    val supermarket: StateFlow<Supermarket?> = _supermarket

    fun loadOrderData(orderId: Long) {
        viewModelScope.launch {
            orderDao.get(orderId).collectLatest { fetchedOrder ->
                _order.value = fetchedOrder

                fetchedOrder.supermarketId.let { supermarketId ->
                    supermarketDao.get(supermarketId).collectLatest { fetchedSupermarket ->
                        _supermarket.value = fetchedSupermarket
                    }
                }
            }
        }

        viewModelScope.launch {
            orderLineDao.getOrderLinesWithProductsForOrder(orderId).collectLatest {
                _orderLinesWithProducts.value = it
            }
        }
    }

    fun updateOrderLineCompletion(orderLineId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            val existingOrderLineWithProduct = _orderLinesWithProducts.value.find { it.orderLine.id == orderLineId }
            existingOrderLineWithProduct?.let { existing ->
                val updatedOrderLine = existing.orderLine.copy(completed = isCompleted)
                orderLineDao.update(updatedOrderLine)

                _orderLinesWithProducts.value = _orderLinesWithProducts.value.map { olwp ->
                    if (olwp.orderLine.id == orderLineId) {
                        olwp.copy(orderLine = updatedOrderLine)
                    } else {
                        olwp
                    }
                }
            }

            // Update the order status depending on the completion of all order lines
            _order.value?.let { order ->
                val updatedOrder = order.copy(completed = _orderLinesWithProducts.value.all { it.orderLine.completed })
                orderDao.update(updatedOrder)
                _order.value = updatedOrder
            }
        }
    }
}