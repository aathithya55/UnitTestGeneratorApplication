import React from 'react';
import { Download, Copy } from 'lucide-react';
import './TestResults.css';

function TestResults({ code, onDownload, onCopy }) {
  if (!code) {
    return (
      <div className="empty-state">
        <div className="empty-icon">📝</div>
        <h3>No tests generated yet</h3>
        <p>Write or upload Java code and click "Generate Tests"</p>
      </div>
    );
  }

  return (
    <div className="test-results">
      <div className="results-toolbar">
        <span className="results-title">Generated Test Class</span>
        <div className="results-actions">
          <button onClick={onCopy} className="action-btn">
            <Copy size={16} />
            <span>Copy</span>
          </button>
          <button onClick={onDownload} className="action-btn primary">
            <Download size={16} />
            <span>Download</span>
          </button>
        </div>
      </div>
      <pre className="results-code">
        <code>{code}</code>
      </pre>
    </div>
  );
}

export default TestResults;