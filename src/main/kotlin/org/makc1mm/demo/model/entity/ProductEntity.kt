package org.makc1mm.demo.model.entity

import javax.persistence.*

@Entity
@Table(name = "product")
data class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Int,
    @ManyToOne
    @JoinColumn(name = "person_id")
    val person: PersonEntity
)