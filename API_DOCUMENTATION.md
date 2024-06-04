# API endpoint documentation

#### GET "/users/{id}/conversations"
  *Get ids of the users the user is having conversations with*
  
  **Params:**
  id: int

  **Response:**
  [id: int]
#### GET "/users/{id}"

*Gets the info about user*


**Response:**
- 200 {
  complaints: ?[]
  id: number,
  avatarId: number | null,
  firstName: string,
  lastName: string,
  description: string | null,
  role: "student" | "teacher" | "parent",
  subject: string | null
  tags: {
  id: number,
  type: string,
  name: string
  }[]
  }

#### GET "/users/{id}/picture"

*Gets the user id pfp*


**Response:**
- 200 bas64 string | null

#### GET "/users/{id}/assignedComplaints"

*Gets the admin users assigned complaints*


**Response:**
- 200 complaint[]

#### GET "/users/{id}/activeComplaint"

*Gets the users last complaint*


**Response:**
- 200 complaint

#### GET "/users/findByUsername/{username}"

*Finds users with username like given one*


**Response:**
- 200 user[]


#### GET "/users/{id}/recommendedUsers"

*Gets the users system recommends the current user to contact*

**Response:**
- 200 user[]


#### GET "/users/{id}/recommendedTeachers"

*Gets the teachers system recommends the current user to contact*


**Response:**
- 200 teacher[]

#### GET "/users/{id}/friendships"

*Gets the users friend list*


**Response:**
- 200 user[]


#### GET "/users/{id}/friendships/to"

*Gets the users pending friend requests*


**Response:**
- 200 user[]

#### GET "/users/{id}/friendships/from"

*Gets the users friend requests that havent been answered*


**Response:**
- 200 user[]

#### POST "/friendships"
  *Create a new friendship request from current user to user with login given in pram.*
  
  **Params:**
  {
  login: string;
  }

  **Response:**
- 200 friendship

#### PATCH "/friendships/{id}/approve"
  *Current user approves the friendship with id.*

  **Response:**
  STATUS 200 OK

#### PATCH "/friendships/{id}/reject"
  *Current user rejects the friendship with id.*

  **Response:**
  STATUS 200 OK


#### POST "/complaints"
  *Create a new complaint with given JSON body information.*
  
  **Params:**
  {
  text: string,
  title: string,
  complaintantId: int
  }

  **Response:**
  STATUS 200 OK


#### PATCH "/complaints/{id}/assign/{asigneeId}"
  *Admin can assign the complaint to another admin.*

  **Response:**
  STATUS 200 OK

#### PATCH "/complaints/{id}/close"
  *Admin that has assigned complaint can close it.*

  **Response:**
  STATUS 200 OK


#### GET "/complaints/unassigned"

*Admin gets unassigned complaints*

**Response:**
-200 complaint[]


#### GET "/tags"

*Gets the system tags*

**Response:**
- 200 {
  id: number,
  type: string,
  name: string
  }[]

#### POST "/messages/"
  *Create a new message with given JSON body information. File can be passed as array of integers or a base64 string encoding of the file.*
  
  **Params:**
  {
  text: string,
  senderId: int,
  receiverId: int,
  respondsToId: int,
  file: integer[] or base64 string,
  fileName: String
  }

  **Response:**
  STATUS 200 OK message

#### GET "/messages"
  *Get all messages sent between user1 and user2 in chronological order*
  
   **Params:**
  {
  userId1: int,
  userId2: int,
  dateTimeFrom: Timestamp
  }
  **Response:**
  [
    {
      id: int,
      text: string,
      time: timestamp,
      senderId: int,
      receiverId: int,
      respondsToId: int,
      hasFiles: Boolean
    }
  ]

#### GET "/messages/{id}/attachments"
 *Gets all files attached to the message with the given id. Returns them in byte64 encoding.*
  **Params:**
  id: int

  **Response:**
  [file: base64 string]


#### POST "/auth/login"

  *Login with the given email and password.*

  **Request body**
  {
    login: string,
    paswword: string
  }
  

  **Response**
  
  - 200 {
    token: string,
    user: {
    complaints: ?[]
    id: number,
    avatarId: number | null,
    firstName: string,
    lastName: string,
    description: string | null,
    role: "student" | "teacher" | "parent",
    subject: string | null
    tags: {
    id: number,
    type: string,
    name: string
    }[]
    }
  }
  
#### POST "/auth/logout"

  *Perform logout actions for the session*

#### POST "/auth/register"

  *Create a new user*

  **Request body**
  {
  email: string,
  password: string,
  login: string,
  firstName: string,
  lastName: string,
  description: string | null,
  phone: string,
  role: "student" | "teacher" | "parent"
  tags: {
    type: string,
    name: string
  }[] | null
  profile: base64 string | number[] | null
  }

  **Response**
  - 200 {
  complaints: ?[]
  id: number,
  avatarId: number | null,
  firstName: string,
  lastName: string,
  description: string | null,
  role: "student" | "teacher" | "parent",
  subject: string | null
  tags: {
    id: number,
    type: string,
    name: string
  }[]
  }
  
  

# Notes

Endpoints that do not start with /auth need a token in the format of 

"Bearer {{token}}" in the Authorization header. Where {{token}} needs to be replaced with the actual token .
