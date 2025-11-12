data class Weapon(
    val weapon_id: Int? = null,
    val name: String,
    val critical_chance: Int,
    val critical_damage: Double,
    val fire_rate: Int,
    val damage_falloff: Int,
    val damage: Int
)

object WeaponsDAO {
    fun listarArmas(): List<Weapon> {
        val lista = mutableListOf<Weapon>()
        conectarBD()?.use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeQuery("SELECT * FROM Weapons").use { rs ->
                    while (rs.next()) {
                        lista.add(
                            Weapon(
                                weapon_id = rs.getInt("weapon_id"),
                                name = rs.getString("name"),
                                critical_chance = rs.getInt("critical_chance"),
                                critical_damage = rs.getDouble("critical_damage"),
                                fire_rate = rs.getInt("fire_rate"),
                                damage_falloff = rs.getInt("damage_falloff"),
                                damage = rs.getInt("damage")
                            )
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return lista
    }

    fun consultarArmaPorID(id: Int): Weapon? {
        var weapon: Weapon? = null
        conectarBD()?.use { conn ->
            conn.prepareStatement("SELECT * FROM Weapons WHERE weapon_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                pstmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        weapon = Weapon(
                            weapon_id = rs.getInt("weapon_id"),
                            name = rs.getString("name"),
                            critical_chance = rs.getInt("critical_chance"),
                            critical_damage = rs.getDouble("critical_damage"),
                            fire_rate = rs.getInt("fire_rate"),
                            damage_falloff = rs.getInt("damage_falloff"),
                            damage = rs.getInt("damage")
                        )
                    }
                }
            }
        } ?: println("No se pudo establecer la conexión.")
        return weapon
    }

    fun insertarArma(weapon: Weapon) {
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "INSERT INTO Weapons(name, critical_chance, critical_damage, fire_rate, damage_falloff, damage) VALUES (?, ?, ?, ?, ?, ?)"
            ).use { pstmt ->
                pstmt.setString(1, weapon.name)
                pstmt.setInt(2,weapon.critical_chance)
                pstmt.setDouble(3, weapon.critical_damage)
                pstmt.setInt(4, weapon.fire_rate)
                pstmt.setInt(5, weapon.damage_falloff)
                pstmt.setInt(6, weapon.damage)
                pstmt.executeUpdate()
                println("Arma '${weapon.name}' insertada con éxito.")
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun actualizarArma(weapon: Weapon) {
        if (weapon.weapon_id == null) {
            println("No se puede actualizar un arma sin id.")
            return
        }
        conectarBD()?.use { conn ->
            conn.prepareStatement(
                "UPDATE Weapons SET name = ?, critical_chance = ?, critical_damage = ?, fire_rate = ?, damage_falloff = ?, damage = ? WHERE weapon_id = ?"
            ).use { pstmt ->
                pstmt.setString(1, weapon.name)
                pstmt.setInt(2,weapon.critical_chance)
                pstmt.setDouble(3, weapon.critical_damage)
                pstmt.setInt(4, weapon.fire_rate)
                pstmt.setInt(5, weapon.damage_falloff)
                pstmt.setInt(6, weapon.damage)
                pstmt.setInt(7, weapon.weapon_id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Arma  con id=${weapon.weapon_id} actualizado con éxito.")
                } else {
                    println("No se encontró ningun arma con id=${weapon.weapon_id}.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun eliminarArma(id: Int) {
        conectarBD()?.use { conn ->
            conn.prepareStatement("DELETE FROM Weapons WHERE weapon_id = ?").use { pstmt ->
                pstmt.setInt(1, id)
                val filas = pstmt.executeUpdate()
                if (filas > 0) {
                    println("Arma con id=$id eliminado correctamente.")
                } else {
                    println("No se encontró ningún arma con id=$id.")
                }
            }
        } ?: println("No se pudo establecer la conexión.")
    }

    fun llamar_fn_get_dmg_on_crit(id: Int) {
        conectarBD()?.use { conn ->
            val sql = "SELECT fn_get_dmg_on_crit(?)"
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                stmt.executeQuery().use { rs ->
                    if (rs.next()) {
                        val resultado = rs.getInt(1)
                        println("El daño real del arma aplicando los multiplicadores críticos es: $resultado")
                    }
                }
            }
        }
    }
}