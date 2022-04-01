package org.makc1mm.demo.repository

import org.makc1mm.demo.model.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findProductById(id: Long): ProductEntity?
    fun findProductByName(name: String): List<ProductEntity>
}