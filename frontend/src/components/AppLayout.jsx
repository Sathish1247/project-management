import { Outlet, NavLink } from 'react-router';
import LogoutButton from './LogoutButton';
import './AppLayout.css';

function AppLayout() {
  return (
    <div className="app-layout">
      <header className="app-header">
        <h2>Project Management System</h2>

        <nav className="app-nav">
          <NavLink
            to="/dashboard"
            className={({ isActive }) =>
              isActive ? 'nav-link active' : 'nav-link'
            }
          >
            Dashboard
          </NavLink>

          <NavLink
            to="/my-tasks"
            className={({ isActive }) =>
              isActive ? 'nav-link active' : 'nav-link'
            }
          >
            My Tasks
          </NavLink>

          <NavLink
            to="/leave"
            className={({ isActive }) =>
              isActive ? 'nav-link active' : 'nav-link'
            }
          >
            Leave
          </NavLink>
        </nav>

        <LogoutButton />
      </header>

      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
}

export default AppLayout;