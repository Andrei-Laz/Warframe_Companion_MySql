DELIMITER //

DROP FUNCTION IF EXISTS fn_get_dmg_on_crit;
//

CREATE FUNCTION fn_get_dmg_on_crit(p_id_weapon INT)
	RETURNS DOUBLE
	DETERMINISTIC
BEGIN
	DECLARE crit_damage DOUBLE;
	DECLARE crit_multiplier DOUBLE;
	DECLARE base_dmg INT;

	SET crit_multiplier = (
		SELECT 1 + critical_chance * (critical_damage -1)
		FROM Weapons
		WHERE weapon_id = p_id_weapon);
	
	SET base_dmg = (
		SELECT damage
		FROM Weapons
		WHERE weapon_id = p_id_weapon);
	
	SET crit_damage = (base_dmg * crit_multiplier);
	
	RETURN crit_damage;

END;
//

DELIMITER ;