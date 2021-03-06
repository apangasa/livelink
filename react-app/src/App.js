import './App.css';
/*import Open from './Open.js';*/
import  {  FirebaseAuthConsumer,
  IfFirebaseAuthed,
  IfFirebaseUnAuthed,
} from '@react-firebase/auth';
import firebase from "firebase/app";
import "firebase/auth";
import Open from './Open.js';
import React from 'react';
import Form from './Form.js'
class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
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
        <FirebaseAuthConsumer>
          {({ isSignedIn, user, providerId }) => {
            return (
              <>
              <Form user={user} />
              </>
            );
          }}
        </FirebaseAuthConsumer>


        </IfFirebaseAuthed>
      </>
      </>
    )
  }
}
export default App;
