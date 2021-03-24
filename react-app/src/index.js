import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import firebase from "firebase/app";
import "firebase/auth";
import  {FirebaseAuthProvider} from '@react-firebase/auth';
const firebaseConfig = {
  apiKey: "AIzaSyDFD4itev3V4n8m4663VaSjS0pTgoch7SY",
  authDomain: "livelink-6b88b.firebaseapp.com",
  projectId: "livelink-6b88b",
  storageBucket: "livelink-6b88b.appspot.com",
  messagingSenderId: "730187997644",
  appId: "1:730187997644:web:21f8958be0321238187d69"
};

ReactDOM.render(
  <React.StrictMode>
  <FirebaseAuthProvider firebase={firebase} {...firebaseConfig}>
  <App />
</FirebaseAuthProvider>

  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
