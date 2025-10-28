import java.util.Scanner

fun modMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """

            ==== MOD DATABASE MENU ====
            1. Listar mods
            2. Consultar mod por ID
            3. Insertar nuevo mod
            4. Actualizar mod
            5. Eliminar mod
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> {
                println("Lista de Mods:")
                ModsDAO.listarMods().forEach {
                    println("[${it.mod_id}]\n\t" +
                            "Name: ${it.name}\n\t" +
                            "Capacity Cost: ${it.capacity_cost}\n\t" +
                            "Polarity: ${it.polarity}\n\t" +
                            "Rarity: ${it.rarity}\n\t" +
                            "Description: ${it.description}")
                }
            }

            2 -> {
                print("Ingrese el ID del arma: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    val arma = WeaponsDAO.consultarArmaPorID(id)
                    if (arma != null) {
                        println(
                            """
                            === ARMA ENCONTRADA ===
                            ID: ${arma.weapon_id}
                            Name: ${arma.name}
                            Crit Chance: ${arma.critical_chance}%
                            Crit Damage: ${arma.critical_damage}x
                            Fire Rate: ${arma.fire_rate}
                            Damage Falloff: ${arma.damage_falloff}
                            Damage: ${arma.damage}
                            """.trimIndent()
                        )
                    } else println("No se encontró ninguna arma con id=$id.")
                } else println("ID inválido.")
            }

            3 -> {
                println("=== Insertar nueva Arma ===")
                print("Nombre: "); val name = scanner.nextLine()
                print("Critical Chance (%): "); val cc = scanner.nextLine().toIntOrNull() ?: 0
                print("Critical Damage (x): "); val cd = scanner.nextLine().toDoubleOrNull() ?: 1.0
                print("Fire Rate: "); val fr = scanner.nextLine().toIntOrNull() ?: 1
                print("Damage Falloff: "); val df = scanner.nextLine().toIntOrNull() ?: 0
                print("Damage: "); val dmg = scanner.nextLine().toIntOrNull() ?: 0

                val nuevaArma = Weapon(null, name, cc, cd, fr, df, dmg)
                WeaponsDAO.insertarArma(nuevaArma)
            }

            4 -> {
                println("=== Actualizar Arma ===")
                print("Ingrese el ID del arma: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    val existente = WeaponsDAO.consultarArmaPorID(id)
                    if (existente != null) {
                        print("Nuevo nombre (${existente.name}): ")
                        val name = scanner.nextLine().ifBlank { existente.name }
                        print("Crit Chance (${existente.critical_chance}): ")
                        val cc = scanner.nextLine().toIntOrNull() ?: existente.critical_chance
                        print("Crit Damage (${existente.critical_damage}): ")
                        val cd = scanner.nextLine().toDoubleOrNull() ?: existente.critical_damage
                        print("Fire Rate (${existente.fire_rate}): ")
                        val fr = scanner.nextLine().toIntOrNull() ?: existente.fire_rate
                        print("Damage Falloff (${existente.damage_falloff}): ")
                        val df = scanner.nextLine().toIntOrNull() ?: existente.damage_falloff
                        print("Damage (${existente.damage}): ")
                        val dmg = scanner.nextLine().toIntOrNull() ?: existente.damage

                        val actualizada = existente.copy(
                            name = name,
                            critical_chance = cc,
                            critical_damage = cd,
                            fire_rate = fr,
                            damage_falloff = df,
                            damage = dmg
                        )

                        WeaponsDAO.actualizarArma(actualizada)
                    } else println("No se encontró ninguna arma con id=$id.")
                } else println("ID inválido.")
            }

            5 -> {
                println("=== Eliminar Arma ===")
                print("Ingrese el ID del arma: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) WeaponsDAO.eliminarArma(id)
                else println("ID inválido.")
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}