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
    baseURL: '/api',
    timeout: 10_000,
    headers: {
        'Content-Type': 'application/json',
    },
});

export function getApiErrorMessage(error: unknown, fallback: string): string {
    if (axios.isAxiosError<{ message?: string }>(error)) {
        return error.response?.data?.message ?? fallback;
    }
    return fallback;
}
