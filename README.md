# Livelink

## Inspiration
Many times, during job fairs and recruiting sessions, we have found it difficult to _express ourselves_ to company recruiters and _portray our capabilities and passion_. With a large pool of talented candidates and a small, limited number of recruiters, it is difficult and sometimes nearly impossible to fully portray yourself. To combat this issue, we developed Livelink to help people _link_ professionally and efficiently. Recruiters simply use the mobile app with or without AR lens to immediately _scan rooms_ and _see everyone's professional profile_. This should streamline the recruiting process and help candidates express themselves successfully. 

## What it does
Livelink leverages **facial recognition**, **augmented reality**, and **speech recognition** to provide a seamless experience for users to link with others _live_ and _in-person_ with ease. The user can create a professional profile on the web app by uploading an image of themselves for facial recognition and a biography. Then, using the mobile app, others can view that profile if they scan that user. Their profile will be dynamically rendered via AR for recruiters and others to see.

## How we built it
The project work was split into 3 main segments: the web application, the mobile application, and the backend. 

### Web App
The web application was built in **React.js** and was used to handle profile creation and update. This was also where users uploaded their profile pictures on which facial recognition was conducted. The UI/UX of the web application was designed in a **Figma prototype** before we reformatted the HTML and CSS to cooperate with React.js and JSX. We used **Google Cloud Firebase Authentication** to allow users to create accounts and sign in.

### Mobile App
The mobile application was built in **Android** using Java, and it primarily utilized the **Google ARCore** library to dynamically render **Augmented Reality** elements via AR Sceneforms and ViewRenderables with. The mobile application is meant to be used with AR goggles or lens (if the user does not have this, they can simply use the application by holding up their phone). A native voice service, Android **Speech Recognizer**,  and a **custom-built query listener** were employed to handle **continuous speech detection** and verbal commands such as "Livelink detect" or "link now" to connect with other individuals. The application saves a screenshot of the camera feed when the user says "detect". The image is converted to base64 and sent via POST request to the Flask endpoint where facial recognition is conducted. If a match is successful, their profile will be dynamically rendered via the AR Sceneform for the user to see. The user can then say "link now" to connect with the individual (similar to Facebook or LinkedIn connections). 

### Backend
The backend consisted of the machine learning side and the database side. Thus, we created 2 servers with these respective roles. 

##### Machine Learning
The first was a **Flask** server written in Python that was deployed on **Google Cloud App Engine**. This server handled facial recognition. First, the base 64 encoding of the image was converted into an **OpenCV** image. Then, we conducted some preprocessing on the image, such as ensuring its orientation was correct and converting it to greyscale. For feature extraction, the **Google MediaPipe** API was utilized to generate a mesh of the face, which we then converted into a **NumPy** matrix. 

If the class was known, meaning this event was triggered by profile creation from the website, then the script harnessed the **Google Cloud Storage Client** to deserialize the current feature data into a NumPy array, append to it, reserialize it, and deposit the new data into the storage bucket. If the class was unknown, meaning the request was made from the mobile application during a scan, then facial recognition kicked in. The features were extracted in the same manner as before, and the data from the Cloud Storage was again loaded in. This time, though, the data from the Cloud Storage was reformatted to train a classifier that would identify which class of data the new data most resembled. 

We experimented with multiple algorithms but ultimately decided on the very simple KNN classifier because it had very accurate results using Euclidean distance between mesh coordinates and was quite inexpensive due to our low volume of training data. The class was then mapped to a database ID using a dictionary file also stored in the Cloud Storage bucket, and this ID was sent to the second server to query the database and return profile information for the identified individual. 

##### Database
The second server, which was an **Express.js** endpoint written in Node.js, was used to interact with the database. The endpoint was hosted using **Oracle Cloud Infrastructure**, and the database was built on **Google Cloud Firebase Firestore**. This endpoint received requests from the web frontend to make profile insertions into the database and from the other backend server to access profile information for a given ID.


### Google Cloud Platform
**Google Cloud Platform** acted as the network that allowed all the components of our project to interface and communicate with each other. Beyond tools such as Google Cloud App Engine that enabled us to host our Flask machine learning server and Python helper scripts, we had to manage user data that was accessed and modified across various platforms, from the web end to the mobile application. Google Cloud Platform was handy for this data management because we were able to centralize profile information in a Google Cloud Firebase Firestore database, which we built an Express.js server to communicate with. With this structure, all platforms could send HTTP requests to the Express.js endpoint to access or modify the Firestore database. Furthermore, Google Cloud aided on the machine learning end, as feature matrices (among other locally required data structures) could be serialized and deposited in a Google Cloud Storage bucket that was convenient to read from and write to. This optimized the machine learning algorithm because images could be stored in their decomposed feature state rather than conducting repetitive feature extraction every time the prediction endpoint was triggered. Finally, since we were dealing with profile information, we needed a means of account delineation, for which Google Cloud Firebase Authentication was perfect. It integrated seamlessly into the React web app and enabled us to associate profile information with the distinct users to whom it belonged.

## Challenges we ran into
One of the issues encountered on the backend was that the direct file system cannot be written to from Google App Engine, not even temporarily. Since we planned to use Google Cloud Storage, this was seemingly not an issue. However, when it came to writing objects to Google Cloud Storage, it was unclear how we would wrap the object in a file to send it to the bucket. The immediate intuitive solution was to use the pickle library to serialize the Python objects, but this was not possible since we could not even temporarily write to the file system. Thus, we had to approach the problem differently; we opted to create our own functions for serializing our objects into strings and reconstructing the objects from the strings. Then, we could upload and download the string itself to the Cloud Storage bucket and convert it within the Python script.

Building a custom query listener to handle verbal commands was quite the challenge since the complexity of dealing with android services was new to us. Interface methods frequently failed to trigger since the listener's state did not persist across service restarts. After solving this issue, keyword detection and voice-based commands worked well!

Anchoring nodes in relation to the camera to display AR elements was very difficult since the documentation for ArCore did not cover that procedure in-depth. Solving this required a lot of research into prior implementations of ViewRenderable anchoring that developers on Stackoverflow attempted. 

Testing this project was very tough with all of us living in different locations, especially because the application relied on "scanning" another individual. We went through a number of workarounds, sending pictures to each other, flipping our cameras, looking in a mirror, or getting the help of roommates/family, but nothing can replace the magic of in-person hackathons :(.

## Accomplishments that we're proud of

## What we learned
How to implement the Observer pattern in Java to fire listener methods when an event occurs. This pattern was especially powerful in this project since we were working with voice-based commands and continuous speech detection.

Rendering ArCore elements based on world position and not anchor or plane-based.

Handling continuous speech recognition with an android service running in the background. The way the process runs is complex since it's fully in the background, so it was difficult at first to understand how to interact with the service. 

## What's next for Livelink
Adding privacy filters and scaling the system to handle more users. This requires upgrading the machine learning and improving the speech recognition to handle various types of user input. 

Finally, when AR lens or AR goggles become more publicly available, we hope to integrate this system into those technologies to give the world a vision for social media!
