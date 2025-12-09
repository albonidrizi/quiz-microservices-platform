-- Seed Data for Question Service

INSERT INTO question (question_title, option1, option2, option3, option4, right_answer, difficultylevel, category) VALUES
-- Java Questions
('What is Java?', 'OS', 'Programming Language', 'Food', 'Car', 'Programming Language', 'Easy', 'Java'),
('Which keyword is used to define a class in Java?', 'class', 'struct', 'define', 'object', 'class', 'Easy', 'Java'),
('What is the size of int in Java?', '16 bit', '32 bit', '64 bit', '8 bit', '32 bit', 'Easy', 'Java'),
('Which collection stores mapped key-value pairs?', 'List', 'Set', 'Map', 'Queue', 'Map', 'Medium', 'Java'),
('What is the parent class of all classes in Java?', 'Object', 'Main', 'Super', 'Root', 'Object', 'Medium', 'Java'),

-- Python Questions
('What is the output of print(2 ** 3)?', '6', '8', '9', '5', '8', 'Easy', 'Python'),
('Which keyword is used to define a function in Python?', 'func', 'def', 'function', 'define', 'def', 'Easy', 'Python'),
('What data type is [1, 2, 3]?', 'List', 'Tuple', 'Set', 'Dictionary', 'List', 'Easy', 'Python'),
('How do you start a comment in Python?', '//', '#', '/*', '<!--', '#', 'Easy', 'Python'),
('Which of these is immutable?', 'List', 'Set', 'Dictionary', 'Tuple', 'Tuple', 'Medium', 'Python'),

-- JavaScript Questions
('Which keyword is used to declare a variable in ES6?', 'var', 'let', 'dim', 'int', 'let', 'Easy', 'JavaScript'),
('What does DOM stand for?', 'Data Object Model', 'Document Object Model', 'Digital Ordinance Model', 'Desktop Orientation Module', 'Document Object Model', 'Easy', 'JavaScript'),
('Which symbol is used for comments in JavaScript?', '#', '//', '<!--', '**', '//', 'Easy', 'JavaScript'),
('What is the output of "5" + 2?', '7', '52', 'Error', 'NaN', '52', 'Medium', 'JavaScript'),
('Which function is used to parse a string to an integer?', 'Integer.parse()', 'startInt()', 'parseInt()', 'toInteger()', 'parseInt()', 'Medium', 'JavaScript'),

-- Docker Questions
('What is Docker?', 'OS', 'Container Platform', 'Programming Language', 'Database', 'Container Platform', 'Easy', 'Docker'),
('Which command lists running containers?', 'docker list', 'docker ps', 'docker run', 'docker info', 'docker ps', 'Easy', 'Docker'),
('What is the file used to build a Docker image?', 'Dockerbuild', 'Dockerfile', 'docker.yaml', 'image.json', 'Dockerfile', 'Easy', 'Docker'),
('Which command pulls an image from a registry?', 'docker get', 'docker fetch', 'docker pull', 'docker load', 'docker pull', 'Easy', 'Docker'),
('What is a lightweight, standalone, executable package of software?', 'VM', 'Container', 'Pod', 'Cluster', 'Container', 'Medium', 'Docker');
