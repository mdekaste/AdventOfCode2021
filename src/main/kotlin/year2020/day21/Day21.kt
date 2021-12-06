package year2020.day21

import Challenge

fun main() {
    Day21.printSolutions()
}

object Day21 : Challenge("""--- Day 21: Allergen Assessment ---""") {
    val parsed = input.lines().map {
        it.substringBefore(" (").split(" ") to
            it.substringAfter("(contains ").substringBefore(')').split(", ")
    }
    val allergenPossibilities = parsed
        .flatMap { (ingredients, allergens) -> allergens.map { it to ingredients.toSet() } }
        .groupingBy { it.first }
        .aggregate { _, accumulator: MutableList<Set<String>>?, element, _ ->
            when (accumulator) {
                null -> mutableListOf(element.second)
                else -> accumulator.apply { add(element.second) }
            }
        }

    val allIngredients = allergenPossibilities.values.flatten().flatten().toSet()

    override fun part1(): Any? {
        val overlapAllergens = allergenPossibilities.mapValues { (_, foods) -> foods.reduce { acc, list -> acc.intersect(list) } }
        val notAllergens = allIngredients.filter { ingredient -> !overlapAllergens.values.any { it.contains(ingredient) } }
        val sumOfNonAllergenIngredients = notAllergens.sumOf { notAllergen -> parsed.count { it.first.contains(notAllergen) } }
        return sumOfNonAllergenIngredients
    }

    override fun part2(): Any? {
        var overlapAllergens = allergenPossibilities.mapValues { (_, foods) -> foods.reduce { acc, list -> acc.intersect(list) } }.toMutableMap()
        val resultMap = sortedMapOf<String, String>()
        while(overlapAllergens.isNotEmpty()){
            val inbetweenMap = mutableMapOf<String, String>()
            for((allergen, ingredients) in overlapAllergens){
                if(ingredients.size == 1){
                    inbetweenMap[allergen] = ingredients.first()
                }
            }
            for(ingredient in inbetweenMap.values){
                overlapAllergens = overlapAllergens.mapValues { (key, value) -> value - ingredient }.toMutableMap()
                overlapAllergens.entries.removeIf { (key, value) -> value.isEmpty() }
            }
            resultMap.putAll(inbetweenMap)
        }
        return resultMap.values.joinToString(separator = ",")
    }
}
