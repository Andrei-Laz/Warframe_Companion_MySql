import java.util.Scanner

fun warframeMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """

            ==== WARFRAME DATABASE MENU ====
            1. Listar Warframes
            2. Consultar Warframe por ID
            3. Insertar nuevo Warframe
            4. Actualizar Warframe
            5. Eliminar Warframe
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> {
                println("Lista de Warframes:")
                WarframesDAO.listarWarframes().forEach {
                    println("[${it.warframe_id}]\n\t" +
                            "Name: ${it.name}\n\t" +
                            "Health: ${it.health}\n\t" +
                            "Armor: ${it.armor}\n\t" +
                            "Energy: ${it.energy}\n\t" +
                            "Sprint Speed: ${it.sprint_speed}\n\t" +
                            "Passive: ${it.passive}")
                }
            }

            2 -> {
                print("Ingresa el ID del Warframe: ")
                val input = scanner.nextLine()
                val id = input.toIntOrNull()
                if (id != null) {
                    val warframe = WarframesDAO.consultarWarframePorID(id)
                    if (warframe != null) {
                        println(
                            """
                            === WARFRAME ENCONTRADO ===
                            ID: ${warframe.warframe_id}
                            Name: ${warframe.name}
                            Health: ${warframe.health}
                            Armor: ${warframe.armor}
                            Energy: ${warframe.energy}
                            Sprint Speed: ${warframe.sprint_speed}
                            Passive: ${warframe.passive}
                            """.trimIndent()
                        )
                    } else {
                        println("No se encontró ningún Warframe con id=$id.")
                    }
                } else {
                    println("ID inválido.")
                }
            }

            3 -> {
                println("=== Insertar nuevo Warframe ===")
                print("Nombre: ")
                val name = scanner.nextLine()
                print("Health: ")
                val health = scanner.nextLine().toIntOrNull() ?: 0
                print("Armor: ")
                val armor = scanner.nextLine().toIntOrNull() ?: 0
                print("Energy: ")
                val energy = scanner.nextLine().toIntOrNull() ?: 0
                print("Sprint Speed: ")
                val sprint = scanner.nextLine().toDoubleOrNull() ?: 1.0
                print("Passive: ")
                val passive = scanner.nextLine()

                val nuevoWarframe = Warframe(
                    warframe_id = null,
                    name = name,
                    health = health,
                    armor = armor,
                    energy = energy,
                    sprint_speed = sprint,
                    passive = passive
                )

                WarframesDAO.insertarWarframe(nuevoWarframe)
            }

            4 -> {
                println("=== Actualizar Warframe ===")
                print("Ingrese el ID del Warframe a actualizar: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    val currentStats = WarframesDAO.consultarWarframePorID(id)
                    if (currentStats != null) {
                        val id = currentStats.warframe_id
                        println("ID warframe: ${currentStats.warframe_id}")
                        print("Nuevo nombre (${currentStats.name}): ")
                        val name = scanner.nextLine().ifBlank { currentStats.name }
                        print("Health (${currentStats.health}): ")
                        val health = scanner.nextLine().toIntOrNull() ?: currentStats.health
                        print("Armor (${currentStats.armor}): ")
                        val armor = scanner.nextLine().toIntOrNull() ?: currentStats.armor
                        print("Energy (${currentStats.energy}): ")
                        val energy = scanner.nextLine().toIntOrNull() ?: currentStats.energy
                        print("Sprint Speed (${currentStats.sprint_speed}): ")
                        val sprint = scanner.nextLine().toDoubleOrNull() ?: currentStats.sprint_speed
                        print("Passive (${currentStats.passive}): ")
                        val passive = scanner.nextLine().ifBlank { currentStats.passive }

                        val actualizado = currentStats.copy(
                            name = name,
                            health = health,
                            armor = armor,
                            energy = energy,
                            sprint_speed = sprint,
                            passive = passive
                        )

                        WarframesDAO.actualizarWarframe(actualizado)
                    } else {
                        println("No se encontró ningún Warframe con id=$id.")
                    }
                } else {
                    println("ID inválido.")
                }
            }

            5 -> {
                println("=== Eliminar Warframe ===")
                print("Ingrese el ID del Warframe a eliminar: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    WarframesDAO.eliminarWarframe(id)
                } else {
                    println("ID inválido.")
                }
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}