package com.petsvote.rating

import com.petsvote.api.entity.Pet
import kotlin.random.Random

object Randomizer {

    var listPhoto = listOf(
        R.drawable.cat_card, R.drawable.cat2, R.drawable.cat3,
        R.drawable.cat4, R.drawable.cat5, R.drawable.cat6
    )

    fun getRandomPhoto(): Int{
        var rand = Random.nextInt(0,5)
        return listPhoto[rand]
    }

    fun generatePet(): List<Pet>{
        var list = mutableListOf<Pet>()
        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Readson", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Brest", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Enstor", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Ivanovo", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Volly", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Minsk", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Tephokop", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Brest", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Gerrynisson", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Ivanovo", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Honey", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Minsk", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "QuatroXX", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Ivanovo", 123, 2313, 321313, listOf())
        )

        list.add(
            Pet("", 42342342, "", 423424, "", 3434, "Opplensyt", 1,
            "cat", "", "", 123213, 321321, 312, "Belarus",
            "Minsk", 123, 2313, 321313, listOf())
        )

        return list
    }
}