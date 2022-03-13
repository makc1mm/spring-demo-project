package org.makc1mm.demo.model.entity

import javax.persistence.*

@Entity
@Table(name = "person")
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0,
    @Column(unique = true)
    val username: String,
    val email: String,
    val phoneNumber: String
)
