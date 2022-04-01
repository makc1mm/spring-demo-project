package org.makc1mm.demo.model.entity

import javax.persistence.*

@Entity
@Table(name = "person")
data class PersonEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(unique = true)
    val username: String,
    val email: String,
    val phoneNumber: String,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person", cascade = [CascadeType.REMOVE])
    val products: List<ProductEntity> = emptyList()
)
