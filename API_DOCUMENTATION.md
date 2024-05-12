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
  *Create a new message with given JSON body information*
  
  **Params:**
  {
  text: string,
  senderId: int,
  receiverId: int
  }

  **Response:**
  STATUS 200 OK

#### GET "/messages/{userId1}/{userId2}"
  *Get all messages sent between user1 and user2 in chronological order*
  
  **Params:**
  userId1: int
  userId2: int

  **Response:**
  [
    {
      id: int,
      text: string,
      time: timestamp,
      sender: User object,
      receiver: User object
    }
  ]




