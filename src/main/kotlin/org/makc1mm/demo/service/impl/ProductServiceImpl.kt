package org.makc1mm.demo.service.impl

import org.makc1mm.demo.exception.PersonNotFountException
import org.makc1mm.demo.exception.ProductNotFoundException
import org.makc1mm.demo.model.ProductRequest
import org.makc1mm.demo.model.ProductResponse
import org.makc1mm.demo.model.entity.ProductEntity
import org.makc1mm.demo.repository.PersonRepository
import org.makc1mm.demo.repository.ProductRepository
import org.makc1mm.demo.service.ProductService
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val personRepository: PersonRepository
) : ProductService {

    override fun createProduct(product: ProductRequest) {
        productRepository.save(product.toEntity(product.username))
    }

    override fun getProducts(name: String): List<ProductResponse> {
        return productRepository.findProductByName(name).map { it.toResponse() }
    }

    override fun changeProductInfo(id: Long, name: String, description: String, price: Int): ProductResponse {
        val newProduct = productRepository.findProductById(id) ?: throw ProductNotFoundException(name)
        productRepository.save(
            newProduct.copy(
                name = name,
                description = description,
                price = price
            )
        )
        return newProduct.toResponse()
    }

    private fun ProductRequest.toEntity(username: String): ProductEntity {
        val person = personRepository.findByUsername(username) ?: throw PersonNotFountException(username)
        return ProductEntity(
            name = name,
            description = description,
            price = price,
            person = person
        )
    }

    private fun ProductEntity.toResponse() = ProductResponse(
        id = id,
        name = name,
        description = description,
        price = price,
        username = this.person.username
    )
}