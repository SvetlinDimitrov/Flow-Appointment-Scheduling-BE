-- Retrieve user IDs
SET @employee_id = (SELECT id FROM users WHERE email = 'staff1@abv.bg');
SET @client_id = (SELECT id FROM users WHERE email = 'client1@abv.bg');

-- Retrieve service ID
SET @service_id = (SELECT id FROM services WHERE name = 'Fitness');

-- Insert 10 appointments
INSERT INTO appointments (client_id, staff_id, start_date, end_date, status, service_id) VALUES
(@client_id, @employee_id, '2023-10-01 09:00:00', '2023-10-01 10:00:00', 'CANCELED', @service_id),
(@client_id, @employee_id, '2023-10-01 10:00:00', '2023-10-01 11:00:00', 'CANCELED', @service_id),
(@client_id, @employee_id, '2023-10-01 11:00:00', '2023-10-01 12:00:00', 'CANCELED', @service_id),
(@client_id, @employee_id, '2023-10-01 12:00:00', '2023-10-01 13:00:00', 'CANCELED', @service_id),
(@client_id, @employee_id, '2023-10-01 13:00:00', '2023-10-01 14:00:00', 'APPROVED', @service_id),
(@client_id, @employee_id, '2023-10-01 14:00:00', '2023-10-01 15:00:00', 'APPROVED', @service_id),
(@client_id, @employee_id, '2023-10-01 15:00:00', '2023-10-01 16:00:00', 'NOT_APPROVED', @service_id),
(@client_id, @employee_id, '2023-10-01 16:00:00', '2023-10-01 17:00:00', 'NOT_APPROVED', @service_id),
(@client_id, @employee_id, '2023-10-01 18:00:00', '2023-10-01 19:00:00', 'COMPLETED', @service_id);