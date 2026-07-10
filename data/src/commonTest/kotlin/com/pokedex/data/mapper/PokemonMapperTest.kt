package com.pokedex.data.mapper

import com.pokedex.data.remote.dto.PokemonListItemDto
import kotlin.test.Test
import kotlin.test.assertEquals

class PokemonMapperTest {

    @Test
    fun `extractIdFromUrl obtiene el id correcto de una url estandar`() {
        assertEquals(1, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/1/"))
        assertEquals(25, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/25/"))
    }

    @Test
    fun `extractIdFromUrl funciona sin slash final`() {
        assertEquals(150, extractIdFromUrl("https://pokeapi.co/api/v2/pokemon/150"))
    }

    @Test
    fun `spriteUrl construye la url estatica esperada del repositorio de sprites`() {
        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            spriteUrl(1),
        )
    }

    @Test
    fun `toDomain de un item de listado resuelve nombre e imagen sin llamadas extra`() {
        val dto = PokemonListItemDto(name = "bulbasaur", url = "https://pokeapi.co/api/v2/pokemon/1/")

        val domain = dto.toDomain()

        assertEquals(1, domain.id)
        assertEquals("bulbasaur", domain.name)
        assertEquals(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            domain.imageUrl,
        )
    }
}
