# API endpoint documentation

---
#### GET "/"
  *Hello world endpoint description*
  
  **Params:**
  - String: name (default: "World")

  **Response:**
  {body: string}

#### POST "/auth/login"

  *Login with the given email and password.*

  **Request body**
  {
    email: string,
    paswword: string
  }
  

  **Response**
  
  - 200 {
    token: string
  }
  
#### POST "/auth/logout"

  *Perform logout actions for the session*

#### POST "/auth/register"

  *Create a new user*

  **Request body**
  {
  email: string,
  password: string,
  firstName: string,
  lastName: string,
  description: string | null,
  phone: string,
  role: "student" | "teacher" | "parent"
}

  **Response**
  - 200 {
  id: number,
  avatarId: number | null,
  firstName: string,
  lastName: string,
  description: string | null,
  role: "student" | "teacher" | "parent",
  subject: string | null
  }
  
  

# Notes

Endpoints that do not start with /auth need a token in the format of 

"Bearer {{token}}" in the Authorization header. Where {{token}} needs to be replaced with the actual token .