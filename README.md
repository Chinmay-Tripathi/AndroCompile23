# AndroCompile23

This is an Android application which has a code editor which can edit the codes in languages like C++, Python, etc. After writing the code and providing user inputs(if any) the app sends the code to a remote server where the host machine compiles and runs the code and sends the output to the Application. The application prints the output.

## **Project Overview**
The project consists of two main parts:
1. **Android App**: A user-friendly mobile interface that lets users input code, select a language, and receive the result from the server.
2. **Server**: A Flask-based backend that compiles and runs the code using appropriate compilers on the host machine, then sends the output back to the Android app.

## **Files & Directories**

### 1. **`MainActivity.java`** (Android app)
   - **Description**: The main activity of the Android application. It manages user input for code, selects the programming language, and sends the data to the Flask server for execution.
   - **Key Components**:
     - `codeEditor`: A `EditText` widget where users can write their code.
     - `userInputBox`: A `EditText` widget where users can provide any input required by the program.
     - `compileButton`: A button that triggers the compilation and execution process.
     - `outputText`: A `TextView` widget that displays the output or error message returned by the server.
   - **Remember:** Write your host ip address in **String url = "http://<host_ip>:5000/compile";**

### 2. **`HttpClient.java`** (Android app)
   - **Description**: A helper class for making HTTP requests to the server.
   - **Key Components**:
     - `POST` request: Sends the code, compiler choice, and user input to the Flask server.
     - Handles server response and updates the UI with the output or error message.

### 3. **`server.py`** (Flask Server)
   - **Description**: A Flask application that handles requests from the Android app. It compiles and runs the code using the appropriate compiler and returns the output to the app.
   - **Key Components**:
     - `compile_code`: A POST route that handles the compilation and execution of code sent from the Android app.
     - `Python and C++ Compiler`: The server uses the full paths of the compilers (Python and C++) to execute the code.
     - The server sends a response with either the output or an error message.
   - **Command to run the file:** python3 server.py --host=<host_ip> --port=5000

### 4. **`AndroidManifest.xml`** (Android app)
   - **Description**: The configuration file for the Android app. It includes permissions and settings required for the app to run, such as internet access and setting the main activity.
   
### 5. **`activity_main.xml`** (Android app)
   - **Description**: The layout XML file for the main activity. It defines the user interface components such as `EditText`, `Button`, `Spinner`, and `TextView`.




