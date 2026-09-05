import { useEffect, useState } from 'react';
import './Leave.css';
import api from '../api/axios';

function Leave() {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [reason, setReason] = useState('');

  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchLeaves = async () => {
    try {
      setError('');

      const response = await api.get('/leaves/my-leaves');

      setLeaves(response.data);
    } catch (error) {
      console.error('Failed to fetch leaves:', error);
      setError('Failed to load leave requests.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLeaves();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      setError('');
      setSuccess('');

      await api.post('/leaves', {
        startDate,
        endDate,
        reason,
      });

      setStartDate('');
      setEndDate('');
      setReason('');

      setSuccess('Leave request submitted successfully.');

      await fetchLeaves();
    } catch (error) {
      console.error('Failed to submit leave:', error);
      setError('Failed to submit leave request.');
    }
  };

  return (
    <div className="leave-page">
      <div className="leave-header">
        <h1>Leave Management</h1>
        <p>Apply for leave and view your leave requests.</p>
      </div>

      <div className="leave-form-card">
        <h2>Apply for Leave</h2>

        {error && (
          <p className="leave-error">{error}</p>
        )}

        {success && (
          <p className="leave-success">{success}</p>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="startDate">Start Date</label>

            <input
              id="startDate"
              type="date"
              value={startDate}
              onChange={(event) =>
                setStartDate(event.target.value)
              }
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="endDate">End Date</label>

            <input
              id="endDate"
              type="date"
              value={endDate}
              onChange={(event) =>
                setEndDate(event.target.value)
              }
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="reason">Reason</label>

            <textarea
              id="reason"
              placeholder="Enter reason for leave"
              value={reason}
              onChange={(event) =>
                setReason(event.target.value)
              }
              rows="4"
              required
            />
          </div>

          <button type="submit">
            Apply Leave
          </button>
        </form>
      </div>

      <div className="leave-list-card">
        <h2>My Leave Requests</h2>

        {loading ? (
          <div className="leave-empty">
            <p>Loading leave requests...</p>
          </div>
        ) : leaves.length === 0 ? (
          <div className="leave-empty">
            <p>No leave requests found.</p>
          </div>
        ) : (
          <div className="leave-list">
            {leaves.map((leave) => (
              <div className="leave-item" key={leave.id}>
                <p>
                  <strong>Start Date:</strong> {leave.startDate}
                </p>

                <p>
                  <strong>End Date:</strong> {leave.endDate}
                </p>

                <p>
                  <strong>Reason:</strong> {leave.reason}
                </p>

                <p>
                  <strong>Status:</strong> {leave.status}
                </p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Leave;