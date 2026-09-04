import { Outlet } from 'react-router';
import LogoutButton from './LogoutButton';

function AppLayout() {
  return (
    <div>
      <header>
        <h2>Project Management System</h2>
        <LogoutButton />
      </header>

      <main>
        <Outlet />
      </main>
    </div>
  );
}

export default AppLayout;