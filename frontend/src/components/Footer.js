import React from 'react';
import { Heart, Code } from 'lucide-react';
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-left">
          <Code size={16} />
          <span>Built with Spring Boot & React</span>
        </div>
        <div className="footer-center">
          <span>Unit Test Generator</span>
          <span className="separator">•</span>
          <span>College Project 2024</span>
        </div>
        <div className="footer-right">
          <span>Made with</span>
          <Heart size={14} className="heart" />
          <span>for developers</span>
        </div>
      </div>
    </footer>
  );
}

export default Footer;