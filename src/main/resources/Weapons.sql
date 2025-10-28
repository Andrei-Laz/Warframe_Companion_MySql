create table Weapons (
weapon_id integer primary key autoincrement,
name varchar(100),
critical_chance integer,
critical_damage numeric,
fire_rate numeric,
damage_falloff integer,
damage integer)