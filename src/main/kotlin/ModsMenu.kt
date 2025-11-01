import java.util.Scanner

fun modMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """

            ==== MOD DATABASE MENU ====
            1. Listar Mods
            2. Consultar Mod por ID
            3. Insertar nuevo Mod
            4. Actualizar Mod
            5. Eliminar Mod
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
                    println(
                        "[${it.mod_id}]\n\t" +
                                "Name: ${it.name}\n\t" +
                                "Capacity Cost: ${it.capacity_cost}\n\t" +
                                "Polarity: ${it.polarity}\n\t" +
                                "Rarity: ${it.rarity}\n\t" +
                                "Description: ${it.description}"
                    )
                }
            }

            2 -> {
                print("Ingrese el ID del Mod: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    val mod = ModsDAO.consultarModPorID(id)
                    if (mod != null) {
                        println(
                            """
                            === MOD ENCONTRADO ===
                            ID: ${mod.mod_id}
                            Name: ${mod.name}
                            Capacity Cost: ${mod.capacity_cost}
                            Polarity: ${mod.polarity}
                            Rarity: ${mod.rarity}
                            Description: ${mod.description}
                            """.trimIndent()
                        )
                    } else println("No se encontró ningún Mod con id=$id.")
                } else println("ID inválido.")
            }

            3 -> {
                println("=== Insertar nuevo Mod ===")
                print("Nombre: "); val name = scanner.nextLine()
                print("Capacity Cost: "); val cost = scanner.nextLine().toIntOrNull() ?: 0
                print("Polarity: "); val polarity = scanner.nextLine()
                print("Rarity: "); val rarity = scanner.nextLine()
                print("Description: "); val desc = scanner.nextLine()

                val nuevoMod = Mod(
                    mod_id = null,
                    name = name,
                    capacity_cost = cost,
                    polarity = polarity,
                    rarity = rarity,
                    description = desc
                )

                ModsDAO.insertarMod(nuevoMod)
            }

            4 -> {
                println("=== Actualizar Mod ===")
                print("Ingrese el ID del Mod: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    val existente = ModsDAO.consultarModPorID(id)
                    if (existente != null) {
                        print("Nuevo nombre (${existente.name}): ")
                        val name = scanner.nextLine().ifBlank { existente.name }
                        print("Capacity Cost (${existente.capacity_cost}): ")
                        val cost = scanner.nextLine().toIntOrNull() ?: existente.capacity_cost
                        print("Polarity (${existente.polarity}): ")
                        val polarity = scanner.nextLine().ifBlank { existente.polarity }
                        print("Rarity (${existente.rarity}): ")
                        val rarity = scanner.nextLine().ifBlank { existente.rarity }
                        print("Description (${existente.description}): ")
                        val desc = scanner.nextLine().ifBlank { existente.description }

                        val actualizado = existente.copy(
                            name = name,
                            capacity_cost = cost,
                            polarity = polarity,
                            rarity = rarity,
                            description = desc
                        )

                        ModsDAO.actualizarMod(actualizado)
                    } else println("No se encontró ningún Mod con id=$id.")
                } else println("ID inválido.")
            }

            5 -> {
                println("=== Eliminar Mod ===")
                print("Ingrese el ID del Mod: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) ModsDAO.eliminarMod(id)
                else println("ID inválido.")
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}
