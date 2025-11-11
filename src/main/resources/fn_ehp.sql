DELIMITER //

DROP FUNCTION IF EXISTS fn_get_ehp;
-- Function that calculate effective hit points of a warframe
//

CREATE FUNCTION fn_get_ehp(p_id_warframe INT)
    RETURNS DOUBLE
    DETERMINISTIC
BEGIN
    DECLARE v_health DOUBLE;
    DECLARE v_armor DOUBLE;
    DECLARE v_shields DOUBLE;
    DECLARE ehp DOUBLE;

    -- Get the warframe stats
    SELECT health, armor, shields
    INTO v_health, v_armor, v_shields
    FROM Warframes
    WHERE warframe_id = p_id_warframe;

    
    SET ehp = (v_health * ((v_armor + 300) / 300)) + v_shields;

    RETURN ehp;
END;
//

DELIMITER ;
