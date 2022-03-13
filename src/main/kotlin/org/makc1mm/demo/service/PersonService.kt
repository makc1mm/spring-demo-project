package org.makc1mm.demo.service

import org.makc1mm.demo.model.PersonRequest
import org.makc1mm.demo.model.PersonResponse

interface PersonService {
    fun createPerson(person: PersonRequest)
    fun getAllPersons(): List<PersonResponse>
    fun getPersonByUsername(username: String): PersonResponse
    fun changePersonInfo(username: String, email: String, phoneNumber: String): PersonResponse
    fun deletePersonByUsername(username: String)
}