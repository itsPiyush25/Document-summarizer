import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [text, setText] = useState('');
  const [summaryType, setSummaryType] = useState('SHORT');
  const [maxLength, setMaxLength] = useState(200);
  const [summary, setSummary] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSummary('');

    try {
      const response = await axios.post('http://localhost:9090/api/v1/summarize', {
        text,
        summaryType,
        maxLength
      });
      if (response.data.status === 'success') {
        setSummary(response.data.summary);
      } else {
        setError(response.data.message || 'Unknown error');
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to fetch summary');
    } finally {
      setLoading(false);
    }
  };

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = (event) => {
      setText(event.target.result);
    };
    reader.readAsText(file);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-gray-100 p-4 md:p-8">
      <div className="max-w-6xl mx-auto">
        <header className="text-center mb-10">
          <h1 className="text-4xl md:text-5xl font-bold text-gray-800 mb-4">
            AI Document Summarizer
          </h1>
          <p className="text-gray-600 text-lg">
            Upload a text file or paste your document below to get a concise summary.
          </p>
        </header>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Left Panel: Input Form */}
          <div className="bg-white rounded-2xl shadow-xl p-6 md:p-8">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Input Document</h2>
            
            <form onSubmit={handleSubmit}>
              {/* Text Area */}
              <div className="mb-6">
                <label className="block text-gray-700 font-medium mb-2" htmlFor="text">
                  Document Text
                </label>
                <textarea
                  id="text"
                  rows="10"
                  className="w-full p-4 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
                  placeholder="Paste your document text here..."
                  value={text}
                  onChange={(e) => setText(e.target.value)}
                  required
                />
                <div className="mt-2 text-sm text-gray-500">
                  Minimum 10 characters. Maximum 10,000 characters.
                </div>
              </div>

              {/* File Upload */}
              <div className="mb-6">
                <label className="block text-gray-700 font-medium mb-2">
                  Or Upload a Text File
                </label>
                <div className="flex items-center">
                  <input
                    type="file"
                    accept=".txt,.pdf,.csv"
                    onChange={handleFileUpload}
                    className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
                  />
                </div>
                <p className="mt-1 text-sm text-gray-500">Supports .txt, .pdf, .csv files.</p>
              </div>

              {/* Options */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                <div>
                  <label className="block text-gray-700 font-medium mb-2" htmlFor="summaryType">
                    Summary Type
                  </label>
                  <select
                    id="summaryType"
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={summaryType}
                    onChange={(e) => setSummaryType(e.target.value)}
                  >
                    <option value="SHORT">Short Summary</option>
                    <option value="BULLET">Bullet Points</option>
                    <option value="DETAILED">Detailed Explanation</option>
                  </select>
                </div>
                <div>
                  <label className="block text-gray-700 font-medium mb-2" htmlFor="maxLength">
                    Max Length (optional)
                  </label>
                  <input
                    type="number"
                    id="maxLength"
                    min="10"
                    max="10000"
                    className="w-full p-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    value={maxLength}
                    onChange={(e) => setMaxLength(parseInt(e.target.value) || 200)}
                  />
                </div>
              </div>

              {/* Submit Button */}
              <button
                type="submit"
                disabled={loading || text.length < 10}
                className="w-full py-3 px-4 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-300 text-white font-semibold rounded-lg shadow-md transition duration-300 flex items-center justify-center"
              >
                {loading ? (
                  <>
                    <svg className="animate-spin h-5 w-5 mr-3 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Processing...
                  </>
                ) : 'Generate Summary'}
              </button>
            </form>
          </div>

          {/* Right Panel: Output */}
          <div className="bg-white rounded-2xl shadow-xl p-6 md:p-8">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Generated Summary</h2>
            
            {error && (
              <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                <div className="flex items-center">
                  <svg className="w-5 h-5 text-red-500 mr-2" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
                  </svg>
                  <span className="text-red-700 font-medium">Error: {error}</span>
                </div>
              </div>
            )}

            {summary ? (
              <div className="bg-gray-50 border border-gray-200 rounded-xl p-6">
                <div className="flex justify-between items-center mb-4">
                  <span className="inline-block px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm font-medium">
                    {summaryType === 'SHORT' ? 'Short Summary' : summaryType === 'BULLET' ? 'Bullet Points' : 'Detailed Explanation'}
                  </span>
                  <button
                    onClick={() => navigator.clipboard.writeText(Array.isArray(summary) ? summary.join('\n') : summary)}
                    className="text-gray-500 hover:text-gray-700"
                    title="Copy to clipboard"
                  >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z" />
                    </svg>
                  </button>
                </div>
                <div className="prose max-w-none">
                  {summaryType === 'BULLET' ? (
                    <ul className="list-disc pl-5 space-y-2">
                      {(Array.isArray(summary) ? summary : summary.split('\n')).map((line, idx) => (
                        <li key={idx} className="text-gray-700">{line}</li>
                      ))}
                    </ul>
                  ) : (
                    <p className="text-gray-700 whitespace-pre-line">{summary}</p>
                  )}
                </div>
              </div>
            ) : (
              <div className="text-center py-12 text-gray-500">
                <svg className="w-16 h-16 mx-auto text-gray-300 mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
                <p className="text-lg">Your summary will appear here after generation.</p>
                <p className="text-sm mt-2">Try pasting a document and clicking "Generate Summary".</p>
              </div>
            )}

            {/* API Info */}
            <div className="mt-8 pt-6 border-t border-gray-200">
              <h3 className="text-lg font-medium text-gray-700 mb-2">API Information</h3>
              <div className="text-sm text-gray-600">
                <p>Endpoint: <code className="bg-gray-100 px-2 py-1 rounded">POST http://localhost:9090/api/v1/summarize</code></p>
                <p className="mt-1">The backend is running on port 9090 with Spring Boot.</p>
              </div>
            </div>
          </div>
        </div>

        <footer className="mt-12 text-center text-gray-500 text-sm">
          <p>Document Summarizer UI • Built with React & Tailwind CSS • Backend: Spring Boot</p>
        </footer>
      </div>
    </div>
  );
}

export default App;
