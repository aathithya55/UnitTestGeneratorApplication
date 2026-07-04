import React from 'react';
import Editor from 'react-simple-code-editor';
import { Wand2 } from 'lucide-react';
import Prism from 'prismjs';
import 'prismjs/components/prism-java';
import './CodeEditor.css';

function CodeEditor({ code, onChange, onGenerate, isLoading }) {
  const highlightCode = (code) => {
    return Prism.highlight(code, Prism.languages.java, 'java');
  };

  return (
    <div className="code-editor-container">
      <div className="editor-toolbar">
        <div className="toolbar-left">
          <span className="file-name">Source.java</span>
        </div>
        <button 
          className={`generate-btn ${isLoading ? 'loading' : ''}`}
          onClick={onGenerate}
          disabled={isLoading}
        >
          {isLoading ? (
            <>
              <div className="spinner" />
              <span>Generating...</span>
            </>
          ) : (
            <>
              <Wand2 size={18} />
              <span>Generate Tests</span>
            </>
          )}
        </button>
      </div>
      <div className="editor-wrapper">
        <Editor
          value={code}
          onValueChange={onChange}
          highlight={highlightCode}
          padding={20}
          className="code-editor"
          textareaClassName="code-textarea"
          style={{
            fontFamily: '"JetBrains Mono", monospace',
            fontSize: 14,
            backgroundColor: '#0f172a',
            minHeight: '400px',
          }}
        />
      </div>
    </div>
  );
}

export default CodeEditor;