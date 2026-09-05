import { useEffect, useState } from 'react';
import './Dashboard.css';
import api from '../api/axios';

function Dashboard() {
  const [tasks, setTasks] = useState([]);
  const [leaves, setLeaves] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchDashboardData = async () => {
    try {
      setError('');

      const [tasksResponse, leavesResponse] = await Promise.all([
        api.get('/tasks/my-tasks'),
        api.get('/leaves/my-leaves'),
      ]);

      setTasks(tasksResponse.data);
      setLeaves(leavesResponse.data);
    } catch (error) {
      console.error('Failed to load dashboard data:', error);
      setError('Failed to load dashboard data.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const totalTasks = tasks.length;

  const completedTasks = tasks.filter(
    (task) => task.status === 'COMPLETED'
  ).length;

  const pendingLeaves = leaves.filter(
    (leave) => leave.status === 'PENDING'
  ).length;

  const recentTasks = tasks.slice(-5).reverse();
  const recentLeaves = leaves.slice(-5).reverse();

  if (loading) {
    return (
      <div className="dashboard-page">
        <p className="dashboard-message">
          Loading dashboard...
        </p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard-page">
        <p className="dashboard-error">{error}</p>
      </div>
    );
  }

  return (
    <div className="dashboard-page">
      <div className="dashboard-header">
        <h1>Dashboard</h1>
        <p>Overview of your project management activities.</p>
      </div>

      <div className="dashboard-cards">
        <div className="dashboard-card">
          <h2>My Tasks</h2>
          <p className="dashboard-card-value">
            {totalTasks}
          </p>
          <p className="dashboard-card-label">
            Assigned to you
          </p>
        </div>

        <div className="dashboard-card">
          <h2>Completed Tasks</h2>
          <p className="dashboard-card-value">
            {completedTasks}
          </p>
          <p className="dashboard-card-label">
            Tasks completed
          </p>
        </div>

        <div className="dashboard-card">
          <h2>Pending Leaves</h2>
          <p className="dashboard-card-value">
            {pendingLeaves}
          </p>
          <p className="dashboard-card-label">
            Awaiting approval
          </p>
        </div>
      </div>

      <div className="dashboard-section">
        <h2>Recent Tasks</h2>

        {recentTasks.length === 0 ? (
          <div className="dashboard-empty">
            <p>No tasks assigned to you.</p>
          </div>
        ) : (
          <div className="dashboard-list">
            {recentTasks.map((task) => (
              <div
                className="dashboard-list-item"
                key={task.id}
              >
                <div>
                  <h3>{task.title}</h3>
                  <p>{task.description}</p>
                </div>

                <span className="dashboard-status">
                  {task.status}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>

      <div className="dashboard-section">
        <h2>Recent Leave Requests</h2>

        {recentLeaves.length === 0 ? (
          <div className="dashboard-empty">
            <p>No leave requests found.</p>
          </div>
        ) : (
          <div className="dashboard-list">
            {recentLeaves.map((leave) => (
              <div
                className="dashboard-list-item"
                key={leave.id}
              >
                <div>
                  <h3>
                    {leave.startDate} to {leave.endDate}
                  </h3>

                  <p>{leave.reason}</p>
                </div>

                <span className="dashboard-status">
                  {leave.status}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;