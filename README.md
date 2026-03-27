# Video Controller Service

A Java Spring Boot service to **control video playback** on a local machine using **VLC media player**. Designed as a **prototype for automated test scenarios**, such as those used for automated testing of vision systems.

---

## Project Setup

- Created using [Spring Initializr](https://start.spring.io)  
  - **Project:** Maven, **Java 17**, **Spring Boot 3.5.13**  
  - **Dependencies:** Spring Web  
  - **Package:** `com.example.videocontroller`
- Opened in **IntelliJ Community Edition** and run Spring Boot from the IDE  
- **VLC Media Player** used for video playback  
- Windows environment  
- JSON input used for file paths to handle spaces and encoding issues  

**Screenshots of Setup:**

**Spring Initializr settings:**  
![Spring Initializr](screenshots/spring-initializr.png)

**Running in IntelliJ IDE:**  
![IntelliJ Run](screenshots/intellij-run.png)

---

## Project Files

- `src/main/java/com/example/videocontroller/VideocontrollerApplication.java` — Spring Boot entry point  
- `src/main/java/com/example/videocontroller/VideoController.java` — exposes REST endpoints and executes VLC commands  
- `src/main/resources/application.properties` — configuration for VLC path and RC port  
- `pom.xml` — Maven dependencies and Spring Boot plugin  

---

## How it Works

- `VideoController.java` exposes **3 REST endpoints**:  
  - `POST /video/play` — starts a video from JSON input:  
    ```json
    {"file": "C:\\Path\\To\\Your\\VideoFile.avi"}
    ```  
  - `POST /video/pause` — pauses the currently playing video  
  - `POST /video/stop` — stops playback and closes VLC  

- The service **launches VLC via command line** and optionally communicates via the **VLC RC interface** (`vlc.rc-port=9999`) for pause/stop functionality.  
- Spring Boot runs the embedded **Tomcat server on port 8080**.  
- Each endpoint uses `ProcessBuilder` to execute VLC commands.  

**Design Considerations / Notes:**  
- Only a **minimal prototype**: multiple `/play` calls may start multiple VLC instances.  
- No `/status` endpoint implemented; could be added to track playback state.  
- Error handling is minimal; invalid paths or failed VLC launches log exceptions but do not return detailed HTTP errors.  
- Future improvements:  
  - Use a **single VLC instance** with RC interface to handle multiple requests safely.  
  - Add **debounce** or request throttling to prevent overlapping commands.  
  - Implement `/status` endpoint for test automation to check current video state.  

---

## Running the Project

1. Start Spring Boot from **IntelliJ IDE** by running `VideocontrollerApplication.java`.  
2. Keep the terminal open for REST calls.  

### Test Endpoints Using CMD

Run the following commands in **one Command Prompt session** to demonstrate **play → pause → stop**:

```cmd
REM Play video
curl -X POST -H "Content-Type: application/json" ^
     -d "{\"file\":\"C:\\Path\\To\\Your\\VideoFile.avi\"}" ^
     http://localhost:8080/video/play

REM Pause video
curl -X POST -H "Content-Type: application/json" ^
     -d "{}" ^
     http://localhost:8080/video/pause

REM Stop video
curl -X POST -H "Content-Type: application/json" ^
     -d "{}" ^
     http://localhost:8080/video/stop
