# ExamCell Result Generation API Documentation (No Authentication)

**WARNING:** This version of the API has security features (Authentication & Authorization) removed for a specific test environment. DO NOT use this configuration in production.

This document outlines the available API endpoints for the ExamCell Result Generation system.

## Authentication

NONE. Authentication and authorization are bypassed in this configuration.

## Identifying Users

Since there is no login/JWT, users (students/professors) must be identified explicitly in API calls:
*   **Students:** Identified by their `rollNumber` (String), typically passed as a path variable.
*   **Professors:** Identified by their `employeeId` (String), typically passed as a path variable or request parameter.

## Base URL

All API endpoints are relative to the base URL where the application is deployed (e.g., `http://localhost:8080`). The paths listed below should be appended to the base URL.

## API Endpoints

--- 

### Student Endpoints (`/api/students`)

These endpoints relate to student information and results, identified by Roll Number.

1.  **Get Student Subjects**
    *   **Endpoint:** `GET /api/students/{rollNumber}/subjects`
    *   **Description:** Retrieves the list of subjects for the specified student's calculated current semester.
    *   **Path Parameter:**
        *   `{rollNumber}` (string): The roll number of the student (e.g., `BTECH1000121`).
    *   **Response (Success - 200 OK):**
        ```json
        [
            {
                "id": "uuid-subject-1",
                "code": "CS301",
                "name": "Data Structures",
                "semester": 3
            }
            // ... more subjects
        ]
        ```

2.  **Get All Student Results**
    *   **Endpoint:** `GET /api/students/{rollNumber}/results`
    *   **Description:** Retrieves all recorded marks (results) for the specified student.
    *   **Path Parameter:**
        *   `{rollNumber}` (string): The roll number of the student.
    *   **Response (Success - 200 OK):**
        ```json
        [
            {
                "subject": {
                    "id": "uuid-subject-CS101",
                    "code": "CS101",
                    "name": "Intro to Programming",
                    "semester": 1
                },
                "marksObtained": 85.5,
                "maxMarks": 100.0,
                "semester": 1
            }
            // ... more results
        ]
        ```

3.  **Get Student Results by Semester**
    *   **Endpoint:** `GET /api/students/{rollNumber}/results/{semester}`
    *   **Description:** Retrieves recorded marks (results) for the specified student for a specific semester.
    *   **Path Parameters:**
        *   `{rollNumber}` (string): The roll number of the student.
        *   `{semester}` (integer): The semester number (e.g., `3`).
    *   **Response (Success - 200 OK):** (Similar structure to Get All Results, but filtered)
        ```json
        [
            {
                "subject": {
                    "id": "uuid-subject-CS301",
                    "code": "CS301",
                    "name": "Data Structures",
                    "semester": 3
                },
                "marksObtained": 92.0,
                "maxMarks": 100.0,
                "semester": 3
            }
        ]
        ```

--- 

### Professor & Subject Endpoints (`/api/professors`, `/api/subjects`)

1.  **Get Professor's Assigned Subjects**
    *   **Endpoint:** `GET /api/professors/{employeeId}/subjects`
    *   **Description:** Retrieves the list of subjects assigned to a specific professor.
    *   **Path Parameter:**
        *   `{employeeId}` (string): The employee ID of the professor.
    *   **Response (Success - 200 OK):**
        ```json
        [
            {
                "id": "uuid-subject-CS301",
                "code": "CS301",
                "name": "Data Structures",
                "semester": 3
            }
            // ... more assigned subjects
        ]
        ```

2.  **Get Students Enrolled in a Subject**
    *   **Endpoint:** `GET /api/subjects/{subjectId}/students`
    *   **Description:** Retrieves the list of students enrolled in a specific subject.
    *   **Path Parameter:**
        *   `{subjectId}` (UUID): The ID of the subject.
    *   **Response (Success - 200 OK):**
        ```json
        [
            {
                "id": "uuid-student-1",
                "rollNumber": "BTECH/10001/21",
                "course": "BTECH",
                "branch": "CSE",
                "batchYear": 2021
            }
            // ... more students
        ]
        ```

--- 

### Marks Entry Endpoints (`/api/marks`)

These endpoints are used by professors to enter and manage student marks.

1.  **Batch Enter Marks**
    *   **Endpoint:** `POST /api/marks`
    *   **Description:** Submits marks for multiple students. The professor performing the action must be identified.
    *   **Request Parameter:**
        *   `professorEmployeeId` (string): The Employee ID of the professor submitting the marks (required for tracking).
    *   **Request Body:** A JSON array of `MarkEntryDto` objects.
        ```json
        [
            {
                "studentId": "uuid-student-1", // Student's internal UUID
                "subjectId": "uuid-subject-CS301",
                "marksObtained": 88.0
            }
            // ... more entries
        ]
        ```
    *   **Example Call:** `POST /api/marks?professorEmployeeId=PROF123`
    *   **Response (Success - 201 Created):**
        ```json
        {
            "message": "Marks entered successfully",
            "count": 1 // Number of records created/updated
        }
        ```
    *   **Response (Error):** `400`, `404`.

2.  **Update Existing Mark**
    *   **Endpoint:** `PUT /api/marks/{recordId}`
    *   **Description:** Updates the marks obtained for a single existing marks record. The professor performing the action must be identified.
    *   **Path Parameter:**
        *   `{recordId}` (UUID): The ID of the `MarksRecord` to update.
    *   **Request Parameter:**
        *   `professorEmployeeId` (string): The Employee ID of the professor updating the mark (required for tracking).
    *   **Request Body:** A JSON object containing the new marks.
        ```json
        {
            "marksObtained": 95.0 
        }
        ```
    *   **Example Call:** `PUT /api/marks/uuid-marks-record?professorEmployeeId=PROF123`
    *   **Response (Success - 200 OK):** The updated `MarksRecord` entity.
        ```json
        {
           "id": "uuid-marksrecord-to-update",
           "student": { ... }, 
           "subject": { ... }, 
           "marksObtained": 95.0,
           "maxMarks": 100.0,
           "enteredBy": { ... }, 
           "createdAt": "...",
           "updatedAt": "..."
        }
        ```
     *   **Response (Error):** `400`, `404`.

--- 

## Error Handling

Errors are generally handled by the `GlobalExceptionHandler` and return a standard JSON response body:

```json
{
    "timestamp": "2024-07-28T14:15:30.123456",
    "status": <HTTP_STATUS_CODE>,
    "error": "<HTTP_STATUS_REASON>",
    "message": "<Specific error message>",
    "path": "<Request URI>"
}
```

Common status codes:
*   `400 Bad Request`: Invalid input data.
*   `404 Not Found`: Resource not found.
*   `500 Internal Server Error`: Unexpected server-side error. 