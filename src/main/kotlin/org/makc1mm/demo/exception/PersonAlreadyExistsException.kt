package org.makc1mm.demo.exception

import org.springframework.http.HttpStatus

class PersonAlreadyExistsException(username: String) : BaseException(
    httpStatus = HttpStatus.BAD_REQUEST,
    description = "Person with username: '$username' already exists"
)