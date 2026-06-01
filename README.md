ConnectAI Overview

ConnectAI Overview
ConnectAI is an Android-based chat application designed to provide a seamless interface for interacting with large language models via the OpenRouter API. It utilizes a single-activity architecture to facilitate real-time, bidirectional communication between a user and an AI backend.

The application serves as a demonstration of modern Android development practices, including Material 3 design, asynchronous networking with OkHttp, and dynamic UI updates using RecyclerView.

Core Functionality
The primary purpose of ConnectAI is to allow users to send text prompts and receive AI-generated responses.

User Interaction: Users input text via an EditText field and trigger a request using a Button 
app/src/main/java/com/example/connectai/MainActivity.java
#53-61
AI Integration: The app communicates with the OpenRouter API endpoint https://openrouter.ai/api/v1/chat/completions 
app/src/main/java/com/example/connectai/MainActivity.java
#82
 using the openai/gpt-3.5-turbo model 
app/src/main/java/com/example/connectai/MainActivity.java
#73
Asynchronous Processing: API calls are handled on a background thread to prevent UI blocking 
app/src/main/java/com/example/connectai/MainActivity.java
#70
For details on setting up the environment and API keys, see Getting Started.

High-Level Architecture
ConnectAI follows a standard Android architectural pattern where MainActivity acts as the central controller for UI logic and network orchestration.

Data Flow Diagram
This diagram illustrates the transition from Natural Language input in the UI to the Code Entities responsible for processing the request.

User Input to API Request Flow
