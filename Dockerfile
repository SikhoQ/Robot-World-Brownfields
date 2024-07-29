# Use the official Node.js image as the base image
FROM node:14

# Create and set the working directory
WORKDIR /usr/src/server

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose the port that the app will run on
EXPOSE 5050

# Start the application
CMD ["node", "server.js"]
