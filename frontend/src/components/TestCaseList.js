import React from 'react';
import { TestTube, Zap, AlertTriangle, CheckCircle } from 'lucide-react';
import './TestCaseList.css';

function TestCaseList({ testCases }) {
  const getIcon = (type) => {
    switch (type) {
      case 'NORMAL': return <CheckCircle size={18} className="icon-normal" />;
      case 'EDGE_CASE': return <Zap size={18} className="icon-edge" />;
      case 'NEGATIVE': return <AlertTriangle size={18} className="icon-negative" />;
      default: return <TestTube size={18} />;
    }
  };

  const getBadgeClass = (type) => {
    switch (type) {
      case 'NORMAL': return 'badge-normal';
      case 'EDGE_CASE': return 'badge-edge';
      case 'NEGATIVE': return 'badge-negative';
      default: return '';
    }
  };

  if (!testCases || testCases.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-icon">🔬</div>
        <h3>No test cases yet</h3>
        <p>Generate tests to see the breakdown here</p>
      </div>
    );
  }

  return (
    <div className="test-case-list">
      <div className="stats-bar">
        <div className="stat">
          <span className="stat-value">{testCases.length}</span>
          <span className="stat-label">Total Tests</span>
        </div>
        <div className="stat">
          <span className="stat-value">{testCases.filter(t => t.testType === 'NORMAL').length}</span>
          <span className="stat-label">Normal</span>
        </div>
        <div className="stat">
          <span className="stat-value">{testCases.filter(t => t.testType === 'EDGE_CASE').length}</span>
          <span className="stat-label">Edge Cases</span>
        </div>
        <div className="stat">
          <span className="stat-value">{testCases.filter(t => t.testType === 'NEGATIVE').length}</span>
          <span className="stat-label">Negative</span>
        </div>
      </div>

      <div className="cases-grid">
        {testCases.map((testCase, index) => (
          <div key={index} className="case-card animate-fade-in" style={{ animationDelay: `${index * 0.05}s` }}>
            <div className="case-header">
              {getIcon(testCase.testType)}
              <span className={`case-badge ${getBadgeClass(testCase.testType)}`}>
                {testCase.testType}
              </span>
            </div>
            <h4 className="case-name">{testCase.methodName}</h4>
            <p className="case-desc">{testCase.description}</p>
            <div className="case-footer">
              <span className="case-expected">{testCase.expectedBehavior}</span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default TestCaseList;