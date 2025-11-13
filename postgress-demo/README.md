# Author: Anique Ali

**Steps to Install and run Docker**

1. Go to official Docker download page and download the installer.

https://www.docker.com/

2. After installing verify if Docker is in the system run the following commands:

    docker --version
    docker compose version

These commands should return the version number of Docker.

The Docker desktop app will open, and you will need to create your account after installation.

3. Pull the work from Presentation branch in Spring_MVC project repository into your IntelliJ project locally.

4. Run the following commands from the folder that has the docker-compose.yml file (docker yml file):
    
    docker compose up -d (Start background and check what's running)
    docker compose ps (Optional check to make sure that Docker containers are running)
    
5. Open the browser and type http://localhost:8081 or change the port number to which you are outputting. You should 
see your work once you are on the web address above. 

6. If you open Docker app you will see that specific Docker yml file running. You can stop or start running the yml 
file from their. 

7. To stop running from the IntelliJ terminal you can use the following command:

    docker compose down (Stop containers)
    
    Or to delete this currently running projects database data use:
    
    docker compose down -v 

Do not worry the work that has been seeded into the code will still show once you rerun the start background 
commands above so they will not be deleted with the command "docker compose down -v" . 

    
    