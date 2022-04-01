package org.makc1mm.demo.exception

import org.springframework.http.HttpStatus

class ProductNotFoundException(productName: String) : BaseException(
    httpStatus = HttpStatus.NOT_FOUND,
    description = "Product: '$productName' not found"
)