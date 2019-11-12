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

  async componentDidMount() {
    const update = async () => {
      const res = await fetch('/api/stories');
      const messages = await res.json();
      this.setState({...this.state, messages});
      this.timeout = setTimeout(update, 250);
    }
    this.timeout = setTimeout(update, 0);
  }

  componentWillUnmount() {
    clearTimeout(this.timeout)
  }
}

