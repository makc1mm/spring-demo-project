package org.makc1mm.demo.exception

import org.springframework.http.HttpStatus

abstract class BaseException(
    val httpStatus: HttpStatus,
    val description: String
): RuntimeException(description)