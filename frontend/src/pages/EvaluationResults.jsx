import React from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { ArrowLeft, CheckCircle, XCircle, BarChart3, AlertTriangle } from 'lucide-react';

const ErrorDisplay = ({ errorMessage }) => {
  const [expanded, React_useState] = React.useState(false);

  const getShortMessage = (msg) => {
    if (!msg) return 'Failed';
    if (msg.includes('429 Too Many Requests') || msg.includes('Quota exceeded')) {
      return 'Rate limit exceeded — please retry shortly.';
    }
    if (msg.includes('503 Service Unavailable')) {
      return 'Gemini API is currently overloaded — please try again later.';
    }
    if (msg.includes('400 Bad Request')) {
      return 'Invalid request sent to Gemini API.';
    }
    const shortMsg = msg.split('\n')[0].substring(0, 100);
    return shortMsg + (msg.length > 100 ? '...' : '');
  };

  return (
    <div className="bg-red-500/10 border border-red-500/20 p-3 rounded-lg text-red-400 text-xs font-mono break-words">
      <div className="flex items-center space-x-1 mb-1">
        <XCircle className="w-4 h-4 flex-shrink-0" />
        <span className="font-semibold">{getShortMessage(errorMessage)}</span>
      </div>
      {errorMessage && errorMessage.length > 100 && (
        <div className="mt-2">
          <button 
            onClick={() => React_useState(!expanded)} 
            className="text-[10px] underline hover:text-red-300"
          >
            {expanded ? 'Hide details' : 'Show details'}
          </button>
          {expanded && (
            <div className="mt-2 p-2 bg-black/40 rounded border border-red-500/30 whitespace-pre-wrap text-[10px] max-h-32 overflow-y-auto custom-scrollbar">
              {errorMessage}
            </div>
          )}
        </div>
      )}
    </div>
  );
};
export default function EvaluationResults() {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  
  const result = location.state?.evaluationResult;

  if (!result) {
    return (
      <div className="h-full flex flex-col items-center justify-center text-center p-8">
        <AlertTriangle className="w-12 h-12 text-yellow-500 mb-4" />
        <h2 className="text-xl font-bold text-white mb-2">No Evaluation Data</h2>
        <p className="text-[var(--muted-foreground)] mb-6">Please run an evaluation from the project details page first.</p>
        <button onClick={() => navigate(`/project/${id}`)} className="btn-secondary px-6 py-2 rounded-xl">
          Go Back
        </button>
      </div>
    );
  }

  // Get unique prompt versions and test inputs from the flat runs list
  const versions = [...new Set(result.runs.map(r => r.versionLabel))].sort();
  const inputs = [...new Set(result.runs.map(r => r.inputText))];
  
  // Create a map to quickly look up a run by (input, version)
  const runMap = {};
  result.runs.forEach(run => {
    runMap[`${run.inputText}|${run.versionLabel}`] = run;
  });

  const getWinner = () => {
    if (!result.averageScoresByVersion) return null;
    let winner = null;
    let maxScore = -1;
    for (const [v, score] of Object.entries(result.averageScoresByVersion)) {
      if (score > maxScore) {
        maxScore = score;
        winner = v;
      }
    }
    return winner;
  };
  
  const winner = getWinner();

  return (
    <div className="max-w-7xl mx-auto flex flex-col h-full">
      <div className="mb-6 flex items-center justify-between">
        <div className="flex items-center space-x-4">
          <button onClick={() => navigate(`/project/${id}`)} className="p-2 glass-panel rounded-lg hover:bg-white/10 text-white transition-colors">
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <h1 className="text-3xl font-bold text-white">Evaluation Results</h1>
            <p className="text-[var(--muted-foreground)]">Compare scores across prompt versions.</p>
          </div>
        </div>
        
        <div className="flex space-x-4">
          <div className="glass-panel px-4 py-2 rounded-xl flex flex-col items-center justify-center">
            <span className="text-xs text-[var(--muted-foreground)] uppercase">Successful</span>
            <span className="text-lg font-bold text-green-400">{result.successfulRuns}</span>
          </div>
          <div className="glass-panel px-4 py-2 rounded-xl flex flex-col items-center justify-center">
            <span className="text-xs text-[var(--muted-foreground)] uppercase">Failed</span>
            <span className="text-lg font-bold text-red-400">{result.failedRuns}</span>
          </div>
        </div>
      </div>

      <div className="flex-1 overflow-auto custom-scrollbar">
        <div className="inline-block min-w-full align-middle">
          <div className="glass-panel rounded-2xl overflow-hidden border border-[var(--border)]">
            <table className="min-w-full divide-y divide-[var(--border)]">
              <thead className="bg-black/20">
                <tr>
                  <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-white sm:pl-6 w-1/4">
                    Test Input
                  </th>
                  {versions.map(v => (
                    <th key={v} scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-white relative">
                      {v === winner && (
                        <div className="absolute -top-3 left-1/2 transform -translate-x-1/2 bg-yellow-500/20 text-yellow-400 border border-yellow-500/50 text-[10px] uppercase px-2 py-0.5 rounded-full flex items-center space-x-1">
                          <Sparkles className="w-3 h-3" />
                          <span>Winner</span>
                        </div>
                      )}
                      <div className="flex flex-col">
                        <span className={`text-lg ${v === winner ? 'text-yellow-400' : 'text-blue-400'}`}>{v}</span>
                        {result.averageScoresByVersion[v] && (
                          <span className="text-xs font-normal text-[var(--muted-foreground)]">Avg Score: <span className="text-white font-medium">{result.averageScoresByVersion[v]}</span>/10</span>
                        )}
                      </div>
                    </th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border)] bg-transparent">
                {inputs.map((input) => (
                  <tr key={input} className="hover:bg-white/[0.02] transition-colors">
                    <td className="py-4 pl-4 pr-3 text-sm font-medium text-white sm:pl-6 border-r border-[var(--border)] align-top">
                      {input}
                    </td>
                    {versions.map(v => {
                      const run = runMap[`${input}|${v}`];
                      if (!run) return <td key={v} className="px-3 py-4 text-sm text-[var(--muted-foreground)] border-r border-[var(--border)]">No run</td>;
                      
                      let scoreData = null;
                      if (run.scoreJson) {
                        try {
                          scoreData = JSON.parse(run.scoreJson);
                        } catch(e) {}
                      }

                      return (
                        <td key={v} className="px-3 py-4 text-sm text-gray-300 border-r border-[var(--border)] last:border-r-0 align-top min-w-[300px]">
                          {run.status === 'FAILED' ? (
                            <ErrorDisplay errorMessage={run.errorMessage} />
                          ) : (
                            <div className="flex flex-col space-y-4">
                              <div className="bg-black/30 p-3 rounded-lg text-sm max-h-40 overflow-y-auto custom-scrollbar whitespace-pre-wrap">
                                {run.modelOutput}
                              </div>
                              
                              {scoreData && (
                                <div className="grid grid-cols-2 gap-2 text-xs">
                                  <div className="bg-white/5 p-2 rounded-lg border border-white/5">
                                    <div className="text-[var(--muted-foreground)] mb-1">Overall</div>
                                    <div className="text-lg font-bold text-green-400">{scoreData.overall_score}/10</div>
                                  </div>
                                  
                                  <div className="bg-white/5 p-2 rounded-lg border border-white/5">
                                    <div className="text-[var(--muted-foreground)] mb-1">Judge</div>
                                    {scoreData.judge_error ? (
                                      <div className="text-red-400 text-[10px] leading-tight truncate" title={scoreData.judge_error}>Failed</div>
                                    ) : !scoreData.judge_enabled ? (
                                      <div className="text-gray-500">Disabled</div>
                                    ) : (
                                      <div className="text-white font-medium">{scoreData.judge_score}/10</div>
                                    )}
                                  </div>

                                  <div className="bg-white/5 p-2 rounded-lg border border-white/5">
                                    <div className="text-[var(--muted-foreground)] mb-1">Keywords</div>
                                    <div className="text-white">{scoreData.keywords_matched}/{scoreData.keywords_total} ({scoreData.keyword_score}/10)</div>
                                  </div>

                                  <div className="bg-white/5 p-2 rounded-lg border border-white/5">
                                    <div className="text-[var(--muted-foreground)] mb-1">Length</div>
                                    <div className="text-white">{scoreData.output_length} chars ({scoreData.length_score}/10)</div>
                                  </div>
                                </div>
                              )}
                              <div className="text-[10px] text-[var(--muted-foreground)] text-right">
                                Latency: {run.latencyMs}ms
                              </div>
                            </div>
                          )}
                        </td>
                      );
                    })}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
      
      {/* Add custom scrollbar styles for this page */}
      <style dangerouslySetInnerHTML={{__html: `
        .custom-scrollbar::-webkit-scrollbar {
          width: 6px;
          height: 6px;
        }
        .custom-scrollbar::-webkit-scrollbar-track {
          background: rgba(255, 255, 255, 0.02);
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb {
          background: rgba(255, 255, 255, 0.1);
          border-radius: 10px;
        }
        .custom-scrollbar::-webkit-scrollbar-thumb:hover {
          background: rgba(255, 255, 255, 0.2);
        }
      `}} />
    </div>
  );
}
