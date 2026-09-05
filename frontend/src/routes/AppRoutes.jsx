import { Routes, Route, Navigate } from 'react-router';
import Login from '../pages/Login';
import Dashboard from '../pages/Dashboard';
import TaskBoard from '../pages/TaskBoard';
import ProtectedRoute from './ProtectedRoute';
import AppLayout from '../components/AppLayout';
import Leave from '../pages/Leave';

function AppRoutes() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route
        element={
          <ProtectedRoute>
            <AppLayout />
          </ProtectedRoute>
        }
      >
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/my-tasks" element={<TaskBoard />} />
        <Route path="/leave" element={<Leave />} />
      </Route>

      <Route
        path="*"
        element={<Navigate to="/login" replace />}
      />
    </Routes>
  );
}

export default AppRoutes;