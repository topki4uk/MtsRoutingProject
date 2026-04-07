CREATE TABLE IF NOT EXISTS data_records (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
      type INTEGER NOT NULL,
      random_text VARCHAR(1000) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
