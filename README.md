🕌 Hasanah — Prayer Posture Detection & Correction System

An AI-powered system that analyzes Muslim prayer postures using computer vision and deep learning, detects mistakes, and provides personalized correction videos to help users learn proper prayer movements.

🌟 Key Features

Detects prayer posture errors using YOLOv8 + CNNs

Shows correction videos tailored to each mistake

Tracks user history and progress

Mobile-friendly interface

Secure login and privacy-focused design

📖 Project Overview

Hasanah helps users improve their prayer performance by analyzing uploaded images/videos and detecting incorrect postures such as Ruku’, Sujud, Takbeer, and hand placement errors.
The system uses a custom dataset and advanced CV techniques to provide accurate, real-time guidance.

🧠 Technical Highlights

YOLOv8 for real-time object detection

CNN-based feature extraction

Pose Estimation for joint landmark detection

Dataset: 3,225 images (Training/Validation/Testing)

Performance: 93% accuracy, 91% recall, 95% mAP50

🏗 System Architecture

(Replace with your architecture image)
![Architecture Diagram](./images/architecture.png)

📊 Results
| Metric   | Score   |
| -------- | ------- |
| Accuracy | **93%** |
| Recall   | **91%** |
| mAP50    | **95%** |
| mAP50–95 | **76%** |

▶ How to Use

Upload an image of a prayer posture

System analyzes movement

Incorrect posture(s) are highlighted

Correction video is displayed

User can review past attempts in history

📄 Full Documentation

👉 See the full project report here:
./Hasanah.pdf
