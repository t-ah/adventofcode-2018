fun main(args: Array<String>) {
    println("Day 14")
    val numberOfRecipes = 540561
    val numberStr = numberOfRecipes.toString()
    val recipes = arrayListOf(3, 7)
    val elves = arrayOf(0, 1)
    var last = "37"

    inf@while (true) {
        val newRecipe = recipes[elves[0]] + recipes[elves[1]]
        for (char in newRecipe.toString()) {
            recipes.add(char.toString().toInt())
            last += char
            if (recipes.size >= numberStr.length) {
                if (last == numberStr) {
                    break@inf
                }
                last = last.substring(1)
            }
        }
        for (i in 0 until 2) elves[i] = (elves[i] + 1 + recipes[elves[i]]) % recipes.size
    }

    println("Part 1: " + recipes.subList(numberOfRecipes, numberOfRecipes + 10).joinToString(""))
    println("Part 2: ${recipes.size - numberStr.length}")
}