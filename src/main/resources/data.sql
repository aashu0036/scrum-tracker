SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO team (name, manager_id) VALUES
('Platform', 1),
('Sourcing', 2),
('Infrastructure', 3),
('DevOps', 4),
('Security', 5);


INSERT INTO user (first_name, last_name, username, password, role, team_id) VALUES
('Alice', 'Johnson', 'alice.johnson', 'pass', 'USER', 1),
('Bob', 'Williams', 'bob.williams', 'pass', 'USER', 2),
('Charlie', 'Davis', 'charlie.davis', 'pass', 'ADMIN', 1),
('David', 'Miller', 'david.miller', 'pass', 'USER', 3),
('Eva', 'Garcia', 'eva.garcia', 'pass', 'ADMIN', 2),
('Frank', 'Martinez', 'frank.martinez', 'pass', 'USER', 3),
('Grace', 'Rodriguez', 'grace.rodriguez', 'pass', 'ADMIN', 1),
('Henry', 'Lopez', 'henry.lopez', 'pass', 'USER', 2),
('Isabel', 'Gonzalez', 'isabel.gonzalez', 'pass', 'USER', 1),
('James', 'Wilson', 'james.wilson', 'pass', 'ADMIN', 3);