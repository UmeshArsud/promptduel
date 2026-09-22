import React, { useState, useContext } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import { Lock, Mail, User, Sparkles } from 'lucide-react';

export default function Register() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { register } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);
    try {
      await register(name, email, password);
      // Automatically navigate to login after successful registration
      navigate('/login');
    } catch (err) {
      setError(err.response?.data || 'Failed to register. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 relative overflow-hidden">
      {/* Decorative background blur */}
      <div className="absolute top-[20%] right-[-10%] w-[50%] h-[50%] bg-violet-600/30 rounded-full blur-[120px] pointer-events-none"></div>
      <div className="absolute bottom-[-10%] left-[-10%] w-[50%] h-[50%] bg-fuchsia-600/20 rounded-full blur-[120px] pointer-events-none"></div>

      <div className="glass-panel w-full max-w-md p-8 rounded-2xl relative z-10">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center p-3 bg-violet-500/10 rounded-xl mb-4">
            <Sparkles className="w-8 h-8 text-[var(--primary)]" />
          </div>
          <h1 className="text-3xl font-bold mb-2">Create Account</h1>
          <p className="text-[var(--muted-foreground)]">Join PromptDuel today</p>
        </div>

        {error && (
          <div className="bg-red-500/10 border border-red-500/20 text-red-400 p-3 rounded-lg mb-6 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium text-[var(--muted-foreground)] mb-1">Name</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <User className="h-5 w-5 text-[var(--muted-foreground)]" />
              </div>
              <input
                type="text"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="glass-input block w-full pl-10 pr-3 py-2.5 rounded-xl text-white placeholder-gray-500 focus:outline-none"
                placeholder="Jane Doe"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-[var(--muted-foreground)] mb-1">Email</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Mail className="h-5 w-5 text-[var(--muted-foreground)]" />
              </div>
              <input
                type="email"
                required
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="glass-input block w-full pl-10 pr-3 py-2.5 rounded-xl text-white placeholder-gray-500 focus:outline-none"
                placeholder="you@example.com"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-[var(--muted-foreground)] mb-1">Password</label>
            <div className="relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Lock className="h-5 w-5 text-[var(--muted-foreground)]" />
              </div>
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="glass-input block w-full pl-10 pr-3 py-2.5 rounded-xl text-white placeholder-gray-500 focus:outline-none"
                placeholder="••••••••"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="btn-primary w-full py-3 rounded-xl font-medium flex justify-center items-center mt-2"
          >
            {isSubmitting ? (
              <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
            ) : "Sign Up"}
          </button>
        </form>

        <p className="mt-8 text-center text-sm text-[var(--muted-foreground)]">
          Already have an account?{' '}
          <Link to="/login" className="text-[var(--primary)] hover:text-violet-400 font-medium transition-colors">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  );
}
