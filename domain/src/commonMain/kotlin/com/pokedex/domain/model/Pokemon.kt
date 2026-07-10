package com.pokedex.domain.model

/**
 * Domain model for a Pokemon within the list.
 * `imageUrl` is already resolved from the data layer (see id -> static sprite
 * mapping strategy); the domain does not know HOW it was built,
 * it only consumes it.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
)
