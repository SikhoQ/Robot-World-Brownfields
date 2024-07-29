// Import the Express module
const express = require('express');

// Create an instance of an Express application
const app = express();

// Define the port on which the server will listen
const PORT = process.env.PORT || 5000;

// Define a simple route handler for the default home page
app.get('/', (req, res) => {
  res.send('Hello, World!');
});

// Start the server and listen on the defined port
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
