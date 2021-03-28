import React from "react";
import "./RegiterScreen.css";




function RegisterScreen(props) {


  return (
    <div style={{backgroundColor: '#906CE0'}}>
    <h1 style={{color: 'white', fontSize: '70px', paddingLeft: '600px'}}>Tell us about yourself</h1>
    <div className="form"  style={{paddingLeft: '600px'}}>

      <div className="overlap-group4">

        <div className="place valign-text-middle inter-regular-normal-white-21-3px">Name</div>
        <input className='rectangle-24'></input>
      </div>
      <div className="overlap-group1">
        <div className="email valign-text-middle inter-regular-normal-white-21-3px">Email</div>
        <input className="rectangle-24"></input>
      </div>
      <div className="flex-row">
        <div className="overlap-group3">
          <div className="image valign-text-middle inter-regular-normal-white-21-3px">Image</div>
          <input className="rectangle-243"></input>
        </div>
        <div className="overlap-group2">
          <img className="icons8-upload-24-1" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605f335d71e52022c867a447/img/icons8-upload-24-1@2x.png'} />
        </div>
      </div>
      <div className="flex-row-1">
        <div className="overlap-group">
          <div className="hobbies valign-text-middle inter-regular-normal-white-21-3px">Hobbies</div>
          <input className="rectangle-246"></input>
        </div>
        <img className="image-8" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605f335d71e52022c867a447/img/image-8@2x.png'} />
      </div>
      <div className="text-1 valign-text-middle inter-regular-normal-white-21-3px" style={{paddingLeft: '400px'}}>Would you like to keep your account Public?</div>
      <div className="flex-row-2" style={{paddingLeft: '400px'}}>
        <div className="private valign-text-middle inter-regular-normal-white-21-3px">Private</div>
        <div className="switchswitch">
          <div className="overlap-group5">
            <div className="x-knob-base"></div>
            <div className="x-knob-indicator-on"></div>
          </div>
        </div>
        <div className="public valign-text-middle inter-regular-normal-white-21-3px">Public</div>
      </div>
    </div>
    </div>
  );
}



export default RegisterScreen
