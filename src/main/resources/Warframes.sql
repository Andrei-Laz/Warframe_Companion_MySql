CREATE TABLE Warframes (
    warframe_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50),
    health INTEGER,
    armor INTEGER,
    energy INTEGER,
    sprint_speed NUMERIC,
    passive VARCHAR(200)
);