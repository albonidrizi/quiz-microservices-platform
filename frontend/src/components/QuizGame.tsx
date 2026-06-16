import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import { CheckCircle2, ChevronRight, Clock, Trophy } from 'lucide-react';
import { api, getApiErrorMessage } from '../utils';
import type { QuestionWrapper } from '../utils';

type Answer = {
    id: number;
    response: string;
};

export default function QuizGame() {
    const { id } = useParams();
    const [questions, setQuestions] = useState<QuestionWrapper[]>([]);
    const [answers, setAnswers] = useState<Answer[]>([]);
    const [currentQ, setCurrentQ] = useState(0);
    const [score, setScore] = useState<number | null>(null);
    const [loading, setLoading] = useState(true);
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const controller = new AbortController();

        api.get(`quiz/get/${id}`, { signal: controller.signal })
            .then((response) => {
                setQuestions(response.data);
                setLoading(false);
            })
            .catch((requestError) => {
                if (!controller.signal.aborted) {
                    setError(getApiErrorMessage(requestError, 'Could not load this quiz.'));
                    setLoading(false);
                }
            });

        return () => controller.abort();
    }, [id]);

    const submitQuiz = async (finalAnswers: Answer[]) => {
        setSubmitting(true);
        setError(null);
        try {
            const response = await api.post(`quiz/submit/${id}`, finalAnswers);
            setScore(response.data);
        } catch (requestError) {
            setError(getApiErrorMessage(requestError, 'Could not submit your answers. Please try again.'));
        } finally {
            setSubmitting(false);
        }
    };

    const handleAnswer = (option: string) => {
        if (submitting) return;

        const questionId = questions[currentQ].id;
        const newAnswers = answers.filter((answer) => answer.id !== questionId);
        newAnswers.push({ id: questionId, response: option });
        setAnswers(newAnswers);

        if (currentQ < questions.length - 1) {
            setTimeout(() => setCurrentQ((previous) => previous + 1), 200);
        } else {
            void submitQuiz(newAnswers);
        }
    };

    if (loading) {
        return (
            <div className="flex items-center justify-center h-64" aria-label="Loading quiz">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-cyan-400" />
            </div>
        );
    }

    if (error) {
        return (
            <div role="alert" className="text-center p-10 glass-panel rounded-3xl mt-10">
                <h2 className="text-2xl font-bold mb-4">Something went wrong</h2>
                <p className="text-red-200 mb-8">{error}</p>
                <Link to="/" className="glass-button px-6 py-3 rounded-lg font-bold">Back to Home</Link>
            </div>
        );
    }

    if (questions.length === 0) {
        return (
            <div className="text-center p-10 glass-panel rounded-3xl mt-10">
                <h2 className="text-3xl font-bold mb-4">No Questions Found</h2>
                <p className="text-gray-400 mb-8">This quiz has no questions. Try creating a new one.</p>
                <Link to="/" className="glass-button px-6 py-3 rounded-lg font-bold">Back to Home</Link>
            </div>
        );
    }

    if (score !== null) {
        return (
            <div className="flex flex-col items-center justify-center p-10 text-center space-y-8">
                <motion.div
                    initial={{ scale: 0 }}
                    animate={{ scale: 1 }}
                    transition={{ type: 'spring', bounce: 0.5 }}
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
                <Link to="/" className="glass-button px-8 py-4 rounded-xl text-lg font-bold flex items-center gap-2 hover:bg-white/10">
                    Play Again <ChevronRight />
                </Link>
            </div>
        );
    }

    const question = questions[currentQ];
    const progress = ((currentQ + 1) / questions.length) * 100;

    return (
        <div className="max-w-3xl mx-auto">
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

            <motion.div
                key={currentQ}
                initial={{ opacity: 0, x: 50 }}
                animate={{ opacity: 1, x: 0 }}
                className="glass-panel p-8 rounded-3xl relative overflow-hidden"
            >
                <div className="absolute top-0 right-0 p-4 opacity-10">
                    <CheckCircle2 className="w-32 h-32" />
                </div>
                <h2 className="text-2xl font-bold mb-8 leading-snug pr-8">{question.questionTitle}</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    {[question.option1, question.option2, question.option3, question.option4].map((option) => (
                        <button
                            type="button"
                            key={option}
                            onClick={() => handleAnswer(option)}
                            disabled={submitting}
                            className="bg-white/5 hover:bg-white/10 border border-white/10 hover:border-cyan-400/50 p-6 rounded-2xl text-left transition-all duration-300 group relative overflow-hidden disabled:cursor-wait disabled:opacity-60"
                        >
                            <span className="relative z-10 font-semibold group-hover:text-cyan-300 transition-colors">{option}</span>
                            <div className="absolute inset-0 bg-gradient-to-r from-cyan-500/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity" />
                        </button>
                    ))}
                </div>
            </motion.div>
        </div>
    );
}
