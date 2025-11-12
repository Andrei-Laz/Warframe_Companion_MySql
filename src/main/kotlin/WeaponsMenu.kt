import java.util.Scanner

fun weaponMenu() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """
            
            ==== WEAPONS DATABASE MENU ====
            1. Listar armas
            2. Consultar arma por ID
            3. Insertar nueva arma
            4. Actualizar arma
            5. Eliminar arma
            6. Añadir mod a un arma
            7. Mostrar las mods de un arma
            8. Eliminar mod de un arma
            9. Calcular daño crítico de un arma
            10. Inserta nueva arma con validación
            11. Consulta información completa de un arma
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> {
                println("Lista de armas:")
                WeaponsDAO.listarArmas().forEach {
                    println("[ID: ${it.weapon_id}]\n\t" +
                            "Name: ${it.name}\n\t" +
                            "Critical Chance: ${it.critical_chance}\n\t" +
                            "Critical Damage: ${it.critical_damage}\n\t" +
                            "Fire Rate:: ${it.fire_rate}\n\t" +
                            "Damage Falloff: ${it.damage_falloff}\n\t" +
                            "Damage: ${it.damage}")
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
                print("Fire Rate: "); val fr = scanner.nextLine().toDoubleOrNull() ?: 1.0
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
                        val fr = scanner.nextLine().toDoubleOrNull() ?: existente.fire_rate
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

            6 -> {
                println("=== Asignar Mod a Arma ===")
                print("ID del arma: ")
                val wid = scanner.nextLine().toIntOrNull()
                print("ID del mod: ")
                val mid = scanner.nextLine().toIntOrNull()

                if (wid != null && mid != null)
                    WeaponModsDAO.asignarModAWeapon(wid, mid)
                else
                    println("ID inválido.")
            }

            7 -> {
                println("=== Listar Mods de un Arma ===")
                print("Ingrese el ID del arma: ")
                val wid = scanner.nextLine().toIntOrNull()
                if (wid != null) {
                    val mods = WeaponModsDAO.listarModsDeWeapon(wid)
                    if (mods.isEmpty()) {
                        println("Esta arma no tiene mods asignados.")
                    } else {
                        println("Mods del arma con id=$wid:")
                        mods.forEach {
                            println("[${it.mod_id}] ${it.name} | Cost: ${it.capacity_cost} | Polarity: ${it.polarity} | Rarity: ${it.rarity}")
                        }
                    }
                } else println("ID inválido.")
            }

            8 -> {
                println("=== Quitar Mod de un Arma ===")
                print("Ingrese el ID del arma: ")
                val wid = scanner.nextLine().toIntOrNull()
                print("Ingrese el ID del mod: ")
                val mid = scanner.nextLine().toIntOrNull()
                if (wid != null && mid != null) {
                    WeaponModsDAO.eliminarRelacion(wid, mid)
                }
                else {
                    println("IDs inválidos.")
                }
            }

            9 -> {
                println("=== Calcular el daño de un arma al aplicar los multiplicadores críticos")
                print("Ingrese el ID del arma: ")
                val id = scanner.nextLine().toIntOrNull()
                if (id != null) {
                    WeaponsDAO.llamar_fn_get_dmg_on_crit(id)
                }
                else {
                    println("ID inválido.")
                }

            }

            10 -> {
                println("=== Insertar nueva Arma con Validación ===")
                print("Nombre: "); val name = scanner.nextLine()
                print("Critical Chance (%): "); val cc = scanner.nextLine().toIntOrNull() ?: 0
                print("Critical Damage (x): "); val cd = scanner.nextLine().toDoubleOrNull() ?: 1.0
                print("Fire Rate: "); val fr = scanner.nextLine().toDoubleOrNull() ?: 1.0
                print("Damage Falloff: "); val df = scanner.nextLine().toIntOrNull() ?: 0
                print("Damage: "); val dmg = scanner.nextLine().toIntOrNull() ?: 0

                val nuevaArma = Weapon(null, name, cc, cd, fr, df, dmg)
                WeaponsDAO.llamar_sp_insert_wpn_with_validation(nuevaArma)
            }

            11 -> {
                println("=== Consultar toda la información de un arma ===")
                print("Ingrese el ID del arma: ")
                val id = scanner.nextLine().toIntOrNull()

                if (id != null) {
                    val detalles = WeaponModsDAO.llamar_sp_get_wpn_full_details(id)

                    if (detalles != null) {
                        println("\n=== DETALLES DEL ARMA ===")
                        println("ID: ${detalles.weapon.weapon_id}")
                        println("Name: ${detalles.weapon.name}")
                        println("Critical Chance: ${detalles.weapon.critical_chance}%")
                        println("Critical Damage: ${detalles.weapon.critical_damage}x")
                        println("Fire Rate: ${detalles.weapon.fire_rate}")
                        println("Damage Falloff: ${detalles.weapon.damage_falloff}")
                        println("Damage: ${detalles.weapon.damage}")

                        println("\n=== MODS INSTALADOS (${detalles.mods.size}) ===")
                        if (detalles.mods.isNotEmpty()) {
                            detalles.mods.forEach { mod ->
                                println(" - ${mod.name}")
                                println("   Cost: ${mod.capacity_cost} | Polarity: ${mod.polarity} | Rarity: ${mod.rarity}")
                                println("   Description: ${mod.description}")
                                println()
                            }
                        } else {
                            println(" - Esta arma no tiene mods asignados")
                        }
                    } else {
                        println("No se encontró el arma con ID: $id") // Cambié weaponId por id aquí también
                    }
                } else {
                    println("Error: ID inválido. Debe ser un número entero.")
                }
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}