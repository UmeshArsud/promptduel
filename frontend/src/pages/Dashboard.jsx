import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axiosClient from '../api/axiosClient';
import { Plus, Folder, ArrowRight } from 'lucide-react';

export default function Dashboard() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newDescription, setNewDescription] = useState('');
  const [isCreating, setIsCreating] = useState(false);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const res = await axiosClient.get('/projects');
      setProjects(res.data);
    } catch (err) {
      console.error("Failed to fetch projects", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateProject = async (e) => {
    e.preventDefault();
    setIsCreating(true);
    try {
      const res = await axiosClient.post('/projects', {
        title: newTitle,
        description: newDescription
      });
      setProjects([...projects, res.data]);
      setShowModal(false);
      setNewTitle('');
      setNewDescription('');
    } catch (err) {
      console.error("Failed to create project", err);
    } finally {
      setIsCreating(false);
    }
  };

  return (
    <div className="max-w-6xl mx-auto h-full flex flex-col">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-white mb-2">Projects</h1>
          <p className="text-[var(--muted-foreground)]">Manage and evaluate your AI prompts.</p>
        </div>
        <button 
          onClick={() => setShowModal(true)}
          className="btn-primary px-5 py-2.5 rounded-xl flex items-center space-x-2"
        >
          <Plus className="w-5 h-5" />
          <span>New Project</span>
        </button>
      </div>

      {loading ? (
        <div className="flex-1 flex items-center justify-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-[var(--primary)]"></div>
        </div>
      ) : projects.length === 0 ? (
        <div className="flex-1 flex flex-col items-center justify-center glass-panel rounded-2xl p-12 text-center">
          <div className="w-16 h-16 bg-white/5 rounded-full flex items-center justify-center mb-4">
            <Folder className="w-8 h-8 text-[var(--muted-foreground)]" />
          </div>
          <h3 className="text-xl font-medium text-white mb-2">No projects yet</h3>
          <p className="text-[var(--muted-foreground)] mb-6 max-w-sm">
            Create your first project to start comparing and evaluating different system prompts.
          </p>
          <button 
            onClick={() => setShowModal(true)}
            className="btn-secondary px-5 py-2.5 rounded-xl"
          >
            Create Project
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {projects.map((project) => (
            <Link 
              key={project.id} 
              to={`/project/${project.id}`}
              className="glass-panel p-6 rounded-2xl hover:border-[var(--primary)] transition-all duration-300 group"
            >
              <div className="flex justify-between items-start mb-4">
                <div className="p-3 bg-[var(--primary)]/10 text-[var(--primary)] rounded-xl group-hover:bg-[var(--primary)] group-hover:text-white transition-colors">
                  <Folder className="w-6 h-6" />
                </div>
                <ArrowRight className="w-5 h-5 text-[var(--muted-foreground)] group-hover:text-white group-hover:translate-x-1 transition-all" />
              </div>
              <h3 className="text-xl font-bold text-white mb-2 truncate">{project.title}</h3>
              <p className="text-[var(--muted-foreground)] text-sm line-clamp-2 mb-4 h-10">
                {project.description}
              </p>
              <div className="flex space-x-4 text-xs text-[var(--muted-foreground)]">
                <div className="flex items-center space-x-1">
                  <span className="w-2 h-2 rounded-full bg-blue-400"></span>
                  <span>{project.promptVersionCount} versions</span>
                </div>
                <div className="flex items-center space-x-1">
                  <span className="w-2 h-2 rounded-full bg-fuchsia-400"></span>
                  <span>{project.testInputCount} inputs</span>
                </div>
              </div>
            </Link>
          ))}
        </div>
      )}

      {/* Create Project Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center z-50 p-4">
          <div className="glass-panel w-full max-w-md p-6 rounded-2xl transform scale-100 animate-in fade-in zoom-in duration-200">
            <h2 className="text-2xl font-bold text-white mb-6">Create New Project</h2>
            <form onSubmit={handleCreateProject} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-[var(--muted-foreground)] mb-1">Title</label>
                <input
                  type="text"
                  required
                  value={newTitle}
                  onChange={(e) => setNewTitle(e.target.value)}
                  className="glass-input block w-full px-4 py-2.5 rounded-xl text-white placeholder-gray-500 focus:outline-none"
                  placeholder="e.g., Customer Support Bot"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-[var(--muted-foreground)] mb-1">Description</label>
                <textarea
                  value={newDescription}
                  onChange={(e) => setNewDescription(e.target.value)}
                  className="glass-input block w-full px-4 py-2.5 rounded-xl text-white placeholder-gray-500 focus:outline-none resize-none h-24"
                  placeholder="Briefly describe the goal of these prompts..."
                />
              </div>
              <div className="flex space-x-3 pt-4">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="flex-1 btn-secondary py-2.5 rounded-xl"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={isCreating}
                  className="flex-1 btn-primary py-2.5 rounded-xl flex justify-center items-center"
                >
                  {isCreating ? 'Creating...' : 'Create Project'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
