PRAGMA foreign_keys = ON;

-- Quiz table
CREATE TABLE IF NOT EXISTS Quiz (
    id INTEGER PRIMARY KEY,          -- auto-incrementing ID
    title TEXT NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Question table
CREATE TABLE IF NOT EXISTS Question (
    id INTEGER PRIMARY KEY,
    quiz_id INTEGER NOT NULL,
    question_text TEXT NOT NULL,
    question_type TEXT DEFAULT 'multiple_choice',
    FOREIGN KEY (quiz_id) REFERENCES Quiz(id) ON DELETE CASCADE
);

-- Answer table
CREATE TABLE IF NOT EXISTS Answer (
    id INTEGER PRIMARY KEY,
    question_id INTEGER NOT NULL,
    answer_text TEXT NOT NULL,
    is_correct INTEGER DEFAULT 0,   -- 0 = false, 1 = true
    FOREIGN KEY (question_id) REFERENCES Question(id) ON DELETE CASCADE
);