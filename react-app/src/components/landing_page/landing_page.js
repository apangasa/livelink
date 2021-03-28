import React from "react";
import Footer from "../Footer/footer";
import Links from "../Footer/footer";
import "./landing_page.css";
import "firebase/auth";
import firebase from "firebase/app";

function Landingpage(props) {

  return (
      <div>


    <div className="landingpage" style={{ width: '100%'}}>
      <div style = {{backgroundColor: 'blue'}}>
      <div className="overlap-group">
        <div className="content">
          <div className="header-copy">
            <h1 className="title manrope-extra-bold-eerie-black-72px">Connect over AR</h1>
            </div>
            <div className="adjustable-primary-large">
              <div className="label manrope-bold-white-20px">Create your Livelink avatar/profile</div>
              </div>
            </div>
            </div>
        <div style={{ paddingLeft: '40vw', width: '100%'}} className="frame-2">
          <img className="user-5" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/user-5@2x.svg' />
        </div>
        <img className="half-circle-1" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/half-circle-1@2x.svg' />
        <img className="user-3" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/user-3@2x.svg' />
        <img className="user-4" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/user-4@2x.svg' />
        <img className="user-2" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/user-2@2x.svg' />
        <div className="header-1">
          <div className="left-nav">
            <div className="nav-items">
              <div className="label-1 manrope-bold-eerie-black-14px">About</div>
              <div className="label-2 manrope-bold-eerie-black-14px">Jobs</div>
              <div className="label-3 manrope-bold-eerie-black-14px">Blog</div>
              <div className="header-menu-dropdown">
                <div className="label-4 manrope-bold-eerie-black-14px">More</div>
                <div className="essential-icons-chevron-down">
                  <img className="vector" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/vector@2x.svg' />
                </div>
              </div>
              <div className="menu-item-default"></div>
            </div>
          </div>
          <div className="right-nav">
            <div className="adjustable-secondary-medium">
              <div  className="login manrope-bold-electric-violet-14px"
                  onClick={() => {
                    const googleAuthProvider = new firebase.auth.GoogleAuthProvider();
                    firebase.auth().signInWithPopup(googleAuthProvider);
                  }}
                >
                  Login
                </div>
            </div>
            <div className="adjustable-primary-medium">
              <div className="sign-up manrope-bold-white-14px"
              onClick={() => {
                const googleAuthProvider = new firebase.auth.GoogleAuthProvider();
                firebase.auth().signInWithPopup(googleAuthProvider);
              }}
              >Sign up</div>
            </div>
          </div>
        </div>
      </div>
      <img className="image-2" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/6059c6f93ce41a6c7995fa21/img/image-2@2x.svg' />
    </div>



    {/* <div style={{width: "1440px", height: "529px", backgroundColor: 'black' }}>
    <Links></Links>

    </div> */}

</div>


  );
}


export default Landingpage
