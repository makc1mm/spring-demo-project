package org.makc1mm.demo.exception

import org.springframework.http.HttpStatus

class PersonNotFountException(username: String) : BaseException(
    httpStatus = HttpStatus.NOT_FOUND,
    description = "Person with username: $username not found"
)