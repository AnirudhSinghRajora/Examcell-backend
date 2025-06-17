-- Dummy Data for ExamCell Result Generation Application

-- IMPORTANT: Ensure your database (examcell) is created and accessible.
-- For MariaDB/MySQL, UUIDs are typically stored as BINARY(16).
-- We use UUID_TO_BIN for insertions.

-- Clean up existing data to ensure a fresh start for each run
DELETE FROM marks_records;
DELETE FROM enrollments;
DELETE FROM professor_subject_assignments; -- For ManyToMany relationship
DELETE FROM subjects;
DELETE FROM students;
DELETE FROM professors;
DELETE FROM users;
DELETE FROM subject_branches; -- For ElementCollection


-- Insert Users (Professor and Student)
-- Using UUID_TO_BIN to convert UUID strings to binary for MySQL/MariaDB
INSERT INTO users (id, username, role) VALUES
                                           (UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445501'), 'prof.alice', 'PROFESSOR'),
                                           (UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445502'), 'student.bob', 'STUDENT');

-- Insert Professor
INSERT INTO professors (id, employee_id) VALUES
    (UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445501'), 'PROF123');

-- Insert Student
INSERT INTO students (id, roll_number, course, branch, batch_year) VALUES
    (UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445502'), 'LIT2024042', 'BTECH', 'CSE', 2023);

-- Insert Subjects
-- Ensure subject IDs are distinct
INSERT INTO subjects (id, code, name, course, semester, credits) VALUES
    (UNHEX(REPLACE('f0a1a2b3-c4d5-e6f7-890a-112233445503', '-', '')), 'CS301', 'Data Structures', 'BTECH', 3, 3.0),
    (UNHEX(REPLACE('f0a1a2b3-c4d5-e6f7-890a-112233445504', '-', '')), 'CS401', 'Operating Systems', 'BTECH', 4, 4.0);

-- Insert Subject Branches for CS301 (Data Structures)
INSERT INTO subject_branches (subject_id, branch) VALUES
    (UUID_TO_BIN('f0a1a2b3-c4d5-e6f7-890a-112233445503'), 'CSE');

-- Insert Subject Branches for CS401 (Operating Systems)
INSERT INTO subject_branches (subject_id, branch) VALUES
    (UUID_TO_BIN('f0a1a2b3-c4d5-e6f7-890a-112233445504'), 'CSE');

-- Assign CS301 to Professor PROF123
INSERT INTO professor_subject_assignments (professor_id, subject_id) VALUES
    (UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445501'), UUID_TO_BIN('f0a1a2b3-c4d5-e6f7-890a-112233445503'));

-- Enroll Student Bob in Data Structures (CS301)
INSERT INTO enrollments (id, student_id, subject_id, enrollment_date) VALUES
    (UUID_TO_BIN('d0a1a2b3-c4d5-e6f7-890a-112233445505'), UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445502'), UUID_TO_BIN('f0a1a2b3-c4d5-e6f7-890a-112233445503'), NOW());

-- Enter marks for Student Bob in Data Structures (CS301) by Professor Alice
INSERT INTO marks_records (id, student_id, subject_id, marks_obtained, max_marks, entered_by_professor_id, created_at, updated_at) VALUES
    (UUID_TO_BIN('e0a1a2b3-c4d5-e6f7-890a-112233445506'), UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445502'), UUID_TO_BIN('f0a1a2b3-c4d5-e6f7-890a-112233445503'), 85.0, 100.0, UUID_TO_BIN('b0a1a2b3-c4d5-e6f7-890a-112233445501'), NOW(), NOW());
