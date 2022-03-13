package org.makc1mm.demo.controller

import org.makc1mm.demo.model.PersonRequest
import org.makc1mm.demo.model.PersonResponse
import org.makc1mm.demo.service.PersonService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/person")
class PersonController(
    private val personService: PersonService
) {

    @PostMapping("/create")
    fun createPerson(@RequestBody person: PersonRequest) {
        personService.createPerson(person)
    }

    @GetMapping("/getAll")
    fun getAllPersons(): List<PersonResponse> {
        return personService.getAllPersons()
    }

    @GetMapping("/getByUsername")
    fun getPersonByUsername(@RequestParam username: String): PersonResponse {
        return personService.getPersonByUsername(username)
    }

    @PutMapping("/changeInfo")
    fun changePersonInfo(
        @RequestParam username: String,
        @RequestParam email: String,
        @RequestParam phoneNumber: String
    ): PersonResponse {
        return personService.changePersonInfo(username, email, phoneNumber)
    }

    @DeleteMapping("/deleteByUserName")
    fun deletePersonByUserName(@RequestParam username: String) {
        personService.deletePersonByUsername(username)
    }
}