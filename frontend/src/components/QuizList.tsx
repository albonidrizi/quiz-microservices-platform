import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../utils';

const categories = ["Java", "Python", "JavaScript", "Docker"];

export default function QuizList() {
    const [category, setCategory] = useState(categories[0]);
    const [numQ, setNumQ] = useState(5);
    const [title, setTitle] = useState("");
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);

    const handleCreate = async () => {
        setLoading(true);
        try {
            // Note: The backend POST /quiz/create returns a plain string "Success" usually, 
            // but for a real app we need the ID. 
            // WAIT: The current backend implementation just sends "Success" and saves to DB but doesn't return ID?
            // Let me check QuizService.java again. 
            // Yes: return new ResponseEntity<>("Success", HttpStatus.CREATED);
            // This is a problem for the frontend. We can't play the quiz if we don't know the ID.
            // I might need to refactor the backend to return the Quiz ID.
            // For now, I will implement this assuming I will fix the backend.

            const response = await api.post('quiz/create', {
                categoryName: category,
                numQuestions: numQ,
                title: title
            });
            console.log("Quiz Created: ", response.data);
            // alert(`Quiz Created Successfully! ID: ${response.data.id}`);
            navigate(`/play/${response.data.id}`);
        } catch (error) {
            console.error(error);
            alert("Failed to create quiz");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="p-10 flex flex-col items-center">
            <h1 className="text-4xl font-bold mb-8 text-blue-600">Create a New Quiz</h1>
            <div className="bg-white shadow-lg rounded-lg p-8 w-96">
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2">Category</label>
                    <select
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        className="shadow border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                    >
                        {categories.map(c => <option key={c} value={c}>{c}</option>)}
                    </select>
                </div>
                <div className="mb-4">
                    <label className="block text-gray-700 text-sm font-bold mb-2">Number of Questions</label>
                    <select
                        value={numQ}
                        onChange={(e) => setNumQ(Number(e.target.value))}
                        className="shadow border rounded w-full py-2 px-3 text-gray-700"
                    >
                        <option value="3">3</option>
                        <option value="5">5</option>
                        <option value="10">10</option>
                    </select>
                </div>
                <div className="mb-6">
                    <label className="block text-gray-700 text-sm font-bold mb-2">Details Title</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        className="shadow border rounded w-full py-2 px-3 text-gray-700"
                        placeholder="e.g. Java Basics"
                    />
                </div>
                <button
                    onClick={handleCreate}
                    disabled={loading}
                    className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
                >
                    {loading ? 'Creating...' : 'Start Quiz'}
                </button>
            </div>
        </div>
    );
}
