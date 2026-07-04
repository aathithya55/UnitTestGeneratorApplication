import React, { useState } from 'react';
import Header from './components/Header';
import UploadSection from './components/UploadSection';
import CodeEditor from './components/CodeEditor';
import TestResults from './components/TestResults';
import TestCaseList from './components/TestCaseList';
import Footer from './components/Footer';
import './App.css';

function App() {
  const [sourceCode, setSourceCode] = useState(`public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
        return a / b;
    }

    public boolean isEven(int number) {
        return number % 2 == 0;
    }
}`);
  const [className, setClassName] = useState('Calculator');
  const [generatedCode, setGeneratedCode] = useState('');
  const [testCases, setTestCases] = useState([]);
  const [totalTests, setTotalTests] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [status, setStatus] = useState('');
  const [activeTab, setActiveTab] = useState('code');

  const handleCodeChange = (code) => {
    setSourceCode(code);
  };

  const handleClassNameChange = (name) => {
    setClassName(name);
  };

  const handleFileUpload = (fileData) => {
    setSourceCode(fileData.sourceCode);
    setClassName(fileData.className);
  };

  const handleGenerate = async () => {
    setIsLoading(true);
    setStatus('Generating tests...');

    try {
      const response = await fetch('http://localhost:8080/api/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          className: className,
          sourceCode: sourceCode,
          framework: 'junit5',
          includeEdgeCases: true,
          includeBoundaryTests: true,
          includeNegativeTests: true
        })
      });

      const data = await response.json();

      if (data.status === 'SUCCESS') {
        setGeneratedCode(data.generatedCode);
        setTestCases(data.testCases);
        setTotalTests(data.totalTests);
        setStatus(`Generated ${data.totalTests} test cases successfully!`);
        setActiveTab('results');
      } else {
        setStatus('Error: ' + data.message);
      }
    } catch (error) {
      setStatus('Error connecting to server. Make sure backend is running on port 8080.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleDownload = () => {
    const blob = new Blob([generatedCode], { type: 'text/java' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${className}Test.java`;
    a.click();
    URL.revokeObjectURL(url);
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(generatedCode);
    setStatus('Code copied to clipboard!');
    setTimeout(() => setStatus(''), 2000);
  };

  return (
    <div className="app">
      <Header />

      <main className="main-content">
        <div className="container">
          <UploadSection 
            onFileUpload={handleFileUpload}
            className={className}
            onClassNameChange={handleClassNameChange}
          />

          <div className="editor-section">
            <div className="tabs">
              <button 
                className={`tab ${activeTab === 'code' ? 'active' : ''}`}
                onClick={() => setActiveTab('code')}
              >
                Source Code
              </button>
              <button 
                className={`tab ${activeTab === 'results' ? 'active' : ''}`}
                onClick={() => setActiveTab('results')}
              >
                Generated Tests
              </button>
              <button 
                className={`tab ${activeTab === 'cases' ? 'active' : ''}`}
                onClick={() => setActiveTab('cases')}
              >
                Test Cases ({totalTests})
              </button>
            </div>

            <div className="tab-content">
              {activeTab === 'code' && (
                <CodeEditor 
                  code={sourceCode}
                  onChange={handleCodeChange}
                  onGenerate={handleGenerate}
                  isLoading={isLoading}
                />
              )}

              {activeTab === 'results' && (
                <TestResults 
                  code={generatedCode}
                  onDownload={handleDownload}
                  onCopy={handleCopy}
                />
              )}

              {activeTab === 'cases' && (
                <TestCaseList testCases={testCases} />
              )}
            </div>
          </div>

          {status && (
            <div className={`status-bar ${status.includes('Error') ? 'error' : 'success'}`}>
              {status}
            </div>
          )}
        </div>
      </main>

      <Footer />
    </div>
  );
}

export default App;