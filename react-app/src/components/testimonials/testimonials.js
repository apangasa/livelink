import React from "react";
import "./testimonials.css";



function Testimonial(props) {


  return (
      <div style={{backgroundColor: '#906CE0', width: "100%", height: '610px'}}>
    <div className="testimonial" style={{paddingLeft: '200px'}}>
      <div className="whole-testimonial">
        <div className="title-8-col-left-align">
          <h1 className="section-title manrope-extra-bold-white-48px">Our users tell us</h1>
        </div>
        <div style = {{backgroundColor: '#906CE0'}} className="flex-row">
          <div  className="overlap-group">
            <img className="card" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7816cea96f8233a2e6cb/img/card@1x.svg' />
            <div className="incredible-experienc manrope-medium-eerie-black-18px">Incredibly easy to use</div>
            <p className="we-had-an-incredible manrope-normal-eerie-black-14px">I never realized the true power of connection over AR until I used Livelink. I have met amazing people through Livelink and even gotten a date through it! Plus, everybody recognizes me and my TikTok videos, so I often have fans walk up to me. I have connected with hundereds of people  through Livelink. I’m telling you guys, it’s the next big thing!</p>
            <div className="x-client-details-2-white-text">
              <div className="person-details" style={{ backgroundImage: `url(${'https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7816cea96f8233a2e6cb/img/photo@2x.svg'})` }}></div>
              <div className="name manrope-bold-white-18px">Jane Cooper</div>
            </div>
          </div>
          <div className="cards-testimonial-3">
            <div className="overlap-group1">
              <div className="quote-mark">
                <img className="image" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7816cea96f8233a2e6cb/img/-@2x.svg' />
              </div>
              <img className="card-1" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7816cea96f8233a2e6cb/img/card@1x.svg' />
              <div className="title manrope-medium-eerie-black-18px">Livelink is the future of social media</div>
              <p className="content manrope-normal-eerie-black-14px">My wife introduced me to Livelink. At first, I was confused about its use case. One day, I used Livelink in the middle of a conversation with my friend to see what was up on his story. I was easily able to enagage him in an interesting conversation, and ended up feeling great about myself. Livelink is bound to improve your close relations!</p>

              <div className="x-client-details-2-white-text-1">
                <img className="image-1" src='https://anima-uploads.s3.amazonaws.com/projects/60594a944e78cf803e37e4a7/releases/605c7816cea96f8233a2e6cb/img/image-1@2x.svg' />
                <div className="name-1 manrope-bold-white-18px">Alvin Smith</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </div>
  );
}


export default Testimonial
