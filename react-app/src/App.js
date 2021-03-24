import './App.css';
/*import Open from './Open.js';*/
import  {  FirebaseAuthConsumer,
  IfFirebaseAuthed,
  IfFirebaseUnAuthed
} from '@react-firebase/auth';
import firebase from "firebase/app";
import "firebase/auth";

import React from 'react';
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: '',

    };


  }

  render() {
    return (
      <>
      <IfFirebaseUnAuthed>
      <button
          onClick={() => {
            const googleAuthProvider = new firebase.auth.GoogleAuthProvider();
            firebase.auth().signInWithPopup(googleAuthProvider);
          }}
        >
          Sign In with Google
        </button>
      </IfFirebaseUnAuthed>
        <IfFirebaseAuthed>
        <button
          onClick={() => {
            firebase.auth().signOut();
          }}
        >
          Sign Out
        </button>
        <p> your signed in </p>
        </IfFirebaseAuthed>
      <p> hello world </p>
      </>
    )
  }
}
export default App;
