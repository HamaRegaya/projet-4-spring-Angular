# Use the official Node.js image as the base image
FROM node:16 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the Angular app files
COPY . .

# Build the Angular project
RUN npm run build --prod

# Use the Nginx image to serve the build
FROM nginx:alpine

# Copy the build files from the previous stage
COPY --from=build /app/dist/summer-workshop-angular /usr/share/nginx/html

# Expose port 80 for the container
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]