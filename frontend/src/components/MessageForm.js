import React, { useState } from 'react';
import { sendMessage } from '../services/api';
import './MessageForm.css';

const CATEGORIES = ['Sports', 'Finance', 'Movies'];

function MessageForm({ onMessageSent }) {
  const [category, setCategory] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    // Validation
    if (!category) {
      setError('Please select a category');
      return;
    }

    if (!message || message.trim() === '') {
      setError('Message cannot be empty');
      return;
    }

    setLoading(true);

    try {
      await sendMessage(category, message);
      setSuccess('Message sent successfully!');
      setCategory('');
      setMessage('');
      // Trigger log history refresh
      if (onMessageSent) {
        setTimeout(() => {
          onMessageSent();
        }, 500); // Small delay to ensure backend has processed the message
      }
    } catch (err) {
      setError(
        err.response?.data?.message ||
        'Failed to send message. Please try again.'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="message-form">
      <div className="form-group">
        <label htmlFor="category">Category</label>
        <select
          id="category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="form-control"
          disabled={loading}
        >
          <option value="">Select a category</option>
          {CATEGORIES.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      <div className="form-group">
        <label htmlFor="message">Message</label>
        <textarea
          id="message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          className="form-control"
          rows="6"
          placeholder="Enter your message here..."
          disabled={loading}
        />
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <button
        type="submit"
        className="submit-button"
        disabled={loading}
      >
        {loading ? 'Sending...' : 'Send Message'}
      </button>
    </form>
  );
}

export default MessageForm;

