--Vehicles:

DROP INDEX IF EXISTS idx_vehicles_last_technical_inspection;

--Technical issue:
DROP INDEX IF EXISTS idx_technical_issues_fk_vehicle;
DROP INDEX IF EXISTS idx_technical_issues_date;

--Fines:
DROP INDEX IF EXISTS idx_fines_fk_inspector;
DROP INDEX IF EXISTS idx_fines_fk_passenger;
DROP INDEX IF EXISTS idx_fines_date;

--Inspections:
DROP INDEX IF EXISTS idx_inspections_fk_ride;

--Tickets:
DROP INDEX IF EXISTS idx_tickets_fk_passenger;

--drivers license
DROP INDEX IF EXISTS idx_drivers_license_date;