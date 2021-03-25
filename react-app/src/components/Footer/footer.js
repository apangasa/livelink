import React from "react";
import "./footer.css";

function App() {
  return <SubFooter {...subFooterData} />;
}

export default App;


function SubFooter(props) {
  const {
    text1,
    socialIconsWhiteInstagram,
    path,
    socialIconsWhiteDribbble,
    path2,
    socialIconsWhiteTwitter,
    path3,
    socialIconsWhiteYoutube,
    path4,
  } = props;

  return (
    <div className="sub-footer">
      <div className="overlap-group">
        <p className="text-1 manrope-normal-alto-14px">{text1}</p>
        <div className="social-links">
          <div
            className="social-icons-white-instagram"
            style={{ backgroundImage: `url(${socialIconsWhiteInstagram})` }}
          >
            <img className="path-1" src={path} />
          </div>
          <div className="social-icons-white-dribbble" style={{ backgroundImage: `url(${socialIconsWhiteDribbble})` }}>
            <img className="path-1" src={path2} />
          </div>
          <div className="social-icons-white-twitter" style={{ backgroundImage: `url(${socialIconsWhiteTwitter})` }}>
            <img className="path" src={path3} />
          </div>
          <div className="social-icons-white-youtube" style={{ backgroundImage: `url(${socialIconsWhiteYoutube})` }}>
            <img className="path-2" src={path4} />
          </div>
        </div>
      </div>
    </div>
  );
}
const subFooterData = {
    text1: "© 2021 Livelink. All rights reserved",
    socialIconsWhiteInstagram: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg",
    path: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path@2x.svg",
    socialIconsWhiteDribbble: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg",
    path2: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/path-1@2x.svg",
    socialIconsWhiteTwitter: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg",
    path3: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path-2@2x.svg",
    socialIconsWhiteYoutube: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7ad995364b502f74b422/img/bg@2x.svg",
    path4: "https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c935e57894026b3396d78/img/path-3@2x.svg",
};

