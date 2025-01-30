--Vehicles:
CREATE INDEX idx_vehicles_last_technical_inspection ON vehicles(last_technical_inspection);

--Technical issue:
CREATE INDEX idx_technical_issues_fk_vehicle ON technical_issues(fk_vehicle);
CREATE INDEX idx_unresolved_technical_issues
ON technical_issues (status)
WHERE status != 'Resolved';

--Fines:
CREATE INDEX idx_fines_fk_inspector ON fines(fk_inspector);
CREATE INDEX idx_fines_fk_passenger ON fines(fk_passenger);
CREATE INDEX idx_fines_status_deadline
ON fines (status, deadline)
WHERE status != 'Paid';


--drivers license
CREATE INDEX idx_drivers_license_date ON drivers_licenses(expires_on);