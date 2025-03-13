package com.example.androcompile23;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText codeEditor;
    private EditText userInputBox;
    private Spinner languageSpinner;
    private TextView outputText;
    private Button compileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        codeEditor = findViewById(R.id.codeEditor);
        userInputBox = findViewById(R.id.userInputBox);
        languageSpinner = findViewById(R.id.languageSpinner);
        outputText = findViewById(R.id.outputText);
        compileButton = findViewById(R.id.compileButton);

        // Button click listener
        compileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeEditor.getText().toString();
                String userInput = userInputBox.getText().toString();
                String language = languageSpinner.getSelectedItem().toString();

                // Check if code is empty
                if (code.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please write some code", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send code to the server for compilation and execution
                compileAndRunCode(code, userInput, language);
            }
        });
    }

    private void compileAndRunCode(String code, String userInput, String language) {
        // Set up server endpoint URL (Flask server)
        String url = "http://192.168.57.72:5000/compile";  // Change this to your actual server IP

        try {
            // Prepare the data to send to the server
            HashMap<String, String> data = new HashMap<>();
            data.put("code", code);  // The code written in the app
            data.put("compiler", language);  // The compiler (language)
            data.put("input", userInput);  // User input for running the code

            // Send a POST request to the server (using your HttpClient)
            new HttpClient(url, data, new HttpClient.ResponseListener() {
                @Override
                public void onResponse(String response) {
                    try {
                        // Try to parse the response as a JSON object
                        JSONObject jsonResponse = new JSONObject(response);

                        // Check if the response contains "output" or "error"
                        if (jsonResponse.has("output")) {
                            // Display the output of the compiled and run code
                            outputText.setText(jsonResponse.getString("output"));
                        } else if (jsonResponse.has("error")) {
                            // Display the error message if compilation or execution failed
                            outputText.setText(jsonResponse.getString("error"));
                        } else {
                            outputText.setText("Unknown response format.");
                        }
                    } catch (Exception e) {
                        // Handle parsing errors (non-JSON responses)
                        outputText.setText("Error parsing response: " + response); // Show raw response for debugging
                    }
                }

                @Override
                public void onError(String error) {
                    // Handle any errors from the HTTP request
                    outputText.setText("Error: " + error);
                }
            }).execute();  // Execute the HTTP request
        } catch (Exception e) {
            outputText.setText("Failed to send request: " + e.getMessage());
        }
    }
}
