# Tic Tac Toe - Roguelike Edition

A Java-based Tic Tac Toe game with a twist - featuring random power-ups each round that change how the game is played! Try to beat a challenging AI

## Features

- Tic Tac Toe gameplay with a twist
- Power-Up System: Player gets a random Power Up at the start of each round
- Challenging AI: Uses the Negamax algorithm for the challenge
- Multiple Game Modes: 
  - Single round quick play
  - Best of three
- Four Power-Ups:
  - Freeze: Block the AI from using an entire row for 2 rounds
  - Convert: Change any position on the board to your mark
  - Place Two: Place two marks in a single turn
  - Start Over: Restart the round without losing and with a new power-up

## Getting Started

### Used language

- JDK 25

## How to Play

1. Choose your game mode: Single round or best of three
2. Select your mark: X, O, or random
3. Receive your power-up at the start of each round
4. Make your moves: Enter numbers 1-9 corresponding to board available positions
5. Use power-ups: Enter 0 during your turn to activate your power-up
6. Win the game: Win with Power Ups or outsmart the AI

### Board Layout
```
  1  |  2  |  3
_____|_____|_____
  4  |  5  |  6
_____|_____|_____
  7  |  8  |  9
```

## AI Intelligence

The game uses the **Negamax algorithm**, a variant of Minimax that is more efficient, to create an intelligent AI opponent that:
- Calculates the best possible move
- Adapts to power-up effects (like frozen rows)
- Provides a challenging gameplay experience

## Power-Up Details

### Freeze
- Blocks an entire row from being used by the AI
- Lasts for 2 rounds
- Strategic positioning can force the AI into difficult situations or make it skip rounds!!

### Convert
- Place your mark on any position, even if occupied
- Can convert opponent's marks to yours disrupting the AI strategies

### Place Two
- Place two marks consecutively in a single turn
- Doubles your offensive and defensive options
- Can quickly turn the game in your favor

### Start Over
- Resets the current round
- Grants you a new random power-up
- Use when the game isn't going your way


## Acknowledgements

The project was created for:
- Tuwaiq Java Spring Boot Bootcamp
- Meant to showcase understanding of Java

---

have fun!
