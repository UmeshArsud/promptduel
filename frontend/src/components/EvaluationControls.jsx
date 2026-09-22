import React from 'react';
import { Play, Sparkles, AlertCircle } from 'lucide-react';

export default function EvaluationControls({ onEvaluate, isEvaluating, useJudgeScoring, setUseJudgeScoring }) {
  return (
    <div className="glass-panel p-4 rounded-xl flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0 sticky top-0 z-10 mb-6 border-violet-500/30 shadow-violet-900/10 shadow-xl">
      <div className="flex items-center space-x-3">
        <div className="p-2 bg-[var(--primary)]/10 rounded-lg">
          <Sparkles className="w-5 h-5 text-[var(--primary)]" />
        </div>
        <div>
          <h2 className="text-white font-bold text-lg">Evaluation Matrix</h2>
          <p className="text-[var(--muted-foreground)] text-sm">Compare prompt versions across all test inputs.</p>
        </div>
      </div>
      
      <div className="flex items-center space-x-6">
        <div className="flex items-center space-x-3">
          <label className="flex items-center cursor-pointer">
            <div className="relative">
              <input 
                type="checkbox" 
                className="sr-only" 
                checked={useJudgeScoring}
                onChange={(e) => setUseJudgeScoring(e.target.checked)}
                disabled={isEvaluating}
              />
              <div className={`block w-12 h-6 rounded-full transition-colors ${useJudgeScoring ? 'bg-[var(--primary)]' : 'bg-[var(--muted)]'}`}></div>
              <div className={`dot absolute left-1 top-1 bg-white w-4 h-4 rounded-full transition-transform ${useJudgeScoring ? 'transform translate-x-6' : ''}`}></div>
            </div>
            <div className="ml-3 text-sm font-medium text-white flex items-center space-x-1">
              <span>Use AI Judge Scoring</span>
            </div>
          </label>
        </div>
        
        <button
          onClick={onEvaluate}
          disabled={isEvaluating}
          className="btn-primary px-6 py-2.5 rounded-xl font-medium flex items-center space-x-2"
        >
          {isEvaluating ? (
            <>
              <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              <span>Running...</span>
            </>
          ) : (
            <>
              <Play className="w-5 h-5 fill-current" />
              <span>Run Evaluation</span>
            </>
          )}
        </button>
      </div>
    </div>
  );
}
