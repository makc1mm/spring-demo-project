package org.makc1mm.demo.repository

import org.makc1mm.demo.model.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<PersonEntity, Long> {
    fun findByUsername(username: String): PersonEntity?
    fun deleteByUsername(username: String)
}