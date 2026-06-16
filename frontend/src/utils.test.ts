import { describe, expect, it } from 'vitest';
import { api, getApiErrorMessage } from './utils';

describe('API client', () => {
    it('uses the same-origin reverse proxy', () => {
        expect(api.defaults.baseURL).toBe('/api');
        expect(api.defaults.timeout).toBe(10_000);
    });

    it('returns a safe fallback for unknown errors', () => {
        expect(getApiErrorMessage(new Error('internal detail'), 'Request failed')).toBe('Request failed');
    });
});
