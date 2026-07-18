# LifeSimulation

A JavaFX-based artificial life simulation featuring autonomous agents called "Bobbles" that survive, hunt, drink, and reproduce in a dynamic environment.

## Overview

LifeSimulation is an interactive ecosystem simulation where creatures called "Bobbles" navigate a 2D world, managing their health, hunger, and thirst while seeking food and water sources. The simulation includes dynamic weather (rain), mating behavior, and configurable parameters.

## Features

- **Autonomous Bobbles**: Self-controlled creatures with needs (hunger, thirst, health)
- **Behavior System**: Bobbles wander, seek food, find water, and can mate
- **Environment**: Dynamic (very simple) world with apple trees (food sources) and water ponds
- **Weather**: Periodic rain that fills water ponds and creates new ones
- **Reproduction**: Mating behavior that passes attributes to offspring
- **Configurable**: Adjust population, food sources, water ponds, and mating

## Requirements

- Java 21+
- Maven

## Build & Run

```bash
mvn clean javafx:run
```

## Usage

1. Launch the application
2. Configure simulation parameters:
   - Bobble population
   - Number of apple trees
   - Number of water ponds
   - Enable/disable mating
   - Advanced setting tab
3. Click "Start Simulation" to begin

The simulation runs automatically. Bobbles will:
- Wander randomly when not hungry or thirsty
- Seek and consume food when hungry
- Seek water when thirsty
- Mate when both needs are met (if enabled)


## Problems / TODOs

- Application doesn't fully close/stop when told to
- World is very simple and doesn't leave a lot of options for the bobbles
- Pathing of bobbles is very simple and can easily misbehave
