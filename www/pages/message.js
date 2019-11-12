import Layout from '../components/MyLayout.js'
import React, { Component } from 'react'

export default class extends Component {
    constructor() {
      super();
      this.state = {
        value: '',
      };
    }
    handleChange = evt => {
    // This triggers everytime the input is changed
        this.setState({
           value: evt.target.value,
        });
    };

    submitForm = evt => {
      evt.preventDefault();
      fetch('/api/stories', {
        method: 'post',
        headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({"message": this.state.value})
      }).then((res) => {
        res.status === 200 ? this.setState({ submitted: true }) : ''
      })
    }


  render() {
    return (
        <Layout>
          <p>This is the message page</p>
          <form onSubmit={this.submitForm}>
            <label>
              <textarea rows="5" value={this.state.value} onChange={this.handleChange} />
            </label>
            <br/>
            <input type="submit" value="Submit" />
          </form>

        </Layout>
      )
  }

}
