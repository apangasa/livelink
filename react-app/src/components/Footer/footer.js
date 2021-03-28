import React from "react";
import "./footer.css";

function SubFooter(props) {


  return (
    <div className="sub-footer" style={{width: '100%'}}>
      <div className="overlap-group-footer" style={{width: '100%'}}>
        <p className="text-1 manrope-normal-alto-14px">© 2021 Livelink. All rights reserved</p>
        <div className="social-links">
          <div
            className="social-icons-white-instagram"
            style={{ backgroundImage: `url(${'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg'})` }}
          >
            <img className="path-1" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path@2x.svg'} />
          </div>
          <div className="social-icons-white-dribbble" style={{ backgroundImage: `url(${'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg'})` }}>
            <img className="path-1" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/path-1@2x.svg'} />
          </div>
          <div className="social-icons-white-twitter" style={{ backgroundImage: `url(${'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path-2@2x.svg'})` }}>
            <img className="path" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg'} />
          </div>
          <div className="social-icons-white-youtube" style={{ backgroundImage: `url(${'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path-3@2x.svg'})` }}>
            <img className="path-2" src={'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path-3@2x.svg'} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default SubFooter