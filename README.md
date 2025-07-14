Please Find below rest URL's to assess data.

Get All Employees: GetMapping: localhost:8080/api/employees
Get employees in a department: GetMapping: localhost:8080/api/departments/{department_Id}/employees
Get All departments: GetMapping: localhost:8080/api/departments
Add new employee to department: PostMapping: localhost:8080/api/departments/{department_Id}/employees
Add All employee And department: PostMapping: localhost:8080/api/departments
Delete employee And department: DeleteMapping: localhost:8080/api/departments/{department_Id}/employees/{employee_Id}
Get employees-per-department PDF: GetMapping: localhost:8080/api/employees-per-department

************************
Sample Data:

{
    "departments": [
        {
            "id": "dept01",
            "name": "Human Resources",
            "location": "Building A",
            "employees": [
                {
                    "id": "emp001",
                    "name": "Alice Smith",
                    "email": "alice.smith@example.com",
                    "position": "Recruiter",
                    "salary": 60000
                },
                {
                    "id": "emp003",
                    "name": "Charlie Brown",
                    "email": "charlie.brown@example.com",
                    "position": "HR Assistant",
                    "salary": 40000
                }
            ]
        },
        {
            "id": "dept02",
            "name": "Engineering",
            "location": "Building B",
            "employees": [
                {
                    "id": "emp002",
                    "name": "Bob Johnson",
                    "email": "bob.johnson@example.com",
                    "position": "Software Engineer",
                    "salary": 80000
                },
                {
                    "id": "emp004",
                    "name": "Diana Prince",
                    "email": "diana.prince@example.com",
                    "position": "System Architect",
                    "salary": 90000
                }
            ]
        }
    ]
}

************************
