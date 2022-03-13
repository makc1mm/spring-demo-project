package org.makc1mm.demo.repository

import org.makc1mm.demo.model.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

interface PersonRepository : JpaRepository<PersonEntity, Long> {
    fun findByUsername(username: String): PersonEntity?

    @Transactional
    fun deleteByUsername(username: String)
}