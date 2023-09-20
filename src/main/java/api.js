
const apiUrl = 'https://bh0ycuvcnh.execute-api.us-east-2.amazonaws.com/dev';


function displayUsers(users) {
    const userList = document.getElementById('userList');
    userList.innerHTML = '<h2>Users:</h2>';
    users.forEach((user) => {
        userList.innerHTML += `<p>User ID: ${user.userId}, Username: ${user.username}, Email: ${user.email}, First Name: ${user.firstName}, Last Name: {user.LastName} </p>`;
    });
}

function getUsers() {
     fetch(`${apiUrl}/user/users`, {
         method: 'GET',
         headers: {
             'Content-Type': 'application/json',
         },
     })
     .then((response) => {
         console.log('Response Status:', response.status); // Log the status code
         return response.json();
     })
     .then((data) => {
         console.log('Response Data:', data); // Log the response data
         displayUsers(data);
     })
     .catch((error) => {
         console.error('Error fetching users:', error);
     });
 }

function createUser() {
    const newUser = {
        // Replace with user data you want to create
        userId: 'user123',
        username: 'newuser',
        email: 'newuser@example.com',
        password: 'newpassword',
        firstName: 'New',
        lastName: 'User',
    };

    fetch(`${apiUrl}/user`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newUser),
    })
    .then((response) => response.json())
    .then((createdUser) => {
        console.log('User created successfully:', createdUser);
        // You can update the UI or perform other actions here
    })
    .catch((error) => {
        console.error('Error creating user:', error);
    });
}

// Function to update a user
function updateUser() {
    const updatedUser = {
        // Replace with the updated user data
        username: 'updateduser',
        email: 'updateduser@example.com',
        password: 'updatedpassword',
        firstName: 'Updated',
        lastName: 'User',
    };

    const userIdToUpdate = 'user123'; // Replace with the user ID you want to update

    fetch(`${apiUrl}/user/${userIdToUpdate}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedUser),
    })
    .then((response) => response.json())
    .then((updatedUserData) => {
        console.log('User updated successfully:', updatedUserData);
        // You can update the UI or perform other actions here
    })
    .catch((error) => {
        console.error('Error updating user:', error);
    });
}

// Function to delete a user
function deleteUser() {
    const userIdToDelete = 'user123'; // Replace with the user ID you want to delete

    fetch(`${apiUrl}/user/${userIdToDelete}`, {
        method: 'DELETE',
    })
    .then((response) => {
        if (response.status === 200) {
            console.log(`User with ID ${userIdToDelete} deleted successfully`);
            // You can update the UI or perform other actions here
        } else {
            console.error('Error deleting user:', response.statusText);
        }
    })
    .catch((error) => {
        console.error('Error deleting user:', error);
    });
}