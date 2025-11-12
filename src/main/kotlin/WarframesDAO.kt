data class Warframe(
    val warframe_id: Int? = null, //this is so because the id is automatically created by the database
    val name: String,
    val health: Int,
    val armor: Int,
    val energy: Int,
    val sprint_speed: Double,
    val passive: String
)

object WarframesDAO {
    fun listarWarframes(): List<Warframe> {
        val lista = mutableListOf<Warframe>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM Warframes").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Warframe(
                                warframe_id = rs.getInt("warframe_id"),
                                name = rs.getString("name"),
                                health = rs.getInt("health"),
                                armor = rs.getInt("armor"),
                                energy = rs.getInt("energy"),
                                sprint_speed = rs.getDouble("sprint_speed"),
                                passive = rs.getString("passive")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarWarframePorID(id: Int): Warframe? {
        var warframe: Warframe? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM Warframes WHERE warframe_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        warframe = Warframe(
                            warframe_id = rs.getInt("warframe_id"),
                            name = rs.getString("name"),
                            health = rs.getInt("health"),
                            armor = rs.getInt("armor"),
                            energy = rs.getInt("energy"),
                            sprint_speed = rs.getDouble("sprint_speed"),
                            passive = rs.getString("passive")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return warframe
    }

    fun insertarWarframe(warframe: Warframe) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO Warframes(name, health, armor, energy, sprint_speed, passive) VALUES (?, ?, ?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, warframe.name)
                pstmt.setInt(2,warframe.health)
                pstmt.setInt(3, warframe.armor)
                pstmt.setInt(4, warframe.energy)
                pstmt.setDouble(5, warframe.sprint_speed)
                pstmt.setString(6, warframe.passive)
                pstmt.executeUpdate()
                println("Warframe '${warframe.name}' insertado con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarWarframe(warframe: Warframe) {
        if (warframe.warframe_id == null) {
            println("No se puede actualizar un Warframe sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE Warframes SET name = ?, health = ?, armor = ?, energy = ?, sprint_speed = ?, passive = ? WHERE warframe_id = ?"
            ).use { pstmt ->
                pstmt.setString(1, warframe.name)
                pstmt.setInt(2,warframe.health)
                pstmt.setInt(3, warframe.armor)
                pstmt.setInt(4, warframe.energy)
                pstmt.setDouble(5, warframe.sprint_speed)
                pstmt.setString(6, warframe.passive)
                pstmt.setInt(7, warframe.warframe_id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Warframe con id=${warframe.warframe_id} actualizado con éxito.")
                } else {
                    println("No se encontró ningun Warframe con id=${warframe.warframe_id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarWarframe(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM Warframes WHERE warframe_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Warframe con id=$id eliminado correctamente.")
                } else {
                    println("No se encontró ningún Warframe con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun llamar_fn_get_ehp(id: Int) {
        conectarBD()?.use { conn ->
            val sql = "SELECT fn_get_ehp(?)"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        val resultado = rs.getInt(1)
                        println("El total de EHP del Warframe seleccionado es: $resultado")
                    }
                }
            }
        }
    }
}