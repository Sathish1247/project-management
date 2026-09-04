import { useEffect, useState } from 'react';
import api from '../api/axios';
import './TaskBoard.css';

function TaskBoard() {
  const [tasks, setTasks] = useState([]);
  const [selectedStatuses, setSelectedStatuses] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchTasks = async () => {
    try {
      setError('');

      const response = await api.get('/tasks/my-tasks');

      setTasks(response.data);

      const initialStatuses = {};

      response.data.forEach((task) => {
        initialStatuses[task.id] = task.status;
      });

      setSelectedStatuses(initialStatuses);

    } catch (error) {
      console.error('Failed to fetch tasks:', error);
      setError('Failed to load tasks.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const handleStatusChange = (taskId, newStatus) => {
    setSelectedStatuses((currentStatuses) => ({
      ...currentStatuses,
      [taskId]: newStatus,
    }));
  };

  const handleSubmit = async (taskId) => {
    const newStatus = selectedStatuses[taskId];

    try {
      setError('');

      await api.put(
        `/tasks/${taskId}/status`,
        {
          status: newStatus,
        }
      );

      await fetchTasks();

    } catch (error) {
      console.error('Failed to update task status:', error);
      setError('Failed to update task status.');
    }
  };

  if (loading) {
    return (
      <div className="task-board">
        <p className="task-message">Loading tasks...</p>
      </div>
    );
  }

  if (error && tasks.length === 0) {
    return (
      <div className="task-board">
        <p className="task-error">{error}</p>
      </div>
    );
  }

  return (
    <div className="task-board">
      <div className="task-board-header">
        <h1>My Tasks</h1>
        <p>Tasks assigned to you</p>
      </div>

      {error && (
        <p className="task-error">{error}</p>
      )}

      {tasks.length === 0 ? (
        <div className="empty-tasks">
          <p>No tasks assigned to you.</p>
        </div>
      ) : (
        <div className="task-list">
          {tasks.map((task) => (
            <div className="task-card" key={task.id}>
              <h2>{task.title}</h2>

              <p className="task-description">
                {task.description}
              </p>

              <div className="task-status">
                <label htmlFor={`status-${task.id}`}>
                  Status
                </label>

                <select
                  id={`status-${task.id}`}
                  value={
                    selectedStatuses[task.id] || task.status
                  }
                  onChange={(event) =>
                    handleStatusChange(
                      task.id,
                      event.target.value
                    )
                  }
                >
                  <option value="TODO">TODO</option>
                  <option value="IN_PROGRESS">
                    IN_PROGRESS
                  </option>
                  <option value="COMPLETED">
                    COMPLETED
                  </option>
                </select>

                <button
                  onClick={() => handleSubmit(task.id)}
                >
                  Submit
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default TaskBoard;