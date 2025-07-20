DELETE FROM event;

-- No H2 não existe foreign_key_checks, então omitimos

ALTER TABLE event ALTER COLUMN id RESTART WITH 1;

INSERT INTO event (title, description, event_time, event_local, soft_delete) VALUES (
'Palestra sobre Spring Boot',
'Uma palestra voltada para iniciantes em Spring Boot.',
PARSEDATETIME('25-07-2025 19:00', 'dd-MM-yyyy HH:mm'),
'Auditório Principal - SP',
FALSE
);
