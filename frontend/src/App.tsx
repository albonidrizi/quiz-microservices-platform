import { BrowserRouter, Routes, Route } from 'react-router-dom';
import QuizList from './components/QuizList';
import QuizGame from './components/QuizGame';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-gray-100 text-gray-900 font-sans">
        <nav className="bg-white shadow p-4 mb-4">
          <div className="container mx-auto">
            <h1 className="text-xl font-bold text-blue-600">Quiz Platform</h1>
          </div>
        </nav>
        <Routes>
          <Route path="/" element={<QuizList />} />
          <Route path="/play/:id" element={<QuizGame />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
