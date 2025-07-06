CREATE TABLE bets (
  id UUID PRIMARY KEY,
  user_id VARCHAR(255),
  event_id VARCHAR(255),
  event_market_id VARCHAR(255),
  event_winner_id VARCHAR(255),
  bet_amount DECIMAL
);
