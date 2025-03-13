import os
import subprocess
from flask import Flask, request, jsonify
import tempfile

app = Flask(__name__)

# Define full paths to compilers
PYTHON_PATH = r"C:\Users\chinm\AppData\Local\Microsoft\WindowsApps\python3.exe"  # Change this path
GPP_PATH = r"C:\MinGW\bin\g++.exe"  # Change this path to your MinGW installation

@app.route('/compile', methods=['POST'])
def compile_code():
    try:
        data = request.json
        code = data.get("code", "")
        compiler = data.get("compiler", "")
        user_input = data.get("input", "")

        if not code or not compiler:
            return jsonify({"error": "Missing code or compiler"}), 400
        
        if compiler.lower() == "python":
            # Use a temporary file for Python
            with tempfile.NamedTemporaryFile(delete=False, suffix=".py") as temp_file:
                temp_file.write(code.encode())  
                temp_filename = temp_file.name

            # Execute Python script
            result = subprocess.run(
                [PYTHON_PATH, temp_filename], 
                capture_output=True, text=True, input=user_input
            )

            # Cleanup
            os.remove(temp_filename)

        elif compiler.lower() == "c++":
            # Use a temporary file for C++
            with tempfile.NamedTemporaryFile(delete=False, suffix=".cpp") as temp_file:
                temp_file.write(code.encode())  
                cpp_filename = temp_file.name
                exe_filename = cpp_filename.replace(".cpp", ".exe")  

            # Compile the C++ code
            compile_result = subprocess.run(
                [GPP_PATH, cpp_filename, "-o", exe_filename], 
                capture_output=True, text=True
            )

            if compile_result.returncode != 0:
                os.remove(cpp_filename)
                return jsonify({"error": compile_result.stderr})

            # Run compiled C++ program
            result = subprocess.run(
                [exe_filename], 
                capture_output=True, text=True, input=user_input
            )

            # Cleanup
            os.remove(cpp_filename)
            os.remove(exe_filename)

        else:
            return jsonify({"error": "Unsupported compiler"}), 400

        return jsonify({"output": result.stdout})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
