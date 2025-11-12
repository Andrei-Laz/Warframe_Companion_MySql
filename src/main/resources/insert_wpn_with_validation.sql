CREATE PROCEDURE sp_insert_wpn_with_validation(
    IN p_name VARCHAR(100),
    IN p_critical_chance INT,
    IN p_critical_damage DECIMAL(5,2),
    IN p_fire_rate DECIMAL(5,2),
    IN p_damage_falloff INT,
    IN p_damage INT
)
BEGIN
    DECLARE duplicate_count INT DEFAULT 0;

    -- Check if the weapon already exists by name
    SELECT COUNT(*) INTO duplicate_count 
    FROM Weapons 
    WHERE name = p_name;

    IF duplicate_count > 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Ya existe un arma con ese nombre.';
    ELSE
        INSERT INTO Weapons (name, critical_chance, critical_damage, fire_rate, damage_falloff, damage)
        VALUES (p_name, p_critical_chance, p_critical_damage, p_fire_rate, p_damage_falloff, p_damage);

        SELECT CONCAT('Arma "', p_name, '" insertada correctamente con ID = ', LAST_INSERT_ID()) AS message;
    END IF;
END