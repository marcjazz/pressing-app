-- Create default permission if not exist
INSERT INTO permissions(id, name, description) VALUES(DEFAULT, 'Create user', NULL) ON CONFLICT (name) DO UPDATE SET name='Create user';

-- Create default role if not exist
INSERT INTO roles(id, name, description) VALUES(DEFAULT, 'ROLE_ADMINISTRATION', NULL) ON CONFLICT (name) DO UPDATE SET name='ROLE_ADMINISTRATION';

-- Create default user if not exist
INSERT INTO "users"(id, first_name, last_name, username, password, telephone, is_active)
VALUES(DEFAULT, 'Administrator', 'Administrator', 'Admin', '$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW', '(+237) 677996655', true) 
ON CONFLICT (username) DO UPDATE SET password='$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW';

INSERT INTO user_roles (user_id, role_id)
SELECT
    (SELECT id FROM "users" WHERE username='Admin'),
    (SELECT id FROM roles WHERE name='ROLE_ADMINISTRATION')
WHERE NOT EXISTS (
    SELECT 1 FROM user_roles
    WHERE user_id = (SELECT id FROM "users" WHERE username='Admin')
    AND role_id = (SELECT id FROM roles WHERE name='ROLE_ADMINISTRATION')
);

INSERT INTO role_permissions (role_id, permission_id)
SELECT
    (SELECT id FROM roles WHERE name='ROLE_ADMINISTRATION'),
    (SELECT id FROM permissions WHERE name='Create user')
WHERE NOT EXISTS (
    SELECT 1 FROM role_permissions
    WHERE role_id = (SELECT id FROM roles WHERE name='ROLE_ADMINISTRATION')
    AND permission_id = (SELECT id FROM permissions WHERE name='Create user')
);
