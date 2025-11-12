CREATE PROCEDURE sp_get_wpn_full_details(
    IN p_weapon_id INT
)
BEGIN
    SELECT 
        w.weapon_id,
        w.name AS weapon_name,
        w.critical_chance,
        w.critical_damage,
        w.fire_rate,
        w.damage_falloff,
        w.damage,
        m.mod_id,
        m.name AS mod_name,
        m.capacity_cost,
        m.polarity,
        m.rarity,
        m.description
    FROM Weapons w
    LEFT JOIN Weapon_Mods wm ON w.weapon_id = wm.weapon_id
    LEFT JOIN Mods m ON wm.mod_id = m.mod_id
    WHERE w.weapon_id = p_weapon_id;
END