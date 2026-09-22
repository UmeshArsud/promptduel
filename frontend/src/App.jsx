import React, { useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthContext, AuthProvider } from './context/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import DashboardLayout from './components/layout/DashboardLayout';
import Dashboard from './pages/Dashboard';
import ProjectDetails from './pages/ProjectDetails';
import EvaluationResults from './pages/EvaluationResults';

const ProtectedRoute = ({ children }) => {
  const { token, loading } = useContext(AuthContext);
  if (loading) return <div className="min-h-screen flex items-center justify-center">Loading...</div>;
  if (!token) return <Navigate to="/login" replace />;
  return children;
};

function AppRoutes() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        
        <Route path="/" element={<ProtectedRoute><DashboardLayout /></ProtectedRoute>}>
          <Route index element={<Dashboard />} />
          <Route path="project/:id" element={<ProjectDetails />} />
          <Route path="project/:id/results" element={<EvaluationResults />} />
        </Route>
      </Routes>
    </Router>
  );
}

function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;
