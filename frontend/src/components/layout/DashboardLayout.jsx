import React, { useContext } from 'react';
import { Outlet, Link, useNavigate, useLocation } from 'react-router-dom';
import { AuthContext } from '../../context/AuthContext';
import { LogOut, LayoutDashboard, Sparkles, User as UserIcon } from 'lucide-react';

export default function DashboardLayout() {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen flex bg-[var(--background)]">
      {/* Sidebar */}
      <div className="w-64 glass-panel border-y-0 border-l-0 flex flex-col z-20">
        <div className="p-6 flex items-center space-x-3">
          <div className="p-2 bg-violet-500/20 rounded-lg">
            <Sparkles className="w-6 h-6 text-[var(--primary)]" />
          </div>
          <span className="text-xl font-bold text-gradient tracking-tight">PromptDuel</span>
        </div>

        <nav className="flex-1 px-4 py-4 space-y-2">
          <Link 
            to="/" 
            className={`flex items-center space-x-3 px-4 py-3 rounded-xl transition-all duration-200 ${
              location.pathname === '/' ? 'bg-white/10 text-white font-medium' : 'text-[var(--muted-foreground)] hover:bg-white/5 hover:text-white'
            }`}
          >
            <LayoutDashboard className="w-5 h-5" />
            <span>Projects</span>
          </Link>
        </nav>

        <div className="p-4 border-t border-[var(--border)]">
          <div className="flex items-center justify-between px-2 py-2">
            <div className="flex items-center space-x-3 overflow-hidden">
              <div className="w-8 h-8 rounded-full bg-[var(--primary)] flex items-center justify-center flex-shrink-0">
                <span className="text-sm font-bold text-white">
                  {user?.name?.charAt(0).toUpperCase() || <UserIcon className="w-4 h-4" />}
                </span>
              </div>
              <div className="truncate">
                <p className="text-sm font-medium text-white truncate">{user?.name}</p>
                <p className="text-xs text-[var(--muted-foreground)] truncate">{user?.email}</p>
              </div>
            </div>
            <button 
              onClick={handleLogout}
              className="p-2 text-[var(--muted-foreground)] hover:text-red-400 hover:bg-red-500/10 rounded-lg transition-colors"
              title="Logout"
            >
              <LogOut className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>

      {/* Main Content Area */}
      <div className="flex-1 flex flex-col relative h-screen overflow-hidden">
        {/* Subtle background gradients for main area */}
        <div className="absolute top-0 right-0 w-[40%] h-[30%] bg-violet-600/10 rounded-full blur-[100px] pointer-events-none"></div>
        <div className="absolute bottom-0 left-[20%] w-[40%] h-[30%] bg-fuchsia-600/10 rounded-full blur-[100px] pointer-events-none"></div>
        
        <main className="flex-1 overflow-y-auto p-8 relative z-10">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
