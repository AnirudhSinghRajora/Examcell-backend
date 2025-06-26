# API Documentation

This document provides a comprehensive overview of the RESTful APIs exposed by the Spring Boot Admin Backend application.

---

## AuthController (`/api/auth`)

### 1. Register a new user
- **Endpoint**: `/api/auth/signup`
- **Method**: `POST`
- **Purpose**: Registers a new user in the system.
- **Request Body**: `SignupRequest`
  ```json
  {
    "email": "user@example.com",
    "password": "yourpassword",
    "firstName": "John",
    "lastName": "Doe",
    "role": "STUDENT"
  }
  ```
- **Response**: `AuthResponse`
  ```json
  {
    "token": "jwt_token_string",
    "id": 1,
    "email": "user@example.com",
    "role": "STUDENT",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully registered the user.
  - `400 Bad Request`: Invalid request body or user already exists.

### 2. User Login
- **Endpoint**: `/api/auth/login`
- **Method**: `POST`
- **Purpose**: Authenticates a user and returns a JWT token.
- **Request Body**: `AuthRequest`
  ```json
  {
    "email": "user@example.com",
    "password": "yourpassword"
  }
  ```
- **Response**: `AuthResponse`
  ```json
  {
    "token": "jwt_token_string",
    "id": 1,
    "email": "user@example.com",
    "role": "STUDENT",
    "firstName": "John",
    "lastName": "Doe"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully logged in.
  - `401 Unauthorized`: Invalid credentials.

---

## TeacherController (`/api/teachers`)

### 1. Get All Teachers
- **Endpoint**: `/api/teachers`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of all teachers. Supports searching by name and filtering by department.
- **Parameters**:
  - `search` (Optional): Search term for teacher's name.
  - `department` (Optional): Filter teachers by department.
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria (e.g., `firstName,asc`).
- **Response**: `Page<TeacherDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "firstName": "Jane",
        "lastName": "Doe",
        "employeeId": "EMP001",
        "department": "Computer Science",
        "email": "jane.doe@example.com"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved teachers.

### 2. Get Teacher by ID
- **Endpoint**: `/api/teachers/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single teacher by their ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the teacher.
- **Response**: `TeacherDTO`
  ```json
  {
    "id": 1,
    "firstName": "Jane",
    "lastName": "Doe",
    "employeeId": "EMP001",
    "department": "Computer Science",
    "email": "jane.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the teacher.
  - `404 Not Found`: Teacher with the given ID does not exist.

### 3. Get Teacher by Employee ID
- **Endpoint**: `/api/teachers/employee/{employeeId}`
- **Method**: `GET`
- **Purpose**: Retrieves a single teacher by their employee ID.
- **Parameters**:
  - `employeeId` (Path Variable): The employee ID of the teacher.
- **Response**: `TeacherDTO`
  ```json
  {
    "id": 1,
    "firstName": "Jane",
    "lastName": "Doe",
    "employeeId": "EMP001",
    "department": "Computer Science",
    "email": "jane.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the teacher.
  - `404 Not Found`: Teacher with the given employee ID does not exist.

### 4. Create a New Teacher
- **Endpoint**: `/api/teachers`
- **Method**: `POST`
- **Purpose**: Creates a new teacher record.
- **Request Body**: `TeacherDTO`
  ```json
  {
    "firstName": "Alice",
    "lastName": "Smith",
    "employeeId": "EMP002",
    "department": "Mathematics",
    "email": "alice.smith@example.com"
  }
  ```
- **Response**: `TeacherDTO`
  ```json
  {
    "id": 2,
    "firstName": "Alice",
    "lastName": "Smith",
    "employeeId": "EMP002",
    "department": "Mathematics",
    "email": "alice.smith@example.com"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the teacher.
  - `400 Bad Request`: Invalid request body.

### 5. Update an Existing Teacher
- **Endpoint**: `/api/teachers/{id}`
- **Method**: `PUT`
- **Purpose**: Updates an existing teacher record.
- **Parameters**:
  - `id` (Path Variable): The ID of the teacher to update.
- **Request Body**: `TeacherDTO`
  ```json
  {
    "firstName": "Jane",
    "lastName": "Doe-Updated",
    "employeeId": "EMP001",
    "department": "Computer Science",
    "email": "jane.doe@example.com"
  }
  ```
- **Response**: `TeacherDTO`
  ```json
  {
    "id": 1,
    "firstName": "Jane",
    "lastName": "Doe-Updated",
    "employeeId": "EMP001",
    "department": "Computer Science",
    "email": "jane.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully updated the teacher.
  - `400 Bad Request`: Invalid request body.
  - `404 Not Found`: Teacher with the given ID does not exist.

### 6. Delete a Teacher
- **Endpoint**: `/api/teachers/{id}`
- **Method**: `DELETE`
- **Purpose**: Deletes a teacher record by their ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the teacher to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the teacher.
  - `404 Not Found`: Teacher with the given ID does not exist.

---

## TeacherPortalController (`/api/teacher`)

### 1. Get Teacher Dashboard
- **Endpoint**: `/api/teacher/dashboard/{teacherId}`
- **Method**: `GET`
- **Purpose**: Retrieves dashboard statistics for a specific teacher.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
- **Response**: `TeacherDashboardDTO`
  ```json
  {
    "teacherName": "John Doe",
    "totalSubjects": 5,
    "totalStudents": 150,
    "pendingQueries": 3,
    "recentMarksUploads": [
      {
        "subjectName": "Mathematics",
        "semester": "Fall 2023",
        "uploadDate": "2023-11-15"
      }
    ]
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved dashboard.
  - `404 Not Found`: Teacher with the given ID does not exist.

### 2. Get Teacher Queries
- **Endpoint**: `/api/teacher/{teacherId}/queries`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of queries for a specific teacher.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
  - `status` (Optional): Filter queries by status (e.g., "OPEN", "RESOLVED").
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<QueryDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "subject": "Mathematics",
        "queryText": "Doubt about calculus.",
        "response": null,
        "status": "OPEN",
        "createdAt": "2023-10-20T10:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved queries.
  - `404 Not Found`: Teacher with the given ID does not exist.

### 3. Respond to a Query
- **Endpoint**: `/api/teacher/{teacherId}/queries/{queryId}/respond`
- **Method**: `POST`
- **Purpose**: Allows a teacher to respond to a specific query.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
  - `queryId` (Path Variable): The ID of the query to respond to.
  - `response` (Request Parameter): The response text.
- **Response**: `QueryDTO` (Updated Query)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Mathematics",
    "queryText": "Doubt about calculus.",
    "response": "Here's the explanation...",
    "status": "RESOLVED",
    "createdAt": "2023-10-20T10:00:00Z"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully responded to the query.
  - `404 Not Found`: Query or Teacher with the given ID does not exist.

### 4. Submit Query to Admin
- **Endpoint**: `/api/teacher/{teacherId}/queries/admin`
- **Method**: `POST`
- **Purpose**: Allows a teacher to submit a new query to an admin.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher submitting the query.
- **Request Body**: `QueryDTO` (partial, only queryText and subject needed)
  ```json
  {
    "subject": "System Issue",
    "queryText": "I am unable to upload marks for Subject X."
  }
  ```
- **Response**: `QueryDTO` (Created Query)
  ```json
  {
    "id": 2,
    "teacherId": 1,
    "teacherName": "John Doe",
    "subject": "System Issue",
    "queryText": "I am unable to upload marks for Subject X.",
    "response": null,
    "status": "OPEN",
    "createdAt": "2023-11-20T14:30:00Z"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the query.
  - `400 Bad Request`: Invalid request body.

### 5. Get Subject Marks for Teacher
- **Endpoint**: `/api/teacher/{teacherId}/subjects/{subjectId}/marks`
- **Method**: `GET`
- **Purpose**: Retrieves all marks for a specific subject taught by a given teacher.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
  - `subjectId` (Path Variable): The ID of the subject.
- **Response**: `List<MarkDTO>`
  ```json
  [
    {
      "id": 1,
      "studentId": 101,
      "studentName": "Student A",
      "subjectId": 201,
      "subjectName": "Mathematics",
      "semester": "Fall 2023",
      "internal1": 25,
      "internal2": 28,
      "external": 70,
      "total": 123
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved marks.
  - `404 Not Found`: Teacher or Subject with the given ID does not exist.

### 6. Create Mark
- **Endpoint**: `/api/teacher/{teacherId}/marks`
- **Method**: `POST`
- **Purpose**: Creates a new mark record for a student under a specific teacher.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher creating the mark.
- **Request Body**: `MarkDTO`
  ```json
  {
    "studentId": 102,
    "subjectId": 201,
    "semester": "Fall 2023",
    "internal1": 20,
    "internal2": 22,
    "external": 65
  }
  ```
- **Response**: `MarkDTO` (Created Mark)
  ```json
  {
    "id": 2,
    "studentId": 102,
    "studentName": "Student B",
    "subjectId": 201,
    "subjectName": "Mathematics",
    "semester": "Fall 2023",
    "internal1": 20,
    "internal2": 22,
    "external": 65,
    "total": 107
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the mark.
  - `400 Bad Request`: Invalid request body.

### 7. Update Mark
- **Endpoint**: `/api/teacher/{teacherId}/marks/{markId}`
- **Method**: `PUT`
- **Purpose**: Updates an existing mark record.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
  - `markId` (Path Variable): The ID of the mark to update.
- **Request Body**: `MarkDTO`
  ```json
  {
    "internal1": 25,
    "internal2": 28,
    "external": 75
  }
  ```
- **Response**: `MarkDTO` (Updated Mark)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subjectId": 201,
    "subjectName": "Mathematics",
    "semester": "Fall 2023",
    "internal1": 25,
    "internal2": 28,
    "external": 75,
    "total": 128
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully updated the mark.
  - `400 Bad Request`: Invalid request body.
  - `404 Not Found`: Mark with the given ID does not exist.

### 8. Delete Mark
- **Endpoint**: `/api/teacher/{teacherId}/marks/{markId}`
- **Method**: `DELETE`
- **Purpose**: Deletes a mark record.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher.
  - `markId` (Path Variable): The ID of the mark to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the mark.
  - `404 Not Found`: Mark with the given ID does not exist.

### 9. Upload Marks from Excel
- **Endpoint**: `/api/teacher/{teacherId}/marks/upload`
- **Method**: `POST`
- **Purpose**: Uploads marks for a subject and semester from an Excel file.
- **Parameters**:
  - `teacherId` (Path Variable): The ID of the teacher uploading marks.
  - `file` (Request Parameter): The Excel file containing marks.
  - `subjectId` (Request Parameter): The ID of the subject.
  - `semester` (Request Parameter): The semester (e.g., "Fall 2023").
- **Request Body**: `multipart/form-data` with `file`, `subjectId`, and `semester` parts.
- **Response**: `List<MarkDTO>` (List of uploaded marks)
  ```json
  [
    {
      "id": 3,
      "studentId": 103,
      "studentName": "Student C",
      "subjectId": 202,
      "subjectName": "Physics",
      "semester": "Fall 2023",
      "internal1": 20,
      "internal2": 25,
      "external": 60,
      "total": 105
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully uploaded marks.
  - `400 Bad Request`: Invalid file or parameters.
  - `500 Internal Server Error`: Error during file processing.

---

## StudentPortalController (`/api/student`)

### 1. Get Student Dashboard
- **Endpoint**: `/api/student/dashboard/{studentId}`
- **Method**: `GET`
- **Purpose**: Retrieves dashboard statistics for a specific student.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student.
- **Response**: `StudentDashboardDTO`
  ```json
  {
    "studentName": "Student A",
    "rollNo": "STU001",
    "department": "Computer Science",
    "semester": "Fall 2023",
    "totalSubjectsRegistered": 5,
    "averageGPA": 3.5,
    "pendingBonafideRequests": 1,
    "unresolvedQueries": 2
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved dashboard.
  - `404 Not Found`: Student with the given ID does not exist.

### 2. Get Student Results
- **Endpoint**: `/api/student/{studentId}/results`
- **Method**: `GET`
- **Purpose**: Retrieves all mark results for a specific student.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student.
- **Response**: `List<MarkDTO>`
  ```json
  [
    {
      "id": 1,
      "studentId": 101,
      "studentName": "Student A",
      "subjectId": 201,
      "subjectName": "Mathematics",
      "semester": "Fall 2023",
      "internal1": 25,
      "internal2": 28,
      "external": 70,
      "total": 123
    },
    {
      "id": 2,
      "studentId": 101,
      "studentName": "Student A",
      "subjectId": 202,
      "subjectName": "Physics",
      "semester": "Fall 2023",
      "internal1": 22,
      "internal2": 25,
      "external": 65,
      "total": 112
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved results.
  - `404 Not Found`: Student with the given ID does not exist.

### 3. Get Student Queries
- **Endpoint**: `/api/student/{studentId}/queries`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of queries submitted by a specific student.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student.
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<QueryDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "subject": "Exam Schedule",
        "queryText": "When is the final exam for Math?",
        "response": "Check the notice board.",
        "status": "RESOLVED",
        "createdAt": "2023-10-25T09:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved queries.
  - `404 Not Found`: Student with the given ID does not exist.

### 4. Submit a Query
- **Endpoint**: `/api/student/{studentId}/queries`
- **Method**: `POST`
- **Purpose**: Allows a student to submit a new query.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student submitting the query.
- **Request Body**: `QueryDTO` (partial, only subject and queryText needed)
  ```json
  {
    "subject": "Grade Discrepancy",
    "queryText": "My physics grade seems incorrect."
  }
  ```
- **Response**: `QueryDTO` (Created Query)
  ```json
  {
    "id": 2,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Grade Discrepancy",
    "queryText": "My physics grade seems incorrect.",
    "response": null,
    "status": "OPEN",
    "createdAt": "2023-11-20T15:00:00Z"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the query.
  - `400 Bad Request`: Invalid request body.

### 5. Get Student Bonafide Requests
- **Endpoint**: `/api/student/{studentId}/bonafide-requests`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of bonafide requests submitted by a specific student.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student.
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<BonafideRequestDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "purpose": "Visa Application",
        "status": "PENDING",
        "createdAt": "2023-11-10T11:00:00Z",
        "approvedBy": null,
        "rejectionReason": null
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved requests.
  - `404 Not Found`: Student with the given ID does not exist.

### 6. Submit a Bonafide Request
- **Endpoint**: `/api/student/{studentId}/bonafide-requests`
- **Method**: `POST`
- **Purpose**: Allows a student to submit a new bonafide request.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student submitting the request.
- **Request Body**: `BonafideRequestDTO` (partial, only purpose needed)
  ```json
  {
    "purpose": "Bank Loan"
  }
  ```
- **Response**: `BonafideRequestDTO` (Created Request)
  ```json
  {
    "id": 2,
    "studentId": 101,
    "studentName": "Student A",
    "purpose": "Bank Loan",
    "status": "PENDING",
    "createdAt": "2023-11-20T15:30:00Z",
    "approvedBy": null,
    "rejectionReason": null
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the request.
  - `400 Bad Request`: Invalid request body.

### 7. Get All Subjects
- **Endpoint**: `/api/student/subjects`
- **Method**: `GET`
- **Purpose**: Retrieves a list of all available subjects.
- **Response**: `List<SubjectDTO>`
  ```json
  [
    {
      "id": 1,
      "name": "Mathematics",
      "code": "MA101",
      "semester": "Fall 2023"
    },
    {
      "id": 2,
      "name": "Physics",
      "code": "PH101",
      "semester": "Fall 2023"
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved subjects.

---

## SubjectController (`/api/subjects`)

### 1. Get All Active Subjects
- **Endpoint**: `/api/subjects`
- **Method**: `GET`
- **Purpose**: Retrieves a list of all active subjects.
- **Response**: `List<Subject>`
  ```json
  [
    {
      "id": 1,
      "name": "Mathematics",
      "code": "MA101",
      "semester": "Fall 2023",
      "active": true
    },
    {
      "id": 2,
      "name": "Physics",
      "code": "PH101",
      "semester": "Fall 2023",
      "active": true
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved subjects.

### 2. Get Subject by ID
- **Endpoint**: `/api/subjects/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single subject by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the subject.
- **Response**: `Subject`
  ```json
  {
    "id": 1,
    "name": "Mathematics",
    "code": "MA101",
    "semester": "Fall 2023",
    "active": true
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the subject.
  - `404 Not Found`: Subject with the given ID does not exist.

### 3. Get Subjects by Semester
- **Endpoint**: `/api/subjects/semester/{semester}`
- **Method**: `GET`
- **Purpose**: Retrieves a list of subjects offered in a specific semester.
- **Parameters**:
  - `semester` (Path Variable): The semester (e.g., "Fall 2023").
- **Response**: `List<Subject>`
  ```json
  [
    {
      "id": 1,
      "name": "Mathematics",
      "code": "MA101",
      "semester": "Fall 2023",
      "active": true
    },
    {
      "id": 2,
      "name": "Physics",
      "code": "PH101",
      "semester": "Fall 2023",
      "active": true
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved subjects.
  - `404 Not Found`: No subjects found for the given semester.

---

## StudentController (`/api/students`)

### 1. Get All Students
- **Endpoint**: `/api/students`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of all students. Supports searching by name, filtering by semester and department.
- **Parameters**:
  - `search` (Optional): Search term for student's name.
  - `semester` (Optional): Filter students by semester.
  - `department` (Optional): Filter students by department.
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria (e.g., `firstName,asc`).
- **Response**: `Page<StudentDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "rollNo": "STU001",
        "semester": "Fall 2023",
        "department": "Computer Science",
        "email": "john.doe@example.com"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved students.

### 2. Get Student by ID
- **Endpoint**: `/api/students/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single student by their ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the student.
- **Response**: `StudentDTO`
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "rollNo": "STU001",
    "semester": "Fall 2023",
    "department": "Computer Science",
    "email": "john.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the student.
  - `404 Not Found`: Student with the given ID does not exist.

### 3. Get Student by Roll Number
- **Endpoint**: `/api/students/roll/{rollNo}`
- **Method**: `GET`
- **Purpose**: Retrieves a single student by their roll number.
- **Parameters**:
  - `rollNo` (Path Variable): The roll number of the student.
- **Response**: `StudentDTO`
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "rollNo": "STU001",
    "semester": "Fall 2023",
    "department": "Computer Science",
    "email": "john.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the student.
  - `404 Not Found`: Student with the given roll number does not exist.

### 4. Create a New Student
- **Endpoint**: `/api/students`
- **Method**: `POST`
- **Purpose**: Creates a new student record.
- **Request Body**: `StudentDTO`
  ```json
  {
    "firstName": "Emily",
    "lastName": "Brown",
    "rollNo": "STU002",
    "semester": "Spring 2024",
    "department": "Electrical Engineering",
    "email": "emily.brown@example.com"
  }
  ```
- **Response**: `StudentDTO`
  ```json
  {
    "id": 2,
    "firstName": "Emily",
    "lastName": "Brown",
    "rollNo": "STU002",
    "semester": "Spring 2024",
    "department": "Electrical Engineering",
    "email": "emily.brown@example.com"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the student.
  - `400 Bad Request`: Invalid request body.

### 5. Update an Existing Student
- **Endpoint**: `/api/students/{id}`
- **Method**: `PUT`
- **Purpose**: Updates an existing student record.
- **Parameters**:
  - `id` (Path Variable): The ID of the student to update.
- **Request Body**: `StudentDTO`
  ```json
  {
    "firstName": "John",
    "lastName": "Doe-Updated",
    "rollNo": "STU001",
    "semester": "Fall 2023",
    "department": "Computer Science",
    "email": "john.doe@example.com"
  }
  ```
- **Response**: `StudentDTO`
  ```json
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe-Updated",
    "rollNo": "STU001",
    "semester": "Fall 2023",
    "department": "Computer Science",
    "email": "john.doe@example.com"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully updated the student.
  - `400 Bad Request`: Invalid request body.
  - `404 Not Found`: Student with the given ID does not exist.

### 6. Delete a Student
- **Endpoint**: `/api/students/{id}`
- **Method**: `DELETE`
- **Purpose**: Deletes a student record by their ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the student to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the student.
  - `404 Not Found`: Student with the given ID does not exist.

---

## QueryController (`/api/queries`)

### 1. Get All Queries
- **Endpoint**: `/api/queries`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of all queries. Supports searching and filtering by status.
- **Parameters**:
  - `search` (Optional): Search term for query text or subject.
  - `status` (Optional): Filter queries by status (e.g., "OPEN", "RESOLVED", "CLOSED").
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<QueryDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "subject": "Mathematics",
        "queryText": "Doubt about calculus.",
        "response": null,
        "status": "OPEN",
        "createdAt": "2023-10-20T10:00:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved queries.

### 2. Get Query by ID
- **Endpoint**: `/api/queries/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single query by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the query.
- **Response**: `QueryDTO`
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Mathematics",
    "queryText": "Doubt about calculus.",
    "response": null,
    "status": "OPEN",
    "createdAt": "2023-10-20T10:00:00Z"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the query.
  - `404 Not Found`: Query with the given ID does not exist.

### 3. Create a New Query
- **Endpoint**: `/api/queries`
- **Method**: `POST`
- **Purpose**: Creates a new query record.
- **Request Body**: `QueryDTO`
  ```json
  {
    "studentId": 101,
    "subject": "Library Access",
    "queryText": "I cannot access the online library resources."
  }
  ```
- **Response**: `QueryDTO` (Created Query)
  ```json
  {
    "id": 2,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Library Access",
    "queryText": "I cannot access the online library resources.",
    "response": null,
    "status": "OPEN",
    "createdAt": "2023-11-20T16:00:00Z"
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the query.
  - `400 Bad Request`: Invalid request body.

### 4. Update Query Status
- **Endpoint**: `/api/queries/{id}/status`
- **Method**: `PUT`
- **Purpose**: Updates the status of a query.
- **Parameters**:
  - `id` (Path Variable): The ID of the query to update.
  - `status` (Request Parameter): The new status (e.g., "RESOLVED", "CLOSED").
- **Response**: `QueryDTO` (Updated Query)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Mathematics",
    "queryText": "Doubt about calculus.",
    "response": "Here's the explanation...",
    "status": "RESOLVED",
    "createdAt": "2023-10-20T10:00:00Z"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully updated the query status.
  - `404 Not Found`: Query with the given ID does not exist.

### 5. Respond to a Query (Admin/Teacher)
- **Endpoint**: `/api/queries/{id}/respond`
- **Method**: `POST`
- **Purpose**: Allows an admin or teacher to respond to a query.
- **Parameters**:
  - `id` (Path Variable): The ID of the query.
  - `response` (Request Parameter): The response text.
  - `respondedBy` (Request Parameter): The name/ID of the responder.
- **Response**: `QueryDTO` (Updated Query)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subject": "Mathematics",
    "queryText": "Doubt about calculus.",
    "response": "Here's the explanation...",
    "status": "RESOLVED",
    "createdAt": "2023-10-20T10:00:00Z"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully responded to the query.
  - `404 Not Found`: Query with the given ID does not exist.

### 6. Delete a Query
- **Endpoint**: `/api/queries/{id}`
- **Method**: `DELETE`
- **Purpose**: Deletes a query record by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the query to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the query.
  - `404 Not Found`: Query with the given ID does not exist.

---

## MarkController (`/api/marks`)

### 1. Get All Marks
- **Endpoint**: `/api/marks`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of all marks. Supports searching by student name, subject code, and filtering by semester.
- **Parameters**:
  - `studentSearch` (Optional): Search term for student's name.
  - `subjectCode` (Optional): Filter marks by subject code.
  - `semester` (Optional): Filter marks by semester.
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<MarkDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "subjectId": 201,
        "subjectName": "Mathematics",
        "semester": "Fall 2023",
        "internal1": 25,
        "internal2": 28,
        "external": 70,
        "total": 123
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved marks.

### 2. Get Mark by ID
- **Endpoint**: `/api/marks/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single mark record by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the mark.
- **Response**: `MarkDTO`
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subjectId": 201,
    "subjectName": "Mathematics",
    "semester": "Fall 2023",
    "internal1": 25,
    "internal2": 28,
    "external": 70,
    "total": 123
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the mark.
  - `404 Not Found`: Mark with the given ID does not exist.

### 3. Get Marks by Student ID
- **Endpoint**: `/api/marks/student/{studentId}`
- **Method**: `GET`
- **Purpose**: Retrieves all mark records for a specific student.
- **Parameters**:
  - `studentId` (Path Variable): The ID of the student.
- **Response**: `List<MarkDTO>`
  ```json
  [
    {
      "id": 1,
      "studentId": 101,
      "studentName": "Student A",
      "subjectId": 201,
      "subjectName": "Mathematics",
      "semester": "Fall 2023",
      "internal1": 25,
      "internal2": 28,
      "external": 70,
      "total": 123
    },
    {
      "id": 2,
      "studentId": 101,
      "studentName": "Student A",
      "subjectId": 202,
      "subjectName": "Physics",
      "semester": "Fall 2023",
      "internal1": 22,
      "internal2": 25,
      "external": 65,
      "total": 112
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved marks.
  - `404 Not Found`: Student with the given ID does not exist.

### 4. Create a New Mark
- **Endpoint**: `/api/marks`
- **Method**: `POST`
- **Purpose**: Creates a new mark record.
- **Request Body**: `MarkDTO`
  ```json
  {
    "studentId": 102,
    "subjectId": 201,
    "semester": "Fall 2023",
    "internal1": 20,
    "internal2": 22,
    "external": 65
  }
  ```
- **Response**: `MarkDTO` (Created Mark)
  ```json
  {
    "id": 3,
    "studentId": 102,
    "studentName": "Student B",
    "subjectId": 201,
    "subjectName": "Mathematics",
    "semester": "Fall 2023",
    "internal1": 20,
    "internal2": 22,
    "external": 65,
    "total": 107
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the mark.
  - `400 Bad Request`: Invalid request body.

### 5. Update an Existing Mark
- **Endpoint**: `/api/marks/{id}`
- **Method**: `PUT`
- **Purpose**: Updates an existing mark record.
- **Parameters**:
  - `id` (Path Variable): The ID of the mark to update.
- **Request Body**: `MarkDTO`
  ```json
  {
    "internal1": 25,
    "internal2": 28,
    "external": 75
  }
  ```
- **Response**: `MarkDTO` (Updated Mark)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "subjectId": 201,
    "subjectName": "Mathematics",
    "semester": "Fall 2023",
    "internal1": 25,
    "internal2": 28,
    "external": 75,
    "total": 128
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully updated the mark.
  - `400 Bad Request`: Invalid request body.
  - `404 Not Found`: Mark with the given ID does not exist.

### 6. Delete a Mark
- **Endpoint**: `/api/marks/{id}`
- **Method**: `DELETE`
- **Purpose**: Deletes a mark record by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the mark to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the mark.
  - `404 Not Found`: Mark with the given ID does not exist.

### 7. Upload Marks from Excel
- **Endpoint**: `/api/marks/upload`
- **Method**: `POST`
- **Purpose**: Uploads marks for a subject, semester, and specifies the uploader from an Excel file.
- **Parameters**:
  - `file` (Request Parameter): The Excel file containing marks.
  - `subjectId` (Request Parameter): The ID of the subject.
  - `semester` (Request Parameter): The semester (e.g., "Fall 2023").
  - `uploadedBy` (Request Parameter): The name/ID of the entity uploading the marks (e.g., "Admin", "Teacher-123").
- **Request Body**: `multipart/form-data` with `file`, `subjectId`, `semester`, and `uploadedBy` parts.
- **Response**: `List<MarkDTO>` (List of uploaded marks)
  ```json
  [
    {
      "id": 4,
      "studentId": 104,
      "studentName": "Student D",
      "subjectId": 203,
      "subjectName": "Chemistry",
      "semester": "Fall 2023",
      "internal1": 18,
      "internal2": 20,
      "external": 55,
      "total": 93
    }
  ]
  ```
- **Status Codes**:
  - `200 OK`: Successfully uploaded marks.
  - `400 Bad Request`: Invalid file or parameters.
  - `500 Internal Server Error`: Error during file processing.

### 8. Download Excel Template for Marks
- **Endpoint**: `/api/marks/template`
- **Method**: `GET`
- **Purpose**: Downloads an Excel template file for uploading marks.
- **Response**: `byte[]` (Excel file content)
- **Status Codes**:
  - `200 OK`: Successfully retrieved the template.
  - `500 Internal Server Error`: Error generating the template.

---

## DashboardController (`/api/dashboard`)

### 1. Get Dashboard Statistics
- **Endpoint**: `/api/dashboard/stats`
- **Method**: `GET`
- **Purpose**: Retrieves various statistics for the administrative dashboard.
- **Response**: `DashboardStatsDTO`
  ```json
  {
    "totalStudents": 1500,
    "totalTeachers": 120,
    "totalSubjects": 50,
    "pendingBonafideRequests": 15,
    "openQueries": 25,
    "recentActivities": [
      "New student registered: John Doe",
      "Marks uploaded for Mathematics - Fall 2023"
    ]
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved dashboard statistics.

---

## BonafideRequestController (`/api/bonafide-requests`)

### 1. Get All Bonafide Requests
- **Endpoint**: `/api/bonafide-requests`
- **Method**: `GET`
- **Purpose**: Retrieves a paginated list of all bonafide requests. Supports searching and filtering by status.
- **Parameters**:
  - `search` (Optional): Search term for student name or purpose.
  - `status` (Optional): Filter requests by status (e.g., "PENDING", "APPROVED", "REJECTED").
  - `page` (Optional): Page number (0-indexed).
  - `size` (Optional): Number of records per page.
  - `sort` (Optional): Sorting criteria.
- **Response**: `Page<BonafideRequestDTO>`
  ```json
  {
    "content": [
      {
        "id": 1,
        "studentId": 101,
        "studentName": "Student A",
        "purpose": "Visa Application",
        "status": "PENDING",
        "createdAt": "2023-11-10T11:00:00Z",
        "approvedBy": null,
        "rejectionReason": null
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 20,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "offset": 0,
      "paged": true,
      "unpaged": false
    },
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 20,
    "number": 0,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved requests.

### 2. Get Bonafide Request by ID
- **Endpoint**: `/api/bonafide-requests/{id}`
- **Method**: `GET`
- **Purpose**: Retrieves a single bonafide request by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the request.
- **Response**: `BonafideRequestDTO`
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "purpose": "Visa Application",
    "status": "PENDING",
    "createdAt": "2023-11-10T11:00:00Z",
    "approvedBy": null,
    "rejectionReason": null
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully retrieved the request.
  - `404 Not Found`: Request with the given ID does not exist.

### 3. Create a New Bonafide Request
- **Endpoint**: `/api/bonafide-requests`
- **Method**: `POST`
- **Purpose**: Creates a new bonafide request record.
- **Request Body**: `BonafideRequestDTO`
  ```json
  {
    "studentId": 101,
    "purpose": "Scholarship Application"
  }
  ```
- **Response**: `BonafideRequestDTO` (Created Request)
  ```json
  {
    "id": 2,
    "studentId": 101,
    "studentName": "Student A",
    "purpose": "Scholarship Application",
    "status": "PENDING",
    "createdAt": "2023-11-20T16:30:00Z",
    "approvedBy": null,
    "rejectionReason": null
  }
  ```
- **Status Codes**:
  - `201 Created`: Successfully created the request.
  - `400 Bad Request`: Invalid request body.

### 4. Approve Bonafide Request
- **Endpoint**: `/api/bonafide-requests/{id}/approve`
- **Method**: `POST`
- **Purpose**: Approves a specific bonafide request.
- **Parameters**:
  - `id` (Path Variable): The ID of the request to approve.
  - `approvedBy` (Request Parameter): The name/ID of the approver.
- **Response**: `BonafideRequestDTO` (Approved Request)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "purpose": "Visa Application",
    "status": "APPROVED",
    "createdAt": "2023-11-10T11:00:00Z",
    "approvedBy": "Admin User",
    "rejectionReason": null
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully approved the request.
  - `404 Not Found`: Request with the given ID does not exist.

### 5. Reject Bonafide Request
- **Endpoint**: `/api/bonafide-requests/{id}/reject`
- **Method**: `POST`
- **Purpose**: Rejects a specific bonafide request with a reason.
- **Parameters**:
  - `id` (Path Variable): The ID of the request to reject.
  - `rejectionReason` (Request Parameter): The reason for rejection.
  - `rejectedBy` (Request Parameter): The name/ID of the rejecter.
- **Response**: `BonafideRequestDTO` (Rejected Request)
  ```json
  {
    "id": 1,
    "studentId": 101,
    "studentName": "Student A",
    "purpose": "Visa Application",
    "status": "REJECTED",
    "createdAt": "2023-11-10T11:00:00Z",
    "approvedBy": null,
    "rejectionReason": "Missing required documents.",
    "rejectedBy": "Admin User"
  }
  ```
- **Status Codes**:
  - `200 OK`: Successfully rejected the request.
  - `404 Not Found`: Request with the given ID does not exist.

### 6. Delete a Bonafide Request
- **Endpoint**: `/api/bonafide-requests/{id}`
- **Method**: `DELETE`
- **Purpose**: Deletes a bonafide request record by its ID.
- **Parameters**:
  - `id` (Path Variable): The ID of the request to delete.
- **Response**: `Void` (No Content)
- **Status Codes**:
  - `204 No Content`: Successfully deleted the request.
  - `404 Not Found`: Request with the given ID does not exist.

</rewritten_file>