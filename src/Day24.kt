import java.io.File

fun main(args: Array<String>) {
    println("Day 24")
    val groups = arrayListOf<CombatGroup>()
    var team = ""
    var id = 0
    File("res/day24.txt").readLines().map {
        it.replace("(", "").replace(")", "").replace("to ", "")
                .replace(",", "").replace(";", "")
                .split(" ")
    }.forEach{
        if (it.size > 2) {
            val units = it[0].toInt()
            val hp = it[4].toInt()
            val weak = hashSetOf<String>()
            val immune = hashSetOf<String>()
            var i = 7
            while (i < it.size) {
                when (it[i]) {
                    "weak" -> inner@ for (j in i + 1..i + 5) {
                        when (it[j]) {
                            "immune", "with" -> break@inner
                            else -> weak.add(it[j])
                        }
                    }
                    "immune" -> inner@ for (j in i + 1..i + 5) {
                        when (it[j]) {
                            "weak", "with" -> break@inner
                            else -> immune.add(it[j])
                        }
                    }
                    "does" -> {
                        groups.add(CombatGroup(id++, team, units, hp, it[i + 6].toInt(), weak, it[i + 1].toInt(), it[i + 2], immune))
                    }
                }
                i++
            }
        }
        else if (it.isNotEmpty()) team = it[0]
    }

    val result = run(0, groups.map { it.copy() }.toMutableList()).sumBy { it.units }
    println("Part 1: $result")

    for (boost in 1..55) {
        val test = run(boost, groups.map { it.copy() }.toMutableList())
        if (test.isEmpty()) continue
        val count = test.filter{ it.team == "Immune" }.sumBy { it.units }
        val otherCount = test.filter{ it.team == "Infection:" }.sumBy { it.units }
        println("$boost: $count + $otherCount")
    }
}

fun run(boost: Int, groups: MutableList<CombatGroup>) : List<CombatGroup> {
    groups.filter { it.team == "Immune" }.forEach { it.dmg += boost }
    while (true) {
        val totalUnits = groups.sumBy { it.units }
        // determine selection order
        val selectionOrder = groups.sortedWith(Comparator { g1, g2 ->
            when {
                g1.dmg * g1.units < g2.dmg * g2.units -> 1
                g1.dmg * g1.units > g2.dmg * g2.units -> -1
                g1.dmg * g1.units == g2.dmg * g2.units && g1.init < g2.init -> 1
                g1.dmg * g1.units == g2.dmg * g2.units && g1.init > g2.init -> -1
                else -> 0
            }
        })

        // run selection
        val selection = hashMapOf<Int, CombatGroup>()
        val defendingGroups = groups.toMutableList() // each group can only be chosen once
        for (attacker in selectionOrder) {
            val oppTeams = defendingGroups.filter { it.team != attacker.team }
            var sel: CombatGroup? = null
            var currentDmg = 0
            for (oppTeam in oppTeams) {
                val dmg = getDamage(attacker, oppTeam)
                if (dmg > currentDmg) {
                    currentDmg = dmg
                    sel = oppTeam
                }
                else if (sel != null && dmg == currentDmg) {
                    if (oppTeam.units * oppTeam.dmg > sel.units * sel.dmg) {
                        sel = oppTeam
                    }
                    else if (oppTeam.units * oppTeam.dmg == sel.units * sel.dmg) {
                        if (oppTeam.init > sel.init) {
                            sel = oppTeam
                        }
                    }
                }
            }
            if (sel != null) {
                defendingGroups.remove(sel)
                selection[attacker.id] = sel
            }
        }

        // run attacking
        val attackingOrder = groups.sortedByDescending { it.init }
        for (attacker in attackingOrder) {
            val defender = selection[attacker.id] ?: continue
            if (attacker.units > 0 && defender.units > 0) {
                val dmg = getDamage(attacker, defender)
                defender.units -= (dmg / defender.hp)
                if (defender.units <= 0) groups.remove(defender)
                //println("${attacker.id} atk ${defender.id} for $dmg")
            }
        }

        val someTeam = groups.first().team
        if (groups.none { it.team != someTeam }) return groups
        if (totalUnits == groups.sumBy { it.units }) return listOf()
    }
}

fun getDamage(atk: CombatGroup, def: CombatGroup) : Int {
    return when {
        def.immunities.contains(atk.atkType) -> 0
        def.weakness.contains(atk.atkType) -> 2 * atk.dmg * atk.units
        else -> atk.dmg * atk.units
    }
}

data class CombatGroup (val id: Int, val team: String, var units: Int, val hp: Int, val init: Int,
                        val weakness: Set<String>, var dmg: Int, val atkType: String, val immunities: Set<String>)