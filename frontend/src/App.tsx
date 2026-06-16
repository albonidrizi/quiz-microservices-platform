import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import QuizList from './components/QuizList';
import QuizGame from './components/QuizGame';
import { Gamepad2, ShieldCheck } from 'lucide-react';

function App() {
  return (
    <BrowserRouter>
      <div className="min-h-screen text-white relative overflow-hidden">
        {/* Background Ambient Glow */}
        <div className="absolute top-0 left-0 w-full h-full overflow-hidden -z-10 pointer-events-none">
          <div className="absolute top-[-10%] left-[-10%] w-96 h-96 bg-purple-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob"></div>
          <div className="absolute top-[-10%] right-[-10%] w-96 h-96 bg-cyan-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000"></div>
          <div className="absolute bottom-[-20%] left-[20%] w-96 h-96 bg-pink-600 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-4000"></div>
        </div>

        <nav className="glass-panel sticky top-4 mx-4 md:mx-auto max-w-6xl rounded-2xl px-6 py-4 flex justify-between items-center z-50">
          <Link to="/" className="flex items-center gap-2 group cursor-pointer">
            <div className="p-2 bg-gradient-to-tr from-cyan-500 to-blue-500 rounded-lg group-hover:scale-110 transition-transform">
              <Gamepad2 className="w-6 h-6 text-white" />
            </div>
            <h1 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-cyan-400 to-purple-400 neon-text">
              QuizUltra
            </h1>
          </Link>

          <div className="flex gap-4 items-center">
            <Link to="/" className="text-gray-300 hover:text-white transition-colors flex items-center gap-1 font-medium text-sm">
              Quizzes
            </Link>
            <div className="hidden md:flex items-center gap-1 text-emerald-300 font-medium text-sm">
              <ShieldCheck className="w-4 h-4" /> Protected scoring
            </div>
          </div>
        </nav>

        <main className="container mx-auto px-4 py-8 relative z-10">
          <Routes>
            <Route path="/" element={<QuizList />} />
            <Route path="/play/:id" element={<QuizGame />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}

export default App;
