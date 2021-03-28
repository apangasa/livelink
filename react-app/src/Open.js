import React from 'react';
import Footer from './components/Footer/footer';
import Landingpage from './components/landing_page/landing_page';
import RegisterScreen from './components/RegisterScreen/RegisterScreen';
import Testimonial from './components/testimonials/testimonials';
/*import './variablesGaurang.scss';*/
import './stylesGaurang.css'

class Open extends React.Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
    <div>
    <Landingpage></Landingpage>
    <Testimonial></Testimonial>
    {/* <RegisterScreen></RegisterScreen> */}
    <Footer></Footer>
    </div>
    )
  }
}
export default Open;
