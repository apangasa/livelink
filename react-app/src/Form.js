import firebase from "firebase/app";
import "firebase/auth";
import React from 'react';
class Form extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,

    };
  }
  componentDidMount() {
    console.log(this.props.user);
    //send to raghu, then set loading false
    //if return no user, do form for new data
    //if data returned show data
    //
  }
  render() {
    return (
      <>
      <button
        onClick={() => {
          firebase.auth().signOut();
        }}
      >
        Sign Out
      </button>
      <p> Hi! {this.props.user.displayName} </p>
      <p> You linked with: 0 people loser</p> 
      <p> your signed in </p>
      <p> current data </p>
      <p> update form </p>
      </>
    )
  }
}
export default Form;
