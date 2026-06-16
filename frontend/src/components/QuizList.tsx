import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, getApiErrorMessage } from '../utils';
import { motion } from 'framer-motion';
import { Code2, Hash, Play, Sparkles } from 'lucide-react';

const categories = [
    { name: "Java", color: "from-orange-500 to-red-500", icon: <Code2 /> },
    { name: "Python", color: "from-blue-400 to-yellow-300", icon: <Hash /> },
    { name: "JavaScript", color: "from-yellow-400 to-orange-400", icon: <Code2 /> },
    { name: "Docker", color: "from-blue-600 to-blue-400", icon: <Code2 /> }
];

export default function QuizList() {
    const [selectedCategory, setSelectedCategory] = useState(categories[0].name);
    const [numQ, setNumQ] = useState(5);
    const [title, setTitle] = useState("");
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleCreate = async () => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.post('quiz/create', {
                categoryName: selectedCategory,
                numQuestions: numQ,
                title: title || 'New Challenge'
            });
            navigate(`/play/${response.data.id}`);
        } catch (error) {
            setError(getApiErrorMessage(error, "Could not create the quiz. Please try again."));
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto flex flex-col md:flex-row gap-12 items-start pt-10">
            {/* Left Side: Hero Text */}
            <div className="flex-1 space-y-6">
                <motion.div
                    initial={{ opacity: 0, x: -20 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ duration: 0.5 }}
                >
                    <span className="text-cyan-400 font-semibold tracking-wider text-sm uppercase">Ready to Play?</span>
                    <h2 className="text-5xl font-extrabold mt-2 leading-tight">
                        Test Your <br />
                        <span className="bg-clip-text text-transparent bg-gradient-to-r from-purple-400 to-pink-400">Coding Skills</span>
                    </h2>
                    <p className="text-gray-400 mt-4 text-lg leading-relaxed">
                        Select a technology stack, choose your difficulty, and challenge yourself with our microservices-powered engine.
                    </p>
                </motion.div>

                <div className="grid grid-cols-2 gap-4">
                    {categories.map((cat) => (
                        <button
                            type="button"
                            key={cat.name}
                            onClick={() => setSelectedCategory(cat.name)}
                            aria-pressed={selectedCategory === cat.name}
                            className={`p-4 rounded-xl cursor-pointer border text-left transition-all duration-300 ${selectedCategory === cat.name ? 'border-cyan-400 bg-white/10 shadow-lg shadow-cyan-500/20' : 'border-white/10 bg-white/5 hover:bg-white/10'}`}
                        >
                            <div className={`w-10 h-10 rounded-lg bg-gradient-to-br ${cat.color} flex items-center justify-center mb-3 shadow-lg`}>
                                {cat.icon}
                            </div>
                            <h3 className="font-bold text-lg">{cat.name}</h3>
                        </button>
                    ))}
                </div>
            </div>

            {/* Right Side: Config Panel */}
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2 }}
                className="glass-panel p-8 rounded-3xl w-full md:w-96"
            >
                <h3 className="text-2xl font-bold mb-6 flex items-center gap-2">
                    <Sparkles className="text-yellow-400" />
                    Setup Quiz
                </h3>

                <div className="space-y-6">
                    <div>
                        <label className="block text-gray-400 text-sm font-medium mb-2">Category Selected</label>
                        <div className="p-3 bg-black/20 rounded-lg text-cyan-300 font-mono border border-white/5">
                            {selectedCategory}
                        </div>
                    </div>

                    <div>
                        <label className="block text-gray-400 text-sm font-medium mb-2">Number of Questions</label>
                        <div className="flex gap-2">
                            {[3, 5].map(n => (
                                <button
                                    type="button"
                                    key={n}
                                    onClick={() => setNumQ(n)}
                                    className={`flex-1 py-2 rounded-lg text-sm font-bold transition-all ${numQ === n ? 'bg-cyan-500/20 text-cyan-300 border border-cyan-500/50' : 'bg-white/5 text-gray-400 hover:bg-white/10'}`}
                                >
                                    {n}
                                </button>
                            ))}
                        </div>
                    </div>

                    <div>
                        <label className="block text-gray-400 text-sm font-medium mb-2">Session Title (Optional)</label>
                        <input
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            maxLength={255}
                            className="w-full bg-black/20 border border-white/10 rounded-xl px-4 py-3 text-white focus:outline-none focus:border-cyan-500 transition-colors"
                            placeholder="e.g. Daily Drill"
                        />
                    </div>

                    {error ? (
                        <p role="alert" className="rounded-xl border border-red-400/30 bg-red-500/10 p-3 text-sm text-red-200">
                            {error}
                        </p>
                    ) : null}

                    <button
                        type="button"
                        onClick={handleCreate}
                        disabled={loading}
                        className="w-full relative group overflow-hidden bg-gradient-to-r from-cyan-500 to-blue-500 rounded-xl py-4 font-bold text-lg shadow-lg shadow-blue-500/30 hover:shadow-blue-500/50 transition-all transform hover:scale-[1.02] disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        <span className="relative z-10 flex items-center justify-center gap-2">
                            {loading ? 'Initializing Engine...' : <>Start Challenge <Play className="w-5 h-5 fill-current" /></>}
                        </span>
                        <div className="absolute inset-0 bg-white/20 translate-y-full group-hover:translate-y-0 transition-transform duration-300"></div>
                    </button>
                </div>
            </motion.div>
        </div>
    );
}
