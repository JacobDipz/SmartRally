# SmartRally: AI Badminton Coach

# Abstract
This project presents an AI-powered model coaching system that helps badminton players improve their skills by analyzing gameplay videos with computer vision and deep learning techniques. The system focuses on three main aspects of badminton performance: the player’s actions, the shuttlecock’s movement path, and the player’s positions on court. It gives personalized feedback and recommends the best action to take in each situation, which is called the Next Best Move (NBM). The neural network is trained to predict the NBM accuracy reached an accuracy of about 70%. 
To track where the shuttlecock is hit and where it lands, the system uses a tool called TrackNet. Player movements and locations are calculated using a YOLOv11 OpenPose model. The results are displayed in a video that includes color coded visual feedback showing the player’s shot, direction they are aiming at, and color coded captions that mark important information.
The project also includes a mobile app developed with Kivy, which allows the user to upload their videos, view a series of videos with the generated feedback, and store the generated analysis for future reference. Overall, this system provides a low cost and accessible way for badminton players to receive training and improve their performance using AI.


## Folder Structure

### `player_tracking`
Tracks and analyzes player positions and movements.

- `court_detection.py` – Detects the badminton court using Canny edge detection and Hough lines.
- `skeletal_extraction.py` – Uses a YOLO-OpenPose model to detect player keypoints.
- `locations.py` – Maps players to specific court sections (3x3 grid for players).

---

### `tracknet`
Detects the shuttlecock’s hit and landing points.

- `tracknet.py` – Runs the TrackNet model to track shuttlecock movement.
- Uses `locations.py` with a 4x4 grid to determine shuttlecock locations.

---

### `slowfast`
Recognizes player actions.

- `slowfast.py` – Uses the SlowFast model for action recognition and player stroke classification.

---

### `next_best_move`
Predicts the next best moves using a neural network.

- `nbm_train.py` – Trains a PyTorch model using the Shuttleset dataset.
- `nbm_predict.py` – Loads the trained model and predicts the next best move using 5 inputs:
  1. Shot just played  
  2. Player location  
  3. Opponent location  
  4. Shuttlecock hit section  
  5. Shuttlecock land section
- `nbm_inputs.py` – Connects everything: player tracking, shuttlecock detection, pose recognition, and model prediction. Also generates annotated output videos.

---

### `app`
The mobile app interface built with Kivy.

- Sends a video input to `nbm_inputs.py` and recieves a list of annotated videos.
- Displays the predicted moves and generated output videos.
- This is the **main entry point** running the app runs the entire rest of the pipeline.

---

## How It Works

1. Input a badminton video through the app.
2. Player movements and court positions are tracked.
3. Shuttlecock hit and landing locations are detected.
4. Player actions are recognized using pose data.
5. All data is fed into a neural network to suggest the **Next Best Move**.
6. The app displays the results with visual annotations.

---

## To Run the App

1. Go to the `app` folder.
2. Run the app:
   ```bash
   python main.py
