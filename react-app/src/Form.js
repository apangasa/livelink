import firebase from "firebase/app";
import "firebase/auth";
import React from 'react';
import './form.css';
import Switch from 'react-switch'


class Form extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      name: '',
      email: '',
      linkCount: 'loading',
      isPrivate: false,
      bio: '',
      profilePic: '',
      isPic: false,
      newName:'',
      newBio: '',
      file: null,
      base64URL: ""

    };
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleChangeName = this.handleChangeName.bind(this);
    this.handleChangeBio = this.handleChangeBio.bind(this);
    this.handleChangePrivate = this.handleChangePrivate.bind(this)
  }

  getBase64 = file => {
   return new Promise(resolve => {
     let fileInfo;
     let baseURL = "";
     // Make new FileReader
     let reader = new FileReader();

     // Convert the file to base64 text
     reader.readAsDataURL(file);

     // on reader load somthing...
     reader.onload = () => {
       // Make a fileInfo Object
       console.log("Called", reader);
       baseURL = reader.result;
       console.log(baseURL);
       resolve(baseURL);
     };
     console.log(fileInfo);
   });
 };

 handleFileInputChange = e => {
    console.log(e.target.files[0]);
    let { file } = this.state;

    file = e.target.files[0];

    this.getBase64(file)
      .then(result => {
        file["base64"] = result;
        console.log("File Is", file);
        this.setState({
          base64URL: result,
          file,
          profilePic: result
        });
      })
      .catch(err => {
        console.log(err);
      });

    this.setState({
      file: e.target.files[0]
    });
  };



  handleSubmit(event) {
    let myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    let body = JSON.stringify({
        name: this.state.newName,
        email: this.state.email,
        numLinkConnections: this.state.linkCount,
        isPrivate: this.state.isPrivate,
        bio: this.state.newBio,
        profilePic: this.state.profilePic
    });

  let requestOptions = {
  method: 'POST',
  redirect: 'follow',
  body: body,
  headers: myHeaders
  };

    fetch("https://cors-anywhere.herokuapp.com/http://129.213.124.196:3000/insert-profile?id="+this.props.user.uid, requestOptions)
    this.setState({name: this.state.newName,
    bio: this.state.newBio
    })
    event.preventDefault();
  }
  handleChangePrivate() {
    this.setState({isPrivate: !this.state.isPrivate});
  }
  handleChangeName(event) {
    this.setState({newName: event.target.value})
  }
  handleChangeBio(event) {
    this.setState({newBio: event.target.value})
  }
  handleChangePrivate(event) {
    this.setState({isPrivate: !this.state.isPrivate})
  }
  componentDidMount() {
    console.log(this.props.user);
    let requestOptions = {
  method: 'GET',
  redirect: 'follow'
};
  let output;
  console.log("hello");
fetch("https://cors-anywhere.herokuapp.com/http://129.213.124.196:3000/get?id="+this.props.user.uid, requestOptions)
  .then(response => response.json())
  .then(data => {
    this.setState({
      bio: data.bio,
      name: data.name,
      isPrivate: data.isPrivate,
      linkCount: data.numLinkConnections,
      profilePic: data.profilePic
    });
  })
  .catch(error => {
    console.log("hello3")
    this.setState({
      loading: false,
    name: 'Add Name',
    linkCount: 0,
    isPrivate: false,
    bio: 'Add bio!',
    profilePic: '',
    isPic: false,
    newName:'',
    newBio: '',
    file: null,
    base64URL: ""})

    console.log('error', error)}
  );
    //send to raghu, then set loading false
    //if return no user, do form for new data
    //if data returned show data
    console.log(output);
    console.log("user recieved");


  }
  render() {
    return (
      <>

      <div className="main-content" >
      <div style={{width: '100%vw', backgroundColor: 'purple'}}></div>

  <div  className="header pb-8 pt-5 pt-lg-8 d-flex align-items-center"  >

    <div style={{backgroundColor:'#906CE0'}} className="mask bg">
    <button
        onClick={() => {
          firebase.auth().signOut();
        }}
        style={{backgroundColor: 'black', width: '140px', height:'50px', borderRadius: '20px', fontSize:'20px'}}
      className={"login manrope-bold-electric-violet-14px"}>
        Sign Out
      </button>
    </div>
    <div  className="container-fluid d-flex align-items-center">

      <div className="row width-wide">
        <div className="col-lg-7 col-md-10">
          <h1 className="display-2 text-white">Hello {this.state.name}</h1>
          <p className="text-white mt-0 mb-5">This is your profile page. Tell us about yourself!</p>
        </div>
      </div>
    </div>
  </div>
  <div className="container-fluid mt--7">
    <div className="row">
      <div className="col-xl-4 order-xl-2 mb-5 mb-xl-0">
        <div className="card card-profile shadow">
          <div className="row justify-content-center">
            <div className="col-lg-3 order-lg-2">
              <div className="card-profile-image">
                {this.state.profilePic ? <img src={this.state.profilePic} className={"rounded-circle"}/>: '' }
              </div>

            </div>

          </div>

          <div className="card-header text-center border-0 pt-8 pt-md-4 pb-0 pb-md-4">

          </div>
          <div className="card-body pt-0 pt-md-4">
            <div className="row">
              <div className="col">
                <div className="card-profile-stats d-flex justify-content-center mt-md-5">
                  <div>
                  <input type="file" name="file" onChange={this.handleFileInputChange} />
                    <span className="heading">{this.state.linkCount}</span>
                    <span className="description">Links</span>
                  </div>
                </div>
              </div>
            </div>
            <div className="text-center">
              <h3>
                {this.state.name}
              </h3>

            </div>
          </div>
        </div>
      </div>
      <div className="col-xl-8 order-xl-1">
        <div className="card bg-secondary shadow">
          <div className="card-header bg-white border-0">
            <div className="row align-items-center">
              <div className="col-8">
                <h3 className="mb-0">My account</h3>
              </div>
              <div className="col-4 text-right">
              <div>
              <h5>{this.state.isPrivate ? "Private" : "Public" }</h5>
                <Switch href="#!" onChange={this.handleChangePrivate} checked={this.state.isPrivate}></Switch>
                </div>
              </div>
            </div>
          </div>
          <div className="card-body">
            <form>
              <h6 className="heading-small text-muted mb-4">User information</h6>
              <div className="pl-lg-4">
                <div className="row">
                  <div className="col-lg-6">
                    <div className="form-group focused">
                      <label className="form-control-label" htmlFor="input-username">Name</label>
                      <input type="text" id="input-username" className="form-control form-control-alternative" onChange={this.handleChangeName} defaultValue={this.state.name} />
                    </div>
                  </div>
                  <div className="col-lg-6">
                    <div className="form-group">
                      <label className="form-control-label" htmlFor="input-email">Email address</label>
                      <input type="email" id="input-email" className="form-control form-control-alternative" defaultValue={this.props.user.email} />
                    </div>
                  </div>
                </div>
              </div>
              {/*<hr className="my-4">*/}
              {/*<!-- Address -->*/}

              {/*<hr className="my-4">*/}
              {/*<!-- Description -->*/}
              <h6 className="heading-small text-muted mb-4">About me</h6>
              <div className="pl-lg-4">
                <div className="form-group focused">
                  <label>About Me</label>
                  <textarea rows="4" className="form-control form-control-alternative" onChange={this.handleChangeBio} defaultValue={this.state.bio}></textarea>
                </div>
              </div>
              <div className="pl-lg-4">
                <div className="form-group focused">
                  <button style={{backgroundColor: 'black', width: '140px', height:'50px', borderRadius: '20px', fontSize:'20px', color:'#906CE0'}}  onClick={this.handleSubmit}> Update</button>
                </div>
              </div>

            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</>
    )
  }
}
export default Form;
