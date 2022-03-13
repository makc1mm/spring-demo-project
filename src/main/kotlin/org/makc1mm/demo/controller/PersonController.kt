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

    @GetMapping("/get/all")
    fun getAllPersons(): List<PersonResponse> {
        return personService.getAllPersons()
    }

    @GetMapping("/get/{username}")
    fun getPersonByUsername(@PathVariable username: String): PersonResponse {
        return personService.getPersonByUsername(username)
    }

    @PutMapping("/change/{username}")
    fun changePersonInfo(
        @PathVariable username: String,
        @RequestParam email: String,
        @RequestParam phoneNumber: String
    ): PersonResponse {
        return personService.changePersonInfo(username, email, phoneNumber)
    }

    @DeleteMapping("/delete/{username}")
    fun deletePersonByUserName(@PathVariable username: String) {
        personService.deletePersonByUsername(username)
    }
}