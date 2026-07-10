package com.pokedex.app.navigation

import kotlinx.serialization.Serializable

/**
 * Rutas de navegación tipadas (Navigation Compose Multiplatform con
 * kotlinx.serialization). Solo se pasa el `pokemonId` primitivo al
 * detalle, nunca un objeto de dominio completo: la pantalla de detalle
 * es responsable de pedir sus propios datos, manteniendo cada feature
 * independiente y lista para, por ejemplo, un deep link directo.
 */
sealed interface Destination {
    @Serializable
    data object PokemonList : Destination

    @Serializable
    data class PokemonDetail(val pokemonId: Int) : Destination
}
