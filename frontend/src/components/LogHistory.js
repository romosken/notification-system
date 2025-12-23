import React, { useState, useEffect, useImperativeHandle, forwardRef } from 'react';
import { getLogHistory } from '../services/api';
import './LogHistory.css';

const LogHistory = forwardRef((props, ref) => {
  const [logs, setLogs] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const pageSize = 10;

  const fetchLogs = async (page) => {
    setLoading(true);
    setError('');
    try {
      const data = await getLogHistory(page, pageSize);
      setLogs(data.content || []);
      setTotalPages(data.totalPages || 0);
      setTotalElements(data.totalElements || 0);
    } catch (err) {
      setError('Failed to load log history. Please try again.');
      console.error('Error fetching logs:', err);
    } finally {
      setLoading(false);
    }
  };

  useImperativeHandle(ref, () => ({
    refresh: () => {
      fetchLogs(0);
      setCurrentPage(0);
    }
  }));

  useEffect(() => {
    fetchLogs(currentPage);
  }, [currentPage]);

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      const date = new Date(dateString);
      return date.toLocaleString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      });
    } catch (e) {
      return dateString;
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages - 1) {
      setCurrentPage(currentPage + 1);
    }
  };

  if (loading && logs.length === 0) {
    return <div className="loading">Loading log history...</div>;
  }

  return (
    <div className="log-history">
      {error && <div className="alert alert-error">{error}</div>}

      {logs.length === 0 && !loading ? (
        <div className="empty-state">No log entries found.</div>
      ) : (
        <>
          <div className="log-header">
            <p className="log-count">
              Showing {logs.length} of {totalElements} entries
            </p>
          </div>

          <div className="log-list">
            {logs.map((log) => (
              <div key={log.id} className="log-item">
                <div className="log-item-header">
                  <span className="log-category">{log.categoryName}</span>
                  <span className="log-channel">{log.channelName}</span>
                </div>
                <div className="log-message">{log.message}</div>
                <div className="log-item-footer">
                  <span className="log-user">{log.userName || 'Unknown User'}</span>
                  <span className="log-date">{formatDate(log.sentAt)}</span>
                </div>
              </div>
            ))}
          </div>

          <div className="pagination">
            <button
              onClick={handlePreviousPage}
              disabled={currentPage === 0 || loading}
              className="pagination-button"
            >
              Previous
            </button>
            <span className="pagination-info">
              Page {currentPage + 1} of {totalPages || 1}
            </span>
            <button
              onClick={handleNextPage}
              disabled={currentPage >= totalPages - 1 || loading}
              className="pagination-button"
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
});

LogHistory.displayName = 'LogHistory';

export default LogHistory;

