DELETE FROM event;

SET foreign_key_checks = 1;

ALTER TABLE event AUTO_INCREMENT = 1;

INSERT INTO event (title, description, event_time, event_local, soft_delete) VALUES (
    'Palestra sobre Spring Boot',
    'Uma palestra voltada para iniciantes em Spring Boot.',
    STR_TO_DATE('25-07-2025 19:00', '%d-%m-%Y %H:%i'),
    'Auditório Principal - SP',
    FALSE
);
