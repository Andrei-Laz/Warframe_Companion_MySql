data class Mod(
    val mod_id: Int? = null, //this is so because the id is automatically created by the database
    val name: String,
    val capacity_cost: Int,
    val polarity: String,
    val rarity: String,
    val description: String
)

object ModsDAO {
    fun listarMods(): List<Mod> {
        val lista = mutableListOf<Mod>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM Mods").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Mod(
                                mod_id = rs.getInt("mod_id"),
                                name = rs.getString("name"),
                                capacity_cost = rs.getInt("capacity_cost"),
                                polarity = rs.getString("polarity"),
                                rarity = rs.getString("rarity"),
                                description = rs.getString("description")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarModPorID(id: Int): Mod? {
        var mod: Mod? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM Mods WHERE mod_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        mod = Mod(
                            mod_id = rs.getInt("mod_id"),
                            name = rs.getString("name"),
                            capacity_cost = rs.getInt("capacity_cost"),
                            polarity = rs.getString("polarity"),
                            rarity = rs.getString("rarity"),
                            description = rs.getString("description")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return mod
    }

    fun insertarMod(mod: Mod) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO Mods(name, capacity_cost, polarity, rarity, description) VALUES (?, ?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, mod.name)
                pstmt.setInt(2,mod.capacity_cost)
                pstmt.setString(3, mod.polarity)
                pstmt.setString(4, mod.rarity)
                pstmt.setString(5, mod.description)
                pstmt.executeUpdate()
                println("Mod '${mod.name}' insertada con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarMod(mod: Mod) {
        if (mod.mod_id == null) {
            println("No se puede actualizar un Mod sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE Mods SET name = ?, capacity_cost = ?, polarity = ?, rarity = ?, description = ? WHERE mod_id = ?"
            ).use { pstmt ->
                pstmt.setString(1, mod.name)
                pstmt.setInt(2,mod.capacity_cost)
                pstmt.setString(3, mod.polarity)
                pstmt.setString(4, mod.rarity)
                pstmt.setString(5, mod.description)
                pstmt.setInt(6, mod.mod_id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Mod con id=${mod.mod_id} actualizada con éxito.")
                } else {
                    println("No se encontró ninguna mod con id=${mod.mod_id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarMod(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM Mods WHERE mod_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Mod con id=$id eliminada correctamente.")
                } else {
                    println("No se encontró ningún mod con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }
}