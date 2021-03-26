import './App.css';
/*import Open from './Open.js';*/
import  {  FirebaseAuthConsumer,
  IfFirebaseAuthed,
  IfFirebaseUnAuthed
} from '@react-firebase/auth';
import { FirebaseDatabaseNode } from "@react-firebase/database";

import firebase from "firebase/app";
import "firebase/auth";
import Open from './Open.js';
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
      <>
      <IfFirebaseUnAuthed>

        <Open />

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
        <p> current data </p>
        <FirebaseDatabaseNode path="user/">
      {data => {
        const { value } = data;
        if (value === null || typeof value === "undefined") return null;
        const keys = Object.keys(value);
        const values = Object.values(value);
        console.log(keys);
        console.log(values);
        return <>< />;
      }}
    </FirebaseDatabaseNode>
        <p> update form </p>
        </IfFirebaseAuthed>
      </>
      </>
    )
  }
}
export default App;
