package org.makc1mm.demo.model

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val username: String
)