# Core Java Assessment

A single self-contained HTML file (`core-java-assessment.html`) — no build step, no dependencies. Commit it to any repo and open it directly, or serve it with GitHub Pages.

## What it does

- 36 multiple-choice questions across 7 Core Java topics / 12 subtopics (OOP, fundamentals, arrays, strings, exceptions, collections, multithreading, Java 8 lambdas & streams), 1 easy + 1 medium + 1 hard question per subtopic.
- Candidate enters name, ID, and test name before starting.
- Every wrong answer is explained inline with the correct answer and the subtopic to revisit.
- At the end, the candidate sees: overall score & percentage, a performance rating, a subtopic-by-subtopic breakdown, a list of flagged "focus areas," and a full review of missed questions.

## About saving results into the repo

A static HTML page opened in a browser has no way to write files into a GitHub repo or push a commit by itself — that would require the candidate's browser to hold write credentials to your repo, which isn't something to embed in client-side code (anyone viewing the page source could extract it).

What this page does instead:

- **Download report (JSON)** button — generates `results_<candidateId>_<date>.json` containing everything: name, ID, test, date, score, subtopic breakdown, focus areas, and full wrong-answer detail. The candidate downloads it, and it (or whoever runs the assessment) adds it to a `results/` folder in the repo and commits it.
- **Print / Save as PDF** — a clean, print-formatted version of the same report for sharing outside GitHub entirely.

### If you want it to save automatically

That needs a small backend or automation layer, since GitHub write access can't live in client-side JavaScript safely. Two reasonable options if you want to go further:

1. **GitHub Issue Forms** — have the page open a pre-filled "New Issue" with the JSON as the body; a GitHub Action then parses the issue and commits the result file to `results/`.
2. **A tiny serverless function** (Vercel/Netlify/Cloud Run) that holds a GitHub token server-side, accepts the JSON via POST, and commits it through the GitHub API on the candidate's behalf.

Happy to build either of these if you want the automatic version — just say which.
