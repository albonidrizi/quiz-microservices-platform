# Security Policy

## Reporting

Please report security issues privately through GitHub Security Advisories. Do not open a public issue for an unpatched vulnerability.

## Security Model

- The API gateway exposes only the public quiz API.
- The question service is private to the Docker network and requires `X-Internal-Api-Key`.
- Quiz submissions must contain exactly one answer for each question assigned to that quiz.
- Database credentials and the internal API key are supplied through environment variables.
- Production deployments must replace every development default from `.env.example`.

## Supported Version

Security fixes are applied to the latest commit on `main`.
