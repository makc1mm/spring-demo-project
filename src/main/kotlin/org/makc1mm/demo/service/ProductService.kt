package org.makc1mm.demo.service

import org.makc1mm.demo.model.ProductRequest
import org.makc1mm.demo.model.ProductResponse

interface ProductService {
    fun createProduct(product: ProductRequest)
    fun changeProductInfo(id: Long, name: String, description: String, price: Int): ProductResponse
    fun getProducts(name: String): List<ProductResponse>
}