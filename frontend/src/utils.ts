import axios from 'axios';

// types.ts (inline for simplicity or separate file)
export interface QuestionWrapper {
    id: number;
    questionTitle: string;
    option1: string;
    option2: string;
    option3: string;
    option4: string;
}

export const api = axios.create({
    baseURL: 'http://localhost:8765' // Gateway URL
});
