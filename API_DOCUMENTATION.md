# API endpoint documentation
---
#### GET "/"
  *Hello world endpoint description*
  
  **Params:**
  - String: name (default: "World")

  **Response:**
  {body: string}

  ---

#### GET "/users/{id}/conversations"
  *Get ids of the users the user is having conversations with*
  
  **Params:**
  id: int

  **Response:**
  [id: int]


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
  STATUS 200 OK

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




