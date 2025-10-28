import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Scanner

const val URL_BD = "jdbc:sqlite:src/main/resources/Warframe_Companion_DB.sqlite"

fun conectarBD(): Connection? {
    return try {
        DriverManager.getConnection(URL_BD)
    } catch (e: SQLException) {
        e.printStackTrace()
        null
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    var option: Int

    do {
        println(
            """
            
            ==== WARFRAME COMPANION MENU ====
            1. Warframe database menu
            2. Weapon database menu
            3. Mods database menu
            4. Something menu
            0. Salir
            =================================
            Elige una opción:
            """.trimIndent()
        )

        print("> ")
        option = scanner.nextLine().toIntOrNull() ?: -1

        when (option) {
            1 -> {
                warframeMenu()
            }

            2 -> {
                weaponMenu()
            }

            3 -> {
                modMenu()
            }

            4 -> {
            }

            0 -> println("Saliendo del menú...")

            else -> println("Opción inválida, intenta de nuevo.")
        }

    } while (option != 0)
}