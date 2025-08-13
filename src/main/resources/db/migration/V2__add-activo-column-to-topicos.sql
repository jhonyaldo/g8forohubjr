-- V2__add-activo-column-to-topicos.sql

ALTER TABLE topicos ADD COLUMN activo BOOLEAN;
UPDATE topicos SET activo = TRUE;
ALTER TABLE topicos ALTER COLUMN activo SET NOT NULL;