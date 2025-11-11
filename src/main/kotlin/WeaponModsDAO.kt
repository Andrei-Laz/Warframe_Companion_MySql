import java.sql.SQLException

data class WeaponMod(
    val weapon_mod_id: Int? = null,
    val weapon_id: Int,
    val mod_id: Int
)


object WeaponModsDAO {

    fun asignarModAWeapon(weapon_id: Int, mod_id: Int) {
        conectarBD()?.use { conn ->
            try {
                conn.autoCommit = false

                conn.prepareStatement("SELECT * FROM Weapons WHERE weapon_id = ?").use { pstmt ->
                    pstmt.setInt(1, weapon_id)
                    val rs = pstmt.executeQuery()
                    if (!rs.next()) {
                        throw SQLException("No existe un arma con id=$weapon_id")
                    }
                }

                conn.prepareStatement("SELECT * FROM Mods WHERE mod_id = ?").use { pstmt ->
                    pstmt.setInt(1, mod_id)
                    val rs = pstmt.executeQuery()
                    if (!rs.next()) {
                        throw SQLException("No existe un mod con id=$mod_id")
                    }
                }

                conn.prepareStatement(
                    "INSERT INTO Weapon_Mods(weapon_id, mod_id) VALUES (?, ?)"
                ).use { pstmt ->
                    pstmt.setInt(1, weapon_id)
                    pstmt.setInt(2, mod_id)
                    pstmt.executeUpdate()
                }

                conn.commit()
                println("Mod $mod_id asignado correctamente al arma $weapon_id.")

            } catch (e: SQLException) {
                println("Error en la transacción: ${e.message}")
                try {
                    conn.rollback()
                    println("Transacción revertida.")
                } catch (rollbackEx: SQLException) {
                    println("Error al hacer rollback: ${rollbackEx.message}")
                }
            } finally {
                conn.autoCommit = true
            }
        } ?: println("No se pudo establecer la conexión con la BD.")
    }

    fun listarModsDeWeapon(weapon_id: Int): List<Mod> {
        val lista = mutableListOf<Mod>()
        conectarBD()?.use { conn ->
            conn.prepareStatement("""
                SELECT m.* FROM Mods m
                INNER JOIN Weapon_Mods wm ON m.mod_id = wm.mod_id
                WHERE wm.weapon_id = ?
            """.trimIndent()).use { pstmt ->
                pstmt.setInt(1, weapon_id)
                val rs = pstmt.executeQuery()
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
        return lista
    }

    fun eliminarRelacion(weapon_id: Int, mod_id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM Weapon_Mods WHERE weapon_id = ? AND mod_id = ?").use { pstmt ->
                pstmt.setInt(1, weapon_id)
                pstmt.setInt(2, mod_id)
                val filas = pstmt.executeUpdate()
                if (filas > 0)
                    println("Relación entre mod $mod_id y arma $weapon_id eliminada.")
                else
                    println("No se encontró la relación para eliminar.")
            }
        }
    }
}
