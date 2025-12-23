import React, { useRef } from 'react';
import './App.css';
import MessageForm from './components/MessageForm';
import LogHistory from './components/LogHistory';

function App() {
  const logHistoryRef = useRef(null);

  const handleMessageSent = () => {
    if (logHistoryRef.current) {
      logHistoryRef.current.refresh();
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Notification System</h1>
      </header>
      <main className="App-main">
        <div className="container">
          <section className="form-section">
            <h2>Send Message</h2>
            <MessageForm onMessageSent={handleMessageSent} />
          </section>
          <section className="log-section">
            <h2>Notification Log History</h2>
            <LogHistory ref={logHistoryRef} />
          </section>
        </div>
      </main>
    </div>
  );
}

export default App;

