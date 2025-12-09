import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../utils';
import type { QuestionWrapper } from '../utils';

export default function QuizGame() {
    const { id } = useParams();
    const [questions, setQuestions] = useState<QuestionWrapper[]>([]);
    const [answers, setAnswers] = useState<{ id: number, response: string }[]>([]);
    const [currentQ, setCurrentQ] = useState(0);
    const [score, setScore] = useState<number | null>(null);

    useEffect(() => {
        api.get(`quiz/get/${id}`).then(res => setQuestions(res.data));
    }, [id]);

    const handleAnswer = (option: string) => {
        const newAnswers = [...answers];
        // Remove existing answer for this question if any
        const existingIndex = newAnswers.findIndex(a => a.id === questions[currentQ].id);
        if (existingIndex >= 0) newAnswers.splice(existingIndex, 1);

        newAnswers.push({ id: questions[currentQ].id, response: option });
        setAnswers(newAnswers);

        if (currentQ < questions.length - 1) {
            setCurrentQ(currentQ + 1);
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

    if (questions.length === 0) return <div>Loading...</div>;

    if (score !== null) return (
        <div className="text-center p-10">
            <h1 className="text-5xl font-bold text-green-600">Quiz Completed!</h1>
            <p className="text-2xl mt-4">Your Score: {score} / {questions.length}</p>
            <a href="/" className="mt-8 inline-block bg-blue-500 text-white px-4 py-2 rounded">Play Again</a>
        </div>
    );

    const q = questions[currentQ];

    return (
        <div className="max-w-2xl mx-auto mt-10 p-6 bg-white shadow-xl rounded-xl">
            <div className="flex justify-between text-gray-500 mb-4">
                <span>Question {currentQ + 1}/{questions.length}</span>
                <span>Category: Java</span>
            </div>
            <h2 className="text-2xl font-bold mb-6">{q.questionTitle}</h2>
            <div className="grid grid-cols-1 gap-4">
                {[q.option1, q.option2, q.option3, q.option4].map((opt, i) => (
                    <button
                        key={i}
                        onClick={() => handleAnswer(opt)}
                        className="bg-gray-100 hover:bg-blue-100 text-left p-4 rounded-lg border border-gray-200 transition-all hover:border-blue-500"
                    >
                        {opt}
                    </button>
                ))}
            </div>
        </div>
    );
}
