fun main(args: Array<String>) {
    println("Day 14")
    val numberOfRecipes = 540561
    val numberStr = numberOfRecipes.toString()
    val recipes = mutableListOf(3, 7)

    var elfA = 0
    var elfB = 1

    while (true) {
        val newRecipe = recipes[elfA] + recipes[elfB]
        for (char in newRecipe.toString()) recipes.add(char.toString().toInt())
        //if (recipes.size >= numberOfRecipes + 10) break
        if (recipes.size > 6) {
            if (recipes.subList(recipes.size - numberStr.length, recipes.size).joinToString("") == numberStr) {
                println("Part 2: ${recipes.size - numberStr.length}")
                break
            } else if (recipes.subList(recipes.size - (numberStr.length + 1), recipes.size - 1).joinToString("") == numberStr) {
                println("Part 2: ${recipes.size - (numberStr.length + 1)}")
                break
            }
        }
        elfA = (elfA + 1 + recipes[elfA]) % recipes.size
        elfB = (elfB + 1 + recipes[elfB]) % recipes.size
    }

    println("Part 1: " + recipes.subList(numberOfRecipes, numberOfRecipes + 10).joinToString(""))
}