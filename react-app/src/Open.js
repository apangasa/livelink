import React from 'react';
import Footer from './components/Footer/footer';
import Landingpage from './components/landing_page/landing_page';
import Testimonial from './components/testimonials/testimonials';
/*import './variablesGaurang.scss';*/
import './stylesGaurang.css'

class Open extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: '',

    };


  }

  render() {
    return (
    //   <div className="e1_52">
    //     <div className="e1_54" />
    //     <div className="e1_55">
    //       <div className="e1_56">
    //         <div className="e1_57" />
    //         <div className="e1_58" />
    //         <div className="e1_59" />
    //         <div className="e1_60" />
    //         <div className="e1_61" />
    //       </div>
    //       <div className="e1_62">
    //         <div className="e1_63" />
    //         <div className="e1_64" />
    //         <div className="e1_65" />
    //         <div className="e1_66" />
    //         <div className="e1_67" />
    //       </div>
    //       <div className="e1_68">
    //         <div className="e1_69" />
    //         <div className="e1_70" />
    //         <div className="e1_71" />
    //         <div className="e1_72" />
    //         <div className="e1_73" />
    //       </div>
    //       <div className="e1_74">
    //         <div className="e1_75" />
    //         <div className="e1_76" />
    //         <div className="e1_77" />
    //         <div className="e1_78" />
    //         <div className="e1_79" />
    //       </div>
    //       <div className="e1_80">
    //         <div className="e1_81" />
    //         <div className="e1_82" />
    //         <div className="e1_83" />
    //         <div className="e1_84" />
    //         <div className="e1_85" />
    //       </div>
    //     </div>
    //     <div className="e1_86" />
    //     <div className="e1_87" />
    //     <div className="e1_88" />
    //     <div className="e1_807" />
    //     <div className="e1_93" />
    //     <div className="e1_94" />
    //     <div className="e1_95" />
    //     <div className="e1_96">

    //       <div className="e1_97"><span className="e1_98">Connect over AR</span><span className="e1_99">At Livelink, we believe seeing is connecting!</span></div>
    //       <div className="e1_263"><span className="e1_264">Create your Livelink avatar/profile</span></div>
    //     </div>
    //     <div className="e1_156">
    //       <div className="e1_158">
    //         <div className="e1_187"><span className="e1_188">Login</span></div>
    //         <div className="e1_189"><span className="e1_190">Sign up</span></div>
    //       </div>
    //       <div className="e1_161">
    //         <div className="e1_163">
    //           <div className="e1_205"><span className="e1_206">About</span></div>
    //           <div className="e1_213"><span className="e1_214">Jobs</span></div>
    //           <div className="e1_211"><span className="e1_212">Blog</span></div>
    //           <div className="e1_215">
    //             <span className="e1_216">More</span>
    //             <div className="e1_219">
    //               <div className="e1_220" />
    //             </div>
    //           </div>
    //         </div>
    //       </div>
    //     </div>
    //   </div>
    <div>
    <Landingpage></Landingpage>
    <Testimonial></Testimonial>
    </div>
    )
  }
}
export default Open;
