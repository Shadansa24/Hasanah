<div align="center">

# 🕌 Hasanah
### Intelligent Prayer Posture Detection & Correction System

<p align="center">
  <a href="#-key-features">Key Features</a> •
  <a href="#-technical-highlights">Technical Details</a> •
  <a href="#-performance--results">Results</a> •
  <a href="#tech-stack">Tech Stack</a>
</p>

<p>
  <img src="https://img.shields.io/badge/Python-3.9%2B-blue?style=for-the-badge&logo=python" alt="Python" />
  <img src="https://img.shields.io/badge/YOLOv8-Object%20Detection-FFDD00?style=for-the-badge&logo=ultralytics&logoColor=black" alt="YOLOv8" />
  <img src="https://img.shields.io/badge/Accuracy-93%25-success?style=for-the-badge" alt="Accuracy" />
</p>

</div>


---

## 📖 Overview
**Hasanah** is an AI-powered mobile system designed to assist Muslims in perfecting their prayer (Salah). By leveraging Computer Vision, **YOLOv8**, and **Pose Estimation**, the system analyzes prayer movements in real-time, detects postural errors, and provides personalized video corrections.

Our goal is to bridge faith and technology, ensuring users perform their prayers with confidence and accuracy.

---

## 🌟 Key Features

| Feature | Description |
| :--- | :--- |
| 🎯 **AI-Powered Detection** | Utilizes **YOLOv8** for high-speed, real-time posture analysis. |
| 📹 **Visual Guidance** | Automatically plays correction videos tailored to specific mistakes. |
| 📊 **History Tracking** | Logs user performance over time to visualize improvement. |
| 🔒 **Privacy Focused** | Processing is optimized to ensure user data remains secure. |
| 📱 **Modern UI** | A clean, intuitive mobile interface built for accessibility. |

---

## 🧠 Technical Highlights

<details>
<summary><strong>Click to view the Deep Learning Architecture details</strong></summary>

<br>

The system operates on a robust pipeline designed for mobile efficiency:

1.  **Object Detection:** YOLOv8 identifies the user within the frame.
2.  **Feature Extraction:** CNNs analyze deep features of the posture.
3.  **Pose Estimation:** Maps joint landmarks to determine angles and alignment.
4.  **Dataset:** Trained on a custom dataset of **3,225 labeled images** encompassing diverse environments and lighting conditions.

</details>

---

## 🏗 System Architecture

<details> 
<summary><strong>🔍 Click to Expand Architecture Diagram</strong></summary>
<br>

> The flowchart below illustrates the data pipeline from image capture to feedback generation.

![Architecture Diagram](./images/architecture.png)

</details>

---

## 📊 Performance & Results

Our model has been rigorously tested to ensure reliability.

<div align="center">

| Metric | Score | Status |
| :--- | :---: | :---: |
| **Accuracy** | `93%` | 🟢 Excellent |
| **Recall** | `91%` | 🟢 High |
| **mAP50** | `95%` | 🟢 Precision |
| **mAP50–95** | `76%` | 🟡 Robust |

</div>

<details> 
<summary><strong>📸 View Validation Output Images</strong></summary>

<br>

> The image below demonstrates the model's bounding box predictions and confidence scores on the validation set.

![Validation Output](./images/validation.png)

</details>

---

## 🚀 Tech Stack

<div align="center">

### Core AI & Machine Learning
<img src="https://skillicons.dev/icons?i=python,pytorch,tensorflow,opencv" height="50"/>

### Backend & Infrastructure
<img src="https://skillicons.dev/icons?i=firebase,fastapi,git,github,linux" height="50"/>

### Specialized Modules
<img src="https://img.shields.io/badge/Computer_Vision-blue?style=for-the-badge"/> 
<img src="https://img.shields.io/badge/Deep_Learning-purple?style=for-the-badge"/> 
<img src="https://img.shields.io/badge/Pose_Estimation-red?style=for-the-badge"/>

</div>

---

## ▶ User Workflow

How the user interacts with the Hasanah system:

1.  **Upload/Capture:** User uploads an image.
2.  **Analysis:** System detects posture (Standing, Bowing, Prostration, etc.).
3.  **Diagnosis:** Comparison against correct geometric landmarks.
4.  **Feedback:** * *Correct:* ✅ Green Confirmation.
    * *Incorrect:* ❌ Red Highlight + Correction Video displayed.
5.  **Log:** Result is saved to the User History database.

---

## 📄 Documentation

For a deep dive into the methodology, algorithms, and full project report, please refer to the official documentation.

[<img src="https://img.shields.io/badge/Download-Full_Report_PDF-red?style=for-the-badge&logo=adobeacrobatreader" />](./Hasanah.pdf)

---

<div align="center">
  <sub>Built with ❤️ for the community.</sub>
</div>
