import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { api } from '../utils';
import type { QuestionWrapper } from '../utils';
import { motion } from 'framer-motion';
import { CheckCircle2, Clock, Trophy, ChevronRight } from 'lucide-react';

export default function QuizGame() {
    const { id } = useParams();
    const [questions, setQuestions] = useState<QuestionWrapper[]>([]);
    const [answers, setAnswers] = useState<{ id: number, response: string }[]>([]);
    const [currentQ, setCurrentQ] = useState(0);
    const [score, setScore] = useState<number | null>(null);

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        api.get(`quiz/get/${id}`)
            .then(res => {
                setQuestions(res.data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, [id]);

    const handleAnswer = (option: string) => {
        const newAnswers = [...answers];
        const existingIndex = newAnswers.findIndex(a => a.id === questions[currentQ].id);
        if (existingIndex >= 0) newAnswers.splice(existingIndex, 1);
        newAnswers.push({ id: questions[currentQ].id, response: option });
        setAnswers(newAnswers);

        if (currentQ < questions.length - 1) {
            setTimeout(() => setCurrentQ(currentQ + 1), 200); // 200ms delay for visual feedback
        } else {
            submitQuiz(newAnswers);
        }
    };

    const submitQuiz = async (finalAnswers: typeof answers) => {
        try {
            const res = await api.post(`quiz/submit/${id}`, finalAnswers);
            setScore(res.data);
        } catch (e) {
            console.error(e);
        }
    };

    if (loading) return (
        <div className="flex items-center justify-center h-64">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-cyan-400"></div>
        </div>
    );

    if (questions.length === 0) return (
        <div className="text-center p-10 glass-panel rounded-3xl mt-10">
            <h2 className="text-3xl font-bold mb-4">No Questions Found 😕</h2>
            <p className="text-gray-400 mb-8">This quiz seems to have no questions. Try creating a new one!</p>
            <Link to="/" className="glass-button px-6 py-3 rounded-lg font-bold">Back to Home</Link>
        </div>
    );

    if (score !== null) return (
        <div className="flex flex-col items-center justify-center p-10 text-center space-y-8">
            <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ type: "spring", bounce: 0.5 }}
                className="w-32 h-32 bg-gradient-to-tr from-green-400 to-emerald-600 rounded-full flex items-center justify-center shadow-lg shadow-green-500/50"
            >
                <Trophy className="w-16 h-16 text-white" />
            </motion.div>

            <div>
                <h1 className="text-6xl font-black text-transparent bg-clip-text bg-gradient-to-r from-green-300 to-emerald-500 mb-2">
                    {score} / {questions.length}
                </h1>
                <p className="text-gray-400 text-xl font-medium">Mission Accomplished!</p>
            </div>

            <Link
                to="/"
                className="glass-button px-8 py-4 rounded-xl text-lg font-bold flex items-center gap-2 hover:bg-white/10"
            >
                Play Again <ChevronRight />
            </Link>
        </div>
    );

    const q = questions[currentQ];
    const progress = ((currentQ + 1) / questions.length) * 100;

    return (
        <div className="max-w-3xl mx-auto">
            {/* Progress Bar */}
            <div className="mb-8">
                <div className="flex justify-between text-sm text-gray-400 mb-2 font-medium">
                    <span>Question {currentQ + 1} of {questions.length}</span>
                    <span className="flex items-center gap-1"><Clock className="w-4 h-4" /> Time: Unlimited</span>
                </div>
                <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                    <motion.div
                        className="h-full bg-gradient-to-r from-cyan-400 to-blue-500"
                        initial={{ width: 0 }}
                        animate={{ width: `${progress}%` }}
                        transition={{ duration: 0.5 }}
                    />
                </div>
            </div>

            {/* Question Card */}
            <motion.div
                key={currentQ}
                initial={{ opacity: 0, x: 50 }}
                animate={{ opacity: 1, x: 0 }}
                exit={{ opacity: 0, x: -50 }}
                className="glass-panel p-8 rounded-3xl relative overflow-hidden"
            >
                <div className="absolute top-0 right-0 p-4 opacity-10">
                    <CheckCircle2 className="w-32 h-32" />
                </div>

                <h2 className="text-2xl font-bold mb-8 leading-snug pr-8">{q.questionTitle}</h2>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {[q.option1, q.option2, q.option3, q.option4].map((opt, i) => (
                        <button
                            key={i}
                            onClick={() => handleAnswer(opt)}
                            className="bg-white/5 hover:bg-white/10 border border-white/10 hover:border-cyan-400/50 p-6 rounded-2xl text-left transition-all duration-300 group relative overflow-hidden"
                        >
                            <span className="relative z-10 font-semibold group-hover:text-cyan-300 transition-colors">{opt}</span>
                            <div className="absolute inset-0 bg-gradient-to-r from-cyan-500/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity"></div>
                        </button>
                    ))}
                </div>
            </motion.div>
        </div>
    );
}
