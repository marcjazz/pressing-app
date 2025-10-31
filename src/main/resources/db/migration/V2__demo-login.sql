-- Create default permission if not exist
INSERT INTO permission(id, name, description) VALUES(DEFAULT, 'Create user', NULL) ON CONFLICT (name) DO UPDATE SET name='Create user';

-- Create default role if not exist
INSERT INTO role(id, name, description) VALUES(DEFAULT, 'ROLE_ADMINISTRATION', NULL) ON CONFLICT (name) DO UPDATE SET name='ROLE_ADMINISTRATION';

-- Create default user if not exist
INSERT INTO "user"(id, first_name, last_name, username, password, telephone, is_active) 
VALUES(DEFAULT, 'Administrator', 'Administrator', 'Admin', '$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW', '(+237) 677996655', true) 
ON CONFLICT (username) DO UPDATE SET password='$2a$10$OSVh6T1Ah20r7NT9bAU5Xug8gEWYMegyHBu2XBARfE1xKPWRc0qNW';

INSERT INTO user_role (user_id, role_id) 
SELECT 
    (SELECT id FROM "user" WHERE username='Admin'),
    (SELECT id FROM role WHERE name='ROLE_ADMINISTRATION')
WHERE NOT EXISTS (
    SELECT 1 FROM user_role 
    WHERE user_id = (SELECT id FROM "user" WHERE username='Admin') 
    AND role_id = (SELECT id FROM role WHERE name='ROLE_ADMINISTRATION')
);

INSERT INTO role_permission (role_id, permission_id) 
SELECT 
    (SELECT id FROM role WHERE name='ROLE_ADMINISTRATION'),
    (SELECT id FROM permission WHERE name='Create user')
WHERE NOT EXISTS (
    SELECT 1 FROM role_permission 
    WHERE role_id = (SELECT id FROM role WHERE name='ROLE_ADMINISTRATION') 
    AND permission_id = (SELECT id FROM permission WHERE name='Create user')
);
