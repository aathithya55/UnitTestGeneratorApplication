import React, { useState, useRef } from 'react';
import { Upload, FileCode, X, Type } from 'lucide-react';
import './UploadSection.css';

function UploadSection({ onFileUpload, className, onClassNameChange }) {
  const [dragActive, setDragActive] = useState(false);
  const [uploadedFile, setUploadedFile] = useState(null);
  const inputRef = useRef(null);

  const handleDrag = (e) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = async (e) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      await handleFile(e.dataTransfer.files[0]);
    }
  };

  const handleChange = async (e) => {
    e.preventDefault();
    if (e.target.files && e.target.files[0]) {
      await handleFile(e.target.files[0]);
    }
  };

  const handleFile = async (file) => {
    if (!file.name.endsWith('.java')) {
      alert('Please upload a .java file');
      return;
    }

    setUploadedFile(file);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await fetch('http://localhost:8080/api/upload', {
        method: 'POST',
        body: formData
      });

      const data = await response.json();

      if (data.status === 'SUCCESS') {
        onFileUpload(data);
      } else {
        alert('Error: ' + data.message);
      }
    } catch (error) {
      alert('Error uploading file. Make sure backend is running on port 8080.');
    }
  };

  const clearFile = () => {
    setUploadedFile(null);
    inputRef.current.value = '';
  };

  return (
    <div className="upload-section">
      <div className="upload-grid">
        <div 
          className={`upload-area ${dragActive ? 'active' : ''}`}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
          onClick={() => inputRef.current?.click()}
        >
          <input 
            ref={inputRef}
            type="file" 
            accept=".java"
            onChange={handleChange}
            hidden
          />
          <div className="upload-icon">
            <Upload size={40} />
          </div>
          <h3>Drop your Java file here</h3>
          <p>or click to browse (.java files only)</p>
          {uploadedFile && (
            <div className="file-preview">
              <FileCode size={16} />
              <span>{uploadedFile.name}</span>
              <button onClick={(e) => { e.stopPropagation(); clearFile(); }}>
                <X size={14} />
              </button>
            </div>
          )}
        </div>

        <div className="class-name-input">
          <label>
            <Type size={16} />
            <span>Class Name</span>
          </label>
          <input 
            type="text" 
            value={className}
            onChange={(e) => onClassNameChange(e.target.value)}
            placeholder="Enter class name"
          />
          <p className="hint">Used for generating test class name</p>
        </div>
      </div>
    </div>
  );
}

export default UploadSection;