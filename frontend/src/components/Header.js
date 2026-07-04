import React from 'react';
import { Beaker, Github, Sparkles } from 'lucide-react';
import './Header.css';

function Header() {
  return (
    <header className="header">
      <div className="header-content">
        <div className="logo">
          <div className="logo-icon">
            <Beaker size={28} />
          </div>
          <div className="logo-text">
            <h1>Unit Test <span className="gradient-text">Generator</span></h1>
            <p>Automated JUnit Test Case Generator</p>
          </div>
        </div>
        <div className="header-actions">
          <a href="https://github.com/yourusername/unit-test-generator" target="_blank" rel="noopener noreferrer" className="btn-github">
            <Github size={18} />
            <span>GitHub</span>
          </a>
          <div className="badge">
            <Sparkles size={14} />
            <span>v1.0</span>
          </div>
        </div>
      </div>
    </header>
  );
}

export default Header;