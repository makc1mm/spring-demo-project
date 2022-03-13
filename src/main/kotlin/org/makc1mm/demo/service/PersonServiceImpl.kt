package org.makc1mm.demo.service

import org.makc1mm.demo.exception.PersonNotFountException
import org.makc1mm.demo.model.PersonRequest
import org.makc1mm.demo.model.PersonResponse
import org.makc1mm.demo.model.entity.PersonEntity
import org.makc1mm.demo.repository.PersonRepository
import org.springframework.stereotype.Service

@Service
class PersonServiceImpl(
    private val personRepository: PersonRepository
) : PersonService {

    /**
     * TODO: добавить проверку на наличие юзера с таким же именем
     */
    override fun createPerson(person: PersonRequest) {
        personRepository.save(person.toEntity())
    }

    override fun getAllPersons(): List<PersonResponse> {
        return personRepository.findAll().map { it.toResponse() }
    }

    override fun getPersonByUsername(username: String): PersonResponse {
        return personRepository.findByUsername(username)?.toResponse() ?: throw PersonNotFountException(username)
    }

    override fun changePersonInfo(username: String, email: String, phoneNumber: String): PersonResponse {
        val person = personRepository.findByUsername(username) ?: throw PersonNotFountException(username)
        val newPersonInfo = person.copy(
            email = email,
            phoneNumber = phoneNumber
        )
        personRepository.save(newPersonInfo)
        return newPersonInfo.toResponse()
    }

    override fun deletePersonByUsername(username: String) {
        personRepository.deleteByUsername(username)
    }

    private fun PersonRequest.toEntity() = PersonEntity(
        username = username,
        email = email,
        phoneNumber = phoneNumber
    )

    private fun PersonEntity.toResponse() = PersonResponse(
        username = username,
        email = email,
        phoneNumber = phoneNumber
    )

}