import Layout from '../components/MyLayout';
import fetch from 'isomorphic-unfetch';
import React, { Component } from 'react'

export default class extends Component {
constructor() {
      super();
      this.state = {
        messages: [],
      };
    }

  render() {
    return (
      <Layout>
        <h1>Latest stories</h1>
        <ul>
            {this.state.messages.map((message, i) => (
            <li key={i}>
              {message}
            </li>
          ))}
        </ul>
      </Layout>
      )
  }

  componentDidMount() {
  //const res = await fetch('/api/latest');
  //const data = await res.json();
  console.log('Mounted');
  const data = {messages: ['1', '2', '3', '4']};
  this.setState({...this.state, messages: data.messages});

  //return {
    //messages: data.messages
  //};

  }


}

