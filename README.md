#Prerequisites:
1. Docker
2. NPM

#How to run:
1. Open a command prompt in the backend/docker folder and write `docker-compose up`
2. Open a command prompt in the frontend folder, write `npm install` and then `npm start`

#Stack:
1. Java & Spring
2. React.js
3. PostgreSQL

#Some important (in my opinion) implementation details:
1. Regarding the ERD diagram, I could not follow the description exactly because of the way my DataBase is structured, 
so I included a simple ERD diagram containing the entities and links between them 
2. Saving/editing/deleting is done via REST APIs
3. For all the CRUD operations (save marker, save/edit title, save/edit/delete message), there is a web socket that 
listens for the changes and updates them live in the page
4. In the backend, the web socket handler is used in an asynchronous way, so the user does not have to wait for 
the handler to send all notifications before they get the API call response

#Application use note(s):
1. When trying to edit a title or a message, please click on the text
2. As a pseudo-security measure, only the messages created by the same user name as the one present in the input can be 
edited