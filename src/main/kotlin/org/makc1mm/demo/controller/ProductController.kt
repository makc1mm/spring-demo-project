package org.makc1mm.demo.controller

import org.makc1mm.demo.model.ProductRequest
import org.makc1mm.demo.model.ProductResponse
import org.makc1mm.demo.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/product")
class ProductController(private val productService: ProductService) {

    @PostMapping("/create")
    fun createProduct(@RequestBody product: ProductRequest) {
        productService.createProduct(product)
    }

    @PutMapping("/change/{id}")
    fun changeProductInfo(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam description: String,
        @RequestParam price: Int
    ): ProductResponse {
        return productService.changeProductInfo(id, name, description, price)
    }

    @GetMapping("/get/{name}")
    fun getProducts(@PathVariable name: String): List<ProductResponse> {
        return productService.getProducts(name)
    }

}